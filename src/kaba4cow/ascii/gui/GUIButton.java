package kaba4cow.ascii.gui;

import java.util.function.Consumer;

import kaba4cow.ascii.drawing.BoxDrawer;
import kaba4cow.ascii.drawing.Drawer;

public class GUIButton extends GUIObject {

	private String text;

	private final Consumer<?> function;

	public GUIButton(GUIFrame frame, int color, String text, Consumer<?> function) {
		super(frame, color);
		this.text = text;
		this.function = function;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked && mouseInBounds(mouseX, mouseY))
			function.accept(null);
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		BoxDrawer.drawBox(x, y, width - 1, totalLines - 1, true, color);
		return 2 + Drawer.drawString(x + 1, y + 1, false, width - 2, text, color);
	}

	@Override
	public int totalLines(int width) {
		if (text.length() <= width - 2)
			width = text.length() + 2;
		return 2 + Drawer.totalLines(width - 2, text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}