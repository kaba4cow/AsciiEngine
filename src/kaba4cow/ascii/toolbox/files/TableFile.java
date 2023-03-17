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

	public Table addTable(String name) {
		Table table = new Table(name);
		tables.put(name, table);
		return table;
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
					else if (tag.equalsIgnoreCase(ITEM))
						table.addItem(strings);
				}

				line = line.trim();
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

				LinkedList<LinkedList<String>> items = table.getItems();
				for (int i = 0; i < items.size(); i++) {
					writer.append('\t');
					writer.append(ITEM);
					writer.append(ASSIGNMENT);
					LinkedList<String> item = items.get(i);
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

	public class Table {

		private String name;
		private LinkedList<String> columns;
		private LinkedList<LinkedList<String>> items;

		public Table(String name) {
			super();
			this.name = name;
			this.columns = new LinkedList<>();
			this.items = new LinkedList<>();
		}

		public String getName() {
			return name;
		}

		public String getColumn(int index) {
			if (index < 0 || index >= columns.size())
				return null;
			return columns.get(index);
		}

		public String getItemString(int itemIndex, int stringIndex) {
			if (stringIndex < 0 || itemIndex < 0 || itemIndex >= items.size())
				return null;
			LinkedList<String> item = items.get(itemIndex);
			if (stringIndex >= item.size())
				return null;
			return item.get(stringIndex);
		}

		public void setColumn(int index, String column) {
			if (index < 0)
				return;
			else if (index < columns.size())
				columns.set(index, column);
			else
				columns.add(column);
		}

		public void insertColumn(int index, String column) {
			if (index < 0 && index >= columns.size())
				return;
			columns.add(index, column);
			for (int i = 0; i < items.size(); i++)
				insertItemString(i, index, "");
		}

		public void removeColumn(int index) {
			if (index < 0 && index >= columns.size())
				return;
			columns.remove(index);
			for (int i = 0; i < items.size(); i++)
				removeItemString(i, index);
		}

		public void addColumn(String column) {
			columns.add(column);
		}

		public void addItem(String... newItem) {
			LinkedList<String> item = new LinkedList<>();
			for (int i = 0; i < newItem.length; i++)
				item.add(newItem[i]);
			items.add(item);
		}

		public void insertItem(int index, String... newItem) {
			LinkedList<String> item = new LinkedList<>();
			for (int i = 0; i < newItem.length; i++)
				item.add(newItem[i]);
			items.add(index, item);
		}

		public void addItemString(int itemIndex, String string) {
			if (itemIndex < 0 || itemIndex >= items.size())
				return;
			LinkedList<String> item = items.get(itemIndex);
			item.add(string);
		}

		public void removeItemString(int itemIndex, int stringIndex) {
			if (itemIndex < 0 || itemIndex >= items.size())
				return;
			LinkedList<String> item = items.get(itemIndex);
			if (stringIndex < item.size())
				item.remove(stringIndex);
		}

		public void setItemString(int itemIndex, int stringIndex, String string) {
			if (stringIndex < 0 || itemIndex < 0 || itemIndex >= items.size())
				return;
			LinkedList<String> item = items.get(itemIndex);
			if (stringIndex < item.size())
				item.set(stringIndex, string);
			else
				item.add(string);
		}

		public void insertItemString(int itemIndex, int stringIndex, String string) {
			if (stringIndex < 0 || itemIndex < 0 || itemIndex >= items.size())
				return;
			LinkedList<String> item = items.get(itemIndex);
			if (stringIndex >= item.size())
				return;
			item.add(stringIndex, string);
		}

		public int getColumnWidth(int column) {
			if (column < 0 || column >= columns.size())
				return 1;
			int width = 0;
			for (int i = 0; i < items.size(); i++) {
				LinkedList<String> item = items.get(i);
				if (column >= item.size())
					continue;
				int length = item.get(column).length();
				if (length > width)
					width = length;
			}
			return width + 1;
		}

		public LinkedList<String> getColumns() {
			return columns;
		}

		public LinkedList<LinkedList<String>> getItems() {
			return items;
		}

		public void print() {
			System.out.println("TABLE [" + name + "]");
			for (int i = 0; i < columns.size(); i++)
				System.out.print(columns.get(i) + "\t");
			System.out.println();
			for (int j = 0; j < items.size(); j++) {
				LinkedList<String> item = items.get(j);
				int size = item.size();
				if (size > columns.size())
					size = columns.size();
				for (int i = 0; i < size; i++)
					System.out.print(item.get(i) + "\t");
				System.out.println();
			}
			System.out.println();
		}

	}

}
