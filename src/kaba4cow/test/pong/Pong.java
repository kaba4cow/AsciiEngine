package kaba4cow.test.pong;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Keyboard;

public class Pong implements MainProgram {

	private Ball ball;
	private Plate[] plates;

	private boolean pause;

	public Pong() {

	}

	@Override
	public void init() {
		ball = new Ball();
		ball.reset();
		plates = new Plate[] { new Plate(true), new Plate(false) };

		pause = true;
	}

	@Override
	public void update(float dt) {
		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			pause = !pause;
		if (pause)
			return;

		for (Plate plate : plates)
			plate.update(dt, ball);
		ball.update(dt, plates);
	}

	@Override
	public void render() {
		for (Plate plate : plates)
			plate.render();
		ball.render();

		if (pause) {
			BoxDrawer.drawBoxDouble(Display.getWidth() / 2 - 3, Display.getHeight() / 2 - 1, 7, 3, 0x000FFF);
			Drawer.drawString(Display.getWidth() / 2, Display.getHeight() / 2, true, "PAUSE", 0x000FFF);
		}
	}

	@Override
	public void onClose() {

	}

	public static void main(String[] args) {
		Engine.init("ASCII Pong", 30, 40, 40, true);
		Engine.start(new Pong());
	}

}
