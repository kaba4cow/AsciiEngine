package kaba4cow.ascii.toolbox.utils;

import java.util.ArrayList;

public final class StringUtils {

	private StringUtils() {

	}

	public static String[] divideString(String string, int maxLength) {
		ArrayList<String> parts = new ArrayList<>();
		for (int i = 0; i < string.length(); i += maxLength)
			parts.add(string.substring(i, Math.min(i + maxLength, string.length())));
		return parts.toArray(new String[0]);
	}

	public static String repeat(String string, int times) {
		String result = "";
		for (int i = 0; i < times; i++)
			result += string;
		return result;
	}

	public static String capitalize(String string) {
		if (string.length() <= 1)
			return string.toUpperCase();
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	public static String percent(float value) {
		return (int) (100f * value) + "%";
	}

	public static String format1(float value) {
		return String.format("%.1f", value);
	}

	public static String format2(float value) {
		return String.format("%.2f", value);
	}

	public static String format(float value, int digits) {
		return String.format("%." + digits + "f", value);
	}

}
