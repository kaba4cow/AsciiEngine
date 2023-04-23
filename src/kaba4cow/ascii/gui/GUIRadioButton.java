package kaba4cow.ascii.gui;

import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;

public class GUIRadioButton extends GUIObject {

	private final GUIRadioPanel panel;
	private final int index;

	private final String text;

	public GUIRadioButton(GUIRadioPanel panel, int color, String text) {
		super(panel.frame, color);
		this.panel = panel;
		this.index = panel.addButton(this);
		this.text = text;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked && mouseY == bY && mouseX >= bX && mouseX <= bX + 2)
			panel.setIndex(index);
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		Drawer.draw(x, y, Glyphs.LEFT_PARENTHESIS, color);
		Drawer.draw(x + 2, y, Glyphs.RIGHT_PARENTHESIS, color);
		Drawer.draw(x + 1, y, isSelected() ? Glyphs.BULLET : Glyphs.SPACE, color);
		return Drawer.drawString(x + 4, y, false, width - 4, text, color);
	}

	@Override
	public int totalLines(int width) {
		return Drawer.totalLines(width - 4, text);
	}

	public boolean isSelected() {
		return index == panel.getIndex();
	}

	public int getIndex() {
		return index;
	}

}