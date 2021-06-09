package ast;

import symbolTable.Attributes;
import visitor.IVisitor;

public class NodeID extends NodeAST{
	private String name;
	private Attributes definition;
	
	public NodeID(String name) {
		super();
		this.name = name;
	}
	
	public String toString() {
		return "<NODE_ID> name: " +name.toString()+" <NODE_ID>";
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(NodeID nodeId) {
		if(nodeId.getName().contains(this.name) && (nodeId.getName() instanceof String)) return true;
		else return false;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
		
	}

	public Attributes getDefinition() {
		return definition;
	}

	public void setDefinition(Attributes definition) {
		this.definition = definition;
	}

	
	
}
