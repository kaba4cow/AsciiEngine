package kaba4cow.ascii.drawing.drawers;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.toolbox.maths.Maths;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2i;

public final class Drawer {

	private static Frame frame;

	private static final Vector2i clipStart = new Vector2i();
	private static final Vector2i clipEnd = new Vector2i();
	private static boolean clipEnable = false;

	private Drawer() {

	}

	public static void enableClipping(int x0, int y0, int x1, int y1) {
		clipEnable = true;
		clipStart.x = x0;
		clipStart.y = y0;
		clipEnd.x = x1;
		clipEnd.y = y1;
	}

	public static void disableClipping() {
		clipEnable = false;
	}

	public static Frame getCurrentFrame() {
		return frame;
	}

	public static void setFrame(Frame frame) {
		if (frame != null)
			Drawer.frame = frame;
	}

	public static void resetFrame() {
		Drawer.frame = Display.getFrame();
	}

	public static boolean isClipped(int x, int y) {
		if (!clipEnable)
			return false;
		return x < clipStart.x || x > clipEnd.x || y < clipStart.y || y > clipEnd.y;
	}

	public static boolean drawChar(int x, int y, char c, int color) {
		if (x < 0 || x >= frame.width || y < 0 || y >= frame.height || isClipped(x, y))
			return false;
		int index = y * frame.width + x;
		if ((color & 0xFF000000) == 0)
			frame.chars[index] = c;
		if ((color & 0xF0000000) > 0)
			frame.colors[index] = (frame.colors[index] & 0xFFF000) | (color & 0x000FFF);
		else if ((color & 0x0F000000) > 0)
			frame.colors[index] = (frame.colors[index] & 0x000FFF) | (color & 0xFFF000);
		else
			frame.colors[index] = color;
		return true;
	}

	public static void drawFrame(int x, int y, boolean centered, Frame frame) {
		if (Drawer.frame == frame || frame == null)
			return;
		if (centered)
			x -= frame.width / 2;
		int fx, fy;
		int fi = 0;
		for (fy = 0; fy < frame.height; fy++)
			for (fx = 0; fx < frame.width; fx++) {
				drawChar(x + fx, y + fy, frame.chars[fi], frame.colors[fi]);
				fi++;
			}
	}

	public static void drawString(int x, int y, boolean centered, String string, int color) {
		if (string == null)
			return;
		if (centered)
			x -= string.length() / 2;
		for (int c = 0; c < string.length(); c++)
			drawChar(x++, y, string.charAt(c), color);
	}

	public static void drawStrings(int x, int y, boolean centered, String[] strings, int color) {
		if (strings == null)
			return;
		for (int s = 0; s < strings.length; s++)
			drawString(x, y, centered, strings[s], color);
	}

	public static int totalLines(int maxLength, String[] strings) {
		if (strings == null)
			return 1;
		int totalLines = 0;
		for (int s = 0; s < strings.length; s++)
			totalLines += totalLines(maxLength, strings[s]);
		return totalLines;
	}

	public static int totalLines(int maxLength, String string) {
		if (string == null)
			return 1;
		int length = 0;
		int lines = 1;
		for (int c = 0; c < string.length(); c++) {
			length++;
			if (length == maxLength && c < string.length() - 1) {
				length = 0;
				lines++;
			}
		}
		return lines;
	}

	public static int drawStrings(int x, int y, boolean centered, int maxLength, String[] strings, int color) {
		if (strings == null)
			return 1;
		int lines = 0, totalLines = 0;
		for (int s = 0; s < strings.length; s++) {
			lines = drawString(x, y, centered, maxLength, strings[s], color);
			y += lines;
			totalLines += lines;
		}
		return totalLines;
	}

	public static int drawString(int x, int y, boolean centered, int maxLength, String string, int color) {
		if (string == null)
			return 1;
		if (centered)
			x -= maxLength / 2;
		int length = 0;
		int startX = x;
		int lines = 1;
		for (int c = 0; c < string.length(); c++) {
			drawChar(x, y, string.charAt(c), color);
			length++;
			if (length == maxLength && c < string.length() - 1) {
				length = 0;
				x = startX;
				y++;
				lines++;
			} else
				x++;
		}
		return lines;
	}

	public static int drawBigString(int x, int y, boolean centered, String string, char c, int color) {
		if (string == null)
			return 1;

		int size = Display.getCharSize();
		int length = string.length();

		boolean[][] map = Display.getGlyphSheetMap();
		int columns = map.length / size;

		char glyph;
		int i, tX, tY, mX, mY, minX, maxX;

		int totalWidth = 0;

		for (i = 0; i < length; i++) {
			minX = size;
			maxX = 0;
			glyph = string.charAt(i);

			tX = glyph % columns;
			tY = glyph / columns;
			for (mY = 0; mY < size; mY++)
				for (mX = 0; mX < size; mX++)
					if (map[mX + size * tX][mY + size * tY]) {
						if (mX < minX)
							minX = mX;
						else if (mX > maxX)
							maxX = mX;
					}
			if (minX == size) {
				minX = 0;
				maxX = size / 2;
			}
			minX--;
			maxX++;

			totalWidth -= minX;
			totalWidth += maxX;
		}

		if (centered) {
			x -= totalWidth / 2;
			y -= size / 2;
		}

		for (i = 0; i < length; i++) {
			minX = size;
			maxX = 0;
			glyph = string.charAt(i);

			tX = glyph % columns;
			tY = glyph / columns;
			for (mY = 0; mY < size; mY++)
				for (mX = 0; mX < size; mX++)
					if (map[mX + size * tX][mY + size * tY]) {
						if (mX < minX)
							minX = mX;
						else if (mX > maxX)
							maxX = mX;
					}
			if (minX == size) {
				minX = 0;
				maxX = size / 2;
			}
			minX--;
			maxX++;

			x -= minX;

			for (mY = 0; mY < size; mY++)
				for (mX = 0; mX < size; mX++)
					if (map[mX + size * tX][mY + size * tY])
						drawChar(x + mX, y + mY, c, color);
			x += maxX;
		}

		return totalWidth;
	}

