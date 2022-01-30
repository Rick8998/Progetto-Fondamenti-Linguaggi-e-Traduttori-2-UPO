package visitor;

import ast.*;
import symbolTable.Attributes;
import symbolTable.SymbolTable;
import symbolTable.TypeDescriptor;

public class TypeCheckingVisitor implements IVisitor{
	StringBuilder log = new StringBuilder();
	
	@Override
	public void visit(NodeProgram node) {
		SymbolTable.init();
		for(NodeAST nodeAST : node.getDesSts()) {
			nodeAST.accept(this);
		}
	}
	
	@Override
	public void visit(NodeAssing node) {
		node.getId().accept(this);
		node.getExpr().accept(this);
		if(compatible(node.getId().getTypeDescriptor(), node.getExpr().getTypeDescriptor())) {
			if(!node.getId().getTypeDescriptor().equals(node.getExpr().getTypeDescriptor())) {
				convertExpr(node.getExpr());
			}
			node.setTypeDescriptor(symbolTable.TypeDescriptor.VOID);
		}else {
			node.setTypeDescriptor(symbolTable.TypeDescriptor.ERROR);
			log.append("TIPI INCOMPATIBILI, si è cercato di convertire in " + node.getId().getTypeDescriptor().toString() + " da " + node.getExpr().getTypeDescriptor().toString());
		}
	}

	@Override
	public void visit(NodeBinOp node) {
		node.getLeft().accept(this);
		node.getRight().accept(this);
		if(node.getLeft().getTypeDescriptor().equals(symbolTable.TypeDescriptor.ERROR) || node.getRight().getTypeDescriptor().equals(symbolTable.TypeDescriptor.ERROR)) {
			node.setTypeDescriptor(symbolTable.TypeDescriptor.ERROR);
			log.append("Non è possibile assegnare l'operazione");
		}
		else if(node.getLeft().getTypeDescriptor().equals(node.getRight().getTypeDescriptor())) {
		    node.setTypeDescriptor(node.getLeft().getTypeDescriptor());
		}
		else if(compatible(node.getLeft().getTypeDescriptor(), node.getRight().getTypeDescriptor())) {
			node.setRight(convertExpr(node.getRight()));
			node.setTypeDescriptor(node.getLeft().getTypeDescriptor());;
		}
	}
	
	@Override
	public void visit(NodeConv node) {
		node.getNodeExpr().accept(this);
		if(node.getNodeExpr().getTypeDescriptor().equals(TypeDescriptor.INT)) {
			node.getNodeExpr().setTypeDescriptor(TypeDescriptor.FLOAT);
		}
		else {
			node.getNodeExpr().setTypeDescriptor(TypeDescriptor.ERROR);
			log.append("Errore di conversione");
		}
	}
	
	@Override
	public void visit(NodeCost node) {
		if(node.getType() == LangType.TYINT) node.setTypeDescriptor(TypeDescriptor.INT);
		else if(node.getType() == LangType.TYFLOAT) node.setTypeDescriptor(TypeDescriptor.FLOAT);
		else {
			node.setTypeDescriptor(TypeDescriptor.ERROR);
			log.append("La costante è errata: " + node.getValue());
		}
	}

	@Override
	public void visit(NodeDecl node) {
		NodeID id = node.getId();
		if(SymbolTable.lookup(id.getName())!=null) {
			node.setTypeDescriptor(symbolTable.TypeDescriptor.ERROR);
			log.append("MethodVisitOnNodeDecl ERR | Variabile già definita: "+node.getId().getName());
		}else {
			Attributes attr;
			if(node.getType().equals(LangType.TYINT)) attr = new Attributes(symbolTable.TypeDescriptor.INT);
			else if(node.getType().equals(LangType.TYFLOAT)) attr = new Attributes(symbolTable.TypeDescriptor.FLOAT);
			else attr = new Attributes(symbolTable.TypeDescriptor.ERROR);
			
			//Aggiungo nella symbolTable
			SymbolTable.enter(node.getId().getName(), attr);
			node.getId().setTypeDescriptor(attr.getTypeDescr());
			node.getId().setDefinition(attr);
		}
	}

	@Override
	public void visit(NodeDeref node) {
		node.getId().accept(this);
		node.setTypeDescriptor(node.getId().getTypeDescriptor());
	}

	@Override
	public void visit(NodeID node) {
		String nome = node.getName();
		if(SymbolTable.lookup(nome)==null) {
			node.setTypeDescriptor(symbolTable.TypeDescriptor.ERROR);
			log.append("MethodVisitOnNodeID ERR | Variabile già definita: "+node.getName());
		}else {
			Attributes attr = SymbolTable.lookup(node.getName());
			node.setTypeDescriptor(attr.getTypeDescr());
			node.setDefinition(attr);
		}
	}

	@Override
	public void visit(NodePrint node) {
		node.getId().accept(this);
		if(node.getId().getTypeDescriptor().equals(TypeDescriptor.ERROR)) {
			node.setTypeDescriptor(symbolTable.TypeDescriptor.ERROR);
			log.append("MethodVisitOnNodePrint ERR | Variabile già definita: "+node.getId().getName());
		}else {
			node.setTypeDescriptor(symbolTable.TypeDescriptor.VOID);
		}
	}
	
	
	private boolean compatible(symbolTable.TypeDescriptor t1, symbolTable.TypeDescriptor t2) {
		if((!t1.equals(symbolTable.TypeDescriptor.ERROR) && !t2.equals(symbolTable.TypeDescriptor.ERROR) && t1.equals(t2)) || (t1.equals(symbolTable.TypeDescriptor.FLOAT) && t2.equals(symbolTable.TypeDescriptor.INT))) {
			return true;
		}
		return false;
	}
	
	private NodeExpr convertExpr(NodeExpr node) {
		if(node.getTypeDescriptor().equals(symbolTable.TypeDescriptor.FLOAT)) {
			return node;
		} else {
			NodeConv nodeConv = new NodeConv(node);
			nodeConv.setTypeDescriptor(symbolTable.TypeDescriptor.FLOAT);
			return nodeConv;
		}
	}
	
	public String log() {
		return log.toString();
	}
}
