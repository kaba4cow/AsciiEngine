package kaba4cow.test;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.core.Window;
import kaba4cow.ascii.drawing.BoxDrawer;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;
import kaba4cow.ascii.toolbox.maths.Maths;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2f;
import kaba4cow.ascii.toolbox.maths.vectors.Vectors;
import kaba4cow.ascii.toolbox.rng.RNG;

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
		if (Input.isKey(Input.KEY_ESCAPE))
			Engine.requestClose();
		if (Input.isKeyDown(Input.KEY_SPACE))
			pause = !pause;
		if (pause)
			return;

		for (Plate plate : plates)
			plate.update(dt, ball);
		ball.update(dt, plates);
	}

	@Override
	public void render() {
		ball.render();
		for (Plate plate : plates)
			plate.render();

		if (pause) {
			BoxDrawer.drawBox(Window.getWidth() / 2 - 3, Window.getHeight() / 2 - 1, 7, 3, false, 0x000FFF);
			Drawer.drawString(Window.getWidth() / 2, Window.getHeight() / 2, true, "PAUSE", 0x000FFF);
		}
	}

	public static void main(String[] args) {
		Engine.init("Pong", 16, 30);
		Window.createWindowed(40, 40);
		Engine.start(new Pong());
	}

	private static class Plate {

		private static final int SIZE = 8;

		private final boolean top;
		private Vector2f pos;

		private int score;

		public Plate(boolean top) {
			this.pos = new Vector2f(Window.getWidth() / 2, 0);
			this.top = top;
			this.score = 0;
			if (top)
				pos.y = 2;
			else
				pos.y = Window.getHeight() - 3;
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
				if (Input.isKey(Input.KEY_A))
					pos.x -= speed;
				if (Input.isKey(Input.KEY_D))
					pos.x += speed;
			}
			pos.x = Maths.limit(pos.x, SIZE / 2, Window.getWidth() - 1 - SIZE / 2);
		}

		public void render() {
			Drawer.drawLine((int) pos.x - SIZE / 2, (int) pos.y, (int) pos.x + SIZE / 2, (int) pos.y,
					Glyphs.BLACK_SQUARE, 0xFFF000);

			int scoreY = top ? 0 : (Window.getHeight() - 1);
			Drawer.drawString(Window.getWidth() / 2, scoreY, true, "SCORE: " + score, 0x000FFF);
		}

		public boolean isColliding(Vector2f ballPos) {
			boolean collidingX = ballPos.x >= pos.x - SIZE / 2 && ballPos.x <= pos.x + SIZE / 2;
			if (!collidingX)
				return false;
			if ((int) ballPos.y == (int) pos.y) {
				if (top)
					ballPos.y = (int) pos.y + 1;
				else
					ballPos.y = (int) pos.y - 1;
				return true;
			}
			return false;
		}

		public float getSignedDistance(float x) {
			return (x - pos.x) / (SIZE / 2);
		}

		public void score() {
			score++;
		}

	}

	private static class Ball {

		private Vector2f pos;
		private Vector2f vel;

		public Ball() {
			this.pos = new Vector2f();
			this.vel = new Vector2f();
			reset();
		}

		public void update(float dt, Plate[] plates) {
			Vectors.add(pos, vel, 16f * dt, pos);

			if (pos.x <= 0) {
				pos.x = 0;
				vel.x *= -1f;
			} else if (pos.x >= Window.getWidth() - 1) {
				pos.x = Window.getWidth() - 1;
				vel.x *= -1f;
			}
			if (pos.y <= -1) {
				plates[1].score();
				reset();
			} else if (pos.y >= Window.getHeight()) {
				plates[0].score();
				reset();
			}

			for (int i = 0; i < plates.length; i++)
				if (plates[i].isColliding(pos)) {
					float sd = 0.9f * plates[i].getSignedDistance(pos.x);
					if (sd < 0f)
						vel.x = -0.1f + sd;
					else
						vel.x = 0.1f + sd;
					vel.y *= -1f;
					vel.normalize();
					break;
				}
		}

		public void render() {
			Drawer.draw((int) pos.x, (int) pos.y, Glyphs.WHITE_CIRCLE, 0x000FFF);
		}

		public void reset() {
			pos.x = Window.getWidth() / 2;
			pos.y = Window.getHeight() / 2;

			vel.x = RNG.randomFloat(-1f, 1f);
			vel.y = (RNG.randomBoolean() ? 1f : -1f) * RNG.randomFloat(0.25f, 0.75f);
			vel.normalize();
		}

		public boolean isDirectionUp() {
			return vel.y < 0f;
		}

		public float getX() {
			return pos.x;
		}

	}

}
