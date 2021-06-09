package symbolTable;


public class Attributes {
	private TypeDescriptor typeDescr;
	private char register;
	
	public Attributes(TypeDescriptor i) {
		this.typeDescr = i;
	}

	public TypeDescriptor getTypeDescr() {
		return typeDescr;
	}

	public char getRegister() {
		return register;
	}

	public void setRegister(char register) {
		this.register = register;
	}

}
