package kaba4cow.ascii.toolbox.tools;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class TableSorter implements Comparator<LinkedList<String>> {

	private static final TableSorter instance = new TableSorter();

	private int column;

	private TableSorter() {

	}

	public static void sort(Table table, int column, boolean ascending) {
		instance.column = column;
		Collections.sort(table.getItems(), instance);
		if (!ascending)
			Collections.reverse(table.getItems());
	}

	@Override
	public int compare(LinkedList<String> o1, LinkedList<String> o2) {
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