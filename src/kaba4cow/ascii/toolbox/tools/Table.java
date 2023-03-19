package kaba4cow.ascii.toolbox.tools;

import java.util.LinkedList;

public class Table {

	private String name;

	private LinkedList<String> columns;
	private LinkedList<Row> rows;

	public Table(String name) {
		super();
		this.name = name;
		this.columns = new LinkedList<>();
		this.rows = new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		for (int i = 0; i < rows.size(); i++)
			if (rows.get(i).get(column).equals(string))
				return i;
		return -1;
	}

	public String getCell(int column, int row) {
		if (column < 0 || row < 0 || row >= rows.size())
			return null;
		Row cell = rows.get(row);
		if (column >= cell.size())
			return null;
		return cell.get(column);
	}

	public String getCell(String column, int row) {
		if (row < 0 || row >= rows.size())
			return null;
		int j = getColumn(column);
		if (j == -1)
			return null;
		Row cell = rows.get(row);
		while (j >= cell.size())
			cell.add("");
		return cell.get(j);
	}

	public void setColumn(int column, String string) {
		if (column < 0)
			return;
		else if (column < columns.size())
			columns.set(column, string);
		else
			columns.add(string);
	}

	public void insertColumn(int column, String string) {
		if (column < 0 || column >= columns.size())
			return;
		columns.add(column, string);
		for (int row = 0; row < rows.size(); row++)
			insertCell(column, row, "");
	}

	public void removeColumn(int column) {
		if (column < 0 || column >= columns.size())
			return;
		columns.remove(column);
		if (columns.isEmpty())
			rows.clear();
		else
			for (int row = 0; row < rows.size(); row++)
				removeCell(column, row);
	}

	public void addColumn(String string) {
		columns.add(string);
	}

	public void addRow() {
		rows.add(new Row(columns.size()));
	}

	public void insertRow(int row) {
		if (row < 0 || row >= rows.size())
			return;
		rows.add(row, new Row(columns.size()));
	}

	public void addCell(int row, String string) {
		if (row < 0 || row >= rows.size())
			return;
		rows.get(row).add(string);
	}

	public void removeRow(int row) {
		if (row < 0 || row >= rows.size())
			return;
		rows.remove(row);
	}

	public String removeCell(int column, int row) {
		if (row < 0 || row >= rows.size())
			return "";
		return rows.get(row).remove(column);
	}

	public void setCell(int column, int row, String string) {
		if (column < 0 || row < 0 || row >= rows.size())
			return;
		rows.get(row).set(column, string);
	}

	public void insertCell(int column, int row, String string) {
		if (column < 0 || row < 0 || row >= rows.size())
			return;
		rows.get(row).insert(column, string);
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

	public LinkedList<Row> getRows() {
		return rows;
	}

	public int columns() {
		return columns.size();
	}

	public int rows() {
		return rows.size();
	}

	public int getColumnWidth(int column) {
		if (column < 0 || column >= columns.size())
			return 1;
		int width = 1;
		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			while (column >= row.size())
				row.add("");
			String cellText = row.get(column);
			int length = cellText.length();
			if (length > width)
				width = length;
		}
		return width;
	}

	public void print() {
		System.out.println("TABLE [" + name + "]");
		for (int i = 0; i < columns.size(); i++)
			System.out.print(columns.get(i) + "\t");
		System.out.println();
		for (int j = 0; j < rows.size(); j++) {
			Row row = rows.get(j);
			int size = row.size();
			if (size > columns.size())
				size = columns.size();
			for (int i = 0; i < size; i++)
				System.out.print(row.get(i) + "\t");
			System.out.println();
		}
		System.out.println();
	}

	public static class Row {

		private LinkedList<String> cells;

		public Row(int columns) {
			cells = new LinkedList<>();
			for (int i = 0; i < columns; i++)
				cells.add("");
		}

		public String get(int column) {
			if (column < 0 || column >= cells.size())
				return "";
			return cells.get(column);
		}

		public void add(String cell) {
			cells.add(cell);
		}

		public void insert(int column, String string) {
			if (column < 0 || column >= cells.size())
				return;
			cells.add(column, string);
		}

		public void set(int column, String string) {
			if (column < cells.size())
				cells.set(column, string);
			else
				cells.add(string);
		}

		public String remove(int column) {
			if (column < 0 || column >= cells.size())
				return "";
			return cells.remove(column);
		}

		public int size() {
			return cells.size();
		}

	}

}