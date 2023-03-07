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
import kaba4cow.ascii.drawing.gui.GUISlider;
import kaba4cow.ascii.drawing.gui.GUIText;
import kaba4cow.ascii.drawing.gui.GUITextField;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.Printer;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Test implements MainProgram {

	boolean full = false;

	private GUIFrame frame;

	public Test() {

	}

	@Override
	public void init() {
		frame = new GUIFrame(0x000FFF, true, true).setTitle("Window");

		new GUIText(frame, -1, "Sliders:");
		for (int i = 1; i <= 3; i++)
			new GUISlider(frame, -1, RNG.randomFloat(1f));
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Checkboxes:");
		for (int i = 1; i <= 3; i++)
			new GUICheckbox(frame, -1, "Checkbox #" + i, RNG.randomBoolean());
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Progress bar:");
		new GUIProgressBar(frame, -1, f -> (0.04f * Engine.getElapsedTime()) % 1.01f);
		new GUISeparator(frame, -1, false);

		new GUITextField(frame, -1,
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vehicula metus non efficitur fringilla. Maecenas vulputate at eros non porttitor. Donec pharetra non ipsum sed dapibus. Sed ullamcorper, sem non pharetra pellentesque, sapien lectus facilisis enim, at lacinia arcu neque a justo. Donec sodales suscipit libero, eu maximus felis faucibus et. Mauris interdum egestas lacus, ut convallis neque convallis ut. Nam lobortis imperdiet orci, a pellentesque enim faucibus sed. Duis sit amet velit dui. Curabitur ornare, nunc ac aliquam malesuada, mauris orci efficitur sem, non mollis metus ipsum nec nisi. Duis laoreet mi a tortor bibendum iaculis. Ut et mollis libero, id mollis libero. In ac viverra lacus. Aenean auctor commodo maximus.");
		new GUISeparator(frame, -1, false);

		for (int i = 1; i <= 3; i++)
			new GUIButton(frame, -1, "Button #" + i, f -> new GUIText(frame, -1, "Line generated"));
		new GUISeparator(frame, -1, false);

		for (int i = 1; i <= 5; i++)
			new GUIText(frame, -1, "Line #" + i);
	}

	@Override
	public void update(float dt) {
		frame.update();

		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();

		if (Keyboard.isKeyDown(Keyboard.KEY_ENTER))
			init();

		if (Keyboard.isKeyDown(Keyboard.KEY_F))
			Display.takeScreenshot();

		if (Mouse.isKeyDown(Mouse.LEFT))
			Printer.println(Mouse.getTileX() + "\t" + Mouse.getTileY());

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
//		frame.render(0, 0, Display.getWidth(), Display.getHeight(), false);
		frame.render();

//		char mouseChar = Display.getChar(Mouse.getTileX(), Mouse.getTileY());
//		Drawer.drawString(Display.getWidth() - 10, 0, false, mouseChar + " = " + (int) mouseChar, 0x222FFF);

		Drawer.drawString(0, 0, false, "" + Engine.getCurrentFramerate(), 0x000FFF);

//		int x = 0, y = 0;
//		int w = 4;
//		int h = Glyphs.numGlyphs() / 4 + 1;
//		int i = 0;
//		for (x = 0; x < w; x++)
//			for (y = 0; y < h; y++) {
//				Drawer.drawString(x * (Display.getWidth() / 4 + 1), y, false, (char) i + " = " + i++, 0x000FFF);
//				if (i >= Glyphs.numGlyphs())
//					break;
//			}
	}

	public static void main(String[] args) throws Exception {
		Engine.init("Test", 60);
		Display.createWindowed(40, 40, true);
		Engine.start(new Test());
	}

}
