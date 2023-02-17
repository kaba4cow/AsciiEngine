package kaba4cow.test;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.drawers.GUIDrawer;
import kaba4cow.ascii.input.Keyboard;

public class Test implements MainProgram {

	boolean full = false;

	public Test() {

	}

	public void init() {

	}

	public void update(float dt) {
		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();

		if (Keyboard.isKeyDown(Keyboard.KEY_C))
			Display.setDrawCursor(!Display.isDrawCursor());
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			Display.setCursorWaiting(!Display.isCursorWaiting());

		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			full = !full;
			if (full)
				Display.createFullscreen(true);
			else
				Display.createWindowed(40, 30, true);
		}

		GUIDrawer.update();
	}

	public void render() {
		GUIDrawer.startDrawing();
		GUIDrawer.setColor(0x148FFF);

		GUIDrawer.addString("TITLE");
		GUIDrawer.addSeparator(GUIDrawer.TYPE_SINGLE);
		GUIDrawer.addString("A string!");
		GUIDrawer.addSeparator();
		GUIDrawer.addString("A very very very very very very long string!!!");
		GUIDrawer.addSeparator(GUIDrawer.TYPE_SINGLE);
		for (int i = 1; i <= 10; i++)
			GUIDrawer.addString("line " + i);

		GUIDrawer.finishDrawing(0, 0, 20, 15, GUIDrawer.TYPE_SINGLE);

//		char mouseChar = Display.getChar(Mouse.getTileX(), Mouse.getTileY());
//		Drawer.drawString(Display.getWidth() - 10, 0, false, mouseChar + " = " + (int) mouseChar, 0x222FFF);

		Drawer.drawString(0, Display.getHeight() - 1, false, "FPS: " + Engine.getCurrentFramerate(), 0x000FFF);
	}

	public void onClose() {

	}

	public static void main(String[] args) throws Exception {
		Engine.init("Test", 60);
		Display.createWindowed(40, 30, true);
		Engine.start(new Test());
	}

}
