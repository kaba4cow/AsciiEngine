package kaba4cow.ascii.drawing.drawers.gui;

import java.util.ArrayList;

abstract class GUIObject {

	public final int color;

	public GUIObject(ArrayList<GUIObject> list, int color) {
		this.color = color;
		list.add(this);
	}

	public abstract int draw(GUIFrame currentFrame, int x, int y, int width, int height);

	public abstract int totalLines(int width);

}
