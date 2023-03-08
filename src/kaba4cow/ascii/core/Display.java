package kaba4cow.ascii.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.Printer;
import kaba4cow.ascii.toolbox.utils.ProgramUtils;

public final class Display {

	private static final int CHAR_SIZE = 16;

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
	private static Canvas canvas;
	private static BufferStrategy bufferStrategy;
	private static Graphics graphics;

	private static GlyphSheet glyphSheet;

	private static Frame frame;

	private static char cursorChar;
	private static char backgroundChar;
	private static int backgroundColor;

	private static boolean fullscreen;
	private static boolean drawCursor;
	private static boolean cursorWaiting;
	private static boolean ignoreClosing;
	private static boolean takingScreenshot;

	private static boolean cursorOnBar;

	private static int screenX, screenY, tileX, tileY, prevTileX, prevTileY;
	private static int prevColorTemp, bColorTemp, brTemp, bgTemp, bbTemp, fColorTemp, frTemp, fgTemp, fbTemp, xTemp,
			yTemp, xOffsetTemp, yOffsetTemp;

	private Display() {

	}

	public static void init(String title) {
		Printer.println("Initializing display");

		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = screenSize.width;
		SCREEN_HEIGHT = screenSize.height;

		try {
			glyphSheet = new GlyphSheet("kaba4cow/ascii/drawing/glyphs.png");
		} catch (IOException e) {
			Engine.terminate(e);
		}

		IMAGE_CHAR_COLUMNS = glyphSheet.width / CHAR_SIZE;

		TITLE = title;

		backgroundChar = Glyphs.SPACE;
		backgroundColor = 0x000FFF;
	}

	public static void createFullscreen() {
		create(0, 0);
	}

	public static void createWindowed(int width, int height) {
		create(width, height);
	}

	private static void create(int width, int height) {
		fullscreen = width == 0 || height == 0;

		if (fullscreen) {
			WIDTH = SCREEN_WIDTH / CHAR_SIZE;
			HEIGHT = SCREEN_HEIGHT / CHAR_SIZE;
		} else {
			WIDTH = width;
			HEIGHT = height;
		}

		Printer.println("Creating " + (fullscreen ? "fullscreen" : "windowed") + " display: " + WIDTH + "x" + HEIGHT);

		if (window != null) {
			ignoreClosing = true;
			window.dispose();
		}
		ignoreClosing = false;

		DISPLAY_WIDTH = WIDTH * CHAR_SIZE;
		DISPLAY_HEIGHT = HEIGHT * CHAR_SIZE;

		drawCursor = true;
		cursorWaiting = false;

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
		new DropTarget(window, windowListener);

		Dimension dimension = new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT + CHAR_SIZE);

		canvas = new Canvas();
		canvas.setPreferredSize(dimension);
		canvas.setMaximumSize(dimension);
		canvas.setMinimumSize(dimension);
		canvas.addMouseListener(windowListener);
		canvas.addMouseMotionListener(windowListener);
		canvas.addFocusListener(windowListener);
		canvas.addKeyListener(Keyboard.get());
		canvas.addMouseListener(Mouse.get());
		canvas.addMouseMotionListener(Mouse.get());
		canvas.addMouseWheelListener(Mouse.get());

		window.setPreferredSize(dimension);
		window.setFocusable(true);
		window.setUndecorated(true);
		window.setVisible(true);
		window.add(canvas);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setCursor(cursor);

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();

		if (fullscreen)
			device.setFullScreenWindow(window);

		Keyboard.reset();
		Mouse.reset();
		canvas.requestFocus();

