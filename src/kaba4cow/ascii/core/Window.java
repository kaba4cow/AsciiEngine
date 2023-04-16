package kaba4cow.ascii.core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.drawing.Glyphs;
import kaba4cow.ascii.toolbox.Printer;

public final class Window {

	private static final int BAR_COLOR = 0xEEE000;

	private static int GLYPH_SIZE;

	private static int SCREEN_WIDTH;
	private static int SCREEN_HEIGHT;

	private static String TITLE;

	private static int WIDTH;
	private static int HEIGHT;

	private static GraphicsDevice device;
	private static WindowListener windowListener;
	private static JFrame display;
	private static Canvas canvas;

	private static boolean[][] glyphMap;
	private static Frame frame;

	private static int backgroundColor;

	private static boolean fullscreen;
	private static boolean ignoreClosing;

	private static boolean cursorOnBar;

	private static final HashMap<Integer, Stack<int[]>> pixelMap = new HashMap<>();
	private static int glyphTemp, prevGlyphTemp, colorTemp, prevColorTemp, bColorTemp, brTemp, bgTemp, bbTemp,
			fColorTemp, frTemp, fgTemp, fbTemp, xTemp, yTemp;
	private static int[] barGlyphsTemp;

	private static int newWindowWidth = Integer.MAX_VALUE;
	private static int newWindowHeight = Integer.MAX_VALUE;

	private Window() {

	}

	public static void init(String title, int glyphSize) {
		Printer.println("Initializing display");

		try {
			InputStream is = Window.class.getClassLoader().getResourceAsStream("kaba4cow/ascii/core/CP");
			BufferedImage sheet = ImageIO.read(is);
			ColorModel colorModel = sheet.getColorModel();
			glyphMap = new boolean[sheet.getWidth()][sheet.getHeight()];
			for (int y = 0; y < sheet.getHeight(); y++)
				for (int x = 0; x < sheet.getWidth(); x++)
					glyphMap[x][y] = colorModel.getRed(sheet.getRaster().getDataElements(x, y, null)) > 0;
		} catch (IOException e) {
			Engine.terminate(e);
		}

		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = screenSize.width;
		SCREEN_HEIGHT = screenSize.height;

		GLYPH_SIZE = glyphSize;

		TITLE = title;

		backgroundColor = 0x000FFF;
	}

	public static void createFullscreen() {
		newWindowWidth = 0;
		newWindowHeight = 0;
	}

	public static void createWindowed(int width, int height) {
		newWindowWidth = width;
		newWindowHeight = height;
	}

