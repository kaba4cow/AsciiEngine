package kaba4cow.ascii.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import kaba4cow.ascii.toolbox.utils.ProgramUtils;

public class Printer extends PrintStream {

	private static final StringBuilder history = new StringBuilder();

	private static final Printer instance = new Printer();

	private Printer() {
		super(System.out);
		System.setOut(this);
		System.setErr(this);
	}

	public static Printer getInstance() {
		return instance;
	}

	public static String getHistory() {
		return history.toString();
	}

	public static void saveLog() {
		outln("Saving log file");
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

	public static void out(boolean b) {
		history.append(b);
		instance.print(b);
	}

	public static void out(char c) {
		history.append(c);
		instance.print(c);
	}

	public static void out(int i) {
		history.append(i);
		instance.print(i);
	}

	public static void out(long l) {
		history.append(l);
		instance.print(l);
	}

	public static void out(float f) {
		history.append(f);
		instance.print(f);
	}

	public static void out(double d) {
		history.append(d);
		instance.print(d);
	}

	public static void out(char[] s) {
		history.append(s);
		instance.print(s);
	}

	public static void out(String s) {
		history.append(s);
		instance.print(s);
	}

	public static void out(Object obj) {
		history.append(obj);
		instance.print(obj);
	}

	public static void outln() {
		history.append("\n");
		instance.println();
	}

	public static void outln(boolean x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(char x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(int x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(long x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(float x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(double x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(char[] x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(String x) {
		history.append(x).append("\n");
		instance.println(x);
	}

	public static void outln(Object x) {
		history.append(x).append("\n");
		instance.println(x);
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
