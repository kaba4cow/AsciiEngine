package kaba4cow.ascii.drawing.drawers;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.glyphs.BoxGlyphs;
import kaba4cow.ascii.drawing.glyphs.Glyphs;

public final class BoxDrawer {

	private BoxDrawer() {

	}

	private static void drawChar(int x, int y, char c, int color) {
		char s = Display.getChar(x, y);
		if (BoxGlyphs.isBoxGlyph(s) && BoxGlyphs.isBoxGlyph(c))
			c = BoxGlyphs.getIntersection(s, c);
		Drawer.drawChar(x, y, c, color);
	}

	public static void drawHorizontalLine(int y, int x0, int x1, char c, int color) {
		if (x0 > x1) {
			int temp = x0;
			x0 = x1;
			x1 = temp;
		}
		for (int x = x0; x <= x1; x++)
			drawChar(x, y, c, color);
	}

	public static void drawVerticalLine(int x, int y0, int y1, char c, int color) {
		if (y0 > y1) {
			int temp = y0;
			y0 = y1;
			y1 = temp;
		}
		for (int y = y0; y <= y1; y++)
			drawChar(x, y, c, color);
	}

	public static void drawBoxSingle(int x, int y, int width, int height, int color) {
		drawChar(x, y, Glyphs.BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT, color);
		drawChar(x, y + height - 1, Glyphs.BOX_DRAWINGS_LIGHT_UP_AND_RIGHT, color);
		drawChar(x + width - 1, y, Glyphs.BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT, color);
		drawChar(x + width - 1, y + height - 1, Glyphs.BOX_DRAWINGS_LIGHT_UP_AND_LEFT, color);

		drawHorizontalLine(y, x + 1, x + width - 2, Glyphs.BOX_DRAWINGS_LIGHT_HORIZONTAL, color);
		drawHorizontalLine(y + height - 1, x + 1, x + width - 2, Glyphs.BOX_DRAWINGS_LIGHT_HORIZONTAL, color);

		drawVerticalLine(x, y + 1, y + height - 2, Glyphs.BOX_DRAWINGS_LIGHT_VERTICAL, color);
		drawVerticalLine(x + width - 1, y + 1, y + height - 2, Glyphs.BOX_DRAWINGS_LIGHT_VERTICAL, color);
	}

	public static void drawBoxDouble(int x, int y, int width, int height, int color) {
		drawChar(x, y, Glyphs.BOX_DRAWINGS_DOUBLE_DOWN_AND_RIGHT, color);
		drawChar(x, y + height - 1, Glyphs.BOX_DRAWINGS_DOUBLE_UP_AND_RIGHT, color);
		drawChar(x + width - 1, y, Glyphs.BOX_DRAWINGS_DOUBLE_DOWN_AND_LEFT, color);
		drawChar(x + width - 1, y + height - 1, Glyphs.BOX_DRAWINGS_DOUBLE_UP_AND_LEFT, color);

		drawHorizontalLine(y, x + 1, x + width - 2, Glyphs.BOX_DRAWINGS_DOUBLE_HORIZONTAL, color);
		drawHorizontalLine(y + height - 1, x + 1, x + width - 2, Glyphs.BOX_DRAWINGS_DOUBLE_HORIZONTAL, color);

		drawVerticalLine(x, y + 1, y + height - 2, Glyphs.BOX_DRAWINGS_DOUBLE_VERTICAL, color);
		drawVerticalLine(x + width - 1, y + 1, y + height - 2, Glyphs.BOX_DRAWINGS_DOUBLE_VERTICAL, color);
	}

	public static void drawGlyphTable(int color) {
		char i = 0;
		final int length = 8;
		for (int column = 0; column < 8; column++)
			for (int row = 0; row < 32; row++)
				Drawer.drawString(column * length, row, false, i + " = " + (int) i++, color);
	}

	public static void drawGlyphTable(int color, char start, char stop) {
		char i = start;
		final int length = 8;
		for (int column = 0; column < 16; column++)
			for (int row = 0; row < 16; row++) {
				Drawer.drawString(column * length, row, false, i + " = " + (int) i, i % 2 == 0 ? 0x000FFF : 0x333FFF);
				if (++i > stop)
					return;
			}
	}

}
