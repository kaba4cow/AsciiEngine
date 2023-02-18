package kaba4cow.ascii.drawing.drawers.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Mouse;

public final class GUIDrawer {

	private static final HashMap<String, GUIFrame> map = new HashMap<>();
	private static final ArrayList<GUIObject> list = new ArrayList<>();
	private static GUIFrame currentFrame = null;

	private static int color;

	private GUIDrawer() {

	}

	public static void startDrawing(String tag) {
		if (map.containsKey(tag))
			currentFrame = map.get(tag);
		else {
			currentFrame = new GUIFrame();
			map.put(tag, currentFrame);
		}
		list.clear();
		color = 0x000FFF;
	}

	public static void setColor(int color) {
		GUIDrawer.color = color;
	}

	public static void addLineSeparator() {
		new GUISeparator(list, false, color);
	}

	public static void addEmptySeparator() {
		new GUISeparator(list, true, color);
	}

	public static void addString(String string) {
		new GUIString(list, string, color);
	}

	public static void addButton(String string, Consumer<?> function) {
		new GUIButton(list, string, function, color);
	}

	public static void update() {
		int mX = Mouse.getTileX();
		int mY = Mouse.getTileY();
		boolean clicked = Mouse.isKeyDown(Mouse.LEFT);
		for (String tag : map.keySet())
			map.get(tag).update(mX, mY, clicked);
	}

	public static void finishDrawing(int x, int y, int width, int height) {
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width);

		if (currentFrame.scroll > 0)
			currentFrame.scroll = 0;
		else if (currentFrame.scroll <= height - totalHeight - 1)
			currentFrame.scroll = height - totalHeight;

		int currentY = y + 1;
		if (totalHeight > height)
			currentY += currentFrame.scroll;
		BoxDrawer.drawBox(x, y, width, height - 1, false, color);
		Drawer.enableClipping(x, y + 1, x + width, y + height - 2);
		for (int i = 0; i < list.size(); i++)
			currentY += list.get(i).draw(currentFrame, x + 1, currentY, width - 1, height - 1);
		Drawer.disableClipping();

		currentFrame.draw(x, y, width, height);

		currentFrame = null;
		list.clear();
	}

}
