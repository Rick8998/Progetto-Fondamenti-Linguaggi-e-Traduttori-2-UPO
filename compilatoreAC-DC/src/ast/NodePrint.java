package ast;

import visitor.IVisitor;

public class NodePrint extends NodeStm{
	private NodeID id;
	
	public NodePrint(NodeID nodeId) {
		super();
		this.id = nodeId;
	}
	
	public String toString() {
		return "<NODE_PRINT> Id: " +id.toString()+" <NODE_PRINT>\n";
	}
	public boolean equals(NodePrint nodePrint) {
		if(id.equals(nodePrint.getId()) && (nodePrint instanceof NodePrint)) return true;
		else return false;
	}

	public NodeID getId() {
		return id;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	
}
