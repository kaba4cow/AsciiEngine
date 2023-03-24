package kaba4cow.test;

import java.util.ArrayList;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.toolbox.maths.Maths;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2i;
import kaba4cow.ascii.toolbox.maths.vectors.Vectors;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Snake implements MainProgram {

	private Vector2i food;
	private ArrayList<Vector2i> snake;

	private Vector2i direction;
	private Vector2i newDirection;

	private float time;
	private float maxTime;

	private int score;

	private boolean gameOver;
	private boolean pause;

	public Snake() {

	}

	@Override
	public void init() {
		food = new Vector2i();
		snake = new ArrayList<>();
		direction = new Vector2i();
		newDirection = new Vector2i();

		reset();

		Display.setDrawCursor(false);
	}

	private void reset() {
		gameOver = false;
		pause = false;
		score = 0;

		time = 0f;
		maxTime = 0.3f;

		food.x = RNG.randomInt(Display.getWidth());
		food.y = RNG.randomInt(Display.getHeight());

		snake.clear();
		int startX = Display.getWidth() / 2;
		int startY = Display.getHeight() / 2;
		for (int i = 0; i < 4; i++)
			snake.add(new Vector2i(startX, startY + i));

		direction.x = 0;
		direction.y = -1;
		newDirection.set(direction);
	}

	@Override
	public void update(float dt) {
		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();

		if (gameOver) {
			if (Keyboard.isKey(Keyboard.KEY_ENTER))
				reset();
			return;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			pause = !pause;
		if (pause)
			return;

		if (direction.x == 0) {
			if (Keyboard.isKey(Keyboard.KEY_A)) {
				newDirection.x = -1;
				newDirection.y = 0;
			} else if (Keyboard.isKey(Keyboard.KEY_D)) {
				newDirection.x = 1;
				newDirection.y = 0;
			}
		}

		if (direction.y == 0) {
			if (Keyboard.isKey(Keyboard.KEY_W)) {
				newDirection.y = -1;
				newDirection.x = 0;
			} else if (Keyboard.isKey(Keyboard.KEY_S)) {
				newDirection.y = 1;
				newDirection.x = 0;
			}
		}

		time += dt;
		if (time < maxTime)
			return;
		direction.set(newDirection);
		time = 0f;

		Vector2i pos;
		for (int i = 0; i < snake.size(); i++) {
			pos = snake.get(i);
			if (pos.equals(food)) {
				food.x = RNG.randomInt(Display.getWidth());
				food.y = RNG.randomInt(Display.getHeight());
				snake.add(new Vector2i(-1, -1));
				maxTime *= 0.98f;
				score++;
			}
		}

		pos = snake.get(0);
		int tempX, tempY;
		int lastX = pos.x;
		int lastY = pos.y;
		Vectors.add(pos, direction, 1, pos);
		for (int i = 1; i < snake.size(); i++) {
			pos = snake.get(i);

			tempX = pos.x;
			tempY = pos.y;

			pos.x = lastX;
			pos.y = lastY;

			lastX = tempX;
			lastY = tempY;
		}

		gameOver = checkGameOver();
	}

	private boolean checkGameOver() {
		Vector2i p1, p2;
		for (int i = 0; i < snake.size() - 1; i++) {
			p1 = snake.get(i);
			if (p1.x < 0 || p1.x >= Display.getWidth() || p1.y < 0 || p1.y >= Display.getHeight())
				return true;
			for (int j = i + 1; j < snake.size(); j++) {
				p2 = snake.get(j);
				if (p1.equals(p2))
					return true;
			}
		}
		return false;
	}

	@Override
	public void render() {
		for (int y = 0; y < Display.getHeight(); y++)
			for (int x = 0; x < Display.getWidth(); x++)
				if ((x + y) % 2 == 0)
					Drawer.draw(x, y, Glyphs.SPACE, 0x111000);

		Drawer.draw(food.x, food.y, Glyphs.BLACK_HEART_SUIT, 0x000FF7);

		int x, y;
		for (int i = 0; i < snake.size(); i++) {
			x = snake.get(i).x;
			y = snake.get(i).y;
			Drawer.draw(x, y, Glyphs.FULL_BLOCK, 0x000FFF);
		}

		if (gameOver) {
			String scoreString = "SCORE - " + score;
			int width = 2 + Maths.max(scoreString.length(), 9);
			BoxDrawer.drawBox(Display.getWidth() / 2 - width / 2, Display.getHeight() / 2, width, 3, true, 0x000FFF);
			Drawer.drawString(Display.getWidth() / 2, Display.getHeight() / 2 + 1, true, "GAME OVER", 0x000FFF);
			Drawer.drawString(Display.getWidth() / 2, Display.getHeight() / 2 + 2, true, scoreString, 0x000FFF);
		} else if (pause) {
			BoxDrawer.drawBox(Display.getWidth() / 2 - 5, Display.getHeight() / 2, 10, 2, true, 0x000FFF);
			Drawer.drawString(Display.getWidth() / 2, Display.getHeight() / 2 + 1, true, "PAUSE", 0x000FFF);
		}
	}

	public static void main(String[] args) {
		Engine.init("Snake", 60);
		Display.createWindowed(24, 24);
		Engine.start(new Snake());
	}

}
