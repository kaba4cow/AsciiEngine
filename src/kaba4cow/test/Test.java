package kaba4cow.test;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.drawing.glyphs.Glyphs;
import kaba4cow.ascii.drawing.gui.GUIButton;
import kaba4cow.ascii.drawing.gui.GUICheckbox;
import kaba4cow.ascii.drawing.gui.GUIFrame;
import kaba4cow.ascii.drawing.gui.GUIProgressBar;
import kaba4cow.ascii.drawing.gui.GUISeparator;
import kaba4cow.ascii.drawing.gui.GUISlider;
import kaba4cow.ascii.drawing.gui.GUIText;
import kaba4cow.ascii.drawing.gui.GUITextField;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.toolbox.Colors;
import kaba4cow.ascii.toolbox.noise.Noise;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Test implements MainProgram {

	private GUIFrame frame;

	private Frame image;

	public Test() {

	}

	@Override
	public void init() {
//		if (SystemTray.isSupported()) {
//			SystemTray tray = SystemTray.getSystemTray();
//			Image image = Toolkit.getDefaultToolkit().createImage("glyph_list.png");
//			TrayIcon icon = new TrayIcon(image, "Tray Demo");
//			icon.setImageAutoSize(false);
//			icon.setToolTip("Tool tip");
//			try {
//				tray.add(icon);
//			} catch (AWTException e) {
//				e.printStackTrace();
//			}
//			icon.displayMessage("Hello World", "Notification", MessageType.INFO);
//		}

		frame = new GUIFrame(0x000FFF, true, true).setTitle("Window");

		new GUIText(frame, -1, "Sliders:");
		for (int i = 1; i <= 3; i++)
			new GUISlider(frame, -1, RNG.randomFloat(1f));
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Checkboxes:");
		for (int i = 1; i <= 3; i++)
			new GUICheckbox(frame, -1, "Checkbox #" + i, RNG.randomBoolean());
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Progress bar:");
		new GUIProgressBar(frame, -1, f -> (0.04f * Engine.getElapsedTime()) % 1.01f);
		new GUISeparator(frame, -1, false);

		new GUITextField(frame, -1,
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vehicula metus non efficitur fringilla. Maecenas vulputate at eros non porttitor. Donec pharetra non ipsum sed dapibus. Sed ullamcorper, sem non pharetra pellentesque, sapien lectus facilisis enim, at lacinia arcu neque a justo. Donec sodales suscipit libero, eu maximus felis faucibus et. Mauris interdum egestas lacus, ut convallis neque convallis ut. Nam lobortis imperdiet orci, a pellentesque enim faucibus sed. Duis sit amet velit dui. Curabitur ornare, nunc ac aliquam malesuada, mauris orci efficitur sem, non mollis metus ipsum nec nisi. Duis laoreet mi a tortor bibendum iaculis. Ut et mollis libero, id mollis libero. In ac viverra lacus. Aenean auctor commodo maximus.");
		new GUISeparator(frame, -1, false);

		for (int i = 1; i <= 3; i++)
			new GUIButton(frame, -1, "Button #" + i, f -> new GUIText(frame, -1, "Line generated"));
		new GUISeparator(frame, -1, false);

		image = new Frame(Display.getWidth(), Display.getHeight());
		Noise noise = new Noise(578229l);
		for (int y = 0; y < image.height; y++)
			for (int x = 0; x < image.width; x++) {
				int value = (int) (0xF * noise.getCombinedValue(0.23f * x, 0.35f * y, 3));
				int color = Colors.create(value);
				image.glyphs[y * image.width + x] = Glyphs.MEDIUM_SHADE;
				image.colors[y * image.width + x] = color;
			}
	}

	@Override
	public void update(float dt) {
		frame.update();

		if (Keyboard.isKey(Keyboard.KEY_ESCAPE))
			Engine.requestClose();

		if (Keyboard.isKeyDown(Keyboard.KEY_ENTER))
			init();

		if (Keyboard.isKeyDown(Keyboard.KEY_C))
			Display.setDrawCursor(!Display.isDrawCursor());
		if (Keyboard.isKeyDown(Keyboard.KEY_V))
			Display.setCursorWaiting(!Display.isCursorWaiting());
	}

	@Override
	public void render() {
		Display.setBackground(Glyphs.PERCENT_SIGN, 0x400200);
//		Drawer.drawFrame(0, 0, false, image);
		frame.render();
		Drawer.drawString(0, 0, false, "FPS " + Engine.getCurrentFramerate(), 0x333F33);
	}

	public static void main(String[] args) throws Exception {
		Engine.init("Test", 120);
		Display.createWindowed(40, 30);
		Engine.start(new Test());

//		int addLength = 3;
//		int length = "c = 000".length() + addLength;
//		int width = 5;
//		int height = Glyphs.numGlyphs() / width;
//
//		Frame frame = new Frame(length * width - addLength, height);
//		Drawer.setFrame(frame);
//		int i = 0;
//		for (int x = 0; x < width; x++)
//			for (int y = 0; y < height; y++) {
//				char c = (char) i;
//				Drawer.drawString(x * length, y, false, c + String.format(" = %3d", i), 0x000FFF);
//				i++;
//			}
//		Drawer.resetFrame();
//		Display.saveImage(frame, new File("glyphs.png"));
	}

}
