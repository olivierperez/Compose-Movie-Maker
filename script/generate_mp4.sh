#!/bin/sh

FFMPEG="./ffmpeg.exe"
CAPTURES_DIR="../captures"

[ -f $FFMPEG ] || { >&2 echo "You need to download ffmpeg here!" ; exit 1; }

$FFMPEG -framerate 5 -i "$CAPTURES_DIR/screenshot-%d.png" -c:v libx264 "$CAPTURES_DIR/out.mp4"
