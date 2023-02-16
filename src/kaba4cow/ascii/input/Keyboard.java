package kaba4cow.ascii.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public final class Keyboard implements KeyListener {

	private static final ArrayList<Integer> codes = new ArrayList<>();
	private static final HashMap<Integer, Integer> indices = new HashMap<>();

	public static final int KEY_SPACE = register(KeyEvent.VK_SPACE);
	public static final int KEY_COMMA = register(KeyEvent.VK_COMMA);
	public static final int KEY_MINUS = register(KeyEvent.VK_MINUS);
	public static final int KEY_PERIOD = register(KeyEvent.VK_PERIOD);
	public static final int KEY_SLASH = register(KeyEvent.VK_SLASH);
	public static final int KEY_0 = register(KeyEvent.VK_0);
	public static final int KEY_1 = register(KeyEvent.VK_1);
	public static final int KEY_2 = register(KeyEvent.VK_2);
	public static final int KEY_3 = register(KeyEvent.VK_3);
	public static final int KEY_4 = register(KeyEvent.VK_4);
	public static final int KEY_5 = register(KeyEvent.VK_5);
	public static final int KEY_6 = register(KeyEvent.VK_6);
	public static final int KEY_7 = register(KeyEvent.VK_7);
	public static final int KEY_8 = register(KeyEvent.VK_8);
	public static final int KEY_9 = register(KeyEvent.VK_9);
	public static final int KEY_SEMICOLON = register(KeyEvent.VK_SEMICOLON);
	public static final int KEY_A = register(KeyEvent.VK_A);
	public static final int KEY_B = register(KeyEvent.VK_B);
	public static final int KEY_C = register(KeyEvent.VK_C);
	public static final int KEY_D = register(KeyEvent.VK_D);
	public static final int KEY_E = register(KeyEvent.VK_E);
	public static final int KEY_F = register(KeyEvent.VK_F);
	public static final int KEY_G = register(KeyEvent.VK_G);
	public static final int KEY_H = register(KeyEvent.VK_H);
	public static final int KEY_I = register(KeyEvent.VK_I);
	public static final int KEY_J = register(KeyEvent.VK_J);
	public static final int KEY_K = register(KeyEvent.VK_K);
	public static final int KEY_L = register(KeyEvent.VK_L);
	public static final int KEY_M = register(KeyEvent.VK_M);
	public static final int KEY_N = register(KeyEvent.VK_N);
	public static final int KEY_O = register(KeyEvent.VK_O);
	public static final int KEY_P = register(KeyEvent.VK_P);
	public static final int KEY_Q = register(KeyEvent.VK_Q);
	public static final int KEY_R = register(KeyEvent.VK_R);
	public static final int KEY_S = register(KeyEvent.VK_S);
	public static final int KEY_T = register(KeyEvent.VK_T);
	public static final int KEY_U = register(KeyEvent.VK_U);
	public static final int KEY_V = register(KeyEvent.VK_V);
	public static final int KEY_W = register(KeyEvent.VK_W);
	public static final int KEY_X = register(KeyEvent.VK_X);
	public static final int KEY_Y = register(KeyEvent.VK_Y);
	public static final int KEY_Z = register(KeyEvent.VK_Z);
	public static final int KEY_BRACKET_LEFT = register(KeyEvent.VK_OPEN_BRACKET);
	public static final int KEY_BACKSLASH = register(KeyEvent.VK_BACK_SLASH);
	public static final int KEY_BRACKET_RIGHT = register(KeyEvent.VK_CLOSE_BRACKET);
	public static final int KEY_ESCAPE = register(KeyEvent.VK_ESCAPE);
	public static final int KEY_ENTER = register(KeyEvent.VK_ENTER);
	public static final int KEY_TAB = register(KeyEvent.VK_TAB);
	public static final int KEY_BACKSPACE = register(KeyEvent.VK_BACK_SPACE);
	public static final int KEY_INSERT = register(KeyEvent.VK_INSERT);
	public static final int KEY_DELETE = register(KeyEvent.VK_DELETE);
	public static final int KEY_RIGHT = register(KeyEvent.VK_RIGHT);
	public static final int KEY_LEFT = register(KeyEvent.VK_LEFT);
	public static final int KEY_DOWN = register(KeyEvent.VK_DOWN);
	public static final int KEY_UP = register(KeyEvent.VK_UP);
	public static final int KEY_PAGE_UP = register(KeyEvent.VK_PAGE_UP);
	public static final int KEY_PAGE_DOWN = register(KeyEvent.VK_PAGE_DOWN);
	public static final int KEY_HOME = register(KeyEvent.VK_HOME);
	public static final int KEY_END = register(KeyEvent.VK_END);
	public static final int KEY_CAPS_LOCK = register(KeyEvent.VK_CAPS_LOCK);
	public static final int KEY_SCROLL_LOCK = register(KeyEvent.VK_SCROLL_LOCK);
	public static final int KEY_NUM_LOCK = register(KeyEvent.VK_NUM_LOCK);
	public static final int KEY_PRINT_SCREEN = register(KeyEvent.VK_PRINTSCREEN);
	public static final int KEY_PAUSE = register(KeyEvent.VK_PAUSE);
	public static final int KEY_F1 = register(KeyEvent.VK_F1);
	public static final int KEY_F2 = register(KeyEvent.VK_F2);
	public static final int KEY_F3 = register(KeyEvent.VK_F3);
	public static final int KEY_F4 = register(KeyEvent.VK_F4);
	public static final int KEY_F5 = register(KeyEvent.VK_F5);
	public static final int KEY_F6 = register(KeyEvent.VK_F6);
	public static final int KEY_F7 = register(KeyEvent.VK_F7);
	public static final int KEY_F8 = register(KeyEvent.VK_F8);
	public static final int KEY_F9 = register(KeyEvent.VK_F9);
	public static final int KEY_F10 = register(KeyEvent.VK_F10);
	public static final int KEY_F11 = register(KeyEvent.VK_F11);
	public static final int KEY_F12 = register(KeyEvent.VK_F12);
	public static final int KEY_F13 = register(KeyEvent.VK_F13);
	public static final int KEY_F14 = register(KeyEvent.VK_F14);
	public static final int KEY_F15 = register(KeyEvent.VK_F15);
	public static final int KEY_F16 = register(KeyEvent.VK_F16);
	public static final int KEY_F17 = register(KeyEvent.VK_F17);
	public static final int KEY_F18 = register(KeyEvent.VK_F18);
	public static final int KEY_F19 = register(KeyEvent.VK_F19);
	public static final int KEY_F20 = register(KeyEvent.VK_F20);
	public static final int KEY_F21 = register(KeyEvent.VK_F21);
	public static final int KEY_F22 = register(KeyEvent.VK_F22);
	public static final int KEY_F23 = register(KeyEvent.VK_F23);
	public static final int KEY_F24 = register(KeyEvent.VK_F24);
	public static final int KEY_NUM_0 = register(KeyEvent.VK_NUMPAD0);
	public static final int KEY_NUM_1 = register(KeyEvent.VK_NUMPAD1);
	public static final int KEY_NUM_2 = register(KeyEvent.VK_NUMPAD2);
	public static final int KEY_NUM_3 = register(KeyEvent.VK_NUMPAD3);
	public static final int KEY_NUM_4 = register(KeyEvent.VK_NUMPAD4);
	public static final int KEY_NUM_5 = register(KeyEvent.VK_NUMPAD5);
	public static final int KEY_NUM_6 = register(KeyEvent.VK_NUMPAD6);
	public static final int KEY_NUM_7 = register(KeyEvent.VK_NUMPAD7);
	public static final int KEY_NUM_8 = register(KeyEvent.VK_NUMPAD8);
	public static final int KEY_NUM_9 = register(KeyEvent.VK_NUMPAD9);
	public static final int KEY_SHIFT_LEFT = register(KeyEvent.VK_SHIFT);
	public static final int KEY_CONTROL_LEFT = register(KeyEvent.VK_CONTROL);
	public static final int KEY_ALT_LEFT = register(KeyEvent.VK_ALT);

	public static final int NUM_KEYBOARD_BUTTONS = codes.size();

	private final boolean[] prevStates = new boolean[NUM_KEYBOARD_BUTTONS];
	private final boolean[] states = new boolean[NUM_KEYBOARD_BUTTONS];

	private static Keyboard instance;

	private Keyboard() {

	}

	static {
		instance = new Keyboard();
	}

	public static Keyboard get() {
		return instance;
	}

	private static int register(int code) {
		indices.put(code, codes.size());
		codes.add(code);
		return code;
	}

	public static void update() {
		for (int i = 0; i < NUM_KEYBOARD_BUTTONS; i++)
			instance.prevStates[i] = instance.states[i];
	}

	public static void reset() {
		for (int i = 0; i < NUM_KEYBOARD_BUTTONS; i++) {
			instance.prevStates[i] = false;
			instance.states[i] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int index = index(e.getKeyCode());
		states[index] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int index = index(e.getKeyCode());
		states[index] = false;
	}

	public static boolean isKey(int code) {
		return instance.states[index(code)];
	}

	public static boolean isKeyDown(int code) {
		int index = index(code);
		return instance.states[index] && !instance.prevStates[index];
	}

	public static boolean isKeyUp(int code) {
		int index = index(code);
		return !instance.states[index] && instance.prevStates[index];
	}

	private static int index(int code) {
		return indices.get(code);
	}

}
