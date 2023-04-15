package kaba4cow.ascii.core;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Input {

	private static final HashMap<Integer, String> keyboardCodes = new HashMap<>();
	private static final HashMap<Integer, Integer> keyboardIndices = new HashMap<>();

	public static final int KEY_SPACE = registerKeyboard(Keyboard.KEY_SPACE, "SPACE");
	public static final int KEY_COMMA = registerKeyboard(Keyboard.KEY_COMMA, ",");
	public static final int KEY_MINUS = registerKeyboard(Keyboard.KEY_MINUS, "-");
	public static final int KEY_EQUALS = registerKeyboard(Keyboard.KEY_EQUALS, "=");
	public static final int KEY_PERIOD = registerKeyboard(Keyboard.KEY_PERIOD, ".");
	public static final int KEY_SLASH = registerKeyboard(Keyboard.KEY_SLASH, "/");
	public static final int KEY_0 = registerKeyboard(Keyboard.KEY_0, "0");
	public static final int KEY_1 = registerKeyboard(Keyboard.KEY_1, "1");
	public static final int KEY_2 = registerKeyboard(Keyboard.KEY_2, "2");
	public static final int KEY_3 = registerKeyboard(Keyboard.KEY_3, "3");
	public static final int KEY_4 = registerKeyboard(Keyboard.KEY_4, "4");
	public static final int KEY_5 = registerKeyboard(Keyboard.KEY_5, "5");
	public static final int KEY_6 = registerKeyboard(Keyboard.KEY_6, "6");
	public static final int KEY_7 = registerKeyboard(Keyboard.KEY_7, "7");
	public static final int KEY_8 = registerKeyboard(Keyboard.KEY_8, "8");
	public static final int KEY_9 = registerKeyboard(Keyboard.KEY_9, "9");
	public static final int KEY_SEMICOLON = registerKeyboard(Keyboard.KEY_SEMICOLON, ";");
	public static final int KEY_A = registerKeyboard(Keyboard.KEY_A, "A");
	public static final int KEY_B = registerKeyboard(Keyboard.KEY_B, "B");
	public static final int KEY_C = registerKeyboard(Keyboard.KEY_C, "C");
	public static final int KEY_D = registerKeyboard(Keyboard.KEY_D, "D");
	public static final int KEY_E = registerKeyboard(Keyboard.KEY_E, "E");
	public static final int KEY_F = registerKeyboard(Keyboard.KEY_F, "F");
	public static final int KEY_G = registerKeyboard(Keyboard.KEY_G, "G");
	public static final int KEY_H = registerKeyboard(Keyboard.KEY_H, "H");
	public static final int KEY_I = registerKeyboard(Keyboard.KEY_I, "I");
	public static final int KEY_J = registerKeyboard(Keyboard.KEY_J, "J");
	public static final int KEY_K = registerKeyboard(Keyboard.KEY_K, "K");
	public static final int KEY_L = registerKeyboard(Keyboard.KEY_L, "L");
	public static final int KEY_M = registerKeyboard(Keyboard.KEY_M, "M");
	public static final int KEY_N = registerKeyboard(Keyboard.KEY_N, "N");
	public static final int KEY_O = registerKeyboard(Keyboard.KEY_O, "O");
	public static final int KEY_P = registerKeyboard(Keyboard.KEY_P, "P");
	public static final int KEY_Q = registerKeyboard(Keyboard.KEY_Q, "Q");
	public static final int KEY_R = registerKeyboard(Keyboard.KEY_R, "R");
	public static final int KEY_S = registerKeyboard(Keyboard.KEY_S, "S");
	public static final int KEY_T = registerKeyboard(Keyboard.KEY_T, "T");
	public static final int KEY_U = registerKeyboard(Keyboard.KEY_U, "U");
	public static final int KEY_V = registerKeyboard(Keyboard.KEY_V, "V");
	public static final int KEY_W = registerKeyboard(Keyboard.KEY_W, "W");
	public static final int KEY_X = registerKeyboard(Keyboard.KEY_X, "X");
	public static final int KEY_Y = registerKeyboard(Keyboard.KEY_Y, "Y");
	public static final int KEY_Z = registerKeyboard(Keyboard.KEY_Z, "Z");
	public static final int KEY_BRACKET_LEFT = registerKeyboard(Keyboard.KEY_LBRACKET, "[");
	public static final int KEY_BACKSLASH = registerKeyboard(Keyboard.KEY_BACKSLASH, "\\");
	public static final int KEY_BRACKET_RIGHT = registerKeyboard(Keyboard.KEY_RBRACKET, "]");
	public static final int KEY_ESCAPE = registerKeyboard(Keyboard.KEY_ESCAPE, "ESCAPE");
	public static final int KEY_ENTER = registerKeyboard(Keyboard.KEY_RETURN, "ENTER");
	public static final int KEY_TAB = registerKeyboard(Keyboard.KEY_TAB, "TAB");
	public static final int KEY_BACKSPACE = registerKeyboard(Keyboard.KEY_BACK, "BACKSPACE");
	public static final int KEY_INSERT = registerKeyboard(Keyboard.KEY_INSERT, "INSERT");
	public static final int KEY_DELETE = registerKeyboard(Keyboard.KEY_DELETE, "DELETE");
	public static final int KEY_RIGHT = registerKeyboard(Keyboard.KEY_RIGHT, "RIGHT");
	public static final int KEY_LEFT = registerKeyboard(Keyboard.KEY_LEFT, "LEFT");
	public static final int KEY_DOWN = registerKeyboard(Keyboard.KEY_DOWN, "DOWN");
	public static final int KEY_UP = registerKeyboard(Keyboard.KEY_UP, "UP");
	public static final int KEY_HOME = registerKeyboard(Keyboard.KEY_HOME, "HOME");
	public static final int KEY_END = registerKeyboard(Keyboard.KEY_END, "END");
	public static final int KEY_CAPS_LOCK = registerKeyboard(Keyboard.KEY_CAPITAL, "CAPS LOCK");
	public static final int KEY_SCROLL_LOCK = registerKeyboard(Keyboard.KEY_SCROLL, "SCROLL LOCK");
	public static final int KEY_NUM_LOCK = registerKeyboard(Keyboard.KEY_NUMLOCK, "NUM LOCK");
	public static final int KEY_PAUSE = registerKeyboard(Keyboard.KEY_PAUSE, "PAUSE");
	public static final int KEY_F1 = registerKeyboard(Keyboard.KEY_F1, "F1");
	public static final int KEY_F2 = registerKeyboard(Keyboard.KEY_F2, "F2");
	public static final int KEY_F3 = registerKeyboard(Keyboard.KEY_F3, "F3");
	public static final int KEY_F4 = registerKeyboard(Keyboard.KEY_F4, "F4");
	public static final int KEY_F5 = registerKeyboard(Keyboard.KEY_F5, "F5");
	public static final int KEY_F6 = registerKeyboard(Keyboard.KEY_F6, "F6");
	public static final int KEY_F7 = registerKeyboard(Keyboard.KEY_F7, "F7");
	public static final int KEY_F8 = registerKeyboard(Keyboard.KEY_F8, "F8");
	public static final int KEY_F9 = registerKeyboard(Keyboard.KEY_F9, "F9");
	public static final int KEY_F10 = registerKeyboard(Keyboard.KEY_F10, "F10");
	public static final int KEY_F11 = registerKeyboard(Keyboard.KEY_F11, "F11");
	public static final int KEY_F12 = registerKeyboard(Keyboard.KEY_F12, "F12");
	public static final int KEY_F13 = registerKeyboard(Keyboard.KEY_F13, "F13");
	public static final int KEY_F14 = registerKeyboard(Keyboard.KEY_F14, "F14");
	public static final int KEY_F15 = registerKeyboard(Keyboard.KEY_F15, "F15");
	public static final int KEY_F16 = registerKeyboard(Keyboard.KEY_F16, "F16");
	public static final int KEY_F17 = registerKeyboard(Keyboard.KEY_F17, "F17");
	public static final int KEY_F18 = registerKeyboard(Keyboard.KEY_F18, "F18");
	public static final int KEY_F19 = registerKeyboard(Keyboard.KEY_F19, "F19");
	public static final int KEY_NUM_0 = registerKeyboard(Keyboard.KEY_NUMPAD0, "NP 0");
	public static final int KEY_NUM_1 = registerKeyboard(Keyboard.KEY_NUMPAD1, "NP 1");
	public static final int KEY_NUM_2 = registerKeyboard(Keyboard.KEY_NUMPAD2, "NP 2");
	public static final int KEY_NUM_3 = registerKeyboard(Keyboard.KEY_NUMPAD3, "NP 3");
	public static final int KEY_NUM_4 = registerKeyboard(Keyboard.KEY_NUMPAD4, "NP 4");
	public static final int KEY_NUM_5 = registerKeyboard(Keyboard.KEY_NUMPAD5, "NP 5");
	public static final int KEY_NUM_6 = registerKeyboard(Keyboard.KEY_NUMPAD6, "NP 6");
	public static final int KEY_NUM_7 = registerKeyboard(Keyboard.KEY_NUMPAD7, "NP 7");
	public static final int KEY_NUM_8 = registerKeyboard(Keyboard.KEY_NUMPAD8, "NP 8");
	public static final int KEY_NUM_9 = registerKeyboard(Keyboard.KEY_NUMPAD9, "NP 9");
	public static final int KEY_SHIFT_LEFT = registerKeyboard(Keyboard.KEY_LSHIFT, "LEFT SHIFT");
	public static final int KEY_SHIFT_RIGHT = registerKeyboard(Keyboard.KEY_RSHIFT, "RIGHT SHIFT");
	public static final int KEY_CONTROL_LEFT = registerKeyboard(Keyboard.KEY_LCONTROL, "LEFT CTRL");
	public static final int KEY_CONTROL_RIGHT = registerKeyboard(Keyboard.KEY_RCONTROL, "RIGHT CTRL");

	private static final byte[] keyboardStates = new byte[keyboardCodes.size()];

	private static char keyboardCharacter;

	private static final HashMap<Integer, String> mouseCodes = new HashMap<>();
	private static final HashMap<Integer, Integer> mouseIndices = new HashMap<>();

	public static final int LEFT = registerMouse(0, "LEFT BUTTON");
	public static final int MIDDLE = registerMouse(2, "MIDDLE BUTTON");
	public static final int RIGHT = registerMouse(1, "RIGHT BUTTON");

	private static final byte[] mouseStates = new byte[mouseCodes.size()];

	private static int mouseX;
	private static int mouseY;
	private static int mouseDX;
	private static int mouseDY;
	private static int mouseScroll;

	private static final byte NONE = 0;
	private static final byte PRESSED = 1;
	private static final byte DOWN = 2;
	private static final byte RELEASED = 3;

	public static void update() {
		Display.processMessages();
		Keyboard.poll();
		Mouse.poll();

		for (Integer code : keyboardIndices.keySet()) {
			int index = indexKeyboard(code);
			if (Keyboard.isKeyDown(code))
				keyboardStates[index] = DOWN;
			else
				keyboardStates[index] = NONE;
		}

		keyboardCharacter = '\0';
		while (Keyboard.next()) {
			int index = indexKeyboard(Keyboard.getEventKey());
			if (index < 0)
				continue;
			if (Keyboard.getEventKeyState()) {
				keyboardCharacter = Keyboard.getEventCharacter();
				if (!Keyboard.isRepeatEvent()) {
					keyboardStates[index] = PRESSED;
				}
			} else
				keyboardStates[index] = RELEASED;
		}

		for (Integer code : mouseIndices.keySet()) {
			int index = indexMouse(code);
			if (Mouse.isButtonDown(code))
				mouseStates[index] = DOWN;
			else
				mouseStates[index] = NONE;
		}

		while (Mouse.next()) {
			int index = indexMouse(Mouse.getEventButton());
			if (index < 0)
				continue;
			if (Mouse.getEventButtonState())
				mouseStates[index] = PRESSED;
			else
				mouseStates[index] = RELEASED;
		}

		int newX = Mouse.getX();
		if (newX >= Display.getWidth())
			newX = Display.getWidth() - 1;
		int newY = Display.getHeight() - Mouse.getY() + Window.getCursorOffset();
		mouseDX = newX - mouseX;
		mouseDY = newY - mouseY;
		mouseX = newX;
		mouseY = newY;
		mouseScroll = Mouse.getDWheel();
		if (mouseScroll < 0)
			mouseScroll = -1;
		else if (mouseScroll > 0)
			mouseScroll = 1;
	}

	public static void reset() {
		for (int i = 0; i < keyboardStates.length; i++)
			keyboardStates[indexKeyboard(i)] = NONE;
		for (int i = 0; i < mouseStates.length; i++)
			mouseStates[indexMouse(i)] = NONE;
	}

	private static int registerKeyboard(int code, String name) {
		keyboardIndices.put(code, keyboardCodes.size());
		keyboardCodes.put(code, name);
		return code;
	}

	private static int indexKeyboard(int code) {
		Integer index = keyboardIndices.get(code);
		if (index == null)
			return -1;
		return index;
	}

	public static String nameKeyboard(int code) {
		return keyboardCodes.get(code);
	}

	private static int registerMouse(int code, String name) {
		mouseIndices.put(code, mouseCodes.size());
		mouseCodes.put(code, name);
		return code;
	}

	private static int indexMouse(int code) {
		Integer index = mouseIndices.get(code);
		if (index == null)
			return -1;
		return index;
	}

	public static String nameMouse(int code) {
		return mouseCodes.get(code);
	}

	public static boolean isKey(int code) {
		return keyboardStates[indexKeyboard(code)] == DOWN;
	}

	public static boolean isKeyDown(int code) {
		return keyboardStates[indexKeyboard(code)] == PRESSED;
	}

	public static boolean isKeyUp(int code) {
		return keyboardStates[indexKeyboard(code)] == RELEASED;
	}

	public static boolean isTyped() {
		return keyboardCharacter != '\0';
	}

	public static char getCharacter() {
		return keyboardCharacter;
	}

	public static boolean isButton(int code) {
		return mouseStates[indexMouse(code)] == DOWN;
	}

	public static boolean isButtonDown(int code) {
		return mouseStates[indexMouse(code)] == PRESSED;
	}

	public static boolean isButtonUp(int code) {
		return mouseStates[indexMouse(code)] == RELEASED;
	}

	public static int getX() {
		return mouseX;
	}

	public static int getY() {
		return mouseY;
	}

	public static int getDX() {
		return mouseDX;
	}

	public static int getDY() {
		return mouseDY;
	}

	public static int getTileX() {
		return getX() / Window.getGlyphSize();
	}

	public static int getTileY() {
		return getY() / Window.getGlyphSize();
	}

	public static int getTileDX() {
		return getDX() / Window.getGlyphSize();
	}

	public static int getTileDY() {
		return getDY() / Window.getGlyphSize();
	}

	public static int getScroll() {
		return mouseScroll;
	}

	public static String typeString(String string) {
		if (Input.isKeyDown(Input.KEY_DELETE))
			return "";
		else if (Input.isKeyDown(Input.KEY_BACKSPACE))
			return string.substring(0, string.length() - 1);
		else if (Input.isTyped()) {
			char c = Input.getCharacter();
			if (c >= 32 && c < 127)
				return string + c;
		}
		return string;
	}

	public static String readClipboard() {
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			return data;
		} catch (Exception e) {
			return "";
		}
	}

	public static void writeToClipboard(String string) {
		StringSelection selection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	}

}
