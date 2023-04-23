package kaba4cow.ascii.gui;

import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;

public class GUISlider extends GUIObject {

	private float position;

	public GUISlider(GUIFrame frame, int color, float position) {
		super(frame, color);
		this.position = position;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked && mouseInBounds(mouseX, mouseY))
			position = (mouseX - bX) / (float) (bWidth - 1);
	}

	@Override
	public int render(int x, int y, int width, int height) {
		updateBounds(x, y, width, 1);
		Drawer.draw(x, y, Glyphs.BLACK_LEFT_POINTER, color);
		Drawer.draw(x + width - 1, y, Glyphs.BLACK_RIGHT_POINTER, color);
		int length = (int) (position * (width - 2));
		char c;
		for (int i = 0; i < width - 2; i++) {
			if (i == length)
				c = Glyphs.VERTICAL_LINE;
			else
				c = i < length ? Glyphs.BLACK_SQUARE : Glyphs.HYPHEN_MINUS;
			Drawer.draw(x + i + 1, y, c, color);
		}
		return 1;
	}

	@Override
	public int totalLines(int width) {
		return 1;
	}

	public float getPosition() {
		return position;
	}

	public void setPosition(float position) {
		this.position = position;
	}

}