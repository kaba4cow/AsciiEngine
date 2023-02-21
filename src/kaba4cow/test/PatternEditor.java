package kaba4cow.test;

import java.io.File;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;

public class PatternEditor implements MainProgram {

	private int charIndex;

	private Frame frame;

	public PatternEditor() {

	}

	@Override
	public void init() {
		charIndex = 0;

		frame = Frame.read(new File("field.frm"));
	}

	@Override
	public void update(float dt) {
		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();

		charIndex -= Mouse.getScroll();
		if (charIndex < 0)
			charIndex = Glyphs.numGlyphs() - 1;
		else if (charIndex >= Glyphs.numGlyphs())
			charIndex = 0;

		int x = Mouse.getTileX();
		int y = Mouse.getTileY();
		if (Mouse.isKeyDown(Mouse.LEFT)) {
			if (x < Display.getWidth() - 8) {
				Drawer.setFrame(frame);
				Drawer.drawChar(x, y, (char) charIndex, 0x000FFF);
				Drawer.resetFrame();
			}
		} else if (Mouse.isKeyDown(Mouse.RIGHT)) {
			if (x < Display.getWidth() - 8) {
				Drawer.setFrame(frame);
				Drawer.drawChar(x, y, Glyphs.SPACE, 0x000000);
				Drawer.resetFrame();
			}
		}

		if (Keyboard.isKey(Keyboard.KEY_CONTROL_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_S))
			Frame.write(frame, new File("field.frm"));
	}

	@Override
	public void render() {
		Display.setBackground(Glyphs.SPACE, 0x111000);

		int x = Display.getWidth() - 9;
		int center = Display.getHeight() / 4;
		int num = Glyphs.numGlyphs();
		BoxDrawer.drawBox(x - 1, 0, 9, Display.getHeight() / 2, false, 0x000FFF);
		for (int y = 1; y < Display.getHeight() / 2 - 1; y++) {
			int c = y + charIndex - center;
			if (c < 0 || c >= num)
				c = (num + c % num) % num;
			Drawer.drawString(x, y, false, (char) c + " - " + c, y == center ? 0x111FFF : 0x000CCC);
		}

		Drawer.drawFrame(0, 0, false, frame);
	}

	@Override
	public void onClose() {

	}

	public static void main(String[] args) {
		Engine.init("PatternEditor", 60);
		Display.createFullscreen(true);
//		Display.createWindowed(50, 40, true);
		Engine.start(new PatternEditor());
	}

}
