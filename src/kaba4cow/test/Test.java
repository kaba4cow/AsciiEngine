package kaba4cow.test;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.core.Renderer;
import kaba4cow.ascii.core.Window;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.gui.GUIButton;
import kaba4cow.ascii.gui.GUICheckbox;
import kaba4cow.ascii.gui.GUIFrame;
import kaba4cow.ascii.gui.GUIProgressBar;
import kaba4cow.ascii.gui.GUIRadioButton;
import kaba4cow.ascii.gui.GUIRadioPanel;
import kaba4cow.ascii.gui.GUISeparator;
import kaba4cow.ascii.gui.GUISlider;
import kaba4cow.ascii.gui.GUIText;
import kaba4cow.ascii.gui.GUITextField;
import kaba4cow.ascii.toolbox.rng.RNG;

public class Test implements MainProgram {

	private GUIFrame frame;

	private int font = 0;

	public Test() {

	}

	@Override
	public void init() {
		frame = new GUIFrame(0x000FFF, true, true).setTitle("Window").setWidth(30).setHeight(30);

		new GUIText(frame, -1, "Sliders:");
		for (int i = 1; i <= 3; i++)
			new GUISlider(frame, -1, RNG.randomFloat(1f));
		new GUISeparator(frame, -1, false);

		GUIRadioPanel radio = new GUIRadioPanel(frame, -1, "Radio Buttons:");
		for (int i = 1; i <= 3; i++)
			new GUIRadioButton(radio, -1, "Radio Button #" + i);
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Checkboxes:");
		for (int i = 1; i <= 3; i++)
			new GUICheckbox(frame, -1, "Checkbox #" + i, RNG.randomBoolean());
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Progress bar:");
		new GUIProgressBar(frame, -1, f -> (0.04f * Engine.getElapsedTime()) % 1.01f);
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Text Field:");
		new GUITextField(frame, -1,
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vehicula metus non efficitur fringilla. Maecenas vulputate at eros non porttitor. Donec pharetra non ipsum sed dapibus.");
		new GUISeparator(frame, -1, false);

		new GUIText(frame, -1, "Buttons:");
		for (int i = 1; i <= 3; i++)
			new GUIButton(frame, -1, "Button #" + i, f -> new GUIText(frame, -1, "Button pressed"));
		new GUISeparator(frame, -1, false);
	}

	@Override
	public void update(float dt) {
		frame.update();

		if (Input.isKey(Input.KEY_ESCAPE))
			Engine.requestClose();

		if (Input.isKeyDown(Input.KEY_ENTER))
			init();

		if (Input.isKeyDown(Input.KEY_F))
			if (Window.isFullscreen())
				Window.createWindowed(40, 40);
			else
				Window.createFullscreen();

		if (Input.isKeyDown(Input.KEY_A)) {
			font--;
			if (font < 0)
				font = 0;
			Renderer.setFont(font);
		} else if (Input.isKeyDown(Input.KEY_D)) {
			font++;
			if (font >= Renderer.getFontCount())
				font = Renderer.getFontCount() - 1;
			Renderer.setFont(font);
		}
	}

	@Override
	public void render() {
		frame.render();
		Drawer.drawString(0, 0, false, "FPS " + Engine.getCurrentFramerate(), 0x222FFF);
		Drawer.drawString(0, 1, false, Renderer.getFontName(font), 0x222FFF);
	}

	public static void main(String[] args) throws Exception {
		Engine.init("Test", 60);
		Window.createWindowed(40, 40);
		Engine.start(new Test());
	}

}
