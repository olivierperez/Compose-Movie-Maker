#!/bin/sh

FFMPEG="./ffmpeg.exe"
CAPTURES_DIR="../captures"
FPS=60

[ -f $FFMPEG ] || { >&2 echo "You need to download ffmpeg here!" ; exit 1; }

$FFMPEG -framerate "$FPS" -i "$CAPTURES_DIR/screenshot-%d.png" -c:v libx264 "$CAPTURES_DIR/out.mp4"
