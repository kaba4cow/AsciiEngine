package kaba4cow.ascii.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import kaba4cow.ascii.toolbox.utils.ProgramUtils;

public final class Printer {

	private static final StringBuilder history = new StringBuilder();

	private static final PrinterStream instance = new PrinterStream();

	private Printer() {

	}

	public static String getHistory() {
		return history.toString();
	}

	public static void saveLog() {
		println("Saving log file");
		File directory = new File("logs");
		if (!directory.exists())
			directory.mkdirs();

		File file = new File("logs/log_" + ProgramUtils.getDate() + ".txt");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				return;
			}

		try {
			PrintWriter print = new PrintWriter(file);
			String[] history = getHistory().split("\n");
			for (int i = 0; i < history.length; i++)
				print.println(history[i]);
			print.close();
		} catch (FileNotFoundException e) {
			return;
		}
	}

	public static void print(boolean b) {
		history.append(b);
		instance.print(b);
	}

	public static void print(char c) {
		history.append(c);
		instance.print(c);
	}

	public static void print(int i) {
		history.append(i);
		instance.print(i);
	}

	public static void print(long l) {
		history.append(l);
		instance.print(l);
	}

	public static void print(float f) {
		history.append(f);
		instance.print(f);
	}

	public static void print(double d) {
		history.append(d);
		instance.print(d);
	}

	public static void print(char[] s) {
		history.append(s);
		instance.print(s);
	}

	public static void print(String s) {
		history.append(s);
		instance.print(s);
	}

	public static void print(Object obj) {
		history.append(obj);
		instance.print(obj);
	}

	public static void println() {
		history.append("\n");
		instance.println();
	}

	public static void println(boolean x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(char x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(int x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(long x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(float x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(double x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(char[] x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(String x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void println(Object x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	private static class PrinterStream extends PrintStream {

		private PrinterStream() {
			super(System.out);
			System.setOut(this);
			System.setErr(this);
		}

		@Override
		public void print(boolean b) {
			super.print(b);
		}

		@Override
		public void print(char c) {
			super.print(c);
		}

		@Override
		public void print(int i) {
			super.print(i);
		}

		@Override
		public void print(long l) {
			super.print(l);
		}

		@Override
		public void print(float f) {
			super.print(f);
		}

		@Override
		public void print(double d) {
			super.print(d);
		}

		@Override
		public void print(char[] s) {
			super.print(s);
		}

		@Override
		public void print(String s) {
			super.print(s);
		}

		@Override
		public void print(Object obj) {
			super.print(obj);
		}

		@Override
		public void println() {
			super.println();
		}

		@Override
		public void println(boolean x) {
			super.println(x);
		}

		@Override
		public void println(char x) {
			super.println(x);
		}

		@Override
		public void println(int x) {
			super.println(x);
		}

		@Override
		public void println(long x) {
			super.println(x);
		}

		@Override
		public void println(float x) {
			super.println(x);
		}

		@Override
		public void println(double x) {
			super.println(x);
		}

		@Override
		public void println(char[] x) {
			super.println(x);
		}

		@Override
		public void println(String x) {
			super.println(x);
		}

		@Override
		public void println(Object x) {
			super.println(x);
		}

	}

}
