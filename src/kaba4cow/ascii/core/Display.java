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

	private static int CHAR_WIDTH;
	private static int CHAR_HEIGHT;

	private static final int TILES_X = 32;

	private static String TITLE;

	private static int WIDTH;
	private static int HEIGHT;

	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;

	private static JPanel display;
	private static JFrame window;
	private static WindowDragListener dragListener;

	private static BufferedImage glyphSheet;
	private static boolean[][] glyphMap;

	private static Frame frame;

	private static char backgroundChar;
	private static int backgroundColor;
	private static boolean drawCursor;

	private static int screenX, screenY, tileX, tileY;
	private static int bColorTemp, brTemp, bgTemp, bbTemp, fColorTemp, frTemp, fgTemp, fbTemp, xTemp, yTemp;

	private Display() {

	}

	@SuppressWarnings("serial")
	public static void init(String title, int width, int height, boolean square) {
		Printer.outln("Initializing display");

		String imagePath;
		if (square) {
			imagePath = "kaba4cow/ascii/drawing/glyphs2.png";
			CHAR_WIDTH = 16;
			CHAR_HEIGHT = 16;
		} else {
			imagePath = "kaba4cow/ascii/drawing/glyphs1.png";
			CHAR_WIDTH = 9;
			CHAR_HEIGHT = 16;
		}

		InputStream is = new Display().getClass().getClassLoader().getResourceAsStream(imagePath);
		try {
			glyphSheet = ImageIO.read(is);
		} catch (IOException e) {
			Engine.terminate(Errors.LOAD_IMAGE, e);
		}

		ColorModel colorModel = glyphSheet.getColorModel();
		glyphMap = new boolean[glyphSheet.getWidth()][glyphSheet.getHeight()];
		for (yTemp = 0; yTemp < glyphSheet.getHeight(); yTemp++)
			for (xTemp = 0; xTemp < glyphSheet.getWidth(); xTemp++)
				glyphMap[xTemp][yTemp] = colorModel
						.getRed(glyphSheet.getRaster().getDataElements(xTemp, yTemp, null)) > 0;
		glyphSheet = new BufferedImage(glyphSheet.getWidth(), glyphSheet.getHeight(), BufferedImage.TYPE_INT_RGB);

		TITLE = title;

		WIDTH = width;
		HEIGHT = height;

		frame = new Frame(width, height);
		backgroundChar = Glyphs.SPACE;
		backgroundColor = 0x000000;
		drawCursor = true;
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

		dragListener = new WindowDragListener(window);
		display.addMouseListener(dragListener);
		display.addMouseMotionListener(dragListener);
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
	}

	private static void paint(Graphics g) {
		int mouseIndex = 0;
		boolean cursorOnBar = false;
		if (Mouse.getY() < 0) {
			mouseIndex = Mouse.getTileX();
			cursorOnBar = true;
		}
		if (!cursorOnBar && drawCursor)
			mouseIndex = Mouse.getTileY() * WIDTH + Mouse.getTileX();

		dragListener.setActive(cursorOnBar);

		int titleLength = TITLE.length();
		int barChar;
		int barColor;

		for (int i = 0; i < WIDTH; i++) {
			screenX = CHAR_WIDTH * i;

			if (cursorOnBar && i == mouseIndex)
				barChar = Glyphs.CURSOR;
			else if (i > 0 && i - 1 < titleLength)
				barChar = TITLE.charAt(i - 1);
			else if (i >= WIDTH - 6) {
				if (i == WIDTH - 5)
					barChar = Glyphs.HYPHEN_MINUS;
				else if (i == WIDTH - 2)
					barChar = Glyphs.LATIN_CAPITAL_LETTER_X;
				else
					barChar = Glyphs.SPACE;
			} else
				barChar = Glyphs.BOX_DRAWINGS_LIGHT_HORIZONTAL;

			if (i >= WIDTH - 3)
				barColor = 0xFAA000;
			else if (i >= WIDTH - 6 && i <= WIDTH - 4)
				barColor = 0xAAF000;
			else
				barColor = 0xAAA000;

			if (cursorOnBar && Mouse.isKeyUp(Mouse.LEFT)) {
				if (mouseIndex >= WIDTH - 3)
					Engine.requestClose();
				else if (window.getState() != JFrame.ICONIFIED && mouseIndex >= WIDTH - 6 && mouseIndex <= WIDTH - 3)
					window.setState(JFrame.ICONIFIED);
			}

			tileX = CHAR_WIDTH * (barChar % TILES_X);
			tileY = CHAR_HEIGHT * (barChar / TILES_X);

			setColor(barColor, tileX, tileY, tileX + CHAR_WIDTH, tileY + CHAR_HEIGHT);
			g.drawImage(glyphSheet, screenX, 0, screenX + CHAR_WIDTH, CHAR_HEIGHT, tileX, tileY, tileX + CHAR_WIDTH,
					tileY + CHAR_HEIGHT, window);
		}

		for (int i = 0; i < frame.length; i++) {
			screenX = CHAR_WIDTH * (i % WIDTH);
			screenY = CHAR_HEIGHT * (i / WIDTH + 1);

			if (!cursorOnBar && drawCursor && i == mouseIndex) {
				tileX = 0;
				tileY = 0;
				frame.colors[i] |= 0x000FFF;
			} else {
				tileX = CHAR_WIDTH * (frame.chars[i] % TILES_X);
				tileY = CHAR_HEIGHT * (frame.chars[i] / TILES_X);
			}

			setColor(frame.colors[i], tileX, tileY, tileX + CHAR_WIDTH, tileY + CHAR_HEIGHT);
			g.drawImage(glyphSheet, screenX, screenY, screenX + CHAR_WIDTH, screenY + CHAR_HEIGHT, tileX, tileY,
					tileX + CHAR_WIDTH, tileY + CHAR_HEIGHT, window);

			frame.chars[i] = backgroundChar;
			frame.colors[i] = backgroundColor;
		}
	}

	private static void setColor(int color, int startX, int startY, int stopX, int stopY) {
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

		for (yTemp = startY; yTemp < stopY; yTemp++)
			for (xTemp = startX; xTemp < stopX; xTemp++)
				glyphSheet.setRGB(xTemp, yTemp, glyphMap[xTemp][yTemp] ? fColorTemp : bColorTemp);
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

	public static void drawCursor(boolean draw) {
		drawCursor = draw;
	}

	public static boolean isDrawCursor() {
		return drawCursor;
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

	private static class WindowDragListener extends MouseAdapter {

		private final JFrame frame;
		private Point mousePosition = null;
		private boolean active;

		public WindowDragListener(JFrame frame) {
			this.frame = frame;
			this.mousePosition = null;
			this.active = false;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public void mouseReleased(MouseEvent e) {
			mousePosition = null;
		}

		public void mousePressed(MouseEvent e) {
			mousePosition = active ? e.getPoint() : null;
		}

		public void mouseDragged(MouseEvent e) {
			if (mousePosition == null)
				return;
			Point screenPosition = e.getLocationOnScreen();
			frame.setLocation(screenPosition.x - mousePosition.x, screenPosition.y - mousePosition.y);
		}
	}

}
