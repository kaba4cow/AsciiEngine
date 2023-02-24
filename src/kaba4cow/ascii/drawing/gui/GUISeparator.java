package kaba4cow.ascii.drawing.gui;

import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;

public class GUISeparator extends GUIObject {

	private final boolean empty;

	public GUISeparator(GUIFrame frame, int color, boolean empty) {
		super(frame, color);
		this.empty = empty;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		if (!empty)
			BoxDrawer.drawHorizontalLine(y, x, x + width, Glyphs.BOX_DRAWINGS_SINGLE_HORIZONTAL, color);
		return 1;
	}

	@Override
	public int totalLines(int width) {
		return 1;
	}

}