package kaba4cow.test.seabattle;

import java.util.ArrayList;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Field {

	public static final int SIZE = 10;

	public static final int NO_MOVE = 0;
	public static final int HIT = 1;
	public static final int MISS = 2;

	private static final int CELL_WATER = 0;
	private static final int CELL_SHIP = 1;
	private static final int CELL_HIT = 2;
	private static final int CELL_DESTROYED = 3;
	private static final int CELL_MISS = 4;

	public static final char CHAR_WATER = Glyphs.ALMOST_EQUAL_TO;
	public static final char CHAR_SHIP = Glyphs.FULL_BLOCK;
	public static final char CHAR_MISS = Glyphs.BULLET_OPERATOR;
	public static final char CHAR_HIT = Glyphs.DARK_SHADE;
	public static final char CHAR_DESTROYED = Glyphs.LIGHT_SHADE;

	private int length;
	private int count;
	private int amount;
	private boolean vertical;

	private int[][] field;
	private ArrayList<Ship> ships;

	private class Ship {

		private final int x0;
		private final int y0;
		private final int x1;
		private final int y1;

		public Ship(int x0, int y0, int x1, int y1) {
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
		}

		public boolean update(int[][] field) {
			int x, y;
			for (y = y0; y <= y1; y++)
				for (x = x0; x <= x1; x++) {
					if (field[x][y] == CELL_SHIP)
						return false;
				}
			for (y = y0; y <= y1; y++)
				for (x = x0; x <= x1; x++)
					field[x][y] = CELL_DESTROYED;
			return true;
		}

	}

	public Field() {
		this.field = new int[SIZE][SIZE];
		this.ships = new ArrayList<>();
	}

	public void reset() {
		ships.clear();
		int x, y;
		for (y = 0; y < SIZE; y++)
			for (x = 0; x < SIZE; x++)
				field[x][y] = 0;
		length = 4;
		count = 1;
		amount = 0;
		vertical = false;
	}

	public int play(Field field, boolean player, boolean smartAi) {
		int x, y;
		if (player) {
			x = Mouse.getTileX() - Display.getWidth() / 2;
			y = Mouse.getTileY();
			if (Mouse.isKeyDown(Mouse.LEFT) && field.isValidMove(x, y))
				return field.hit(x, y) ? HIT : MISS;
		} else {
			do {
				x = RNG.randomInt(SIZE);
				y = RNG.randomInt(SIZE);
			} while (!field.isSmartMove(x, y, smartAi));
			return field.hit(x, y) ? HIT : MISS;
		}
		return NO_MOVE;
	}

	private boolean isSmartMove(int x, int y, boolean smartAi) {
		if (!smartAi)
			return isValidMove(x, y);
		if (!isValidMove(x, y))
			return false;
		int cx, cy;
		for (cy = y - 1; cy <= y + 1; cy++)
			for (cx = x - 1; cx <= x + 1; cx++) {
				if (cx < 0 || cx >= SIZE || cy < 0 || cy >= SIZE)
					continue;
				if (field[cx][cy] == CELL_HIT && (x == cx || y == cy))
					return true;
				if (field[cx][cy] == CELL_DESTROYED)
					return false;
			}
		return true;
	}

	public boolean isValidMove(int x, int y) {
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
			return false;
		return field[x][y] == CELL_SHIP || field[x][y] == CELL_WATER;
	}

	public void plot(boolean player, boolean manualPlotting) {
		if (length <= 0)
			return;
		int x, y;
		if (player && manualPlotting) {
			if (Mouse.getScroll() != 0)
				vertical = !vertical;
			if (Mouse.isKeyDown(Mouse.LEFT)) {
				x = Mouse.getTileX();
				y = Mouse.getTileY();
				if (isValidPosition(x, y))
					plot(x, y);
			}
		} else
			while (length > 0) {
				vertical = RNG.randomBoolean();
				x = RNG.randomInt(SIZE);
				y = RNG.randomInt(SIZE);
				if (isValidPosition(x, y))
					plot(x, y);
			}
	}

	private void plot(int x, int y) {
		int x0 = x;
		int y0 = y;
		for (int i = 0; i < length; i++) {
			field[x][y] = CELL_SHIP;
			if (i == length - 1)
				break;
			if (vertical)
				y++;
			else
				x++;
		}
		ships.add(new Ship(x0, y0, x, y));
		amount++;
		if (amount >= count) {
			amount = 0;
			count++;
			length--;
		}
	}

	public boolean isDestroyed() {
		return ships.isEmpty();
	}

	public boolean isReady() {
		return length == 0;
	}

	private boolean isValidPosition(int x, int y) {
		int nx, ny;
		for (int i = 0; i < length; i++) {
			if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
				return false;
			for (ny = -1; ny <= 1; ny++)
				for (nx = -1; nx <= 1; nx++)
					if (getCell(x + nx, y + ny) != CELL_WATER)
						return false;
			if (vertical)
				y++;
			else
				x++;
		}
		return true;
	}

	public boolean hit(int x, int y) {
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
			return false;
		if (field[x][y] == CELL_SHIP) {
			field[x][y] = CELL_HIT;
			return true;
		} else if (field[x][y] == CELL_WATER)
			field[x][y] = CELL_MISS;
		return false;
	}

	private int getCell(int x, int y) {
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
			return CELL_WATER;
		return field[x][y];
	}

	public void render(boolean player) {
		for (int i = ships.size() - 1; i >= 0; i--)
			if (ships.get(i).update(field))
				ships.remove(i);

		int offX = player ? 0 : Display.getWidth() / 2;
		int x, y;
		char c = 0;
		int color = 0;
		for (y = 0; y < SIZE; y++)
			for (x = 0; x < SIZE; x++) {
				if (field[x][y] == CELL_WATER)
					c = CHAR_WATER;
				else if (field[x][y] == CELL_SHIP)
					c = CHAR_SHIP;
				else if (field[x][y] == CELL_HIT)
					c = CHAR_HIT;
				else if (field[x][y] == CELL_DESTROYED)
					c = CHAR_DESTROYED;
				else
					c = CHAR_MISS;
				if (!player && c == CHAR_SHIP)
					c = CHAR_WATER;
				if (c == CHAR_WATER)
					color = 0x348359;
				else
					color = 0x348FFF;
				Drawer.draw(offX + x, y, c, color);
			}

		if (player && length > 0) {
			x = Mouse.getTileX();
			y = Mouse.getTileY();
			color = isValidPosition(x, y) ? 0x0006F6 : 0x000F66;
			for (int i = 0; i < length; i++) {
				if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
					break;
				Drawer.draw(x, y, CHAR_SHIP, color);
				if (vertical)
					y++;
				else
					x++;
			}
		}
	}

}
