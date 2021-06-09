package ast;

import symbolTable.TypeDescriptor;
import visitor.IVisitor;

public abstract class NodeAST {
	
	private TypeDescriptor typeDescriptor;
	
	public abstract void accept(IVisitor visitor);
	
	public void setTypeDescriptor(TypeDescriptor typeDescriptor) {
		this.typeDescriptor = typeDescriptor;
	}

	public TypeDescriptor getTypeDescriptor() {
		return typeDescriptor;
	}
	
	
}
