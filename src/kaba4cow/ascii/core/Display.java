package kaba4cow.ascii.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kaba4cow.ascii.Errors;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.Printer;

public final class Display {

	private static final int IMAGE_CHAR_WIDTH = 9;
	private static final int IMAGE_CHAR_HEIGHT = 16;

	private static int CHAR_WIDTH;
	private static int CHAR_HEIGHT;

	private static int IMAGE_CHAR_COLUMNS;

	private static String TITLE;

	private static int WIDTH;
	private static int HEIGHT;

	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;

	private static JPanel display;
	private static JFrame window;
	private static WindowListener windowListener;

	private static GlyphImage glyphImage;

	private static Frame frame;

	private static char backgroundChar;
	private static int backgroundColor;

	private static boolean drawCursor;
	private static boolean cursorWaiting;

	private static boolean cursorPressed;

	private static int screenX, screenY, tileX, tileY;
	private static int bColorTemp, brTemp, bgTemp, bbTemp, fColorTemp, frTemp, fgTemp, fbTemp, xTemp, yTemp;

	private Display() {

	}

	@SuppressWarnings("serial")
	public static void init(String title, int width, int height, boolean square) {
		Printer.outln("Initializing display");

		CHAR_WIDTH = square ? IMAGE_CHAR_HEIGHT : IMAGE_CHAR_WIDTH;
		CHAR_HEIGHT = IMAGE_CHAR_HEIGHT;

		try {
			glyphImage = new GlyphImage("kaba4cow/ascii/drawing/glyphs.png");
		} catch (IOException e) {
			Engine.terminate(Errors.LOAD_IMAGE, e);
		}

		IMAGE_CHAR_COLUMNS = glyphImage.width / IMAGE_CHAR_WIDTH;

		TITLE = title;

		WIDTH = width;
		HEIGHT = height;

		drawCursor = true;
		cursorWaiting = false;

		cursorPressed = false;

		frame = new Frame(width, height);
		backgroundChar = Glyphs.SPACE;
		backgroundColor = 0x000000;
		Drawer.resetFrame();

		SCREEN_WIDTH = WIDTH * CHAR_WIDTH;
		SCREEN_HEIGHT = HEIGHT * CHAR_HEIGHT + CHAR_HEIGHT;

		window = new JFrame(TITLE);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Engine.requestClose();
			}
		});
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		display = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Display.paint(g);
			}
		};
		display.setFocusable(true);

		windowListener = new WindowListener(window);
		display.addMouseListener(windowListener);
		display.addMouseMotionListener(windowListener);
		display.addKeyListener(Keyboard.get());
		display.addMouseListener(Mouse.get());
		display.addMouseMotionListener(Mouse.get());
		display.addMouseWheelListener(Mouse.get());

		window.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		window.setUndecorated(true);
		window.setVisible(true);
		window.add(display);

		window.setResizable(false);
		window.createBufferStrategy(2);
		window.pack();
		window.setLocationRelativeTo(null);

		window.setCursor(window.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));
	}

	public static void destroy() {
		if (!window.isVisible())
			return;
		Printer.outln("Destroying display");
		window.dispose();
	}

	public static void update() {
		window.repaint();
		cursorPressed = false;
	}

	private static void paint(Graphics g) {
		int titleLength = TITLE.length();
		char barChar;
		char cursorChar;

		int mouseIndex = 0;
		boolean cursorOnBar = false;
		if (Mouse.getY() < 0) {
			mouseIndex = Mouse.getTileX();
			cursorOnBar = true;
		}
		if (!cursorOnBar && drawCursor)
			mouseIndex = Mouse.getTileY() * WIDTH + Mouse.getTileX();
		cursorChar = cursorWaiting ? Glyphs.SYSTEM_CURSOR_WAIT
				: (Mouse.isKey(Mouse.LEFT) ? Glyphs.SYSTEM_CURSOR_GRAB : Glyphs.SYSTEM_CURSOR);

		windowListener.setActive(cursorOnBar);

		screenY = 0;
		for (int i = 0; i < WIDTH; i++) {
			screenX = CHAR_WIDTH * i;

			if (i == WIDTH - 2)
				barChar = Glyphs.SYSTEM_MINIMIZE;
			else if (i == WIDTH - 1)
				barChar = Glyphs.SYSTEM_CLOSE;
			else
				barChar = Glyphs.SPACE;
			if (cursorOnBar && i == mouseIndex)
				barChar = cursorChar;
			else if (i > 0 && i - 1 < titleLength)
				barChar = TITLE.charAt(i - 1);

			if (cursorOnBar && cursorPressed) {
				if (mouseIndex == WIDTH - 1)
					Engine.requestClose();
				else if (window.getState() != JFrame.ICONIFIED && mouseIndex == WIDTH - 2)
					window.setState(JFrame.ICONIFIED);
			}

			tileX = IMAGE_CHAR_WIDTH * (barChar % IMAGE_CHAR_COLUMNS);
			tileY = IMAGE_CHAR_HEIGHT * (barChar / IMAGE_CHAR_COLUMNS);

			glyphImage.draw(0xEEE000, g);
		}

		for (int i = 0; i < frame.length; i++) {
			screenX = CHAR_WIDTH * (i % WIDTH);
			screenY = CHAR_HEIGHT * (i / WIDTH + 1);

			if (!cursorOnBar && drawCursor && i == mouseIndex) {
				tileX = IMAGE_CHAR_WIDTH * (cursorChar % IMAGE_CHAR_COLUMNS);
				tileY = IMAGE_CHAR_HEIGHT * (cursorChar / IMAGE_CHAR_COLUMNS);
				frame.colors[i] |= 0x000FFF;
			} else {
				tileX = IMAGE_CHAR_WIDTH * (frame.chars[i] % IMAGE_CHAR_COLUMNS);
				tileY = IMAGE_CHAR_HEIGHT * (frame.chars[i] / IMAGE_CHAR_COLUMNS);
			}

			glyphImage.draw(frame.colors[i], g);

			frame.chars[i] = backgroundChar;
			frame.colors[i] = backgroundColor;
		}
	}

	public static void setBackground(char c, int color) {
		if (backgroundChar != c)
			for (int i = 0; i < frame.chars.length; i++)
				frame.chars[i] = c;
		if (backgroundColor != color)
			for (int i = 0; i < frame.chars.length; i++)
				frame.colors[i] = color;
		backgroundChar = c;
		backgroundColor = color;
	}

	public static char getChar(int x, int y) {
		return frame.getChar(x, y);
	}

	public static int getColor(int x, int y) {
		return frame.getColor(x, y);
	}

	public static Frame getFrame() {
		return frame;
	}

	public static void setDrawCursor(boolean draw) {
		drawCursor = draw;
	}

	public static boolean isDrawCursor() {
		return drawCursor;
	}

	public static void setCursorWaiting(boolean waiting) {
		cursorWaiting = waiting;
	}

	public static boolean isCursorWaiting() {
		return cursorWaiting;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static int getCharWidth() {
		return CHAR_WIDTH;
	}

	public static int getCharHeight() {
		return CHAR_HEIGHT;
	}

	private static class WindowListener extends MouseAdapter {

		private final JFrame frame;
		private Point mousePosition = null;
		private boolean active;

		public WindowListener(JFrame frame) {
			this.frame = frame;
			this.mousePosition = null;
			this.active = false;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public void mouseReleased(MouseEvent e) {
			mousePosition = null;
			if (e.getButton() == MouseEvent.BUTTON1)
				Display.cursorPressed = true;
		}

		public void mousePressed(MouseEvent e) {
			mousePosition = active ? e.getPoint() : null;
			if (e.getButton() == MouseEvent.BUTTON1)
				Display.cursorPressed = false;
		}

		public void mouseDragged(MouseEvent e) {
			if (mousePosition == null)
				return;
			Point screenPosition = e.getLocationOnScreen();
			int newX = screenPosition.x - mousePosition.x;
			int newY = screenPosition.y - mousePosition.y;
			frame.setLocation(newX / CHAR_WIDTH * CHAR_WIDTH, newY / CHAR_HEIGHT * CHAR_HEIGHT);
		}
	}

	private static class GlyphImage {

		private BufferedImage image;

		public final boolean[][] map;

		public final int width;
		public final int height;

		public GlyphImage(String path) throws IOException {
			InputStream is = getClass().getClassLoader().getResourceAsStream(path);
			image = ImageIO.read(is);

			width = image.getWidth();
			height = image.getHeight();

			ColorModel colorModel = image.getColorModel();
			map = new boolean[width][height];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					map[x][y] = colorModel.getRed(image.getRaster().getDataElements(x, y, null)) > 0;
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}

		public void draw(int color, Graphics g) {
			bColorTemp = (color >> 12) & 0xFFF;
			brTemp = (bColorTemp >> 8) & 0xF;
			bgTemp = (bColorTemp >> 4) & 0xF;
			bbTemp = (bColorTemp >> 0) & 0xF;
			bColorTemp = (brTemp << 20) | (bgTemp << 12) | (bbTemp << 4);

			fColorTemp = (color >> 0) & 0xFFF;
			frTemp = (fColorTemp >> 8) & 0xF;
			fgTemp = (fColorTemp >> 4) & 0xF;
			fbTemp = (fColorTemp >> 0) & 0xF;
			fColorTemp = (frTemp << 20) | (fgTemp << 12) | (fbTemp << 4);

			for (yTemp = tileY; yTemp < tileY + IMAGE_CHAR_HEIGHT; yTemp++)
				for (xTemp = tileX; xTemp < tileX + IMAGE_CHAR_WIDTH; xTemp++)
					image.setRGB(xTemp, yTemp, map[xTemp][yTemp] ? fColorTemp : bColorTemp);

			g.drawImage(image, screenX, screenY, screenX + CHAR_WIDTH, screenY + CHAR_HEIGHT, tileX, tileY,
					tileX + IMAGE_CHAR_WIDTH, tileY + IMAGE_CHAR_HEIGHT, window);
		}

	}

}
