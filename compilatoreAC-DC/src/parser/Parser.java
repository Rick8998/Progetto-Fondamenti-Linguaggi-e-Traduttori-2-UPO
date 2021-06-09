package parser;

import java.io.IOException;
import java.util.ArrayList;

import ast.LangOper;
import ast.LangType;
import ast.NodeAssing;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeExpr;
import ast.NodeID;
import ast.NodePrint;
import ast.NodeProgram;
import ast.NodeStm;
import scanner.Scanner;
import scanner.ScannerException;
import token.Token;
import token.TokenType;

public class Parser {
	
	private Scanner scanner;
	
	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}
	
	private NodeProgram parsePrg() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		ArrayList<NodeDecSt> listNodeDecSt = new ArrayList<NodeDecSt>();
		switch (tk.getTipo()) {
		case PRINT:
		case TYFLOAT:
		case TYINT:
		case EOF:
			listNodeDecSt = parseDSs();
			match(TokenType.EOF);
			return new NodeProgram(listNodeDecSt);
		default:
			throw new ParserException("ParserException parsePrg: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
	}
	
	private ArrayList<NodeDecSt> parseDSs() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		//ArrayList<NodeDecSt> listNodeDecSt = new ArrayList<NodeDecSt>();
		switch (tk.getTipo()) {
		case TYFLOAT:
		case TYINT:
			NodeDecl nDecl = parseDcl();
			ArrayList<NodeDecSt> listNodeDecSt1 = parseDSs(); /*ArrayList<NodeDecSt> nds1 = parseDSs()*/
			listNodeDecSt1.add(0, nDecl);
			return listNodeDecSt1;
		case ID:
		case PRINT:
			NodeStm nStm= parseStm();
			ArrayList<NodeDecSt> listNodeDecSt2 = parseDSs(); /*ArrayList<NodeDecSt> nds2 = parseDSs()*/
			listNodeDecSt2.add(0, nStm);
			return listNodeDecSt2;
		case EOF:
			match(TokenType.EOF);
			return new ArrayList<NodeDecSt>(); //devo ritornare un nodeProgram, non null come avverrebbe in questo caso se avessi un file vuoto
		
		default:
			throw new ParserException("ParserException: parseDSs: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
	}
		
	
	private NodeDecl parseDcl() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
			case TYFLOAT:
				match(TokenType.TYFLOAT);
				Token t = match(TokenType.ID);
				match(TokenType.SEMI);
				return new NodeDecl(new NodeID(t.getVal()), LangType.TYFLOAT);
				
			case TYINT:
				match(TokenType.TYINT);
				Token t2 = match(TokenType.ID);
				match(TokenType.SEMI);
				return new NodeDecl(new NodeID(t2.getVal()), LangType.TYINT);
				
			default:
				throw new ParserException("ParserException: parseDcl: Al token:"
						+ "\n Tipo: " + tk.getTipo()
						+ "\n Valore: " + tk.getVal()
						+ "\n Riga: " + tk.getRiga());
		}
	}
	
	private NodeStm parseStm() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
		case ID:
			Token tkId = match(TokenType.ID);
			match(TokenType.ASSIGN);
			NodeExpr ndExpr = parseExp();
			match(TokenType.SEMI);
			return new NodeAssing(new NodeID(tkId.getVal()), ndExpr);
		case PRINT:
			match(TokenType.PRINT);
			Token t2 = match(TokenType.ID);
			match(TokenType.SEMI);
			return new NodePrint(new NodeID(t2.getVal()));
		default:
			throw new ParserException("ParserException: parseStm: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
	}
	
	private NodeExpr parseExp() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
		case INT:
		case FLOAT:
		case ID:
			NodeExpr ndTr = parseTr();
			NodeExpr ndExpr = parseExpP(ndTr);
			return ndExpr;
		default:
			throw new ParserException("ParserException: parseExp: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
		
	}
	
	private NodeExpr parseExpP(NodeExpr leftOp) throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
		case PLUS:
			match(TokenType.PLUS);
			NodeExpr rightOpP = parseExp();
			return new NodeBinOp(LangOper.PLUS, leftOp, rightOpP);
		case MINUS:
			match(TokenType.MINUS);
			NodeExpr rightOpM = parseExp();
			return new NodeBinOp(LangOper.MINUS, leftOp, rightOpM);
		case SEMI:
			//do nothing
			return leftOp;
		default:
			throw new ParserException("ParserException: parseExpP: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
		
	}
	
	private NodeExpr parseTr() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
		case INT:
		case FLOAT:
		case ID:
			NodeExpr ndExprVal = parseVal();
			NodeExpr ndExpr = parseTrP(ndExprVal);
			return ndExpr;
		default:
			throw new ParserException("ParserException: parseTr: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
	}
	
	private NodeExpr parseTrP(NodeExpr leftOP) throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
		case TIMES:
			match(TokenType.TIMES);
			NodeExpr rightOpT = parseTr();
			return new NodeBinOp(LangOper.TIMES, leftOP, rightOpT);
		case DIV:
			match(TokenType.DIV);
			NodeExpr rightOpD = parseTr();
			return new NodeBinOp(LangOper.DIV, leftOP, rightOpD);
		case PLUS:
		case MINUS:
		case SEMI:
			//do nothing
			return leftOP;
		default:
			throw new ParserException("ParserException: parseTrP: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
		
	}
	
	private NodeExpr parseVal() throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		switch (tk.getTipo()) {
		case INT:
			Token tkInt = match(TokenType.INT);
			return new NodeCost(tkInt.getVal(), LangType.TYINT);
		case FLOAT:
			Token tkFloat = match(TokenType.FLOAT);
			return new NodeCost(tkFloat.getVal(), LangType.TYFLOAT);
		case ID:
			Token tkId = match(TokenType.ID);
			return new NodeDeref(new NodeID(tkId.getVal()));
		default:
			throw new ParserException("ParserException: parseVal: Al token:"
					+ "\n Tipo: " + tk.getTipo()
					+ "\n Valore: " + tk.getVal()
					+ "\n Riga: " + tk.getRiga());
		}
	}
	
	private Token match(TokenType type) throws IOException, ScannerException, ParserException {
		Token tk = scanner.peekToken();
		if(type.equals(tk.getTipo())) scanner.nextToken();
		else throw new ParserException("ParserException match: row: "+tk.getRiga()+" expected "+ type.toString() + " but was " + tk.getTipo().toString());
		return tk;
	}
	
	public NodeProgram parse() throws IOException, ScannerException, ParserException {
		return parsePrg();
	}
	
}
