#!/usr/bin/env python3
"""Convert the supplied GIFs into Minecraft 1.21.1 textures using Pillow."""

from pathlib import Path

from PIL import Image, ImageSequence

TOOLS = Path(__file__).resolve().parent
SOURCES = TOOLS / "source-assets"
TEXTURES = TOOLS.parent / "src/main/resources/assets/avaritia_integration/textures"
FRAME_SIZE = (32, 32)
PEARL = "extremely_primordial_pearl"
TERMINUS = "terminus"


def read_frames(name):
    with Image.open(SOURCES / f"{name}.gif") as image:
        if image.format != "GIF" or image.size != FRAME_SIZE:
            raise ValueError(f"{name} must be a 32×32 GIF")
        frames = [frame.convert("RGBA") for frame in ImageSequence.Iterator(image)]
    return frames


def main():
    pearl_frames = read_frames(PEARL)
    icon_frames = read_frames(TERMINUS)
    pixels = [frame.tobytes() for frame in pearl_frames]
    if len(pixels) != 16 or len(set(pixels[:12])) != 12 or pixels[12:] != [pixels[0]] * 4:
        raise ValueError("Pearl GIF must have 12 distinct frames followed by four copies of frame 1")
    if len(icon_frames) != 1:
        raise ValueError("Terminus GIF must have one frame")

    width, height = FRAME_SIZE
    strip = Image.new("RGBA", (width, height * 12))
    for index, frame in enumerate(pearl_frames[:12]):
        strip.paste(frame, (0, index * height))

    pearl_texture = TEXTURES / "item" / f"{PEARL}.png"
    icon_texture = TEXTURES / "aspects" / f"{TERMINUS}.png"
    pearl_texture.parent.mkdir(parents=True, exist_ok=True)
    icon_texture.parent.mkdir(parents=True, exist_ok=True)
    strip.save(pearl_texture)
    icon_frames[0].save(icon_texture)
    pearl_texture.with_suffix(".png.mcmeta").write_bytes(b'{\n\t"animation": {}\n}\n')


if __name__ == "__main__":
    main()
