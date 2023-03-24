package kaba4cow.ascii.drawing.drawers;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.glyphs.BoxGlyphs;
import kaba4cow.ascii.drawing.glyphs.Glyphs;

public final class BoxDrawer {

	private static boolean collisionEnable = true;

	private BoxDrawer() {

	}

	public static void enableCollision() {
		collisionEnable = true;
	}

	public static void disableCollision() {
		collisionEnable = false;
	}

	public static void drawChar(int x, int y, char c, int color) {
		char s = Display.getGlyph(x, y);
		if (collisionEnable && BoxGlyphs.isBoxGlyph(s) && BoxGlyphs.isBoxGlyph(c))
			c = BoxGlyphs.getIntersection(s, c);
		Drawer.draw(x, y, c, color);
	}

	public static void drawHorizontalLine(int y, int x0, int x1, char c, int color) {
		if (x0 > x1) {
			int temp = x0;
			x0 = x1;
			x1 = temp;
		}
		for (int x = x0; x < x1; x++)
			drawChar(x, y, c, color);
	}

	public static void drawVerticalLine(int x, int y0, int y1, char c, int color) {
		if (y0 > y1) {
			int temp = y0;
			y0 = y1;
			y1 = temp;
		}
		for (int y = y0; y < y1; y++)
			drawChar(x, y, c, color);
	}

	public static void drawBox(int x, int y, int width, int height, boolean single, int color) {
		if (single)
			drawBoxSingle(x, y, width, height, color);
		else
			drawBoxDouble(x, y, width, height, color);
	}

	private static void drawBoxSingle(int x, int y, int width, int height, int color) {
		drawChar(x, y, Glyphs.BOX_DRAWINGS_SINGLE_DOWN_AND_RIGHT, color);
		drawChar(x, y + height, Glyphs.BOX_DRAWINGS_SINGLE_UP_AND_RIGHT, color);
		drawChar(x + width, y, Glyphs.BOX_DRAWINGS_SINGLE_DOWN_AND_LEFT, color);
		drawChar(x + width, y + height, Glyphs.BOX_DRAWINGS_SINGLE_UP_AND_LEFT, color);

		drawHorizontalLine(y, x + 1, x + width, Glyphs.BOX_DRAWINGS_SINGLE_HORIZONTAL, color);
		drawHorizontalLine(y + height, x + 1, x + width, Glyphs.BOX_DRAWINGS_SINGLE_HORIZONTAL, color);

		drawVerticalLine(x, y + 1, y + height, Glyphs.BOX_DRAWINGS_SINGLE_VERTICAL, color);
		drawVerticalLine(x + width, y + 1, y + height, Glyphs.BOX_DRAWINGS_SINGLE_VERTICAL, color);

		Drawer.fillRect(x + 1, y + 1, width - 1, height - 1, false, Glyphs.SPACE, color);
	}

	private static void drawBoxDouble(int x, int y, int width, int height, int color) {
		drawChar(x, y, Glyphs.BOX_DRAWINGS_DOUBLE_DOWN_AND_RIGHT, color);
		drawChar(x, y + height, Glyphs.BOX_DRAWINGS_DOUBLE_UP_AND_RIGHT, color);
		drawChar(x + width, y, Glyphs.BOX_DRAWINGS_DOUBLE_DOWN_AND_LEFT, color);
		drawChar(x + width, y + height, Glyphs.BOX_DRAWINGS_DOUBLE_UP_AND_LEFT, color);

		drawHorizontalLine(y, x + 1, x + width, Glyphs.BOX_DRAWINGS_DOUBLE_HORIZONTAL, color);
		drawHorizontalLine(y + height, x + 1, x + width, Glyphs.BOX_DRAWINGS_DOUBLE_HORIZONTAL, color);

		drawVerticalLine(x, y + 1, y + height, Glyphs.BOX_DRAWINGS_DOUBLE_VERTICAL, color);
		drawVerticalLine(x + width, y + 1, y + height, Glyphs.BOX_DRAWINGS_DOUBLE_VERTICAL, color);

		Drawer.fillRect(x + 1, y + 1, width - 1, height - 1, false, Glyphs.SPACE, color);
	}

	public static void drawGlyphTable(int color) {
		char i = 0;
		final int length = 8;
		for (int column = 0; column < 8; column++)
			for (int row = 0; row < 32; row++)
				Drawer.drawString(column * length, row, false, i + " = " + (int) i++, color);
	}

}
