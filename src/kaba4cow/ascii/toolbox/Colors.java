package kaba4cow.ascii.toolbox;

import kaba4cow.ascii.toolbox.maths.Maths;

public final class Colors {

	private Colors() {

	}

	public static int create(int r, int g, int b) {
		return ((r & 0xF) << 8) | ((g & 0xF) << 4) | ((b & 0xF) << 0);
	}

	public static int create(int value) {
		return create(value, value, value);
	}

	public static int combine(int background, int foreground) {
		return ((background & 0xFFF) << 12) | ((foreground & 0xFFF) << 0);
	}

	public static int swap(int color) {
		return combine(getForeground(color), getBackground(color));
	}

	public static int blend(int color0, int color1, float blendFactor) {
		return blendBackground(color0, color1, blendFactor) | blendForeground(color0, color1, blendFactor);
	}

	public static int blendBackground(int color0, int color1, float blendFactor) {
		color0 = getBackground(color0);
		color1 = getBackground(color1);

		if (color0 == color1)
			return color0 << 12;

		float r0 = getRed(color0);
		float g0 = getGreen(color0);
		float b0 = getBlue(color0);

		float r1 = getRed(color1);
		float g1 = getGreen(color1);
		float b1 = getBlue(color1);

		int r = (int) Maths.blend(r0, r1, blendFactor);
		int g = (int) Maths.blend(g0, g1, blendFactor);
		int b = (int) Maths.blend(b0, b1, blendFactor);

		return create(r, g, b) << 12;
	}

	public static int blendForeground(int color0, int color1, float blendFactor) {
		color0 = getForeground(color0);
		color1 = getForeground(color1);

		if (color0 == color1)
			return color0;

		float r0 = getRed(color0);
		float g0 = getGreen(color0);
		float b0 = getBlue(color0);

		float r1 = getRed(color1);
		float g1 = getGreen(color1);
		float b1 = getBlue(color1);

		int r = (int) Maths.blend(r0, r1, blendFactor);
		int g = (int) Maths.blend(g0, g1, blendFactor);
		int b = (int) Maths.blend(b0, b1, blendFactor);

		return create(r, g, b);
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
