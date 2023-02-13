package kaba4cow.ascii.toolbox.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class ProgramUtils {

	private ProgramUtils() {

	}

	public static String getDate() {
		return getDate("MM-dd-YY_HH-mm-ss");
	}

	public static String getDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		return dateFormat.format(Calendar.getInstance().getTime());
	}

}
