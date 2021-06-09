package ast;

import visitor.IVisitor;

public class NodeConv extends NodeExpr{
	private NodeExpr nodeExpr;
	
	

	public NodeConv(NodeExpr nodeExpr) {
		this.nodeExpr = nodeExpr;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	public void setNodeExpr(NodeExpr nodeExpr) {
		this.nodeExpr = nodeExpr;
	}

	public NodeExpr getNodeExpr() {
		return nodeExpr;
	}



}
