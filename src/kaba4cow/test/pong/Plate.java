package kaba4cow.test.pong;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.toolbox.maths.Maths;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2f;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Plate {

	private static final int SIZE = 8;

	private final boolean top;
	private Vector2f pos;

	private int score;

	public Plate(boolean top) {
		this.pos = new Vector2f(Display.getWidth() / 2, 0);
		this.top = top;
		this.score = 0;
		if (top)
			pos.y = 2;
		else
			pos.y = Display.getHeight() - 3;
	}

	public void update(float dt, Ball ball) {
		float speed = 16f * dt;
		if (top) {
			if (ball.isDirectionUp() && RNG.randomBoolean()) {
				float x = ball.getX();
				if (x < pos.x)
					pos.x -= speed;
				else if (x > pos.x)
					pos.x += speed;
			}
		} else {
			if (Keyboard.isKey(Keyboard.KEY_A))
				pos.x -= speed;
			if (Keyboard.isKey(Keyboard.KEY_D))
				pos.x += speed;
		}
		pos.x = Maths.limit(pos.x, SIZE / 2, Display.getWidth() - 1 - SIZE / 2);
	}

	public void render() {
		Drawer.drawLine((int) pos.x - SIZE / 2, (int) pos.y, (int) pos.x + SIZE / 2, (int) pos.y, Glyphs.BLACK_SQUARE,
				0xFFF000);

		int scoreY = top ? 0 : (Display.getHeight() - 1);
		Drawer.drawString(Display.getWidth() / 2, scoreY, true, "SCORE: " + score, 0x000FFF);
	}

	public boolean isColliding(Vector2f ballPos) {
		return ballPos.x >= pos.x - SIZE / 2 && ballPos.x < pos.x + SIZE / 2 + 1f && ballPos.y >= pos.y
				&& ballPos.y < pos.y + 1f;
	}

	public float getSignedDistance(float x) {
		return (x - pos.x) / (SIZE / 2);
	}

	public void score() {
		score++;
	}

}
