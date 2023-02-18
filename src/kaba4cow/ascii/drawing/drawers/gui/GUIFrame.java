package kaba4cow.ascii.drawing.drawers.gui;

import kaba4cow.ascii.input.Mouse;

class GUIFrame {

	public int x;
	public int y;
	public int width;
	public int height;

	public int mouseX;
	public int mouseY;

	public int scroll;
	public boolean clicked;

	public boolean drawn;

	public GUIFrame() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.mouseX = 0;
		this.mouseY = 0;
		this.scroll = 0;
		this.drawn = false;
		this.clicked = false;
	}

	public void update(int mX, int mY, boolean clicked) {
		this.clicked = false;
		if (!drawn)
			return;

		this.mouseX = mX;
		this.mouseY = mY;

		if (mX > x && mX < x + width && mY > y && mY < y + height) {
			scroll += Mouse.getScroll();
			this.clicked = clicked && mY > y + 1 && mY < y + height - 1;
		} else
			this.clicked = false;

		drawn = false;
	}

	public void draw(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.drawn = true;
	}

}