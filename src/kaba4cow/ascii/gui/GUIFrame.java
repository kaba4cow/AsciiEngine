package kaba4cow.ascii.gui;

import java.util.ArrayList;

import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.core.Window;
import kaba4cow.ascii.drawing.BoxDrawer;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;
import kaba4cow.ascii.toolbox.maths.Maths;

public class GUIFrame {

	private static boolean frameClicked = false;

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
	private boolean clicked;

	private final ArrayList<GUIObject> list;

	private int color;
	private boolean transparent;

	private final boolean resizable;
	private final boolean moveable;

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
		this.clicked = false;
		this.list = new ArrayList<>();
		this.color = color;
		this.resizable = resizable;
		this.moveable = moveable;
		this.resizing = false;
		this.moving = false;
	}

	public void update() {
		if (!rendered || frameClicked) {
			clicked = false;
			return;
		}
		rendered = false;

		int mX = Input.getTileX();
		int mY = Input.getTileY();

		if (Input.isButtonDown(Input.LEFT)) {
			if (resizable && mY == y + height && mX == x + width) {
				resizing = true;
				tempX = mX;
				tempY = mY;
			} else if (moveable && mY == y && mX >= x && mX <= x + width) {
				moving = true;
				tempX = mX - x;
			}
		}

		if (Input.isButtonUp(Input.LEFT)) {
			if (resizing) {
				resizing = false;
				if (mX >= Window.getWidth())
					mX = Window.getWidth() - 1;
				if (mY >= Window.getHeight())
					mY = Window.getHeight() - 1;
				width = Maths.max(width + mX - tempX, 6);
				height = Maths.max(height + mY - tempY, 3);
			} else if (moving)
				moving = false;
		}

		if (moving) {
			x = mX - tempX;
			y = mY;
			if (x < 0)
				x = 0;
			if (x >= Window.getWidth())
				x = Window.getWidth() - 1;
			if (y < 0)
				y = 0;
			if (y >= Window.getHeight())
				y = Window.getHeight() - 1;
		}

		if (mX > x && mX < x + width && mY > y && mY < y + height) {
			if (!Input.isKey(Input.KEY_SHIFT_LEFT))
				scroll -= Input.getScroll();
			clicked = Input.isButtonDown(Input.LEFT);
		} else
			clicked = false;

		for (int i = 0; i < list.size(); i++)
			list.get(i).update(mX, mY, clicked);

		if (scrollable && Input.isButtonDown(Input.LEFT) && mX == x + width - 1 && mY >= y + 2 && mY < y + height)
			scrolling = true;
		if (!scrollable || Input.isButtonUp(Input.LEFT))
			scrolling = false;

		if (scrolling) {
			float position = (mY - y - 2) / (float) (height - 4);
			scroll = (int) (position * maxScroll);
		}

		clicked = Input.isButton(Input.LEFT) && mX >= x && mX <= x + width && mY >= y && mY <= y + height;
		frameClicked = clicked;
	}

	public void render() {
		if (resizing) {
			int tempWidth = width;
			int tempHeight = height;
			width = Maths.max(width + Input.getTileX() - tempX, 6);
			height = Maths.max(height + Input.getTileY() - tempY, 3);
			render(x, y, width + 1, height + 1, false);
			width = tempWidth;
			height = tempHeight;
		} else
			render(x, y, width + 1, height + 1, false);
	}

	public void render(int x, int y, int width, int height, boolean centered) {
		frameClicked = false;
		clicked = false;
		width--;
		height--;
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width - 1);

		if (totalHeight > height) {
			scrollable = true;
			totalHeight = 2;
			for (int i = 0; i < list.size(); i++)
				totalHeight += list.get(i).totalLines(width - 2);
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
		if (!transparent)
			BoxDrawer.drawBox(x, y, width, height, false, color);
		for (int i = 0; i < title.length(); i++) {
			Drawer.draw(x + i + 1, y, title.charAt(i), color);
			if (i + 2 >= width)
				break;
		}
		if (scrollable)
			width--;
		Drawer.enableClipping(x + 1, y + 1, x + width - 1, y + height - 1);
		for (int i = 0; i < list.size(); i++)
			currentY += list.get(i).render(x + 1, currentY, width - 1, height);
		Drawer.disableClipping();

		if (scrollable) {
			x = x + width;
			Drawer.draw(x, y + 1, Glyphs.BLACK_UP_TRIANGLE, color);
			Drawer.draw(x, y + height - 1, Glyphs.BLACK_DOWN_TRIANGLE, color);
			for (currentY = y + 2; currentY < y + height - 1; currentY++)
				Drawer.draw(x, currentY, Glyphs.BOX_DRAWINGS_SINGLE_VERTICAL, color);

			float position = (height - 4) * scroll / (float) maxScroll;
			Drawer.draw(x, y + 2 + (int) position, Glyphs.FULL_BLOCK, color);
		}
	}

	public int totalHeight(int width) {
		width -= 2;
		int totalHeight = 2;
		for (int i = 0; i < list.size(); i++)
			totalHeight += list.get(i).totalLines(width);
		return totalHeight;
	}

	public GUIFrame clear() {
		list.clear();
		return this;
	}

	public GUIFrame add(GUIObject object) {
		if (object != null && !list.contains(object))
			list.add(object);
		return this;
	}

	public GUIFrame remove(GUIObject object) {
		if (object != null && list.contains(object))
			list.remove(object);
		return this;
	}

	public GUIFrame resetScrollMin() {
		scroll = 0;
		return this;
	}

	public GUIFrame resetScrollMax() {
		scroll = maxScroll;
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

	public boolean isTransparent() {
		return transparent;
	}

	public GUIFrame setTransparent(boolean transparent) {
		this.transparent = transparent;
		return this;
	}

	public boolean wasClicked() {
		return clicked;
	}

	public static boolean framesClicked() {
		return frameClicked;
	}

	public int getX() {
		return x;
	}

	public GUIFrame setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public GUIFrame setY(int y) {
		this.y = y;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public GUIFrame setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public GUIFrame setHeight(int height) {
		this.height = height;
		return this;
	}

}