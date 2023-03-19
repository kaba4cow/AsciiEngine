package kaba4cow.ascii.toolbox.tools;

import java.util.Collections;
import java.util.Comparator;

public class TableSorter implements Comparator<Table.Row> {

	private static final TableSorter instance = new TableSorter();

	private int column;

	private TableSorter() {

	}

	public static void sort(Table table, int column, boolean ascending) {
		instance.column = column;
		Collections.sort(table.getRows(), instance);
		if (!ascending)
			Collections.reverse(table.getRows());
	}

	@Override
	public int compare(Table.Row o1, Table.Row o2) {
		if (column < 0 || column >= o1.size() || column >= o2.size())
			return 0;

		String s1 = o1.get(column);
		String s2 = o2.get(column);

		if (isNumber(s1) && isNumber(s2))
			return Double.compare(Double.parseDouble(s1), Double.parseDouble(s2));

		return s1.compareTo(s2);
	}

	private boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}