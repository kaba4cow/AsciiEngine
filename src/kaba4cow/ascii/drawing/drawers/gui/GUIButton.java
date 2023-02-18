package kaba4cow.ascii.drawing.drawers.gui;

import java.util.ArrayList;
import java.util.function.Consumer;

import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;

class GUIButton extends GUIObject {

	private final String string;

	private final Consumer<?> function;

	public GUIButton(ArrayList<GUIObject> list, String string, Consumer<?> function, int color) {
		super(list, color);
		this.string = string;
		this.function = function;
	}

	@Override
	public int draw(GUIFrame currentFrame, int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		if (currentFrame.clicked) {
			if (currentFrame.mouseX >= x && currentFrame.mouseX < x + width - 1 && currentFrame.mouseY >= y
					&& currentFrame.mouseY < y + totalLines)
				function.accept(null);
		}
		BoxDrawer.drawBox(x, y, width - 1, totalLines - 1, false, color);
		return 2 + Drawer.drawString(x + 1, y + 1, false, width, string, color);
	}

	@Override
	public int totalLines(int width) {
		return 2 + Drawer.totalLines(width, string);
	}

}