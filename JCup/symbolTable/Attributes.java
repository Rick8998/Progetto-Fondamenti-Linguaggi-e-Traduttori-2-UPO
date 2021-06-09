package symbolTable;

public class Attributes {
	TypeDescriptor type;
	private char register;
	
	public Attributes(TypeDescriptor type) {
		this.type = type;
	}

	public TypeDescriptor getTypeDescr() {
		return type;
	}

	public void setTypeDescr(TypeDescriptor type) {
		this.type = type;
	}

	public char getRegister() {
		return register;
	}

	public void setRegister(char register) {
		this.register = register;
	}
}
