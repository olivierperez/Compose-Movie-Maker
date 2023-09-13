#!/bin/sh

FFMPEG="ffmpeg"
CAPTURES_DIR="../captures"
FPS=30
GIF_FPS=5

# [ -f $FFMPEG ] || { >&2 echo "You need to download ffmpeg here!" ; exit 1; }

$FFMPEG -framerate "$FPS" -i "$CAPTURES_DIR/screenshot-%d.png" -c:v libx264 "$CAPTURES_DIR/out.mp4"
$FFMPEG -framerate "$FPS" -i "$CAPTURES_DIR/screenshot-%d.png" -vf "fps=$GIF_FPS,format=gray" "$CAPTURES_DIR/out.gif"
