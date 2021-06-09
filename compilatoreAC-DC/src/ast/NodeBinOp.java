package ast;

import visitor.IVisitor;

public class NodeBinOp extends NodeExpr{
	private LangOper op;
	private NodeExpr left;
	private NodeExpr right;
	
	public NodeBinOp(LangOper langOper, NodeExpr nodeExprLeft, NodeExpr nodeExprRight) {
		super();
		this.op = langOper;
		this.left = nodeExprLeft;
		this.right = nodeExprRight;
	}
	
	public String toString() {
		return "<NODE_BIN_OP> langOper: " + op.toString() + " exprLeft: " + left.toString() + " exprRight: " + right.toString() + " <NODE_BIN_OP>";
	}
	
	public boolean equals(NodeBinOp nodeBinOp) {
		if((this.op.equals(nodeBinOp.getOp())) && (this.left.equals(nodeBinOp.getLeft())) && (this.right.equals(nodeBinOp.getRight())) && (nodeBinOp instanceof NodeBinOp)) {
			return true;
		}
		return false;
	}
	
	public LangOper getOp() {
		return op;
	}
	public NodeExpr getLeft() {
		return left;
	}
	public NodeExpr getRight() {
		return right;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	public void setLeft(NodeExpr left) {
		this.left = left;
	}

	public void setRight(NodeExpr right) {
		this.right = right;
	}
	
}
