package kaba4cow.ascii.drawing.gui;

import java.util.HashMap;

import kaba4cow.ascii.drawing.drawers.Drawer;

public class GUIColorText extends GUIObject {

	private String text;
	private HashMap<Integer, Integer> colors;

	public GUIColorText(GUIFrame frame) {
		super(frame, -1);
		this.text = "";
		this.colors = new HashMap<>();
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int length = 0;
		int startX = x;
		int lines = 1;
		int color = colors.get(0);
		for (int c = 0; c < text.length(); c++) {
			if (colors.containsKey(c))
				color = colors.get(c);
			Drawer.draw(x, y, text.charAt(c), color);
			length++;
			if (length == width && c < text.length() - 1) {
				length = 0;
				x = startX;
				y++;
				lines++;
			} else
				x++;
		}
		return lines;
	}

	@Override
	public int totalLines(int width) {
		return Drawer.totalLines(width, text);
	}

	public String getText() {
		return text;
	}

	public GUIColorText addText(String text, int color) {
		if (color == -1)
			color = this.color;
		this.colors.put(this.text.length(), color);
		this.text += text;
		return this;
	}

}