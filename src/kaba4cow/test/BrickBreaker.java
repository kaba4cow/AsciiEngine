package kaba4cow.test;

import java.util.ArrayList;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.core.Window;
import kaba4cow.ascii.drawing.BoxDrawer;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;
import kaba4cow.ascii.toolbox.maths.Maths;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2f;
import kaba4cow.ascii.toolbox.rng.RNG;

public class BrickBreaker implements MainProgram {

	private ArrayList<GameObject> list;
	private Plate plate;

	public BrickBreaker() {

	}

	@Override
	public void init() {
		list = new ArrayList<>();

		list.add(plate = new Plate());
		list.add(new Ball(plate));

		int offset = Window.getWidth() % Box.SIZE;

		int columns = (Window.getWidth() - offset / 2) / Box.SIZE;
		int rows = (Window.getHeight() - Window.getHeight() / 3) / Box.SIZE;

		for (int i = 0; i < columns; i++)
			for (int j = 0; j < rows; j++)
				list.add(new Box(1 + i * Box.SIZE, 1 + j * Box.SIZE, RNG.chance(0.1f)));
	}

	@Override
	public void update(float dt) {
		int balls = 0;
		int boxes = 0;
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i) instanceof Ball)
				balls++;
			else if (list.get(i) instanceof Box)
				boxes++;
			if (list.get(i).update(dt))
				list.remove(i);
		}
		if (balls == 0)
			list.add(new Ball(plate));
		if (boxes == 0)
			init();

		if (Input.isKey(Input.KEY_ESCAPE))
			Engine.requestClose();
		if (Input.isKeyDown(Input.KEY_ENTER))
			init();
	}

	@Override
	public void render() {
		BoxDrawer.disableCollision();
		for (int i = 0; i < list.size(); i++)
			list.get(i).render();
		BoxDrawer.enableCollision();
	}

	public static void main(String[] args) {
		Engine.init("Brick Breaker", 16, 60);
		Window.createFullscreen();
		Engine.start(new BrickBreaker());
	}

	private abstract class GameObject {

		public float x;
		public float y;

		protected GameObject(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public abstract boolean update(float dt);

		public abstract void render();

	}

	private class Ball extends GameObject {

		private Vector2f vel;

		public Ball(Plate parent) {
			super(parent.x, parent.y - 1f);
			this.vel = new Vector2f();
			this.vel.x = RNG.randomFloat(-1f, 1f);
			this.vel.y = -1f;
			this.vel.normalize();
		}

		public Ball(Ball parent) {
			super(parent.x, parent.y);
			this.vel = new Vector2f();
			this.vel.x = RNG.randomFloat(-1f, 1f);
			this.vel.y = RNG.randomFloat(0.75f, 1f) * Maths.signum(RNG.randomFloat(-1f, 1f));
			this.vel.normalize();
		}

		@Override
		public boolean update(float dt) {
			float speed = 32f * dt;
			x += vel.x * speed;
			y += vel.y * speed;

			if (x < 0f) {
				x = 0f;
				collideY();
			} else if (x > Window.getWidth() - 1f) {
				x = Window.getWidth() - 1f;
				collideY();
			}

			if (y < 0f) {
				y = 0f;
				collideX();
			} else if (y > Window.getHeight())
				return true;

			if (x > plate.x - Plate.SIZE / 2 - 1 && x < plate.x + Plate.SIZE / 2 && y > plate.y - 1f
					&& y < plate.y + 1f) {
				float dist = 2f * (x - plate.x) / (float) Plate.SIZE;
				y = plate.y - 1f;
				vel.x = 0.8f * dist;
				vel.y = -1f;
				vel.normalize();
			}

			return false;
		}

		public void collideX() {
			vel.y *= -1f;
		}

		public void collideY() {
			vel.x *= -1f;
		}

		@Override
		public void render() {
			Drawer.draw((int) x, (int) y, Glyphs.WHITE_CIRCLE, 0x000FFF);
		}

	}

	private class Box extends GameObject {

		public static final int SIZE = 3;

		private final boolean multiplier;

		protected Box(float x, float y, boolean multiplier) {
			super(x, y);
			this.multiplier = multiplier;
		}

		@Override
		public boolean update(float dt) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (list.get(i) instanceof Ball) {
					Ball ball = (Ball) list.get(i);
					float distX = 2f * (x + 0.5f * SIZE - ball.x) / (float) SIZE;
					float distY = 2f * (y + 0.5f * SIZE - ball.y) / (float) SIZE;

					if (Maths.abs(distX) < 1f && Maths.abs(distY) < 1f) {
						if (Maths.abs(distX) > Maths.abs(distY))
							ball.collideY();
						else
							ball.collideX();
						if (multiplier)
							list.add(new Ball(ball));
						return true;
					}
				}
			}

			return false;
		}

		@Override
		public void render() {
			BoxDrawer.drawBox((int) x, (int) y, SIZE - 1, SIZE - 1, true, 0x421FFF);
			if (multiplier)
				Drawer.draw((int) x + 1, (int) y + 1, Glyphs.WHITE_SUN_WITH_RAYS, 0x111FFF);
		}

	}

	private class Plate extends GameObject {

		public static final int SIZE = 10;

		public Plate() {
			super(Window.getWidth() / 2, Window.getHeight() - 3);
		}

		@Override
		public boolean update(float dt) {
			float speed = 40f * dt;
			if (Input.isKey(Input.KEY_A))
				x -= speed;
			else if (Input.isKey(Input.KEY_D))
				x += speed;

			if (x < SIZE / 2 + 1)
				x = SIZE / 2 + 1;
			else if (x > Window.getWidth() - SIZE / 2 - 1)
				x = Window.getWidth() - SIZE / 2 - 1;

			return false;
		}

		@Override
		public void render() {
			Drawer.drawLine((int) x - SIZE / 2, (int) y, (int) x + SIZE / 2 - 1, (int) y, Glyphs.BLACK_SQUARE,
					0xFFF222);
		}

	}

}
