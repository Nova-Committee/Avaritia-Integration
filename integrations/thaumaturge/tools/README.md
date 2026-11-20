# Thaumaturge texture sources

`source-assets/` contains the two supplied GIFs. From the repository root, run `python3 integrations/thaumaturge/tools/convert-textures.py` with Pillow installed. Conversion runs offline and can be repeated.

The pearl's first 12 RGBA frames are stacked vertically in their original order. The four copies of frame 1 at the end are removed. Minecraft plays the resulting 32×384 PNG at one tick per frame. Terminus uses the single 32×32 RGBA frame from its GIF. Both outputs preserve the source pixels and transparency.
