package kaba4cow.ascii.drawing.drawers.gui;

import java.util.ArrayList;

import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;

class GUISeparator extends GUIObject {

	private final boolean empty;

	public GUISeparator(ArrayList<GUIObject> list, boolean empty, int color) {
		super(list, color);
		this.empty = empty;
	}

	@Override
	public int draw(GUIFrame currentFrame, int x, int y, int width, int height) {
		if (!empty)
			BoxDrawer.drawHorizontalLine(y, x, x + width, Glyphs.BOX_DRAWINGS_SINGLE_HORIZONTAL, color);
		return 1;
	}

	@Override
	public int totalLines(int width) {
		return 1;
	}

}