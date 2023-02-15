package kaba4cow.ascii.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;

import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.toolbox.maths.vectors.Vector2i;

public final class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private static final ArrayList<Integer> codes = new ArrayList<>();
	private static final HashMap<Integer, Integer> indices = new HashMap<>();

	public static final int LEFT = register(MouseEvent.BUTTON1);
	public static final int MIDDLE = register(MouseEvent.BUTTON2);
	public static final int RIGHT = register(MouseEvent.BUTTON3);

	public static final int NUM_MOUSE_BUTTONS = codes.size();

	private final boolean[] prevStates = new boolean[NUM_MOUSE_BUTTONS];
	private final boolean[] states = new boolean[NUM_MOUSE_BUTTONS];

	private Vector2i cursorPosition = new Vector2i();
	private Vector2i cursorDelta = new Vector2i();

	private int scroll = 0;

	private static Mouse instance;

	public Mouse() {

	}

	static {
		instance = new Mouse();
	}

	public static Mouse get() {
		return instance;
	}

	private static int register(int code) {
		indices.put(code, codes.size());
		codes.add(code);
		return code;
	}

	public static void update() {
		instance.cursorDelta.x = 0;
		instance.cursorDelta.y = 0;
		instance.scroll = 0;
		for (int i = 0; i < NUM_MOUSE_BUTTONS; i++)
			instance.prevStates[i] = false;
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

	public static int getScroll() {
		return instance.scroll;
	}

	public static Vector2i getCursorPosition() {
		return instance.cursorPosition;
	}

	public static int getTileX() {
		return instance.cursorPosition.x / Display.getCharWidth();
	}

	public static int getTileY() {
		return instance.cursorPosition.y / Display.getCharHeight();
	}

	public static float getX() {
		return instance.cursorPosition.x;
	}

	public static float getY() {
		return instance.cursorPosition.y;
	}

	public static Vector2i getCursorDelta() {
		return instance.cursorDelta;
	}

	public static float getDX() {
		return instance.cursorDelta.x;
	}

	public static float getDY() {
		return instance.cursorDelta.y;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int index = index(e.getButton());
		prevStates[index] = states[index];
		states[index] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int index = index(e.getButton());
		prevStates[index] = states[index];
		states[index] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scroll = -e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int newX = e.getX();
		int newY = e.getY() - Display.getCharHeight();
		cursorDelta.x = newX - cursorPosition.x;
		cursorDelta.y = newY - cursorPosition.y;
		cursorPosition.x = newX;
		cursorPosition.y = newY;
	}

}
