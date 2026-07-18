#!/usr/bin/env python3
"""Create a deterministic inventory of selected CustomNPCs visual assets.

Only Python's standard library is used. The output is evidence for manual review;
it does not decide licensing, runtime compatibility, or final disposition.
"""

from __future__ import print_function

import argparse
import csv
import hashlib
import io
import os
import struct
import subprocess
import sys
import zlib


LOCKED_UPSTREAM_COMMIT = "3448957d046dae5c5b12b0b5f4287bf592e29de7"
CATEGORIES = ("items", "blocks", "models", "gui")
PNG_SIGNATURE = b"\x89PNG\r\n\x1a\n"
COLOR_TYPES = {
    0: "0 (grayscale)",
    2: "2 (truecolor)",
    3: "3 (indexed-color)",
    4: "4 (grayscale-alpha)",
    6: "6 (truecolor-alpha)",
}
FIELDNAMES = (
    "relative_path",
    "category",
    "file_kind",
    "current_exists",
    "upstream_exists",
    "case_only_collision",
    "current_sha256",
    "upstream_sha256",
    "current_png_width",
    "current_png_height",
    "current_png_color_type",
    "upstream_png_width",
    "upstream_png_height",
    "upstream_png_color_type",
    "difference_status",
)


def repo_root():
    return os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir))


def parse_args():
    root = repo_root()
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--current-root",
        default=os.path.join(root, "src", "main", "resources", "assets", "customnpcs", "textures"),
        help="current textures directory",
    )
    parser.add_argument(
        "--upstream-root",
        default=os.path.join(root, "CustomNPC-Plus", "src", "main", "resources", "assets", "customnpcs", "textures"),
        help="locked CustomNPC-Plus textures directory",
    )
    parser.add_argument(
        "--output",
        default=os.path.join(root, "docs", "porting", "visual-assets-inventory.csv"),
        help="CSV output path",
    )
    parser.add_argument(
        "--check",
        action="store_true",
        help="fail instead of writing if the existing output differs",
    )
    parser.add_argument(
        "--skip-upstream-commit-check",
        action="store_true",
        help="allow an alternate/unlocked upstream tree (intended only for isolated self-tests)",
    )
    return parser.parse_args()


def git_output(root, arguments):
    try:
        return subprocess.check_output(
            ["git", "-C", root] + arguments, stderr=subprocess.STDOUT
        ).decode("utf-8").strip()
    except (OSError, subprocess.CalledProcessError) as exc:
        raise RuntimeError("cannot inspect upstream git provenance: %s" % exc)


def verify_upstream_provenance(upstream_root, expected_commit=LOCKED_UPSTREAM_COMMIT):
    actual = git_output(upstream_root, ["rev-parse", "HEAD"])
    if actual != expected_commit:
        raise RuntimeError(
            "upstream HEAD is %s; expected locked commit %s" % (actual, expected_commit)
        )
    dirty = git_output(upstream_root, ["status", "--porcelain", "--untracked-files=all"])
    if dirty:
        raise RuntimeError(
            "upstream CustomNPC-Plus work tree is dirty; tracked modifications/deletions "
            "and untracked files are not valid locked-source provenance:\n%s" % dirty
        )


def collect_files(root):
    """Return exact relative-path keys mapped to the files actually enumerated."""
    files = {}
    for category in CATEGORIES:
        category_root = os.path.join(root, category)
        if not os.path.isdir(category_root):
            continue
        for directory, dirnames, filenames in os.walk(category_root):
            dirnames.sort()
            filenames.sort()
            for filename in filenames:
                full_path = os.path.join(directory, filename)
                relative = os.path.relpath(full_path, root).replace(os.sep, "/")
                files[relative] = full_path
    return files


def inspect_file(path):
    with open(path, "rb") as stream:
        data = stream.read()
    digest = hashlib.sha256(data).hexdigest()
    png = ("", "", "")
    if data.startswith(PNG_SIGNATURE):
        if len(data) < 33:
            raise ValueError("invalid PNG IHDR (truncated): %s" % path)
        chunk_length = struct.unpack(">I", data[8:12])[0]
        chunk_type = data[12:16]
        if chunk_length != 13 or chunk_type != b"IHDR":
            raise ValueError("invalid PNG IHDR chunk: %s" % path)
        chunk_data = data[16:29]
        expected_crc = struct.unpack(">I", data[29:33])[0]
        actual_crc = zlib.crc32(chunk_type + chunk_data) & 0xffffffff
        if actual_crc != expected_crc:
            raise ValueError("invalid PNG IHDR CRC: %s" % path)
        width, height, bit_depth, color_type = struct.unpack(">IIBB", chunk_data[:10])
        del bit_depth
        if width == 0 or height == 0:
            raise ValueError("invalid PNG IHDR dimensions: %s" % path)
        png = (str(width), str(height), COLOR_TYPES.get(color_type, str(color_type)))
    return digest, png


