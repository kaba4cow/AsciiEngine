package kaba4cow.ascii.drawing.drawers;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.toolbox.maths.Maths;

public final class GUIDrawer {

	private GUIDrawer() {

	}

	public static void drawMessage(int color, String... strings) {
		if (strings == null || strings.length == 0)
			return;

		int x = Display.getWidth() / 2;
		int y = Display.getHeight() / 3;
		int width = Display.getWidth() / 2 + 2;

		int maxLength = 0;
		for (int s = 0; s < strings.length; s++)
			maxLength = Maths.max(maxLength, strings[s].length());
		y -= strings.length / 2 + 1;

		int lines = Drawer.drawStrings(x, y + 1, true, width - 4, strings, color);
		BoxDrawer.drawBoxSingle(x - width / 2, y, width, lines + 2, color);
	}

}
