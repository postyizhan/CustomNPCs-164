from __future__ import print_function

import csv
import io
import os
import shutil
import subprocess
import sys
import tempfile
import unittest

import visual_assets_item_consumers as audit


class ParserTest(unittest.TestCase):
    def test_comments_strings_multiline_case_blocks_conditions_and_overrides(self):
        source = r'''
// new ItemFake(1).setTextureName("customnpcs:commented");
String url = "http://example//not-a-comment;";
new ItemAlpha(
  1).setUnlocalizedName("Alpha").setTextureName(
  "customnpcs:ExactCase");
new BlockAlpha(2).setUnlocalizedName("bad").setTextureName("customnpcs:block");
new Foo(2).setUnlocalizedName("alsoBad").setTextureName("customnpcs:foo");
if(!CustomNpcs.DisableExtraNpcItems){
 Item dagger = new ItemDagger(3, MAT).setUnlocalizedName("forward").setTextureName("customnpcs:dagger");
 dagger = new ItemDaggerReversed(4, (ItemDagger) dagger, MAT).setUnlocalizedName("reverse").setTextureName("customnpcs:wrong");
 new ItemKunaiReversed(5, MAT).setUnlocalizedName("kunaiReverse").setTextureName("customnpcs:npcReverseKunai");
}
new ItemPlaceholder(6).setTextureName("customnpcs:placeholder");
'''
        consumers, issues = audit.parse_consumers(source)
        self.assertEqual([], issues)
        by_name = dict((item["name"], item) for item in consumers)
        self.assertEqual("items/ExactCase.png", by_name["Alpha"]["path"])
        self.assertEqual(4, by_name["Alpha"]["line"])
        self.assertEqual("always", by_name["Alpha"]["condition"])
        self.assertEqual("items/dagger.png", by_name["reverse"]["path"])
        self.assertTrue(by_name["reverse"]["override"])
        self.assertEqual("items/npcKunai.png", by_name["kunaiReverse"]["path"])
        self.assertEqual("extra-items-enabled", by_name["kunaiReverse"]["condition"])
        self.assertNotIn("bad", by_name)
        self.assertNotIn("alsoBad", by_name)

    def test_dynamic_and_unknown_namespace_are_reported_not_invented(self):
        source = '''new ItemThing(1).setTextureName(prefix + name);\nnew ItemThing(2).setTextureName("other:name");'''
        consumers, issues = audit.parse_consumers(source)
        self.assertEqual([], consumers)
        self.assertEqual(2, len(issues))
        self.assertIn("dynamic", issues[0])
        self.assertIn("namespace", issues[1])


class RowsTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.mkdtemp()
        self.inventory = os.path.join(self.temp, "inventory.csv")
        self.source = os.path.join(self.temp, "CustomItems.java")

    def tearDown(self): shutil.rmtree(self.temp)

    def write_inventory(self, extra_rows=None):
        fields = ["relative_path", "category", "current_png_width", "current_png_height",
                  "upstream_png_width", "upstream_png_height", "difference_status"]
        rows = [
            ["items/a.png", "items", "16", "16", "16", "16", "different"],
            ["items/a.png.mcmeta", "items", "", "", "", "", "upstream-only"],
            ["items/strip.png", "items", "16", "32", "16", "32", "different"],
            ["blocks/no.png", "blocks", "16", "16", "16", "16", "different"],
        ]
        if extra_rows:
            rows.extend(extra_rows)
        with open(self.inventory, "w", newline="", encoding="utf-8") as stream:
            writer = csv.writer(stream, lineterminator="\n"); writer.writerow(fields); writer.writerows(rows)

    def test_sidecar_non_square_shared_sorting_schema_and_lf(self):
        self.write_inventory()
        with open(self.source, "w") as stream:
            stream.write('new ItemZ(1).setUnlocalizedName("z").setTextureName("customnpcs:a");\n'
                         'new ItemA(2).setUnlocalizedName("a").setTextureName("customnpcs:a");\n')
        rows, issues = audit.build_rows(self.inventory, self.source)
        self.assertEqual([], issues)
        by_path = dict((row["relative_path"], row) for row in rows)
        self.assertEqual("not-applicable-sidecar", by_path["items/a.png.mcmeta"]["consumer_evidence"])
        self.assertEqual("animated-strip;manual-review", by_path["items/a.png.mcmeta"]["flags"])
        self.assertEqual("animated-strip;manual-review", by_path["items/strip.png"]["flags"])
        self.assertEqual("ItemA|a|CustomItems.java:2|always;ItemZ|z|CustomItems.java:1|always",
                         by_path["items/a.png"]["consumers"])
        self.assertIn("shared-icon", by_path["items/a.png"]["flags"])
        rendered = audit.render_csv(rows)
        self.assertNotIn(b"\r\n", rendered)
        self.assertEqual(list(audit.FIELDNAMES), next(csv.reader(io.StringIO(rendered.decode("utf-8")))))

    def test_duplicate_items_relative_path_is_rejected(self):
        self.write_inventory([["items/a.png", "items", "16", "16", "16", "16", "different"]])
        with open(self.source, "w") as stream: stream.write("")
        with self.assertRaisesRegex(ValueError, "duplicate items relative_path"):
            audit.build_rows(self.inventory, self.source)

    def test_unknown_extracted_path_is_an_issue_and_cli_does_not_overwrite(self):
        self.write_inventory()
        with open(self.source, "w") as stream:
            stream.write('new ItemMissing(1).setUnlocalizedName("missing").setTextureName("customnpcs:missing");')
        rows, issues = audit.build_rows(self.inventory, self.source)
        self.assertEqual(3, len(rows))
        self.assertEqual(["extracted consumer path absent from items inventory: items/missing.png"], issues)
        output = os.path.join(self.temp, "out.csv")
        with open(output, "wb") as stream:
            stream.write(b"preserve me\n")
        self.assertEqual(1, audit.main(["--inventory", self.inventory, "--custom-items-source", self.source,
                                       "--output", output]))
        with open(output, "rb") as stream:
            self.assertEqual(b"preserve me\n", stream.read())

    def test_parse_issue_makes_cli_fail_before_writing(self):
        self.write_inventory()
        with open(self.source, "w") as stream:
            stream.write('new ItemDynamic(1).setTextureName(prefix + name);')
        output = os.path.join(self.temp, "out.csv")
        self.assertEqual(1, audit.main(["--inventory", self.inventory, "--custom-items-source", self.source,
                                       "--output", output]))
        self.assertFalse(os.path.exists(output))

    def test_cli_check_missing_stale_current(self):
        self.write_inventory()
        with open(self.source, "w") as stream: stream.write('new ItemA(1).setUnlocalizedName("a").setTextureName("customnpcs:a");')
        output = os.path.join(self.temp, "out.csv")
        self.assertEqual(1, audit.main(["--inventory", self.inventory, "--custom-items-source", self.source, "--output", output, "--check"]))
        with open(output, "wb") as stream: stream.write(b"stale\n")
        self.assertEqual(1, audit.main(["--inventory", self.inventory, "--custom-items-source", self.source, "--output", output, "--check"]))
        self.assertEqual(0, audit.main(["--inventory", self.inventory, "--custom-items-source", self.source, "--output", output]))
        self.assertEqual(0, audit.main(["--inventory", self.inventory, "--custom-items-source", self.source, "--output", output, "--check"]))


class RepositoryBaselineTest(unittest.TestCase):
    def test_frozen_baseline(self):
        root = audit.repo_root()
        rows, issues = audit.build_rows(os.path.join(root, "docs", "porting", "visual-assets-inventory.csv"),
                                        os.path.join(root, "src", "main", "java", "noppes", "npcs", "CustomItems.java"))
        self.assertEqual(358, len(rows))
        with open(os.path.join(root, "docs", "porting", "visual-assets-inventory.csv"), encoding="utf-8") as stream:
            inventory = dict((row["relative_path"], row) for row in csv.DictReader(stream) if row["category"] == "items")
        different = [row for row in rows if inventory[row["relative_path"]]["difference_status"] == "different"]
        self.assertEqual(245, sum(row["consumer_evidence"] == "static-extracted" for row in different))
        none = [row["relative_path"] for row in different if row["consumer_evidence"] == "none-extracted"]
        self.assertEqual(["items/npcDemonicIngot.png", "items/npcLeafBlade.png", "items/npcMithrilIngot.png",
                          "items/npcSoulstoneEmpty.png", "items/npcSoulstoneFilled.png"], none)
        self.assertEqual(11, sum(row["consumer_evidence"] == "not-applicable-sidecar" for row in rows))
        by_path = dict((row["relative_path"], row) for row in rows)
        self.assertNotIn("items/npcReverseKunai.png", by_path)
        self.assertIn("ItemKunaiReversed|npcReverseKunai|CustomItems.java:427|extra-items-enabled",
                      by_path["items/npcKunai.png"]["consumers"])
        daggers = [path for path in by_path if path.endswith("Dagger.png")]
        self.assertEqual(10, len(daggers))
        for path in daggers:
            self.assertIn("ItemDagger|", by_path[path]["consumers"])
            self.assertIn("ItemDaggerReversed|", by_path[path]["consumers"])
            for evidence in by_path[path]["consumers"].split(";"):
                self.assertEqual("extra-items-enabled", evidence.rsplit("|", 1)[1])
        self.assertEqual([], issues)


if __name__ == "__main__": unittest.main()
