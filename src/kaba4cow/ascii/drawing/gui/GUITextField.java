package kaba4cow.ascii.drawing.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.drawing.drawers.BoxDrawer;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.toolbox.Colors;

public class GUITextField extends GUIObject {

	private static final char BACKSPACE = 0x08;
	private static final char DELETE = 0x7F;

	private static final int HOME = 0;
	private static final int END = 1;
	private static final int LEFT = 2;
	private static final int RIGHT = 3;
	private static final int UP = 4;
	private static final int DOWN = 5;

	private final StringBuilder builder;

	private String text;
	private boolean active;

	private int cursor;
	private int cursorX;
	private int cursorY;

	private int lineLength;

	public GUITextField(GUIFrame frame, int color, String string) {
		super(frame, color);
		this.builder = new StringBuilder(string);
		this.text = string;
		this.active = false;
		this.cursor = 0;
		this.cursorX = 0;
		this.cursorY = 0;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked) {
			active = mouseX >= bX + 1 && mouseX < bX + bWidth - 1 && mouseY >= bY + 1 && mouseY < bY + bHeight - 1;

			if (active) {
				int length = text.length();
				cursorX = 0;
				cursorY = 0;
				for (cursor = 0; cursor <= length; cursor++) {
					if (cursor == length || cursorX == mouseX - bX - 1 && cursorY == mouseY - bY - 1)
						break;
					cursorX++;
					if (cursorX >= bWidth - 2) {
						lineLength = cursorX;
						cursorX = 0;
						cursorY++;
					}
				}
				if (lineLength == 0)
					lineLength = bWidth - 2;
			}
		}

		if (active) {
			int length = text.length();
			boolean modified = false;

			if (Keyboard.isKeyDown(Keyboard.KEY_ENTER))
				active = false;
			else if (Keyboard.isKeyDown(Keyboard.KEY_HOME))
				moveCursor(HOME);
			else if (Keyboard.isKeyDown(Keyboard.KEY_END))
				moveCursor(END);
			else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				moveCursor(LEFT);
			else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				moveCursor(RIGHT);
			else if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				moveCursor(UP);
			else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				moveCursor(DOWN);
			else if (Keyboard.isKey(Keyboard.KEY_CONTROL_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
				try {
					String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
							.getData(DataFlavor.stringFlavor);
					data = data.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
					int dataLength = data.length();
					builder.insert(cursor, data);
					text = builder.toString();
					for (int i = 0; i < dataLength; i++)
						moveCursor(RIGHT);
				} catch (Exception e) {
				}
			} else if (Keyboard.getLastTyped() != null) {
				char c = Keyboard.getLastTyped().getKeyChar();
				if (c == BACKSPACE) {
					if (cursor > 0 && cursor <= length) {
						builder.deleteCharAt(cursor - 1);
						moveCursor(LEFT);
						modified = true;
					}
				} else if (c == DELETE) {
					if (cursor >= 0 && cursor < length) {
						builder.deleteCharAt(cursor);
						modified = true;
					}
				} else if (c >= 32 && c < DELETE) {
					builder.insert(cursor, c);
					text = builder.toString();
					moveCursor(RIGHT);
				}
				Keyboard.resetLastTyped();
			}

			if (modified)
				text = builder.toString();
		}
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		BoxDrawer.drawBox(x, y, width - 1, totalLines - 1, true, color);
		totalLines = 2 + Drawer.drawString(x + 1, y + 1, false, width - 2, text, color);

		if (active) {
			int newColor = Colors.swap(Display.getColor(x + 1 + cursorX, y + 1 + cursorY));
			char newChar = Display.getChar(x + 1 + cursorX, y + 1 + cursorY);
			Drawer.drawChar(x + 1 + cursorX, y + 1 + cursorY, newChar, newColor);
		}
		return totalLines;
	}

	@Override
	public int totalLines(int width) {
		return 2 + Drawer.totalLines(width - 2, text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.builder.setLength(0);
		this.builder.append(text);
	}

	private void moveCursor(int movement) {
		int length = text.length();
		if (movement == HOME)
			cursor = 0;
		else if (movement == END)
			cursor = length;
		else if (movement == LEFT)
			cursor--;
		else if (movement == RIGHT)
			cursor++;
		else if (movement == UP)
			cursor -= lineLength;
		else if (movement == DOWN)
			cursor += lineLength;

		if (cursor < 0)
			cursor = 0;
		else if (cursor > length)
			cursor = length;

		cursorX = 0;
		cursorY = 0;
		for (int cursor = 0; cursor <= length; cursor++) {
			if (cursor == this.cursor)
				break;
			cursorX++;
			if (cursorX >= bWidth - 2) {
				cursorX = 0;
				cursorY++;
			}
		}
	}

}