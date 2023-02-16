package kaba4cow.ascii.drawing.drawers;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.toolbox.maths.Maths;

public final class Drawer {

	private static Frame frame;

	private Drawer() {

	}

	public static void setFrame(Frame frame) {
		if (frame != null)
			Drawer.frame = frame;
	}

	public static void resetFrame() {
		Drawer.frame = Display.getFrame();
	}

	public static boolean drawChar(int x, int y, char c, int color) {
		if (x < 0 || x >= frame.width || y < 0 || y >= frame.height)
			return false;
		int index = y * frame.width + x;
		frame.chars[index] = c;
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
		if (centered)
			x -= string.length() / 2;
		for (int c = 0; c < string.length(); c++)
			if (drawChar(x, y, string.charAt(c), color))
				x++;
			else
				break;
	}

	public static void drawStrings(int x, int y, boolean centered, String[] strings, int color) {
		for (int s = 0; s < strings.length; s++)
			drawString(x, y, centered, strings[s], color);
	}

	public static int totalLines(int maxLength, String[] strings) {
		int totalLines = 0;
		for (int s = 0; s < strings.length; s++)
			totalLines += totalLines(maxLength, strings[s]);
		return totalLines;
	}

	public static int totalLines(int maxLength, String string) {
		int length = 0;
		int lines = 1;
		for (int c = 0; c < string.length(); c++) {
			length++;
			if (length == maxLength) {
				length = 0;
				lines++;
			}
		}
		return lines;
	}

	public static int drawStrings(int x, int y, boolean centered, int maxLength, String[] strings, int color) {
		int lines = 0, totalLines = 0;
		for (int s = 0; s < strings.length; s++) {
			lines = drawString(x, y, centered, maxLength, strings[s], color);
			y += lines;
			totalLines += lines;
		}
		return totalLines;
	}

	public static int drawString(int x, int y, boolean centered, int maxLength, String string, int color) {
		if (centered)
			x -= maxLength / 2;
		int length = 0;
		int startX = x;
		int lines = 1;
		for (int c = 0; c < string.length(); c++) {
			drawChar(x, y, string.charAt(c), color);
			length++;
			if (length == maxLength) {
				length = 0;
				x = startX;
				y++;
				lines++;
			} else
				x++;
		}
		return lines;
	}

	public static void drawPattern(int x, int y, boolean centered, char[][] pattern, int color) {
		if (centered) {
			y -= pattern.length / 2;
			x -= pattern[0].length / 2;
		}
		int px, py;
		for (py = 0; py < pattern.length; py++)
			for (px = 0; px < pattern[0].length; px++)
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

	public static void fillCircle(int x, int y, int radius, char c, int color) {
		int radiusSq = radius * radius;
		int distanceSq;
		for (int cx = -radius; cx < radius; cx++)
			for (int cy = -radius; cy < radius; cy++) {
				distanceSq = cx * cx + cy * cy;
				if (distanceSq < radiusSq)
					drawChar(x + cx, y + cy, c, color);
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
