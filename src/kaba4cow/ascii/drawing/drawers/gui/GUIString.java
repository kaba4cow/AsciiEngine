package kaba4cow.ascii.drawing.drawers.gui;

import java.util.ArrayList;

import kaba4cow.ascii.drawing.drawers.Drawer;

class GUIString extends GUIObject {

	private final String string;

	public GUIString(ArrayList<GUIObject> list, String string, int color) {
		super(list, color);
		this.string = string;
	}

	@Override
	public int draw(GUIFrame currentFrame, int x, int y, int width, int height) {
		return Drawer.drawString(x, y, false, width, string, color);
	}

	@Override
	public int totalLines(int width) {
		return Drawer.totalLines(width, string);
	}

}