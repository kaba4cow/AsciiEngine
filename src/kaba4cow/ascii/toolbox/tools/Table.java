package kaba4cow.ascii.toolbox.tools;

import java.util.LinkedList;

import kaba4cow.ascii.toolbox.maths.Maths;

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

	public int getColumn(String column) {
		for (int i = 0; i < columns.size(); i++)
			if (columns.get(i).equals(column))
				return i;
		return -1;
	}

	public int getRow(int column, String string) {
		if (column < 0 || column >= columns.size())
			return -1;
		for (int i = 0; i < items.size(); i++)
			if (items.get(i).get(column).equals(string))
				return i;
		return -1;
	}

	public String getItemString(int itemIndex, int stringIndex) {
		if (stringIndex < 0 || itemIndex < 0 || itemIndex >= items.size())
			return null;
		LinkedList<String> item = items.get(itemIndex);
		if (stringIndex >= item.size())
			return null;
		return item.get(stringIndex);
	}

	public String getItemString(int itemIndex, String column) {
		if (itemIndex < 0 || itemIndex >= items.size())
			return null;
		int j = getColumn(column);
		if (j == -1)
			return null;
		LinkedList<String> item = items.get(itemIndex);
		while (j >= item.size())
			item.add("");
		return item.get(j);
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
		if (index < 0 || index >= columns.size())
			return;
		columns.add(index, column);
		for (int i = 0; i < items.size(); i++)
			insertItemString(i, index, "");
	}

	public void removeColumn(int index) {
		if (index < 0 || index >= columns.size())
			return;
		columns.remove(index);
		if (columns.isEmpty())
			items.clear();
		else
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

	public void removeItem(int itemIndex) {
		if (itemIndex < 0 || itemIndex >= items.size())
			return;
		items.remove(itemIndex);
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
		String columnText = columns.get(column);
		if (columnText == null)
			return 1;
		int width = columnText.length();
		for (int i = 0; i < items.size(); i++) {
			LinkedList<String> item = items.get(i);
			if (column >= item.size())
				continue;
			String itemText = item.get(column);
			if (itemText == null)
				return 1;
			int length = itemText.length();
			if (length > width)
				width = length;
		}
		return Maths.max(1, width);
	}

	public int column(String name) {
		for (int i = 0; i < columns.size(); i++)
			if (columns.get(i).equalsIgnoreCase(name))
				return i;
		return -1;
	}

	public LinkedList<String> getColumns() {
		return columns;
	}

	public LinkedList<LinkedList<String>> getItems() {
		return items;
	}

	public int columns() {
		return columns.size();
	}

	public int rows() {
		return items.size();
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