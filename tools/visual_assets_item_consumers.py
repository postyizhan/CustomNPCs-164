#!/usr/bin/env python3
"""Audit exact-key item texture consumers without copying or approving assets."""
from __future__ import print_function

import argparse
import csv
import io
import os
import re
import sys

FIELDNAMES = ("relative_path", "consumer_evidence", "consumers", "conditions", "flags", "evidence_note")
EXCLUDED_CLASSES = ("ItemBlock", "ItemPlaceholder", "ItemNpcColored")


def repo_root():
    return os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))


def parse_args(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--inventory", default=os.path.join(repo_root(), "docs", "porting", "visual-assets-inventory.csv"))
    parser.add_argument("--custom-items-source", default=os.path.join(repo_root(), "src", "main", "java", "noppes", "npcs", "CustomItems.java"))
    parser.add_argument("--output", default=os.path.join(repo_root(), "docs", "porting", "visual-assets-item-consumers.csv"))
    parser.add_argument("--check", action="store_true")
    return parser.parse_args(argv)


def strip_comments(source):
    """Replace Java comments with whitespace, preserving strings and line numbers."""
    out = []
    i = 0
    state = "code"
    while i < len(source):
        char = source[i]
        nxt = source[i + 1] if i + 1 < len(source) else ""
        if state == "code":
            if char == '"': state = "string"; out.append(char); i += 1
            elif char == "'": state = "char"; out.append(char); i += 1
            elif char == "/" and nxt == "/": state = "line"; out.extend("  "); i += 2
            elif char == "/" and nxt == "*": state = "block"; out.extend("  "); i += 2
            else: out.append(char); i += 1
        elif state in ("string", "char"):
            out.append(char)
            if char == "\\" and i + 1 < len(source): out.append(source[i + 1]); i += 2
            elif (state == "string" and char == '"') or (state == "char" and char == "'"): state = "code"; i += 1
            else: i += 1
        elif state == "line":
            if char == "\n": out.append(char); state = "code"
            else: out.append(" ")
            i += 1
        else:
            if char == "*" and nxt == "/": out.extend("  "); state = "code"; i += 2
            else: out.append("\n" if char == "\n" else " "); i += 1
    return "".join(out)


def statements(source):
    """Return (statement, starting line, extra-items condition) tuples."""
    clean = strip_comments(source)
    result, buffer = [], []
    line, start_line = 1, None
    state, escaped = "code", False
    pending_extra = False
    extra_depths = []
    depth = 0
    for char in clean:
        if start_line is None and not char.isspace(): start_line = line
        buffer.append(char)
        if state == "string":
            if escaped: escaped = False
            elif char == "\\": escaped = True
            elif char == '"': state = "code"
        elif state == "char":
            if escaped: escaped = False
            elif char == "\\": escaped = True
            elif char == "'": state = "code"
        else:
            if char == '"': state = "string"
            elif char == "'": state = "char"
            elif char == "{":
                prefix = "".join(buffer)
                depth += 1
                if pending_extra or re.search(r"if\s*\(\s*!\s*CustomNpcs\.DisableExtraNpcItems\s*\)\s*\{$", prefix):
                    extra_depths.append(depth); pending_extra = False
                buffer = []; start_line = None
            elif char == "}":
                if depth in extra_depths: extra_depths.remove(depth)
                depth -= 1; buffer = []; start_line = None
            elif char == ";":
                text = "".join(buffer).strip()
                if text: result.append((text, start_line, bool(extra_depths)))
                buffer = []; start_line = None
        if char == "\n": line += 1
    return result


def parse_consumers(source):
    consumers = []
    last_dagger_by_variable = {}
    issues = []
    for statement, line, conditional in statements(source):
        constructor = re.search(r"\bnew\s+([A-Za-z_$][\w$]*)\s*\(", statement)
        if not constructor:
            continue
        class_name = constructor.group(1)
        if not class_name.startswith("Item") or class_name in EXCLUDED_CLASSES:
            continue
        name_match = re.search(r'\.setUnlocalizedName\s*\(\s*"([^"\\]*)"\s*\)', statement)
        texture_match = re.search(r'\.setTextureName\s*\(\s*"([^"\\]*)"\s*\)', statement)
        any_texture = re.search(r"\.setTextureName\s*\(", statement)
        variable_match = re.match(r"(?:[\w<>?]+\s+)?([A-Za-z_$][\w$]*)\s*=", statement)
        variable = variable_match.group(1) if variable_match else ""
        if class_name == "ItemDaggerReversed":
            argument = re.search(r"new\s+ItemDaggerReversed\s*\([^,]+,\s*(?:\(ItemDagger\)\s*)?([A-Za-z_$][\w$]*)", statement)
            forward = last_dagger_by_variable.get(argument.group(1) if argument else "")
            texture = forward
            override = True
        else:
            texture = texture_match.group(1) if texture_match else None
            override = False
        if class_name == "ItemDagger" and variable and texture and texture.startswith("customnpcs:"):
            last_dagger_by_variable[variable] = texture
        if class_name == "ItemKunaiReversed":
            texture, override = "customnpcs:npcKunai", True
        if not texture:
            if any_texture or class_name in ("ItemDaggerReversed",): issues.append("line %d: dynamic or unresolved texture for %s" % (line, class_name))
            continue
        if ":" not in texture or not texture.startswith("customnpcs:"):
            issues.append("line %d: unsupported texture namespace for %s: %s" % (line, class_name, texture))
            continue
        name = name_match.group(1) if name_match else ""
        consumers.append({"path": "items/%s.png" % texture.split(":", 1)[1], "class": class_name,
                          "name": name, "line": line, "condition": "extra-items-enabled" if conditional else "always",
                          "override": override})
    return consumers, issues


def build_rows(inventory_path, source_path):
    with open(inventory_path, newline="", encoding="utf-8") as stream:
        inventory = [row for row in csv.DictReader(stream) if row["category"] == "items"]
    inventory_paths = set()
    for item in inventory:
        path = item["relative_path"]
        if path in inventory_paths:
            raise ValueError("duplicate items relative_path in inventory: %s" % path)
        inventory_paths.add(path)
    with open(source_path, encoding="utf-8") as stream: source = stream.read()
    found, issues = parse_consumers(source)
    by_path = {}
    for consumer in found: by_path.setdefault(consumer["path"], []).append(consumer)
    for path in sorted(set(by_path) - inventory_paths):
        issues.append("extracted consumer path absent from items inventory: %s" % path)
    rows = []
    for item in inventory:
        path = item["relative_path"]
        flags = []
        if path.endswith(".png.mcmeta"):
            rows.append(dict(zip(FIELDNAMES, (path, "not-applicable-sidecar", "", "", "animated-strip;manual-review", "animation metadata is outside the static item audit"))))
            continue
        selected = sorted(by_path.get(path, []), key=lambda c: (c["class"], c["name"], c["line"]))
        if selected:
            evidence = "static-extracted"
            consumer_text = ";".join("%s|%s|CustomItems.java:%d|%s" %
                                     (c["class"], c["name"], c["line"], c["condition"]) for c in selected)
            conditions = ";".join(sorted(set(c["condition"] for c in selected)))
            if len(selected) > 1: flags.append("shared-icon")
            if any(c["override"] for c in selected): flags.append("icon-override")
            note = "literal customnpcs texture registration in a parsed Java statement"
        else:
            evidence, consumer_text, conditions = "none-extracted", "", ""
            note = "no consumer matched the controlled static registration pattern"
        width = item.get("upstream_png_width") or item.get("current_png_width")
        height = item.get("upstream_png_height") or item.get("current_png_height")
        if width and height and width != height:
            flags.extend(["animated-strip", "manual-review"])
        if path + ".mcmeta" in inventory_paths:
            flags.extend(["animated-strip", "manual-review"])
        rows.append(dict(zip(FIELDNAMES, (path, evidence, consumer_text, conditions, ";".join(sorted(set(flags))), note))))
    rows.sort(key=lambda row: (row["relative_path"].casefold(), row["relative_path"]))
    return rows, issues


def render_csv(rows):
    output = io.StringIO(newline="")
    writer = csv.DictWriter(output, fieldnames=FIELDNAMES, lineterminator="\n")
    writer.writeheader(); writer.writerows(rows)
    return output.getvalue().encode("utf-8")


def main(argv=None):
    args = parse_args(argv)
    rows, issues = build_rows(args.inventory, args.custom_items_source)
    if issues:
        for issue in issues: print("manual review: %s" % issue, file=sys.stderr)
        return 1
    rendered = render_csv(rows)
    if args.check:
        try:
            with open(args.output, "rb") as stream: existing = stream.read()
        except IOError:
            print("item consumer audit is missing: %s" % args.output, file=sys.stderr); return 1
        if existing != rendered:
            print("item consumer audit is stale: %s" % args.output, file=sys.stderr); return 1
        print("item consumer audit is current: %s" % args.output)
    else:
        parent = os.path.dirname(os.path.abspath(args.output))
        if parent and not os.path.isdir(parent): os.makedirs(parent)
        with open(args.output, "wb") as stream: stream.write(rendered)
        print("wrote %d rows to %s" % (len(rows), args.output))
    counts = {}
    for row in rows: counts[row["consumer_evidence"]] = counts.get(row["consumer_evidence"], 0) + 1
    print("items: total=%d; %s" % (len(rows), ", ".join("%s=%d" % pair for pair in sorted(counts.items()))))
    return 0

if __name__ == "__main__": sys.exit(main())
