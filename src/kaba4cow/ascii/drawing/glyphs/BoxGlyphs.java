package kaba4cow.ascii.drawing.glyphs;

public enum BoxGlyphs {

	// SINGLE

	SINGLE_UP_DOWN(179, 0b1100_0000), //
	SINGLE_UP_DOWN_AND_LEFT(180, 0b1110_0000), //
	SINGLE_DOWN_AND_LEFT(191, 0b0110_0000), //
	SINGLE_UP_AND_RIGHT(192, 0b1001_0000), //
	SINGLE_UP_AND_LEFT_RIGHT(193, 0b1011_0000), //
	SINGLE_DOWN_AND_LEFT_RIGHT(194, 0b0111_0000), //
	SINGLE_UP_DOWN_AND_RIGHT(195, 0b1101_0000), //
	SINGLE_LEFT_RIGHT(196, 0b0011_0000), //
	SINGLE_UP_DOWN_AND_LEFT_RIGHT(197, 0b1111_0000), //
	SINGLE_UP_AND_LEFT(217, 0b1010_0000), //
	SINGLE_DOWN_AND_RIGHT(218, 0b0101_0000), //

	// DOUBLE

	DOUBLE_UP_DOWN_AND_LEFT(185, 0b0000_1110), //
	DOUBLE_UP_DOWN(186, 0b0000_1100), //
	DOUBLE_DOWN_AND_LEFT(187, 0b0000_0110), //
	DOUBLE_UP_AND_LEFT(188, 0b0000_1010), //
	DOUBLE_UP_AND_RIGHT(200, 0b0000_1001), //
	DOUBLE_DOWN_AND_RIGHT(201, 0b0000_0101), //
	DOUBLE_UP_AND_LEFT_RIGHT(202, 0b0000_1011), //
	DOUBLE_DOWN_AND_LEFT_RIGHT(203, 0b0000_0111), //
	DOUBLE_UP_DOWN_AND_RIGHT(204, 0b0000_1101), //
	DOUBLE_LEFT_RIGHT(205, 0b0000_0011), //
	DOUBLE_UP_DOWN_AND_LEFT_RIGHT(206, 0b0000_1111), //

	// SINGLE DOUBLE

	SINGLE_UP_DOWN_AND_DOUBLE_LEFT(181, 0b1100_0010), //
	DOUBLE_UP_DOWN_AND_SINGLE_LEFT(182, 0b0010_1100), //
	DOUBLE_DOWN_AND_SINGLE_LEFT(183, 0b0010_0100), //
	SINGLE_DOWN_AND_DOUBLE_LEFT(184, 0b0100_0010), //
	DOUBLE_UP_AND_SINGLE_LEFT(189, 0b0010_1000), //
	SINGLE_UP_AND_DOUBLE_LEFT(190, 0b1000_0010), //
	SINGLE_UP_DOWN_AND_RIGHT_DOUBLE(198, 0b1100_0001), //
	DOUBLE_UP_DOWN_AND_RIGHT_SINGLE(199, 0b0001_1100), //
	SINGLE_UP_AND_LEFT_RIGHT_DOUBLE(207, 0b1000_0011), //
	DOUBLE_UP_AND_LEFT_RIGHT_SINGLE(208, 0b0011_1000), //
	SINGLE_DOWN_AND_LEFT_RIGHT_DOUBLE(209, 0b0100_0011), //
	DOUBLE_DOWN_AND_LEFT_RIGHT_SINGLE(210, 0b0011_0100), //
	DOUBLE_UP_AND_RIGHT_SINGLE(211, 0b0001_1000), //
	SINGLE_UP_AND_RIGHT_DOUBLE(212, 0b1000_0001), //
	SINGLE_DOWN_AND_RIGHT_DOUBLE(213, 0b0100_0001), //
	DOUBLE_DOWN_AND_RIGHT_SINGLE(214, 0b0001_0100), //
	DOUBLE_UP_DOWN_AND_LEFT_RIGHT_SINGLE(215, 0b0011_1100), //
	SINGLE_UP_DOWN_AND_LEFT_RIGHT_DOUBLE(216, 0b1100_0011); //

	private final char c;
	private final int directions;

	private BoxGlyphs(int code, int directions) {
		this.c = (char) code;
		this.directions = directions;
	}

	public static boolean isBoxGlyph(char c) {
		return c >= 179 && c <= 218;
	}

	public static char getIntersection(char c1, char c2) {
		int directions1 = get(c1).directions;
		int directions2 = get(c2).directions;
		int directions = directions1 | directions2;

		BoxGlyphs intersection = get(directions);
		if (intersection == null) {
			directions1 = (directions1 << 4) | directions1;
			directions2 = (directions2 << 4) | directions2;

			directions = directions1 & 0b1111_0000 | directions2 & 0b1111_0000;

			intersection = get(directions);
		}

		return intersection.c;
	}

	private static BoxGlyphs get(char c) {
		if (!isBoxGlyph(c))
			return null;
		BoxGlyphs[] values = values();
		for (int i = 0; i < values.length; i++)
			if (values[i].c == c)
				return values[i];
		return null;
	}

	private static BoxGlyphs get(int directions) {
		BoxGlyphs[] values = values();
		for (int i = 0; i < values.length; i++)
			if (values[i].directions == directions)
				return values[i];
		return null;
	}

}
