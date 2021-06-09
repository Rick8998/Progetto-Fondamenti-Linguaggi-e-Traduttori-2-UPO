package ast;

import java.util.ArrayList;

import visitor.IVisitor;

public class NodeProgram extends NodeAST{
	private ArrayList<NodeDecSt> desSts;
	
	public NodeProgram(ArrayList<NodeDecSt> listNodeDecSt) {
		super();
		this.desSts = listNodeDecSt;
	}
	
	public String toString() {
		String output = "<NODE_PROGRAM>\n";
		for(NodeDecSt x : desSts) {
			output= output+x.toString();
		}
		return  output + "<NODE_PROGRAM>";
	}

	public boolean equals(NodeProgram nodeProgram) {
		for(NodeDecSt nd : desSts) {
			if(!nodeProgram.getDesSts().contains(nd)) {
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<NodeDecSt> getDesSts() {
		return desSts;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
}
