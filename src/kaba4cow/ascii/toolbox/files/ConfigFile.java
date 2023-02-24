package kaba4cow.ascii.toolbox.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.toolbox.Printer;

public class ConfigFile {

	private static final String ASSIGNMENT = " : ";
	private static final char NEWLINE = '\n';

	private final LinkedHashMap<String, String> content;

	public ConfigFile() {
		this.content = new LinkedHashMap<>();
	}

	public static ConfigFile read(File file) {
		Printer.outln("Loading config file: " + file.getAbsolutePath());
		ConfigFile configFile = new ConfigFile();

		try {
			FileInputStream in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();
			String[] properties;
			while (true) {
				line = line.trim();

				if (!line.isEmpty() && line.contains(ASSIGNMENT)) {
					properties = line.split(ASSIGNMENT);
					configFile.setString(properties[0], properties[1]);
				}

				line = reader.readLine();
				if (line == null)
					break;
			}
			reader.close();
		} catch (IOException e) {
			Engine.terminate(e);
		}

		return configFile;
	}

	public static boolean write(ConfigFile configFile, File file) {
		Printer.outln("Saving config file: " + file.getAbsolutePath());

		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				Engine.terminate(e);
				return false;
			}

		try {
			PrintWriter print = new PrintWriter(file);
			String string = configFile.toString();
			print.append(string);
			print.close();
		} catch (FileNotFoundException e) {
			Engine.terminate(e);
			return false;
		}
		return true;
	}

	private boolean isNull(String string) {
		return string == null || string.isEmpty() || string.equalsIgnoreCase("null");
	}

	private boolean isKeyNull(String key) {
		return isNull(content.get(key));
	}

	public String getString(String key) {
		return content.get(key);
	}

	public ConfigFile setString(String key, String value) {
		if (content.containsKey(key))
			content.replace(key, value);
		else
			content.put(key, value);
		return this;
	}

	public float getFloat(String key) {
		if (isKeyNull(key))
			return 0f;
		return Float.parseFloat(content.get(key));
	}

	public ConfigFile setFloat(String key, float value) {
		if (content.containsKey(key))
			content.replace(key, Float.toString(value));
		else
			content.put(key, Float.toString(value));
		return this;
	}

	public int getInt(String key) {
		if (isKeyNull(key))
			return 0;
		return Integer.parseInt(content.get(key));
	}

	public ConfigFile setInt(String key, int value) {
		if (content.containsKey(key))
			content.replace(key, Integer.toString(value));
		else
			content.put(key, Integer.toString(value));
		return this;
	}

	public boolean getBoolean(String key) {
		if (isKeyNull(key))
			return false;
		return Boolean.parseBoolean(content.get(key));
	}

	public ConfigFile setBoolean(String key, boolean value) {
		if (content.containsKey(key))
			content.replace(key, Boolean.toString(value));
		else
			content.put(key, Boolean.toString(value));
		return this;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (String key : content.keySet()) {
			string.append(key);
			string.append(ASSIGNMENT);
			string.append(content.get(key));
			string.append(NEWLINE);
		}
		return string.toString();
	}

}
