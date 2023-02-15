package kaba4cow.ascii.drawing;

import kaba4cow.ascii.drawing.glyphs.Glyphs;

public class Frame {

	public final int width;
	public final int height;
	public final int length;

	public final char[] chars;
	public final int[] colors;

	public Frame(int width, int height) {
		this.width = width;
		this.height = height;
		this.length = width * height;
		this.chars = new char[length];
		this.colors = new int[length];
	}

	public void fill(char c, int color) {
		for (int i = 0; i < length; i++) {
			chars[i] = c;
			colors[i] = color;
		}
	}

	public char getChar(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Glyphs.SPACE;
		return chars[y * width + x];
	}

	public int getColor(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0x000000;
		return colors[y * width + x];
	}

}
