package kaba4cow.ascii.toolbox.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.Frame;
import kaba4cow.ascii.toolbox.Printer;

public class ImageFile {

	private int width;
	private int height;

	private char[] chars;
	private int[] colors;

	public ImageFile(int width, int height) {
		this.width = width;
		this.height = height;
		this.chars = new char[width * height];
		this.colors = new int[width * height];
	}

	public static ImageFile read(String fileName) {
		return read(new File(fileName));
	}

	public static ImageFile read(File file) {
		Printer.println("Loading image file: " + file.getAbsolutePath());
		ImageFile imageFile = new ImageFile(1, 1);

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			int width = -1;
			int height = -1;
			boolean fillChars = true;
			char[] chars = null;
			int[] colors = null;

			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (width == -1 && height == -1) {
					String[] size = line.split(":");
					width = Integer.parseInt(size[0]);
					height = Integer.parseInt(size[1]);
					chars = new char[width * height];
					colors = new int[width * height];
				} else if (fillChars) {
					String[] values = line.split(" ");
					int index = 0;
					for (int i = 0; i < values.length; i++) {
						String[] info = values[i].split(":");
						int repeats = Integer.parseInt(info[0]);
						int value = Integer.parseInt(info[1]);
						for (int j = 0; j < repeats; j++)
							chars[index++] = (char) value;
					}
					fillChars = false;
				} else {
					String[] values = line.split(" ");
					int index = 0;
					for (int i = 0; i < values.length; i++) {
						String[] info = values[i].split(":");
						int repeats = Integer.parseInt(info[0]);
						int value = Integer.parseInt(info[1]);
						for (int j = 0; j < repeats; j++)
							colors[index++] = value;
					}
				}
			}

			if (chars != null) {
				imageFile.width = width;
				imageFile.height = height;
				imageFile.chars = chars;
				imageFile.colors = colors;
			}

			reader.close();
		} catch (Exception e) {
			Engine.terminate(e);
		}

		return imageFile;
	}

	public static boolean write(ImageFile imageFile, String fileName) {
		return write(imageFile, new File(fileName));
	}

	public static boolean write(ImageFile imageFile, File file) {
		Printer.println("Saving image file: " + file.getAbsolutePath());

		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

			writer.append(Integer.toString(imageFile.width));
			writer.append(':');
			writer.append(Integer.toString(imageFile.height));
			writer.append('\n');

			char[] chars = imageFile.chars;
			int[] colors = imageFile.colors;

			int prev, repeats, length = chars.length;

			prev = chars[0];
			repeats = 1;
			for (int i = 1; i <= length; i++) {
				if (i == length || prev != chars[i]) {
					writer.append(Integer.toString(repeats));
					writer.append(':');
					writer.append(Integer.toString(prev));
					writer.append(' ');

					if (i == length)
						break;

					prev = chars[i];
					repeats = 1;
				} else
					repeats++;
			}
			writer.append('\n');

			prev = colors[0];
			repeats = 1;
			for (int i = 1; i <= length; i++) {
				if (i == length || prev != colors[i]) {
					writer.append(Integer.toString(repeats));
					writer.append(':');
					writer.append(Integer.toString(prev));
					writer.append(' ');

					if (i == length)
						break;
					prev = colors[i];
					repeats = 1;
				} else
					repeats++;
			}

			writer.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public ImageFile copy() {
		ImageFile copy = new ImageFile(width, height);
		for (int i = 0; i < chars.length; i++) {
			copy.chars[i] = chars[i];
			copy.colors[i] = colors[i];
		}
		return copy;
	}

	public Frame toFrame() {
		Frame frame = new Frame(width, height);
		int i, length = chars.length;
		for (i = 0; i < length; i++) {
			frame.chars[i] = chars[i];
			frame.colors[i] = colors[i];
		}
		return frame;
	}

	public void fill(char c, int color) {
		int i, length = chars.length;
		for (i = 0; i < length; i++) {
			chars[i] = c;
			colors[i] = color;
		}
	}

	public void set(int x, int y, char c, int color) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		int i = y * width + x;
		chars[i] = c;
		colors[i] = color;
	}

	public void setChar(int x, int y, char c) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		chars[y * width + x] = c;
	}

	public void setColor(int x, int y, int color) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		colors[y * width + x] = color;
	}

	public int floodFill(int startX, int startY, int prevColor, int newColor) {
		if (startX < 0 || startX >= width || startY < 0 || startY >= height
				|| colors[startY * width + startX] != prevColor)
			return 0;
		boolean[] visited = new boolean[width * height];
		int area = 0;
		int x, y, i;
		int[] currentPosition;
		Stack<int[]> stack = new Stack<>();
		stack.push(new int[] { startX, startY });
		while (!stack.empty()) {
			currentPosition = stack.pop();
			x = currentPosition[0];
			y = currentPosition[1];
			i = y * width + x;
			if (x < 0 || x >= width || y < 0 || y >= height || visited[i])
				continue;
			if (colors[i] == prevColor) {
				colors[i] = newColor;
				area++;
				stack.push(new int[] { x - 1, y });
				stack.push(new int[] { x + 1, y });
				stack.push(new int[] { x, y - 1 });
				stack.push(new int[] { x, y + 1 });
			}
			visited[i] = true;
		}
		return area;
	}

	public int floodFill(int startX, int startY, char prevChar, char newChar) {
		if (startX < 0 || startX >= width || startY < 0 || startY >= height
				|| chars[startY * width + startX] != prevChar)
			return 0;
		boolean[] visited = new boolean[width * height];
		int area = 0;
		int x, y, i;
		int[] currentPosition;
		Stack<int[]> stack = new Stack<>();
		stack.push(new int[] { startX, startY });
		while (!stack.empty()) {
			currentPosition = stack.pop();
			x = currentPosition[0];
			y = currentPosition[1];
			i = y * width + x;
			if (x < 0 || x >= width || y < 0 || y >= height || visited[i])
				continue;
			if (chars[i] == prevChar) {
				chars[i] = newChar;
				area++;
				stack.push(new int[] { x - 1, y });
				stack.push(new int[] { x + 1, y });
				stack.push(new int[] { x, y - 1 });
				stack.push(new int[] { x, y + 1 });
			}
			visited[i] = true;
		}
		return area;
	}

	public void setSize(int newWidth, int newHeight) {
		final char[] newChars = new char[newWidth * newHeight];
		final int[] newColors = new int[newWidth * newHeight];

		int x, y, i, j;
		for (x = 0; x < newWidth; x++)
			for (y = 0; y < newHeight; y++) {
				i = y * newWidth + x;
				if (x >= width || y >= height) {
					newChars[i] = 0;
					newColors[i] = 0;
					continue;
				}
				j = y * width + x;
				newChars[i] = chars[j];
				newColors[i] = colors[j];
			}

		width = newWidth;
		height = newHeight;
		chars = newChars;
		colors = newColors;
	}

	public char getChar(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0;
		return chars[y * width + x];
	}

	public int getColor(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0;
		return colors[y * width + x];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(chars);
		result = prime * result + Arrays.hashCode(colors);
		result = prime * result + Objects.hash(height, width);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageFile other = (ImageFile) obj;
		return Arrays.equals(chars, other.chars) && Arrays.equals(colors, other.colors) && height == other.height
				&& width == other.width;
	}

}
