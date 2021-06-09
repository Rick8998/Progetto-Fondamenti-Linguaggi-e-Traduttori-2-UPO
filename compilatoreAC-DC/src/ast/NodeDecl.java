package ast;

import visitor.IVisitor;

public class NodeDecl extends NodeDecSt{
	private NodeID id;
	private LangType type;
	
	public NodeDecl(NodeID nodeId, LangType langType) {
		super();
		this.id = nodeId;
		this.type = langType;
	}
	
	public String toString() {
		return "<NODE_DECL> Type: "+ type + ", Id: " +id.toString() + " <NODE_DECL>\n";
	}

	public boolean equals(NodeDecl nodeDecl) {
		if(nodeDecl.getId().equals(id) && nodeDecl.getType().equals(type) && (nodeDecl instanceof NodeDecl)) {
			return true;
		}
		else return false;
	}
	
	public LangType getType() {
		return type;
	}

	public NodeID getId() {
		return id;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	
}
