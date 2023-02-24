package kaba4cow.ascii.drawing;

public final class Colors {

	private Colors() {

	}

	public static int create(int r, int g, int b) {
		return (r << 20) | (g << 12) | (b << 4);
	}

	public static int combine(int background, int foreground) {
		return ((background & 0xFFF) << 12) | ((foreground & 0xFFF) << 0);
	}

	public static int swap(int color) {
		return combine(getForeground(color), getBackground(color));
	}

	public static int getRed(int color) {
		return (color >> 8) & 0xF;
	}

	public static int getGreen(int color) {
		return (color >> 4) & 0xF;
	}

	public static int getBlue(int color) {
		return (color >> 0) & 0xF;
	}

	public static int getBackground(int color) {
		return (color >> 12) & 0xFFF;
	}

	public static int getForeground(int color) {
		return (color >> 0) & 0xFFF;
	}

}
