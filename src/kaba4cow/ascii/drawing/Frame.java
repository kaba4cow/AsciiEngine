package kaba4cow.ascii.drawing;

import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.toolbox.files.ImageFile;

public class Frame {

	public final int width;
	public final int height;
	public final int length;

	public final char[] glyphs;
	public final int[] colors;

	public Frame(int width, int height) {
		this.width = width;
		this.height = height;
		this.length = width * height;
		this.glyphs = new char[length];
		this.colors = new int[length];
		fill(Glyphs.SPACE, 0x000000);
	}

	public ImageFile toImageFile() {
		ImageFile image = new ImageFile(width, height);
		int x, y, i = 0;
		for (y = 0; y < height; y++)
			for (x = 0; x < width; x++)
				image.set(x, y, glyphs[i], colors[i++]);
		return image;
	}

	public void fill(char glyph, int color) {
		for (int i = 0; i < length; i++) {
			glyphs[i] = glyph;
			colors[i] = color;
		}
	}

	public char getGlyph(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Glyphs.SPACE;
		return glyphs[y * width + x];
	}

	public int getColor(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0x000000;
		return colors[y * width + x];
	}

}
