package kaba4cow.test;

import java.util.ArrayList;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.toolbox.maths.Maths;
import kaba4cow.ascii.toolbox.rng.RNG;

public class MatrixStream implements MainProgram {

	private ArrayList<Stream> streams;

	public MatrixStream() {

	}

	public void init() {
		streams = new ArrayList<>();
		for (int i = 0; i < Display.getWidth(); i++)
			streams.add(new Stream(i));

		Display.setDrawCursor(false);
	}

	public void update(float dt) {
		streams.forEach(s -> s.update(dt));

		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();
	}

	public void render() {
		streams.forEach(s -> s.render());
	}

	public static void main(String[] args) {
		Engine.init("Matrix Stream", 30);
		Display.createFullscreen(false);
		Engine.start(new MatrixStream());
	}

	private static class Stream {

		private final int x;
		private float y;

		private int length;
		private float speed;

		private char[] chars;
		private int[] colors;

		public Stream(int x) {
			this.x = x;
			this.y = RNG.randomFloat(Display.getHeight());
			reset();
		}

		private void reset() {
			length = RNG.randomInt(4, 20);
			speed = RNG.randomFloat(8f, 30f);
			chars = new char[length];
			for (int i = 0; i < length; i++)
				chars[i] = (char) RNG.randomInt(1, 255);
			colors = new int[length];
			for (int i = 0; i < length; i++) {
				int b = (int) Maths.map(i, 0, length, 0, 0xF);
				colors[i] = 0x0000F0 & (b << 4);
			}
		}

		public void update(float dt) {
			y += speed * dt;
			if (y >= Display.getHeight()) {
				reset();
				y = -length;
			}
		}

		public void render() {
			for (int i = 0; i < length; i++)
				Drawer.drawChar(x, (int) y + i, chars[i], colors[i]);
		}

	}

}
