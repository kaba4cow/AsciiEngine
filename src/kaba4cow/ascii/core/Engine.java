package kaba4cow.ascii.core;

import kaba4cow.ascii.Errors;
import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.Printer;

public final class Engine {

	private static int FRAMERATE;
	private static float FRAMETIME;

	private static int CURRENT_FRAMERATE;
	private static float ELAPSED_TIME;

	private static boolean SAVE_LOG_ON_EXIT;

	private static boolean CLOSE_REQUESTED;

	private static MainProgram PROGRAM;

	private Engine() {

	}

	public static void init(String title, int framerate, int width, int height, boolean square) {
		Printer.outln("Initializing engine");

		FRAMERATE = framerate;
		FRAMETIME = 1f / (float) FRAMERATE;

		CURRENT_FRAMERATE = FRAMERATE;
		ELAPSED_TIME = 0f;

		CLOSE_REQUESTED = false;

		Display.init(title, width, height, square);
	}

	public static void start(MainProgram program) {
		Printer.outln("Starting engine");

		PROGRAM = program;

		final long nanoseconds = 1000000000l;
		final double invNanoseconds = 1d / nanoseconds;

		int frames = 0;
		long frameCounter = 0l;
		long lastTime = System.nanoTime();
		double unprocessedTime = 0d;

		boolean render;
		long startTime;
		long passedTime;

		PROGRAM.init();

		while (true) {
			render = false;
			startTime = System.nanoTime();
			passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime * invNanoseconds;
			frameCounter += passedTime;

			while (unprocessedTime > FRAMETIME) {
				render = true;
				unprocessedTime -= FRAMETIME;

				PROGRAM.update(FRAMETIME);
				Keyboard.update();
				Mouse.update();
				ELAPSED_TIME += FRAMETIME;

				if (frameCounter >= nanoseconds) {
					CURRENT_FRAMERATE = frames;
					frames = 0;
					frameCounter = 0;
				}
			}

			if (render) {
				PROGRAM.render();
				Display.update();
				frames++;
			} else
				sleep(1l);

			if (isCloseRequested())
				break;
		}

		Printer.outln("Destroying engine");
		Display.destroy();
		PROGRAM.onClose();
		if (SAVE_LOG_ON_EXIT)
			Printer.saveLog();
	}

	public static void setFramerate(int framerate) {
		FRAMERATE = framerate;
		FRAMETIME = 1f / (float) FRAMERATE;
	}

	public static int getCurrentFramerate() {
		return CURRENT_FRAMERATE;
	}

	public static float getElapsedTime() {
		return ELAPSED_TIME;
	}

	public static MainProgram getProgram() {
		return PROGRAM;
	}

	public static void saveLogOnExit(boolean save) {
		SAVE_LOG_ON_EXIT = save;
	}

	public static void requestClose() {
		CLOSE_REQUESTED = true;
	}

	public static boolean isCloseRequested() {
		return CLOSE_REQUESTED;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void terminate(Errors error, Exception exception) {
		if (error == null)
			error = Errors.UNKNOWN;
		if (exception == null)
			exception = new Exception();
		exception.printStackTrace();
		Printer.outln("Program terminated");
		Printer.saveLog();
		System.exit(error.getCode());
	}

}