	public static int totalWidth(String string) {
		if (string == null)
			return 1;

		int size = Display.getCharSize();
		int length = string.length();

		boolean[][] map = Display.getGlyphSheetMap();
		int columns = map.length / size;

		char glyph;
		int i, tX, tY, mX, mY, minX, maxX;

		int totalWidth = 0;

		for (i = 0; i < length; i++) {
			minX = size;
			maxX = 0;
			glyph = string.charAt(i);

			tX = glyph % columns;
			tY = glyph / columns;
			for (mY = 0; mY < size; mY++)
				for (mX = 0; mX < size; mX++)
					if (map[mX + size * tX][mY + size * tY]) {
						if (mX < minX)
							minX = mX;
						else if (mX > maxX)
							maxX = mX;
					}
			if (minX == size) {
				minX = 0;
				maxX = size / 2;
			}
			minX--;
			maxX++;

			totalWidth -= minX;
			totalWidth += maxX;
		}

		return totalWidth;
	}

	public static void drawPattern(int x, int y, boolean centered, char[][] pattern, int color) {
		if (pattern == null)
			return;

		if (centered) {
			y -= pattern.length / 2;
			x -= pattern[0].length / 2;
		}
		int px, py;
		for (py = 0; py < pattern.length; py++)
			for (px = 0; px < pattern[py].length; px++)
				drawChar(x + px, y + py, pattern[py][px], color);
	}

	public static void drawRect(int x, int y, int width, int height, boolean centered, char c, int color) {
		if (centered) {
			x -= width / 2;
			y -= height / 2;
		}
		drawLine(x, y, x + width, y, c, color);
		drawLine(x, y + height, x + width + 1, y + height, c, color);
		drawLine(x, y, x, y + height, c, color);
		drawLine(x + width, y, x + width, y + height, c, color);
	}

	public static void fillRect(int x, int y, int width, int height, boolean centered, char c, int color) {
		if (centered) {
			x -= width / 2;
			y -= height / 2;
		}
		for (int rx = 0; rx < width; rx++)
			for (int ry = 0; ry < height; ry++)
				drawChar(x + rx, y + ry, c, color);
	}

	private static void drawCirclePoints(int centerX, int centerY, int x, int y, char c, int color) {
		drawChar(x + centerX, y + centerY, c, color);
		drawChar(x + centerX, -y + centerY, c, color);
		drawChar(-x + centerX, -y + centerY, c, color);
		drawChar(-x + centerX, y + centerY, c, color);
		drawChar(y + centerX, x + centerY, c, color);
		drawChar(y + centerX, -x + centerY, c, color);
		drawChar(-y + centerX, -x + centerY, c, color);
		drawChar(-y + centerX, x + centerY, c, color);
	}

	public static void drawCircle(int centerX, int centerY, int radius, char c, int color) {
		int x = 0;
		int y = radius;

		int d = 3 - 2 * radius;

		drawCirclePoints(centerX, centerY, x, y, c, color);
		while (x <= y) {
			if (d <= 0)
				d = d + 4 * x + 6;
			else {
				d = d + 4 * x - 4 * y + 10;
				y--;
			}

			x++;
			drawCirclePoints(centerX, centerY, x, y, c, color);
		}
	}

	private static void fillCirclePoints(int centerX, int centerY, int x, int y, char c, int color) {
		int i;
		for (i = -x; i <= x; i++) {
			drawChar(i + centerX, -y + centerY, c, color);
			drawChar(i + centerX, y + centerY, c, color);
		}
		for (i = -y; i <= y; i++) {
			drawChar(i + centerX, -x + centerY, c, color);
			drawChar(i + centerX, x + centerY, c, color);
		}
	}

	public static void fillCircle(int centerX, int centerY, int radius, char c, int color) {
		int x = 0;
		int y = radius;

		int d = 3 - 2 * radius;

		fillCirclePoints(centerX, centerY, x, y, c, color);
		while (x <= y) {
			if (d <= 0)
				d = d + 4 * x + 6;
			else {
				d = d + 4 * x - 4 * y + 10;
				y--;
			}

			x++;
			fillCirclePoints(centerX, centerY, x, y, c, color);
		}
	}

	public static void drawLine(int x0, int y0, int x1, int y1, char c, int color) {
		if (x0 == x1 && y0 == y1)
			drawChar(x0, y0, c, color);
		else {
			int dx = Maths.abs(x1 - x0);
			int dy = Maths.abs(y1 - y0);
			int sx = x0 < x1 ? 1 : -1;
			int sy = y0 < y1 ? 1 : -1;
			int e = dx - dy;
			int e2 = 0;
			drawChar(x0, y0, c, color);
			while (true) {
				e2 = 2 * e;
				if (e2 > -dy) {
					e -= dy;
					x0 += sx;
				}
				if (e2 < dx) {
					e += dx;
					y0 += sy;
				}
				drawChar(x0, y0, c, color);
				if (x0 == x1 && y0 == y1)
					return;
			}
		}
	}

}
