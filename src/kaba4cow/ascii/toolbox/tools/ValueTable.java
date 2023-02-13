package kaba4cow.ascii.toolbox.tools;

import java.util.HashMap;

public class ValueTable<K, V> {

	private HashMap<K, HashMap<K, V>> table;

	public ValueTable() {
		this.clear();
	}

	public void put(K column, K row, V value) {
		if (!table.containsKey(column))
			table.put(column, new HashMap<>());
		table.get(column).put(row, value);
	}

	public V get(K column, K row) {
		if (!table.containsKey(column))
			return null;
		return table.get(column).get(row);
	}

	public void clear() {
		table = new HashMap<>();
	}

}
