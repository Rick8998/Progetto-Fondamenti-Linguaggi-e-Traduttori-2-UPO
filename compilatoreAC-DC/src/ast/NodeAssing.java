package ast;

import visitor.IVisitor;

public class NodeAssing extends NodeStm{
	private NodeID id;
	private NodeExpr expr;
	
	public NodeAssing(NodeID nodeId, NodeExpr nodeExpr) {
		this.id = nodeId;
		this.expr = nodeExpr;
	}
	
	public String toString() {
		return "<NODE_ASSIGN> id: " + id.toString() + " expr: " + expr.toString() + " <NODE_ASSIGN>\n";
	}
	
	public boolean equals(NodeAssing nodeAssign) {
		if(this.id.equals(nodeAssign.getId()) && this.expr.equals(nodeAssign.getExpr()) && nodeAssign instanceof NodeAssing) {
			return true;
		}
		
		return false;
	}
	
	public NodeID getId() {
		return id;
	}
	public NodeExpr getExpr() {
		return expr;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	
}
