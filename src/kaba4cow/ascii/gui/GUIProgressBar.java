package kaba4cow.ascii.gui;

import java.util.function.Function;

import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;

public class GUIProgressBar extends GUIObject {

	private final Function<?, Float> function;

	public GUIProgressBar(GUIFrame frame, int color, Function<?, Float> function) {
		super(frame, color);
		this.function = function;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		Drawer.draw(x, y, Glyphs.LEFT_SQUARE_BRACKET, color);
		Drawer.draw(x + width - 1, y, Glyphs.RIGHT_SQUARE_BRACKET, color);
		float progress = function.apply(null);
		int length = (int) (progress * (width - 2));
		for (int i = 0; i < width - 2; i++)
			Drawer.draw(x + i + 1, y, i < length ? Glyphs.BLACK_SQUARE : Glyphs.HYPHEN_MINUS, color);
		return 1;
	}

	@Override
	public int totalLines(int width) {
		return 1;
	}

}