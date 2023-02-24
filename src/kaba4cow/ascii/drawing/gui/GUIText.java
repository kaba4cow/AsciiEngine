package kaba4cow.ascii.drawing.gui;

import kaba4cow.ascii.drawing.drawers.Drawer;

public class GUIText extends GUIObject {

	private String text;

	public GUIText(GUIFrame frame, int color, String text) {
		super(frame, color);
		this.text = text;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		return Drawer.drawString(x, y, false, width, text, color);
	}

	@Override
	public int totalLines(int width) {
		return Drawer.totalLines(width, text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}