	private static void create(int width, int height) {
		fullscreen = width <= 0 || height <= 0;

		if (fullscreen) {
			WIDTH = SCREEN_WIDTH / GLYPH_SIZE;
			HEIGHT = SCREEN_HEIGHT / GLYPH_SIZE;
		} else {
			WIDTH = width;
			HEIGHT = height;

			if (WIDTH >= SCREEN_WIDTH / GLYPH_SIZE)
				WIDTH = SCREEN_WIDTH / GLYPH_SIZE - 1;
			if (HEIGHT >= SCREEN_HEIGHT / GLYPH_SIZE)
				HEIGHT = SCREEN_HEIGHT / GLYPH_SIZE - 1;
		}

		Printer.println("Creating " + (fullscreen ? "fullscreen" : "windowed") + " display: " + WIDTH + "x" + HEIGHT);

		if (display != null) {
			ignoreClosing = true;
			Renderer.destroy();
			display.dispose();
		}
		ignoreClosing = false;

		frame = new Frame(WIDTH, HEIGHT);
		Drawer.resetFrame();

		display = new JFrame(TITLE);
		display.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!ignoreClosing)
					Engine.requestClose();
			}
		});
		display.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowListener = new WindowListener(display);
		new DropTarget(display, windowListener);

		Dimension dimension = new Dimension(WIDTH * GLYPH_SIZE, HEIGHT * GLYPH_SIZE + GLYPH_SIZE);

		canvas = new Canvas();
		canvas.setFocusable(false);
		canvas.setPreferredSize(dimension);
		canvas.setMaximumSize(dimension);
		canvas.setMinimumSize(dimension);

		display.setPreferredSize(dimension);
		display.setFocusable(true);
		display.setUndecorated(true);
		display.add(canvas);
		display.setResizable(false);
		display.pack();
		display.setLocationRelativeTo(null);
		display.addFocusListener(windowListener);

		if (fullscreen)
			device.setFullScreenWindow(display);

		Renderer.init(canvas, fullscreen);

		display.requestFocus();
		display.setFocusTraversalKeysEnabled(false);

		display.setIgnoreRepaint(true);
		canvas.setIgnoreRepaint(true);
		display.setVisible(true);

		barGlyphsTemp = new int[WIDTH];
	}

	public static void destroy() {
		if (!display.isVisible())
			return;
		Printer.println("Destroying display");
		ignoreClosing = false;
		Renderer.destroy();
		display.dispose();
	}

	public static void refresh() {
		if (newWindowWidth == Integer.MAX_VALUE || newWindowHeight == Integer.MAX_VALUE)
			return;
		create(newWindowWidth, newWindowHeight);
		newWindowWidth = Integer.MAX_VALUE;
		newWindowHeight = Integer.MAX_VALUE;
	}

	public static void update() {
		for (xTemp = 0; xTemp < frame.length; xTemp++) {
			frame.glyphs[xTemp] = 0;
			frame.colors[xTemp] = backgroundColor;
		}

		if (fullscreen) {
			windowListener.setActive(false);
			return;
		}

		if (windowListener.isActive()) {
			windowListener.update();
			if (!Input.isButton(Input.LEFT))
				windowListener.setActive(false);
		}

		int mouseIndex = 0;
		boolean tempCursorOnBar = false;
		if (Input.getY() < 0) {
			mouseIndex = Input.getTileX();
			tempCursorOnBar = true;
		}
		if (!tempCursorOnBar)
			mouseIndex = Input.getTileY() * WIDTH + Input.getTileX();
		cursorOnBar = false;

		if (tempCursorOnBar && Input.isButtonUp(Input.LEFT)) {
			if (mouseIndex == WIDTH - 1)
				Engine.requestClose();
			else if (display.getExtendedState() != JFrame.ICONIFIED && mouseIndex == WIDTH - 2)
				display.setExtendedState(JFrame.ICONIFIED);
		}

		if (tempCursorOnBar)
			windowListener.setActive(true);

		cursorOnBar = tempCursorOnBar;
	}

	public static void render() {
		Renderer.update();

		Stack<int[]> positions;
		int[] position;

		int titleLength = TITLE.length();
		int barOffset = fullscreen ? 0 : 1;

		pixelMap.clear();
		for (yTemp = 0; yTemp < HEIGHT; yTemp++)
			for (xTemp = 0; xTemp < WIDTH; xTemp++) {
				colorTemp = frame.colors[yTemp * WIDTH + xTemp];
				if (!pixelMap.containsKey(colorTemp))
					pixelMap.put(colorTemp, new Stack<>());
				pixelMap.get(colorTemp).push(new int[] { xTemp, yTemp });
			}
		if (!fullscreen)
			for (xTemp = 0; xTemp < WIDTH; xTemp++) {
				if (xTemp == WIDTH - 2)
					glyphTemp = Glyphs.HYPHEN_MINUS;
				else if (xTemp == WIDTH - 1)
					glyphTemp = Glyphs.LATIN_SMALL_LETTER_X;
				else
					glyphTemp = Glyphs.SPACE;
				if (xTemp > 0 && xTemp - 1 < titleLength)
					glyphTemp = TITLE.charAt(xTemp - 1);
				barGlyphsTemp[xTemp] = glyphTemp;

				if (!pixelMap.containsKey(BAR_COLOR))
					pixelMap.put(BAR_COLOR, new Stack<>());
				pixelMap.get(BAR_COLOR).push(new int[] { xTemp, -barOffset });
			}

		for (Integer key : pixelMap.keySet()) {
			positions = pixelMap.get(key);
			colorTemp = key;
			while (!positions.isEmpty()) {
				position = positions.pop();
				if (position[1] < 0)
					glyphTemp = barGlyphsTemp[position[0]];
				else
					glyphTemp = frame.glyphs[position[1] * WIDTH + position[0]];

				xTemp = position[0];
				yTemp = position[1] + barOffset;

				drawGlyph();
			}
		}
	}

	private static void drawGlyph() {
		if (colorTemp == prevColorTemp && glyphTemp == prevGlyphTemp) {
			Renderer.draw();
			return;
		}

		if (colorTemp != prevColorTemp) {
			bColorTemp = (colorTemp >> 12) & 0xFFF;
			brTemp = (bColorTemp >> 8) & 0xF;
			brTemp |= brTemp << 4;
			bgTemp = (bColorTemp >> 4) & 0xF;
			bgTemp |= bgTemp << 4;
			bbTemp = (bColorTemp >> 0) & 0xF;
			bbTemp |= bbTemp << 4;

			fColorTemp = (colorTemp >> 0) & 0xFFF;
			frTemp = (fColorTemp >> 8) & 0xF;
			frTemp |= frTemp << 4;
			fgTemp = (fColorTemp >> 4) & 0xF;
			fgTemp |= fgTemp << 4;
			fbTemp = (fColorTemp >> 0) & 0xF;
			fbTemp |= fbTemp << 4;
		}
		prevColorTemp = colorTemp;

		Renderer.draw(xTemp, yTemp, glyphTemp, brTemp, bgTemp, bbTemp, frTemp, fgTemp, fbTemp);
	}

	public static void setBackground(char c, int color) {
		if (backgroundColor != color) {
			for (int i = 0; i < frame.glyphs.length; i++)
				frame.colors[i] = color;
			backgroundColor = color;
		}
	}

	public static char getGlyph(int x, int y) {
		return frame.getGlyph(x, y);
	}

	public static int getColor(int x, int y) {
		return frame.getColor(x, y);
	}

	public static Frame getFrame() {
		return frame;
	}

	public static boolean[][] getGlyphMap() {
		return glyphMap;
	}

	public static boolean isCursorOnBar() {
		return cursorOnBar;
	}

	public static String getTitle() {
		return TITLE;
	}

	public static void setTitle(String title) {
		if (title != null)
			TITLE = title;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public static int getScreenHeight() {
		return SCREEN_HEIGHT;
	}

	public static boolean isFullscreen() {
		return fullscreen;
	}

	public static int getCursorOffset() {
		return fullscreen ? 0 : -GLYPH_SIZE;
	}

	public static int getGlyphSize() {
		return GLYPH_SIZE;
	}

	private static class WindowListener implements FocusListener, DropTargetListener {

		private final JFrame frame;

		private boolean active;

		private Point startPosition;

		public WindowListener(JFrame frame) {
			this.frame = frame;
			this.active = false;
			this.startPosition = null;
		}

		public void update() {
			if (Input.isButtonDown(Input.LEFT)) {
				startPosition = MouseInfo.getPointerInfo().getLocation();
				startPosition.x -= frame.getLocationOnScreen().x;
				startPosition.y -= frame.getLocationOnScreen().y;
			} else if (Input.isButton(Input.LEFT) && startPosition != null) {
				Point currentPosition = MouseInfo.getPointerInfo().getLocation();
				int newX = currentPosition.x - startPosition.x;
				int newY = currentPosition.y - startPosition.y;
				if (newX < 0)
					newX = 0;
				if (newY < 0)
					newY = 0;
				frame.setLocation(newX, newY);
			} else if (Input.isButtonUp(Input.LEFT))
				startPosition = null;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
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

}
