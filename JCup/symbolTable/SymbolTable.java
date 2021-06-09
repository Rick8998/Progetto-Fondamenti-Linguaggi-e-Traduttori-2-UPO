package symbolTable;

import java.util.HashMap;

public class SymbolTable {
	private HashMap<String, Attributes> table;

	public void init() {
		table = new HashMap<String, Attributes>();
	}

	public boolean enter(String id, Attributes entry) {
		Attributes value = table.get(id);
		if (value != null)
			return false;
		table.put(id, entry);
			return true;
	}

	public Attributes lookup(String id) {return table.get(id);}

	public String toStr() {
		StringBuilder res = new StringBuilder("symbol table\n=============\n");

		for (HashMap.Entry<String, Attributes> entry : table.entrySet())
			res.append(entry.getKey()).append("   \t").append(entry.getValue()).append("\n");

		return res.toString();
	}

	public int size() { return table.size();}
}
