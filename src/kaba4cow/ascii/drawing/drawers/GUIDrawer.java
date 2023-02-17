package kaba4cow.ascii.drawing.drawers;

import java.util.ArrayList;

import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Mouse;

public final class GUIDrawer {

	public static final int TYPE_EMPTY = 0;
	public static final int TYPE_SINGLE = 1;
	public static final int TYPE_DOUBLE = 2;

	private static GUIObject current = null;
	private static ArrayList<GUIObject> list = new ArrayList<>();

	private static int color;
	private static int scroll;

	private GUIDrawer() {

	}

	public static void startDrawing() {
		list.clear();
		color = 0x000FFF;
	}

	public static void setColor(int color) {
		GUIDrawer.color = color;
	}

	public static void addSeparator(int type) {
		new GUISeparator(type, color);
	}

	public static void addSeparator() {
		new GUISeparator(TYPE_EMPTY, color);
	}

	public static void addString(String string) {
		new GUIString(string, color);
	}

	public static void update() {
		scroll += Mouse.getScroll();
	}

	public static void finishDrawing(int x, int y, int width, int height, int type) {
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width);

		if (scroll > 0)
			scroll = 0;
		else if (scroll <= height - totalHeight)
			scroll = height - totalHeight + 1;

		int currentY = y + 1;
		if (totalHeight > height)
			currentY += scroll;
		BoxDrawer.drawBox(x, y, width, height, type == TYPE_SINGLE, color);
		for (int i = 0; i < list.size(); i++) {
			current = list.get(i);
			if (currentY <= y) {
				currentY += current.totalLines(width - 1);
				continue;
			}
			currentY += current.draw(x + 1, currentY, width - 1, height - 1);
			if (currentY >= height)
				break;
		}

		current = null;
		list.clear();
	}

	private static char getChar(int type) {
		if (type == TYPE_SINGLE)
			return Glyphs.BOX_DRAWINGS_SINGLE_HORIZONTAL;
		if (type == TYPE_DOUBLE)
			return Glyphs.BOX_DRAWINGS_DOUBLE_HORIZONTAL;
		return Glyphs.SPACE;
	}

	private static abstract class GUIObject {

		public final int color;

		public GUIObject(int color) {
			this.color = color;
			GUIDrawer.list.add(this);
		}

		public abstract int draw(int x, int y, int width, int height);

		public abstract int totalLines(int width);

	}

	private static class GUISeparator extends GUIObject {

		private final int type;

		public GUISeparator(int type, int color) {
			super(color);
			this.type = type;
		}

		@Override
		public int draw(int x, int y, int width, int height) {
			char c = getChar(type);
			if (c == Glyphs.SPACE)
				return 1;
			BoxDrawer.drawHorizontalLine(y, x, x + width, c, color);
			return 1;
		}

		@Override
		public int totalLines(int width) {
			return 1;
		}

	}

	private static class GUIString extends GUIObject {

		private final String string;

		public GUIString(String string, int color) {
			super(color);
			this.string = string;
		}

		@Override
		public int draw(int x, int y, int width, int height) {
			return Drawer.drawString(x, y, false, width, string, color);
		}

		@Override
		public int totalLines(int width) {
			return Drawer.totalLines(width, string);
		}

	}

}
