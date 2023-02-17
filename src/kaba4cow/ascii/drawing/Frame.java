package kaba4cow.ascii.drawing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import kaba4cow.ascii.drawing.glyphs.Glyphs;

public class Frame {

	public final int width;
	public final int height;
	public final int length;

	public final char[] chars;
	public final int[] colors;

	public Frame(int width, int height) {
		this.width = width;
		this.height = height;
		this.length = width * height;
		this.chars = new char[length];
		this.colors = new int[length];
		fill(Glyphs.SPACE, 0x000000);
	}

	public static Frame read(File file) {
		Frame frame = null;
		try {
			int index = 0;

			FileInputStream in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			while (true) {
				line = line.trim();

				if (line.contains("x")) {
					String[] dimensions = line.split("x");
					int frameWidth = Integer.parseInt(dimensions[0]);
					int frameHeight = Integer.parseInt(dimensions[1]);
					frame = new Frame(frameWidth, frameHeight);
				} else {
					int length = line.length();
					boolean reading = false;
					int charCode = 0, colorCode = 0;
					String token = "";
					for (int i = 0; i < length; i++) {
						if (line.charAt(i) == '[') {
							reading = true;
						} else if (line.charAt(i) == ']') {
							colorCode = Integer.parseInt(token);
							token = "";

							reading = false;
							frame.chars[index] = (char) charCode;
							frame.colors[index] = colorCode;
							index++;
						} else if (reading) {
							if (line.charAt(i) == ' ') {
								charCode = Integer.parseInt(token);
								token = "";
							} else
								token += Character.toString(line.charAt(i));
						}
					}
				}

				line = reader.readLine();
				if (line == null)
					break;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return frame;
	}

	public static void write(Frame frame, File file) {
		char[] chars = frame.chars;
		int[] colors = frame.colors;

		String content = frame.width + "x" + frame.height;
		for (int i = 0; i < chars.length; i++) {
			if (i % frame.width == 0)
				content += "\n";

			content += "[";
			content += (int) chars[i];
			content += " ";
			content += colors[i];
			content += "] ";
		}

		try {
			PrintWriter print;
			print = new PrintWriter(file);
			print.append(content);
			print.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void fill(char c, int color) {
		for (int i = 0; i < length; i++) {
			chars[i] = c;
			colors[i] = color;
		}
	}

	public char getChar(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Glyphs.SPACE;
		return chars[y * width + x];
	}

	public int getColor(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0x000000;
		return colors[y * width + x];
	}

}