		window.setIgnoreRepaint(true);
		canvas.setIgnoreRepaint(true);
	}

	public static void destroy() {
		if (!window.isVisible())
			return;
		Printer.println("Destroying display");
		ignoreClosing = false;
		window.dispose();
	}

	public static void update() {
		if (fullscreen) {
			cursorChar = cursorWaiting ? Glyphs.SYSTEM_CURSOR_WAITING
					: (Mouse.isKey(Mouse.LEFT) ? Glyphs.SYSTEM_CURSOR_GRABBED : Glyphs.SYSTEM_CURSOR);
			windowListener.setActive(false);
			return;
		}

		int mouseIndex = 0;
		boolean tempCursorOnBar = false;
		if (Mouse.getY() < 0) {
			mouseIndex = Mouse.getTileX();
			tempCursorOnBar = true;
		}
		if (!tempCursorOnBar && drawCursor)
			mouseIndex = Mouse.getTileY() * WIDTH + Mouse.getTileX();
		cursorOnBar = false;
		cursorChar = cursorWaiting ? Glyphs.SYSTEM_CURSOR_WAITING
				: (Mouse.isKey(Mouse.LEFT) ? Glyphs.SYSTEM_CURSOR_GRABBED : Glyphs.SYSTEM_CURSOR);

		windowListener.setActive(tempCursorOnBar);

		if (tempCursorOnBar && Mouse.isKeyUp(Mouse.LEFT)) {
			if (mouseIndex == WIDTH - 1)
				Engine.requestClose();
			else if (window.getExtendedState() != JFrame.ICONIFIED && mouseIndex == WIDTH - 2)
				window.setExtendedState(JFrame.ICONIFIED);
		}

		cursorOnBar = tempCursorOnBar;
	}

	public static void render() {
		if (takingScreenshot)
			try {
				takingScreenshot = false;
				File file = new File("screenshot_" + ProgramUtils.getDate() + ".png");
				file.createNewFile();

				BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				graphics = image.getGraphics();
				paint();
				graphics.dispose();

				if (ImageIO.write(image, "png", file))
					Printer.println("Screenshot saved at: " + file.getAbsolutePath());
				else
					Printer.println("Error occured while saving screenshot");
			} catch (IOException e) {
				e.printStackTrace();
			}

		do {
			do {
				graphics = bufferStrategy.getDrawGraphics();
				paint();
			} while (bufferStrategy.contentsRestored());
			bufferStrategy.show();
		} while (bufferStrategy.contentsLost());
	}

	private static void paint() {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, DISPLAY_WIDTH + CHAR_SIZE, DISPLAY_HEIGHT + CHAR_SIZE);

		int titleLength = TITLE.length();
		char currentChar;
		int barOffset = fullscreen ? 0 : 1;

		int mouseIndex = 0;
		if (Mouse.getY() < 0)
			mouseIndex = Mouse.getTileX();
		if (!cursorOnBar && drawCursor)
			mouseIndex = Mouse.getTileY() * WIDTH + Mouse.getTileX();

		screenX = 0;
		screenY = 0;
		if (!fullscreen)
			for (int i = 0; i < WIDTH; i++) {
				if (i == WIDTH - 2)
					currentChar = Glyphs.SYSTEM_HIDE_WINDOW;
				else if (i == WIDTH - 1)
					currentChar = Glyphs.SYSTEM_CLOSE_WINDOW;
				else
					currentChar = Glyphs.SPACE;
				if (cursorOnBar && i == mouseIndex)
					currentChar = cursorChar;
				else if (i > 0 && i - 1 < titleLength)
					currentChar = TITLE.charAt(i - 1);

				tileX = currentChar % IMAGE_CHAR_COLUMNS;
				tileY = currentChar / IMAGE_CHAR_COLUMNS;

				glyphSheet.draw(0xEEE000);

				screenX += CHAR_SIZE;
			}

		for (int i = 0; i < frame.length; i++) {
			currentChar = frame.chars[i];
			if (!cursorOnBar && drawCursor && i == mouseIndex) {
				currentChar = cursorChar;
				frame.colors[i] = 0x000FFF;
			}
			if (currentChar >= Glyphs.numGlyphs())
				continue;

			screenX = CHAR_SIZE * (i % WIDTH);
			screenY = CHAR_SIZE * (i / WIDTH + barOffset);

			tileX = currentChar % IMAGE_CHAR_COLUMNS;
			tileY = currentChar / IMAGE_CHAR_COLUMNS;

			glyphSheet.draw(frame.colors[i]);

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

	public static void takeScreenshot() {
		takingScreenshot = true;
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

	public static boolean isCursorOnBar() {
		return cursorOnBar;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static int getCharSize() {
		return CHAR_SIZE;
	}

	public static int getCursorOffset() {
		return fullscreen ? 0 : -CHAR_SIZE;
	}

	private static class WindowListener extends MouseAdapter implements FocusListener, DropTargetListener {

		private final JFrame frame;

		private Point mousePosition;
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
		}

		public void mousePressed(MouseEvent e) {
			mousePosition = (active && !fullscreen) ? e.getPoint() : null;
		}

		public void mouseDragged(MouseEvent e) {
			if (mousePosition == null)
				return;
			Point screenPosition = e.getLocationOnScreen();
			int newX = screenPosition.x - mousePosition.x;
			int newY = screenPosition.y - mousePosition.y;
			frame.setLocation(newX / CHAR_SIZE * CHAR_SIZE, newY / CHAR_SIZE * CHAR_SIZE);
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (Engine.getProgram() != null)
				Engine.getProgram().onGainedFocus();
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (Engine.getProgram() != null)
				Engine.getProgram().onLostFocus();
		}

		@Override
		public void drop(DropTargetDropEvent event) {
			event.acceptDrop(DnDConstants.ACTION_COPY);

			Transferable transferable = event.getTransferable();
			DataFlavor[] flavors = transferable.getTransferDataFlavors();

			for (DataFlavor flavor : flavors) {
				try {
					if (flavor.isFlavorJavaFileListType()) {
						@SuppressWarnings("unchecked")
						List<File> dropped = (List<File>) transferable.getTransferData(flavor);
						File[] files = new File[dropped.size()];
						for (int i = 0; i < files.length; i++)
							files[i] = dropped.get(i);
						Engine.getProgram().onFilesDropped(files);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			event.dropComplete(true);
		}

		@Override
		public void dragEnter(DropTargetDragEvent event) {
		}

		@Override
		public void dragOver(DropTargetDragEvent event) {
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent event) {
		}

		@Override
		public void dragExit(DropTargetEvent event) {
		}

	}

	private static class GlyphSheet {

		private BufferedImage sheet;
		public final boolean[][] map;

		private final BufferedImage[][] images;
		private BufferedImage image;

		public final int width;
		public final int height;

		public GlyphSheet(String path) throws IOException {
			InputStream is = getClass().getClassLoader().getResourceAsStream(path);
			sheet = ImageIO.read(is);

			width = sheet.getWidth();
			height = sheet.getHeight();

			ColorModel colorModel = sheet.getColorModel();
			map = new boolean[width][height];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					map[x][y] = colorModel.getRed(sheet.getRaster().getDataElements(x, y, null)) > 0;
			sheet = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			int columns = width / CHAR_SIZE;
			int rows = height / CHAR_SIZE;
			images = new BufferedImage[columns][rows];
			for (int y = 0; y < rows; y++)
				for (int x = 0; x < columns; x++)
					images[x][y] = sheet.getSubimage(x * CHAR_SIZE, y * CHAR_SIZE, CHAR_SIZE, CHAR_SIZE);
		}

		public void draw(int color) {
			if (prevColorTemp == color && prevTileX == tileX && prevTileY == tileY) {
				graphics.drawImage(image, screenX, screenY, canvas);
				return;
			}

			if (prevColorTemp != color) {
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
			}
			prevColorTemp = color;

			if (prevTileX != tileX || prevTileY != tileY) {
				image = images[tileX][tileY];
				xOffsetTemp = CHAR_SIZE * tileX;
				yOffsetTemp = CHAR_SIZE * tileY;
			}
			prevTileX = tileX;
			prevTileY = tileY;

			for (yTemp = 0; yTemp < CHAR_SIZE; yTemp++)
				for (xTemp = 0; xTemp < CHAR_SIZE; xTemp++)
					image.setRGB(xTemp, yTemp, map[xTemp + xOffsetTemp][yTemp + yOffsetTemp] ? fColorTemp : bColorTemp);

			graphics.drawImage(image, screenX, screenY, canvas);
		}

	}

}
