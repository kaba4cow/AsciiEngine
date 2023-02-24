package kaba4cow.test;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.gui.GUIButton;
import kaba4cow.ascii.drawing.gui.GUICheckbox;
import kaba4cow.ascii.drawing.gui.GUIFrame;
import kaba4cow.ascii.drawing.gui.GUIProgressBar;
import kaba4cow.ascii.drawing.gui.GUISeparator;
import kaba4cow.ascii.drawing.gui.GUIText;
import kaba4cow.ascii.drawing.gui.GUITextField;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.toolbox.Printer;

public class Test implements MainProgram {

	boolean full = false;

	private GUIFrame frame;

	public Test() {

	}

	@Override
	public void init() {
		frame = new GUIFrame(0x35AFFF);
		new GUIText(frame, -1, "TITLE");
		new GUICheckbox(frame, -1, "A checkbox!", false);
		new GUISeparator(frame, -1, false);
		new GUITextField(frame, -1,
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vehicula metus non efficitur fringilla. Maecenas vulputate at eros non porttitor.");
		new GUIProgressBar(frame, -1, f -> (0.3f * Engine.getElapsedTime()) % 1.01f);

		for (int i = 1; i <= 16; i++)
			new GUIText(frame, -1, "Line #" + i);
		new GUISeparator(frame, -1, false);

		new GUIButton(frame, -1, "A button!", f -> Printer.outln("Button pressed"));
		new GUISeparator(frame, -1, false);
	}

	@Override
	public void update(float dt) {
		frame.update();

		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();

		if (Keyboard.isKeyDown(Keyboard.KEY_ENTER))
			init();
//
//		if (Keyboard.isKeyDown(Keyboard.KEY_C))
//			Display.setDrawCursor(!Display.isDrawCursor());
//		if (Keyboard.isKeyDown(Keyboard.KEY_W))
//			Display.setCursorWaiting(!Display.isCursorWaiting());
//
//		if (Keyboard.isKey(Keyboard.KEY_CONTROL_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_F)) {
//			full = !full;
//			if (full)
//				Display.createFullscreen(true);
//			else
//				Display.createWindowed(40, 30, true);
//		}
	}

	@Override
	public void render() {
		frame.render(0, 0, Display.getWidth() - 1, Display.getHeight(), false);

//		char mouseChar = Display.getChar(Mouse.getTileX(), Mouse.getTileY());
//		Drawer.drawString(Display.getWidth() - 10, 0, false, mouseChar + " = " + (int) mouseChar, 0x222FFF);

		Drawer.drawString(0, 0, false, "" + Engine.getCurrentFramerate(), 0x000FFF);
	}

	public static void main(String[] args) throws Exception {
		Engine.init("Test", 60);
		Display.createWindowed(40, 30, true);
		Engine.start(new Test());
	}

}
