package kaba4cow.ascii.drawing.gui;

import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;

public class GUICheckbox extends GUIObject {

	private final String text;

	private boolean selected;

	public GUICheckbox(GUIFrame frame, int color, String text, boolean selected) {
		super(frame, color);
		this.text = text;
		this.selected = selected;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (frame.clicked && mouseInBounds(mouseX, mouseY))
			selected = !selected;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		Drawer.drawChar(x, y, selected ? Glyphs.WHITE_SUN_WITH_RAYS : Glyphs.WHITE_CIRCLE, color);
		return Drawer.drawString(x + 2, y, false, width - 2, text, color);
	}

	@Override
	public int totalLines(int width) {
		return Drawer.totalLines(width - 2, text);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}