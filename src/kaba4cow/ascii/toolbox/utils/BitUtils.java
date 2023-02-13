package kaba4cow.ascii.toolbox.utils;

public final class BitUtils {

	private BitUtils() {

	}
	
	// int

	public static boolean isSetBit(int number, int index) {
		return (number & (1 << index)) != 0;
	}

	public static int setBit(int number, int index) {
		return number | (1 << index);
	}

	public static int resetBit(int number, int index) {
		return number & ~(1 << index);
	}

	public static int inverseBit(int number, int index) {
		return number ^ (1 << index);
	}

	// long

	public static boolean isSetBit(long number, int index) {
		return (number & (1 << index)) != 0;
	}

	public static long setBit(long number, int index) {
		return number | (1 << index);
	}

	public static long resetBit(long number, int index) {
		return number & ~(1 << index);
	}

	public static long inverseBit(long number, int index) {
		return number ^ (1 << index);
	}

	// byte

	public static boolean isSetBit(byte number, int index) {
		return (number & (1 << index)) != 0;
	}

	public static byte setBit(byte number, int index) {
		return (byte) (number | (1 << index));
	}

	public static byte resetBit(byte number, int index) {
		return (byte) (number & ~(1 << index));
	}

	public static byte inverseBit(byte number, int index) {
		return (byte) (number ^ (1 << index));
	}

	// short

	public static boolean isSetBit(short number, int index) {
		return (number & (1 << index)) != 0;
	}

	public static short setBit(short number, int index) {
		return (short) (number | (1 << index));
	}

	public static short resetBit(short number, int index) {
		return (short) (number & ~(1 << index));
	}

	public static short inverseBit(short number, int index) {
		return (short) (number ^ (1 << index));
	}

}
