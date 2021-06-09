package ast;

import visitor.IVisitor;

public class NodeDeref extends NodeExpr{
	private NodeID id;

	public NodeDeref(NodeID nodeId) {
		this.id = nodeId;
	}
	
	public String toString() {
		return "<NODE_DEREF> id: " + id.toString() + " <NODE_DEREF>";
	}
	
	public boolean equals(NodeDeref nodeDeref) {
		if(this.id.equals(nodeDeref.getId())) {
			return true;
		}
		return false;
	}
	
	public NodeID getId() {
		return id;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}	
}
