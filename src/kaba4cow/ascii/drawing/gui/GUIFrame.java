package kaba4cow.ascii.drawing.gui;

import java.util.ArrayList;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.maths.Maths;

public class GUIFrame {

	private String title;

	private int x;
	private int y;
	private int width;
	private int height;

	private int scroll;
	private int maxScroll;
	private boolean scrolling;
	private boolean scrollable;

	private int tempX;
	private int tempY;
	private boolean resizing;
	private boolean moving;

	private boolean rendered;

	private final ArrayList<GUIObject> list;

	public int color;
	public final boolean resizable;
	public final boolean moveable;

	public GUIFrame(int color, boolean resizable, boolean moveable) {
		this.title = "";
		this.x = 0;
		this.y = 0;
		this.width = 20;
		this.height = 20;
		this.scroll = 0;
		this.maxScroll = 0;
		this.scrollable = false;
		this.rendered = false;
		this.list = new ArrayList<>();
		this.color = color;
		this.resizable = resizable;
		this.moveable = moveable;
		this.resizing = false;
		this.moving = false;
	}

	public void update() {
		if (!rendered)
			return;

		int mX = Mouse.getTileX();
		int mY = Mouse.getTileY();
		boolean clicked;

		if (Mouse.isKeyDown(Mouse.LEFT)) {
			if (resizable && mY == y + height && mX == x + width) {
				resizing = true;
				tempX = mX;
				tempY = mY;
			} else if (moveable && mY == y && mX >= x && mX <= x + width) {
				moving = true;
				tempX = mX - x;
			}
		}

		if (Mouse.isKeyUp(Mouse.LEFT)) {
			if (resizing) {
				resizing = false;
				if (mX >= Display.getWidth())
					mX = Display.getWidth() - 1;
				if (mY >= Display.getHeight())
					mY = Display.getHeight() - 1;
				width = Maths.max(width + mX - tempX, 5);
				height = Maths.max(height + mY - tempY, 2);
			} else if (moving)
				moving = false;
		}

		if (moving) {
			x = mX - tempX;
			y = mY;
			if (x < 0)
				x = 0;
			if (x >= Display.getWidth())
				x = Display.getWidth() - 1;
			if (y < 0)
				y = 0;
			if (y >= Display.getHeight())
				y = Display.getHeight() - 1;
		}

		if (mX > x && mX < x + width && mY > y && mY < y + height) {
			scroll -= Mouse.getScroll();
			clicked = Mouse.isKeyDown(Mouse.LEFT);
		} else
			clicked = false;

		for (int i = 0; i < list.size(); i++)
			list.get(i).update(mX, mY, clicked);

		if (scrollable && Mouse.isKeyDown(Mouse.LEFT) && mX == x + width - 1 && mY >= y + 2 && mY < y + height)
			scrolling = true;
		if (!scrollable || Mouse.isKeyUp(Mouse.LEFT))
			scrolling = false;

		if (scrolling) {
			float position = (mY - y - 2) / (float) (height - 4);
			scroll = (int) (position * maxScroll);
		}

		rendered = false;
	}

	public void render() {
		if (resizing) {
			int tempWidth = width;
			int tempHeight = height;
			width = Maths.max(width + Mouse.getTileX() - tempX, 5);
			height = Maths.max(height + Mouse.getTileY() - tempY, 2);
			render(x, y, width + 1, height + 1, false);
			width = tempWidth;
			height = tempHeight;
		} else
			render(x, y, width + 1, height + 1, false);
	}

	public void render(int x, int y, int width, int height, boolean centered) {
		width--;
		height--;
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width);

		if (totalHeight > height + 1) {
			scrollable = true;
			totalHeight = 2;
			for (int i = 0; i < list.size(); i++)
				totalHeight += list.get(i).totalLines(width - 1);
		} else
			scrollable = false;

		if (centered) {
			x -= width / 2;
			y -= height / 2;
		}

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rendered = true;

		maxScroll = totalHeight - height;
		if (scroll < 0)
			scroll = 0;
		else if (scroll > maxScroll)
			scroll = maxScroll;

		int currentY = y + 1;
		if (totalHeight > height + 1)
			currentY -= scroll;
		BoxDrawer.drawBox(x, y, width, height, false, color);
		for (int i = 0; i < title.length(); i++) {
			Drawer.drawChar(x + i + 1, y, title.charAt(i), color);
			if (i + 2 >= width)
				break;
		}
		if (scrollable)
			width--;
		Drawer.enableClipping(x, y + 1, x + width, y + height - 1);
		for (int i = 0; i < list.size(); i++)
			currentY += list.get(i).render(x + 1, currentY, width - 1, height);
		Drawer.disableClipping();

		if (scrollable) {
			x = x + width;
			Drawer.drawChar(x, y + 1, Glyphs.BLACK_UP_POINTING_TRIANGLE, color);
			Drawer.drawChar(x, y + height - 1, Glyphs.BLACK_DOWN_POINTING_TRIANGLE, color);
			for (currentY = y + 2; currentY < y + height - 1; currentY++)
				Drawer.drawChar(x, currentY, Glyphs.BOX_DRAWINGS_SINGLE_VERTICAL, color);

			float position = (height - 4) * scroll / (float) maxScroll;
			Drawer.drawChar(x, y + 2 + (int) position, Glyphs.FULL_BLOCK, color);
		}
	}

	public int totalHeight(int width) {
		width--;
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width);
		return totalHeight;
	}

	public GUIFrame add(GUIObject object) {
		if (object != null && !list.contains(object))
			list.add(object);
		return this;
	}

	public GUIFrame resetScrollMin() {
		scroll = 0;
		return this;
	}

	public GUIFrame resetScrollMax() {
		scroll = maxScroll + 128;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public GUIFrame setTitle(String title) {
		this.title = title;
		return this;
	}

	public int getColor() {
		return color;
	}

	public GUIFrame setColor(int color) {
		this.color = color;
		for (int i = 0; i < list.size(); i++)
			list.get(i).setColor(color);
		return this;
	}

}