package kaba4cow.ascii.gui;

import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.core.Window;
import kaba4cow.ascii.drawing.BoxDrawer;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.toolbox.Colors;

public class GUITextField extends GUIObject {

	private static final char DELETE = 0x7F;

	private static final int HOME = 0;
	private static final int END = 1;
	private static final int LEFT = 2;
	private static final int RIGHT = 3;
	private static final int UP = 4;
	private static final int DOWN = 5;

	private final StringBuilder builder;

	private String charset;

	private String text;
	private boolean active;

	private int maxCharacters;

	private int cursor;
	private int cursorX;
	private int cursorY;

	private int lineLength;

	public GUITextField(GUIFrame frame, int color, String string) {
		super(frame, color);
		this.builder = new StringBuilder(string);
		this.text = string;
		this.active = false;
		this.maxCharacters = Integer.MAX_VALUE;
		this.cursor = 0;
		this.cursorX = 0;
		this.cursorY = 0;
		this.charset = null;
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked) {
			active = mouseX >= bX && mouseX < bX + bWidth && mouseY >= bY && mouseY < bY + bHeight;

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

			if (Input.isKeyDown(Input.KEY_ENTER))
				active = false;
			else if (Input.isKeyDown(Input.KEY_HOME))
				moveCursor(HOME);
			else if (Input.isKeyDown(Input.KEY_END))
				moveCursor(END);
			else if (Input.isKeyDown(Input.KEY_LEFT))
				moveCursor(LEFT);
			else if (Input.isKeyDown(Input.KEY_RIGHT))
				moveCursor(RIGHT);
			else if (Input.isKeyDown(Input.KEY_UP))
				moveCursor(UP);
			else if (Input.isKeyDown(Input.KEY_DOWN))
				moveCursor(DOWN);
			else if (Input.isKeyDown(Input.KEY_BACKSPACE) && cursor > 0 && cursor <= length) {
				builder.deleteCharAt(cursor - 1);
				moveCursor(LEFT);
				modified = true;
			} else if (Input.isKeyDown(Input.KEY_DELETE) && cursor >= 0 && cursor < length) {
				builder.deleteCharAt(cursor);
				modified = true;
			} else if (Input.isKey(Input.KEY_CONTROL_LEFT) && Input.isKeyDown(Input.KEY_V)) {
				try {
					String data = Input.readClipboard();
					StringBuilder newData = new StringBuilder();
					for (int i = 0; i < data.length(); i++)
						if (isAllowed(data.charAt(i)))
							newData.append(data.charAt(i));
					data = newData.toString();
					builder.insert(cursor, data);
					for (int i = 0; i < data.length(); i++)
						moveCursor(RIGHT);
					text = builder.toString();
				} catch (Exception e) {
				}
			} else if (Input.isTyped()) {
				char c = Input.getCharacter();
				if (c >= 32 && c < DELETE && isAllowed(c) && text.length() < maxCharacters) {
					builder.insert(cursor, c);
					text = builder.toString();
					moveCursor(RIGHT);
				}
			}

			if (modified)
				text = builder.toString();
		}
	}

	private boolean isAllowed(char c) {
		if (c == '\t' || c == '\r' || c == '\n')
			return false;
		if (charset == null || charset.isEmpty())
			return true;
		return charset.indexOf(c, 0) != -1;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		BoxDrawer.drawBox(x, y, width - 1, totalLines - 1, true, color);
		totalLines = 2 + Drawer.drawString(x + 1, y + 1, false, width - 2, text, color);

		if (active && Engine.getElapsedTime() % 0.8f < 0.4f) {
			char newChar = Window.getGlyph(x + 1 + cursorX, y + 1 + cursorY);
			Drawer.draw(x + 1 + cursorX, y + 1 + cursorY, newChar, Colors.swap(color));
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

	public GUITextField setText(String text) {
		this.text = text;
		this.builder.setLength(0);
		this.builder.append(text);
		return this;
	}

	public int getMaxCharacters() {
		return maxCharacters;
	}

	public GUITextField setMaxCharacters(int maxCharacters) {
		this.maxCharacters = maxCharacters;
		return this;
	}

	public GUITextField setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public GUITextField setActive() {
		active = true;
		return this;
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