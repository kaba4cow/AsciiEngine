package kaba4cow.ascii.core;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
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

	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;

	private static String TITLE;

	private static int WIDTH;
	private static int HEIGHT;

	private static int DISPLAY_WIDTH;
	private static int DISPLAY_HEIGHT;

	private static GraphicsDevice device;
	private static WindowListener windowListener;
	private static Cursor cursor;
	private static JFrame window;
	private static JPanel display;

	private static GlyphImage glyphImage;

	private static Frame frame;

	private static char backgroundChar;
	private static int backgroundColor;

	private static boolean fullscreen;
	private static boolean drawCursor;
	private static boolean cursorWaiting;
	private static boolean cursorPressed;
	private static boolean ignoreClosing;

	private static int screenX, screenY, tileX, tileY;
	private static int bColorTemp, brTemp, bgTemp, bbTemp, fColorTemp, frTemp, fgTemp, fbTemp, xTemp, yTemp;

	private Display() {

	}

	public static void init(String title) {
		Printer.outln("Initializing display");

		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = screenSize.width;
		SCREEN_HEIGHT = screenSize.height;

		try {
			glyphImage = new GlyphImage("kaba4cow/ascii/drawing/glyphs.png");
		} catch (IOException e) {
			Engine.terminate(Errors.LOAD_IMAGE, e);
		}

		IMAGE_CHAR_COLUMNS = glyphImage.width / IMAGE_CHAR_WIDTH;

		TITLE = title;

		backgroundChar = Glyphs.SPACE;
		backgroundColor = 0x000000;
	}

	public static void createFullscreen(boolean squareGlyphs) {
		create(0, 0, squareGlyphs);
	}

	public static void createWindowed(int width, int height, boolean squareGlyphs) {
		create(width, height, squareGlyphs);
	}

	@SuppressWarnings("serial")
	private static void create(int width, int height, boolean squareGlyphs) {
		fullscreen = width == 0 || height == 0;

		CHAR_WIDTH = squareGlyphs ? IMAGE_CHAR_HEIGHT : IMAGE_CHAR_WIDTH;
		CHAR_HEIGHT = IMAGE_CHAR_HEIGHT;

		if (fullscreen) {
			WIDTH = SCREEN_WIDTH / CHAR_WIDTH;
			HEIGHT = SCREEN_HEIGHT / CHAR_HEIGHT;
		} else {
			WIDTH = width;
			HEIGHT = height;
		}

		Printer.outln("Creating " + (fullscreen ? "fullscreen" : "windowed") + " display: " + WIDTH + "x" + HEIGHT);

		if (window != null) {
			ignoreClosing = true;
			window.dispose();
		}
		ignoreClosing = false;

		DISPLAY_WIDTH = WIDTH * CHAR_WIDTH;
		DISPLAY_HEIGHT = HEIGHT * CHAR_HEIGHT + CHAR_HEIGHT;

		drawCursor = true;
		cursorWaiting = false;
		cursorPressed = false;

		frame = new Frame(WIDTH, HEIGHT);
		Drawer.resetFrame();

		window = new JFrame(TITLE);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!ignoreClosing)
					Engine.requestClose();
			}
		});
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowListener = new WindowListener(window);

		display = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Display.paint(g);
			}
		};
		display.setFocusable(true);
		display.addMouseListener(windowListener);
		display.addMouseMotionListener(windowListener);
		display.addKeyListener(Keyboard.get());
		display.addMouseListener(Mouse.get());
		display.addMouseMotionListener(Mouse.get());
		display.addMouseWheelListener(Mouse.get());

		window.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		window.setUndecorated(true);
		window.setVisible(true);
		window.add(display);
		window.setResizable(false);
		window.createBufferStrategy(2);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setCursor(cursor);

		if (fullscreen)
			device.setFullScreenWindow(window);

		Keyboard.reset();
		Mouse.reset();
		display.requestFocus();
	}

	public static void destroy() {
		if (!window.isVisible())
			return;
		Printer.outln("Destroying display");
		ignoreClosing = false;
		window.dispose();
	}

	public static void update() {
		window.repaint();
		cursorPressed = false;
	}

	private static void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, DISPLAY_WIDTH + CHAR_WIDTH, DISPLAY_HEIGHT + CHAR_HEIGHT);

		int titleLength = TITLE.length();
		char currentChar, barChar, cursorChar;

		int mouseIndex = 0;
		boolean cursorOnBar = false;
		if (Mouse.getY() < 0) {
			mouseIndex = Mouse.getTileX();
			cursorOnBar = true;
		}
		if (!cursorOnBar && drawCursor)
			mouseIndex = Mouse.getTileY() * WIDTH + Mouse.getTileX();
		cursorChar = cursorWaiting ? Glyphs.SYSTEM_CURSOR_WAITING
				: (Mouse.isKey(Mouse.LEFT) ? Glyphs.SYSTEM_CURSOR_GRABBED : Glyphs.SYSTEM_CURSOR);

		windowListener.setActive(cursorOnBar);

		screenY = 0;
		for (int i = 0; i < WIDTH; i++) {
			screenX = CHAR_WIDTH * i;

			if (i == WIDTH - 2)
				barChar = Glyphs.SYSTEM_HIDE_WINDOW;
			else if (i == WIDTH - 1)
				barChar = Glyphs.SYSTEM_CLOSE_WINDOW;
			else
				barChar = Glyphs.SPACE;
			if (cursorOnBar && i == mouseIndex)
				barChar = cursorChar;
			else if (i > 0 && i - 1 < titleLength)
				barChar = TITLE.charAt(i - 1);

			if (cursorOnBar && cursorPressed) {
				if (mouseIndex == WIDTH - 1)
					Engine.requestClose();
				else if (window.getExtendedState() != JFrame.ICONIFIED && mouseIndex == WIDTH - 2)
					window.setExtendedState(JFrame.ICONIFIED);
			}

			tileX = IMAGE_CHAR_WIDTH * (barChar % IMAGE_CHAR_COLUMNS);
			tileY = IMAGE_CHAR_HEIGHT * (barChar / IMAGE_CHAR_COLUMNS);

			glyphImage.draw(0xEEE000, g);
		}

		for (int i = 0; i < frame.length; i++) {
			currentChar = frame.chars[i];
			if (currentChar >= Glyphs.numGlyphs())
				continue;

			screenX = CHAR_WIDTH * (i % WIDTH);
			screenY = CHAR_HEIGHT * (i / WIDTH + 1);

			if (!cursorOnBar && drawCursor && i == mouseIndex) {
				tileX = IMAGE_CHAR_WIDTH * (cursorChar % IMAGE_CHAR_COLUMNS);
				tileY = IMAGE_CHAR_HEIGHT * (cursorChar / IMAGE_CHAR_COLUMNS);
				frame.colors[i] |= 0x000FFF;
			} else {
				tileX = IMAGE_CHAR_WIDTH * (currentChar % IMAGE_CHAR_COLUMNS);
				tileY = IMAGE_CHAR_HEIGHT * (currentChar / IMAGE_CHAR_COLUMNS);
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

	public static int getCursorOffset() {
		return -CHAR_HEIGHT;
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
			mousePosition = (active && !fullscreen) ? e.getPoint() : null;
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
