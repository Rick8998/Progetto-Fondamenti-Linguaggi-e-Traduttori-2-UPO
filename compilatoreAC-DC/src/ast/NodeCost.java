package ast;

import visitor.IVisitor;

public class NodeCost extends NodeExpr{
	private String value;
	private LangType type;
	
	public NodeCost(String value, LangType type) {
		super();
		this.value = value;
		this.type = type;
	}

	public String toString() {
		return "<NODE_COST> Value:" + value.toString() + "Type:" + type.toString() + " <NODE_COST>";
	}
	
	public boolean equals(NodeCost nodeCost) {
		if(this.value.contains(nodeCost.getValue()) && this.type.equals(nodeCost.getType()) && (nodeCost.getValue() instanceof String)) {
			return true;
		}
		return false;
	}
	
	public LangType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

}
