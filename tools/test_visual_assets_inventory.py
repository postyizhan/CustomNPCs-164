from __future__ import print_function

import os
import shutil
import struct
import csv
import io
import subprocess
import tempfile
import unittest
import zlib

import visual_assets_inventory as inventory


def png(width=2, height=3, color_type=6):
    data = struct.pack(">IIBBBBB", width, height, 8, color_type, 0, 0, 0)
    return (inventory.PNG_SIGNATURE + struct.pack(">I", 13) + b"IHDR" + data
            + struct.pack(">I", zlib.crc32(b"IHDR" + data) & 0xffffffff))


class InventoryTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.mkdtemp()
        self.current = os.path.join(self.temp, "current")
        self.upstream = os.path.join(self.temp, "upstream")
        os.makedirs(self.current)
        os.makedirs(self.upstream)

    def tearDown(self):
        shutil.rmtree(self.temp)

    def write(self, root, relative, data):
        path = os.path.join(root, *relative.split("/"))
        parent = os.path.dirname(path)
        if not os.path.isdir(parent):
            os.makedirs(parent)
        with open(path, "wb") as stream:
            stream.write(data)
        return path

    def test_exact_paths_statuses_and_file_kinds(self):
        self.write(self.current, "items/identical.png", png())
        self.write(self.upstream, "items/identical.png", png())
        self.write(self.current, "items/different.png", png(4, 5))
        self.write(self.upstream, "items/different.png", png(6, 7))
        self.write(self.current, "blocks/current.txt", b"current")
        self.write(self.upstream, "gui/upstream.png.mcmeta", b"{}")
        self.write(self.current, "models/Candle.png", png(8, 9))
        self.write(self.upstream, "models/candle.png", png(10, 11))

        rows = inventory.build_rows(self.current, self.upstream)
        by_path = dict((row["relative_path"], row) for row in rows)
        self.assertEqual("identical", by_path["items/identical.png"]["difference_status"])
        self.assertEqual("different", by_path["items/different.png"]["difference_status"])
        self.assertEqual("current-only", by_path["blocks/current.txt"]["difference_status"])
        self.assertEqual("upstream-only", by_path["gui/upstream.png.mcmeta"]["difference_status"])
        self.assertEqual("txt", by_path["blocks/current.txt"]["file_kind"])
        self.assertEqual("png-mcmeta", by_path["gui/upstream.png.mcmeta"]["file_kind"])
        self.assertEqual("current-only", by_path["models/Candle.png"]["difference_status"])
        self.assertEqual("upstream-only", by_path["models/candle.png"]["difference_status"])
        self.assertEqual("yes", by_path["models/Candle.png"]["case_only_collision"])
        self.assertEqual("yes", by_path["models/candle.png"]["case_only_collision"])
        self.assertEqual("8", by_path["models/Candle.png"]["current_png_width"])
        self.assertEqual("11", by_path["models/candle.png"]["upstream_png_height"])

    def test_malformed_ihdr_is_rejected(self):
        valid = png()
        malformed = [
            valid[:8] + struct.pack(">I", 12) + valid[12:],
            valid[:12] + b"NOPE" + valid[16:],
            valid[:-1] + bytes(bytearray([valid[-1] ^ 1])),
            png(0, 3),
            valid[:20],
        ]
        for index, data in enumerate(malformed):
            path = self.write(self.current, "items/bad%d.png" % index, data)
            with self.assertRaises(ValueError):
                inventory.inspect_file(path)

    def test_render_csv_is_deterministic_with_lf_endings(self):
        self.write(self.current, "items/z.png", png())
        self.write(self.current, "items/A.png", png(4, 5))
        self.write(self.upstream, "items/a.png", png(6, 7))
        rows = inventory.build_rows(self.current, self.upstream)

        first = inventory.render_csv(rows)
        second = inventory.render_csv(inventory.build_rows(self.current, self.upstream))

        self.assertEqual(first, second)
        self.assertNotIn(b"\r\n", first)
        parsed = list(csv.DictReader(io.StringIO(first.decode("utf-8"))))
        self.assertEqual(["items/A.png", "items/a.png", "items/z.png"],
                         [row["relative_path"] for row in parsed])


class ProvenanceTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.mkdtemp()
        subprocess.check_call(["git", "init", "-q", self.temp])
        subprocess.check_call(["git", "-C", self.temp, "config", "user.email", "test@example.invalid"])
        subprocess.check_call(["git", "-C", self.temp, "config", "user.name", "Inventory Test"])
        self.tracked = os.path.join(self.temp, "tracked.txt")
        with open(self.tracked, "w") as stream:
            stream.write("clean\n")
        subprocess.check_call(["git", "-C", self.temp, "add", "tracked.txt"])
        subprocess.check_call(["git", "-C", self.temp, "commit", "-q", "-m", "fixture"])
        self.head = subprocess.check_output(
            ["git", "-C", self.temp, "rev-parse", "HEAD"]
        ).decode("ascii").strip()

    def tearDown(self):
        def remove_readonly(function, path, exc_info):
            del exc_info
            os.chmod(path, 0o700)
            function(path)
        shutil.rmtree(self.temp, onerror=remove_readonly)

    def test_dirty_tracked_file_is_rejected(self):
        with open(self.tracked, "w") as stream:
            stream.write("dirty\n")
        with self.assertRaisesRegex(RuntimeError, "work tree is dirty"):
            inventory.verify_upstream_provenance(self.temp, self.head)
        subprocess.check_call(["git", "-C", self.temp, "checkout", "--", "tracked.txt"])
        os.remove(self.tracked)
        with self.assertRaisesRegex(RuntimeError, "modifications/deletions"):
            inventory.verify_upstream_provenance(self.temp, self.head)

    def test_dirty_untracked_file_is_rejected(self):
        with open(os.path.join(self.temp, "untracked.txt"), "w") as stream:
            stream.write("dirty\n")
        with self.assertRaisesRegex(RuntimeError, "untracked files"):
            inventory.verify_upstream_provenance(self.temp, self.head)


if __name__ == "__main__":
    unittest.main()
