package kaba4cow.ascii.gui;

import java.util.ArrayList;

import kaba4cow.ascii.drawing.Drawer;

public class GUIRadioPanel extends GUIObject {

	private String text;

	private ArrayList<GUIRadioButton> buttons;
	private int index;

	public GUIRadioPanel(GUIFrame frame, int color, String text) {
		super(frame, color);
		this.text = text;
		this.buttons = new ArrayList<>();
		this.index = 0;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {

	}

	@Override
	public int render(int x, int y, int width, int height) {
		return Drawer.drawString(x, y, false, width, text, color);
	}

	@Override
	public int totalLines(int width) {
		return Drawer.totalLines(width, text);
	}

	public int addButton(GUIRadioButton button) {
		if (!buttons.contains(button))
			buttons.add(button);
		return buttons.size() - 1;
	}

	public void setIndex(int index) {
		if (index >= 0 && index < buttons.size())
			this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}