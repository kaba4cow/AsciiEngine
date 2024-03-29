package kaba4cow.ascii.toolbox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public final class StringUtils {

	private StringUtils() {

	}

	public static byte[] encode(String string) {
		final int stringLength = string.length();

		ArrayList<Character> characters = new ArrayList<>();
		for (int i = 0; i < stringLength; i++) {
			char c = string.charAt(i);
			System.out.println(c + ":" + (int) c + ":" + Integer.toHexString(c));
			if (!characters.contains(c))
				characters.add(c);
		}
		Collections.shuffle(characters);

		final int charactersLength = characters.size();

		int[] indices = new int[stringLength];
		for (int i = 0; i < stringLength; i++) {
			char c = string.charAt(i);
			int index = 0;
			for (int j = 0; j < charactersLength; j++)
				if (c == characters.get(j)) {
					index = j;
					break;
				}
			indices[i] = index;
		}

		byte[] bytes = new byte[1 + charactersLength + stringLength];
		bytes[0] = (byte) charactersLength;

		int arrayIndex = 1;
		for (int i = 0; i < charactersLength; i++)
			bytes[arrayIndex++] = (byte) (char) characters.get(i);

		for (int i = 0; i < stringLength; i++)
			bytes[arrayIndex++] = (byte) indices[i];

		return bytes;
	}

	public static String decode(byte[] bytes) {
		int byteIndex = 0;

		ArrayList<Character> characters = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();

		int charactersLength = bytes[byteIndex++];
		for (int i = 0; i < charactersLength; i++)
			characters.add((char) bytes[byteIndex++]);

		int stringLength = bytes.length - byteIndex;
		for (int i = 0; i < stringLength; i++)
			indices.add((int) bytes[byteIndex++]);

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < indices.size(); i++) {
			char c = characters.get(indices.get(i));
			builder.append(c);
		}

		return builder.toString();
	}

	public static void encode(String string, File file) throws IOException {
		FileOutputStream output = new FileOutputStream(file);
		byte[] bytes = StringUtils.encode(string);
		output.write(bytes);
		output.close();
	}

	public static String decode(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		input.read(bytes);
		input.close();
		return decode(bytes);
	}

	public static String[] divideString(String string, int maxLength) {
		ArrayList<String> parts = new ArrayList<>();
		for (int i = 0; i < string.length(); i += maxLength)
			parts.add(string.substring(i, Math.min(i + maxLength, string.length())));
		return parts.toArray(new String[0]);
	}

	public static String repeat(String string, int amount) {
		if (amount == 1)
			return string;
		if (amount == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < amount; i++)
			builder.append(string);
		return builder.toString();
	}

	public static String repeat(char c, int amount) {
		if (amount == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < amount; i++)
			builder.append(c);
		return builder.toString();
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
		return String.format("%.1f", value).replace(',', '.');
	}

	public static String format2(float value) {
		return String.format("%.2f", value).replace(',', '.');
	}

	public static String format(float value, int digits) {
		return String.format("%." + digits + "f", value).replace(',', '.');
	}

}
