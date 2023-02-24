package kaba4cow.ascii.drawing.gui;

import java.util.ArrayList;

import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Mouse;

public class GUIFrame {

	public int x;
	public int y;
	public int width;
	public int height;

	public int scroll;
	public boolean clicked;

	public boolean drawn;

	private final ArrayList<GUIObject> list;
	public final int color;

	public GUIFrame(int color) {
		this.list = new ArrayList<>();
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.scroll = 0;
		this.drawn = false;
		this.clicked = false;
		this.color = color;
	}

	public void update() {
		this.clicked = false;
		if (!drawn)
			return;

		int mX = Mouse.getTileX();
		int mY = Mouse.getTileY();
		if (mX > x && mX < x + width && mY > y && mY < y + height - 1) {
			this.scroll += Mouse.getScroll();
			this.clicked = Mouse.isKeyDown(Mouse.LEFT);
		} else
			this.clicked = false;

		for (int i = 0; i < list.size(); i++)
			list.get(i).update(mX, mY, clicked);

		this.drawn = false;
	}

	public void render(int x, int y, int width, int height, boolean centered) {
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.drawn = true;

		if (centered) {
			x -= width / 2;
			y -= totalHeight / 2;
		}

		if (scroll > 0)
			scroll = 0;
		else if (scroll <= height - totalHeight - 1)
			scroll = height - totalHeight;

		int currentY = y + 1;
		if (totalHeight > height)
			currentY += scroll;
		BoxDrawer.drawBox(x, y, width, height - 1, false, color);
		Drawer.enableClipping(x, y + 1, x + width, y + height - 2);
		for (int i = 0; i < list.size(); i++)
			currentY += list.get(i).render(x + 1, currentY, width - 1, height - 1);
		Drawer.disableClipping();
	}

	public void add(GUIObject object) {
		if (object != null && !list.contains(object))
			list.add(object);
	}

}