package visitor;

import ast.NodeAST;
import ast.NodeAssing;
import ast.NodeBinOp;
import ast.NodeConv;
import ast.NodeCost;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeExpr;
import ast.NodeID;
import ast.NodePrint;
import ast.NodeProgram;
import symbolTable.SymbolTable;

public class CodeGeneratorVisitor implements IVisitor{

	private StringBuffer code;
	private static char[] register;
	static int i = 0;

	public CodeGeneratorVisitor() {
		this.code = new StringBuffer();
		register = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	}
	
	private static char newRegister() {
		char c = register[i];
		i++;
		return c;
	}
	
	@Override
	public void visit(NodeProgram node) {
		SymbolTable.init();
		for(NodeAST nAST : node.getDesSts()) {
			nAST.accept(this);
		}
		
	}

	@Override
	public void visit(NodeAssing node) {
		char c = node.getId().getDefinition().getRegister();
		node.getExpr().accept(this);
		code.append("s" + c);
		code.append(" 0 k ");
		
	}

	@Override
	public void visit(NodeBinOp node) {
		NodeExpr left = node.getLeft();
		NodeExpr right = node.getRight();
		left.accept(this);
		right.accept(this);
		
		switch (node.getOp()) {
		case PLUS:
			code.append("+ ");
			break;
		case DIV:
			code.append("/ ");
			break;
		case MINUS:
			code.append("- ");
			break;
		case TIMES:
			code.append("* ");
			break;
		default:
			break;
		}
		
	}

	@Override
	public void visit(NodeCost node) {
		code.append(node.getValue() + " ");
	}

	@Override
	public void visit(NodeDecl node) {
		NodeID nodeId = node.getId();
		nodeId.getDefinition().setRegister(newRegister());
		
	}

	@Override
	public void visit(NodeDeref node) {
		NodeID nodeId = node.getId();
		char s = nodeId.getDefinition().getRegister();
		code.append("l " + s + " ");
	}

	@Override
	public void visit(NodeID node) {}

	@Override
	public void visit(NodePrint node) {
		NodeID nodeId = node.getId();
		char c = nodeId.getDefinition().getRegister();
		code.append("l" + c + " p P ");
		
	}

	@Override
	public void visit(NodeConv node) {
		node.getNodeExpr().accept(this);
		code.append("5 k ");
	}
	
	public String toString() {
		return code.toString().trim();
	}

}