def file_kind(relative_path):
    lower = relative_path.lower()
    if lower.endswith(".png.mcmeta"):
        return "png-mcmeta"
    if lower.endswith(".png"):
        return "png"
    extension = os.path.splitext(relative_path)[1].lower()
    return extension[1:] if extension else "extensionless"


def casefold_index(paths):
    index = {}
    for path in paths:
        index.setdefault(path.casefold(), set()).add(path)
    return index


def build_rows(current_root, upstream_root):
    current_files = collect_files(current_root)
    upstream_files = collect_files(upstream_root)
    all_paths = set(current_files) | set(upstream_files)
    current_folded = casefold_index(current_files)
    upstream_folded = casefold_index(upstream_files)
    rows = []
    for relative in sorted(all_paths, key=lambda value: (value.casefold(), value)):
        current_path = current_files.get(relative)
        upstream_path = upstream_files.get(relative)
        current_exists = current_path is not None
        upstream_exists = upstream_path is not None
        current_hash, current_png = inspect_file(current_path) if current_exists else ("", ("", "", ""))
        upstream_hash, upstream_png = inspect_file(upstream_path) if upstream_exists else ("", ("", "", ""))
        if current_exists and upstream_exists:
            status = "identical" if current_hash == upstream_hash else "different"
        elif current_exists:
            status = "current-only"
        else:
            status = "upstream-only"
        folded = relative.casefold()
        case_collision = bool(
            current_folded.get(folded, set())
            and upstream_folded.get(folded, set())
            and current_folded.get(folded, set()) != upstream_folded.get(folded, set())
        )
        rows.append({
            "relative_path": relative,
            "category": relative.split("/", 1)[0],
            "file_kind": file_kind(relative),
            "current_exists": "yes" if current_exists else "no",
            "upstream_exists": "yes" if upstream_exists else "no",
            "case_only_collision": "yes" if case_collision else "no",
            "current_sha256": current_hash,
            "upstream_sha256": upstream_hash,
            "current_png_width": current_png[0],
            "current_png_height": current_png[1],
            "current_png_color_type": current_png[2],
            "upstream_png_width": upstream_png[0],
            "upstream_png_height": upstream_png[1],
            "upstream_png_color_type": upstream_png[2],
            "difference_status": status,
        })
    return rows


def render_csv(rows):
    output = io.StringIO(newline="")
    writer = csv.DictWriter(output, fieldnames=FIELDNAMES, lineterminator="\n")
    writer.writeheader()
    writer.writerows(rows)
    return output.getvalue().encode("utf-8")


def summarize(rows):
    for category in CATEGORIES:
        selected = [row for row in rows if row["category"] == category]
        statuses = {}
        for row in selected:
            status = row["difference_status"]
            statuses[status] = statuses.get(status, 0) + 1
        detail = ", ".join("%s=%d" % (key, statuses.get(key, 0)) for key in
                           ("identical", "different", "current-only", "upstream-only"))
        print("%s: total=%d; %s" % (category, len(selected), detail))
    collisions = [row["relative_path"] for row in rows if row["case_only_collision"] == "yes"]
    if collisions:
        print("case-only collisions: %s" % ", ".join(collisions))


def main():
    args = parse_args()
    if not args.skip_upstream_commit_check:
        verify_upstream_provenance(args.upstream_root)
    rows = build_rows(args.current_root, args.upstream_root)
    rendered = render_csv(rows)
    output_path = os.path.abspath(args.output)
    if args.check:
        try:
            with open(output_path, "rb") as stream:
                existing = stream.read()
        except IOError:
            print("inventory is missing: %s" % output_path, file=sys.stderr)
            return 1
        if existing != rendered:
            print("inventory is stale: %s" % output_path, file=sys.stderr)
            return 1
        print("inventory is current: %s" % output_path)
    else:
        output_directory = os.path.dirname(output_path)
        if output_directory and not os.path.isdir(output_directory):
            os.makedirs(output_directory)
        with open(output_path, "wb") as stream:
            stream.write(rendered)
        print("wrote %d rows to %s" % (len(rows), output_path))
    summarize(rows)
    return 0


if __name__ == "__main__":
    sys.exit(main())
