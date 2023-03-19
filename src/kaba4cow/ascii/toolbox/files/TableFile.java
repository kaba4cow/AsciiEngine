package kaba4cow.ascii.toolbox.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;

import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.toolbox.Printer;
import kaba4cow.ascii.toolbox.tools.Table;

public class TableFile {

	private static HashMap<String, TableFile> files = new HashMap<>();

	private static final String TABLE = "TABLE";
	private static final String COLUMN = "COLUMN";
	private static final String ITEM = "ITEM";
	private static final String COMMENT = "//";
	private static final String ASSIGNMENT = " :: ";

	private HashMap<String, Table> tables;

	public TableFile() {
		tables = new HashMap<>();
	}

	public static TableFile get(String fileName) {
		if (!files.containsKey(fileName.toLowerCase())) {
			TableFile file = read(fileName);
			files.put(fileName.toLowerCase(), file);
		}
		return files.get(fileName.toLowerCase());
	}

	public HashMap<String, Table> getTables() {
		return tables;
	}

	public Table getTable(String name) {
		if (containsTable(name))
			return tables.get(name);
		else
			return addTable(name);
	}

	public Table addTable(String name) {
		Table table = new Table(name);
		tables.put(name, table);
		return table;
	}

	public boolean renameTable(String oldName, String newName) {
		if (!containsTable(oldName) || containsTable(newName))
			return false;
		Table table = tables.get(oldName);
		table.setName(newName);
		tables.remove(oldName);
		tables.put(newName, table);
		return true;
	}

	public boolean containsTable(String name) {
		return tables.containsKey(name);
	}

	public void print() {
		for (String name : tables.keySet())
			tables.get(name).print();
	}

	public static TableFile read(String fileName) {
		return read(new File(fileName));
	}

	public static TableFile read(File file) {
		Printer.println("Loading table file: " + file.getAbsolutePath());

		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			TableFile tableFile = new TableFile();

			String line;
			String[] strings;
			String tag;

			Table table = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith(COMMENT))
					continue;

				strings = line.split(ASSIGNMENT);
				tag = strings[0];
				strings = readStrings(strings[1]);

				if (tag.equalsIgnoreCase(TABLE)) {
					table = tableFile.addTable(strings[0]);
				} else if (table != null) {
					if (tag.equalsIgnoreCase(COLUMN))
						table.addColumn(strings[0]);
					else if (tag.equalsIgnoreCase(ITEM)) {
						table.addRow();
						int lastRow = table.rows() - 1;
						for (int row = 0; row < strings.length; row++)
							table.setCell(row, lastRow, strings[row]);
					}
				}
			}
			reader.close();

			return tableFile;
		} catch (Exception e) {
			Engine.terminate(e);
			return null;
		}
	}

	public static boolean write(TableFile tableFile, String fileName) {
		return write(tableFile, new File(fileName));
	}

	public static boolean write(TableFile tableFile, File file) {
		Printer.println("Saving table file: " + file.getAbsolutePath());

		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

			HashMap<String, Table> tables = tableFile.getTables();
			for (String key : tables.keySet()) {
				Table table = tables.get(key);
				writer.append(TABLE);
				writer.append(ASSIGNMENT);
				writer.append("\'");
				writer.append(table.getName());
				writer.append("\'\n\n");

				LinkedList<String> columns = table.getColumns();
				for (int i = 0; i < columns.size(); i++) {
					writer.append('\t');
					writer.append(COLUMN);
					writer.append(ASSIGNMENT);
					writer.append('\'');
					writer.append(writeString(columns.get(i)));
					writer.append("\'\n");
				}
				writer.append('\n');

				LinkedList<Table.Row> items = table.getRows();
				for (int i = 0; i < items.size(); i++) {
					writer.append('\t');
					writer.append(ITEM);
					writer.append(ASSIGNMENT);
					Table.Row item = items.get(i);
					for (int j = 0; j < item.size(); j++) {
						writer.append('\'');
						writer.append(writeString(item.get(j)));
						writer.append('\'');
						if (j < item.size() - 1)
							writer.append(' ');
					}
					writer.append('\n');
				}
				writer.append('\n');
			}

			writer.close();
		} catch (Exception e) {
			Engine.terminate(e);
			return false;
		}
		return true;
	}

	private static String writeString(String string) {
		int i, length = string.length();
		char c;
		StringBuilder builder = new StringBuilder();
		for (i = 0; i < length; i++) {
			c = string.charAt(i);
			if (c == '\\' || c == '\'')
				builder.append('\\');
			builder.append(c);
		}
		return builder.toString();
	}

	private static String[] readStrings(String string) {
		boolean quotes = false;
		boolean backslash = false;
		int i, length = string.length();
		char c;
		StringBuilder builder = new StringBuilder();
		for (i = 0; i < length; i++) {
			c = string.charAt(i);

			if (c == '\'') {
				if (backslash) {
					builder.append(c);
					backslash = false;
				} else
					quotes = !quotes;
			} else if (c == '\\') {
				if (backslash) {
					builder.append(c);
					backslash = false;
				} else
					backslash = true;
			} else if (quotes) {
				builder.append(c);
				backslash = false;
			} else if (c == ' ')
				builder.append('\n');
		}

		return builder.toString().split("\n");
	}

}
