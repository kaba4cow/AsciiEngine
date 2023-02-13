package kaba4cow.test;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;

public class Test implements MainProgram {

	public Test() {

	}

	public void init() {

	}

	public void update(float dt) {
		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();
		if (Keyboard.isKeyDown(Keyboard.KEY_C))
			Display.drawCursor(!Display.isDrawCursor());
	}

	public void render() {
		Drawer.drawString(0, Display.getHeight() - 1, false, "FPS: " + Engine.getCurrentFramerate(), 0x000FFF);
//		BoxDrawer.drawGlyphTable(0x111FFF);

		char mouseChar = Display.getChar(Mouse.getTileX(), Mouse.getTileY());
		Drawer.drawString(Display.getWidth() - 10, 0, false, mouseChar + " = " + (int) mouseChar, 0x222FFF);
	}

	public void onClose() {

	}

	public static void main(String[] args) {
		Engine.init("Test", 60, 90, 40, false);
		Engine.start(new Test());
	}

}
