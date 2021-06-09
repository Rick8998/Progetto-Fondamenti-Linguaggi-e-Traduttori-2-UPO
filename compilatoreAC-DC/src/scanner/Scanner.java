package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import token.*;

public class Scanner {
	final char EOF = (char) -1; // int 65535
	private int riga;
	private PushbackReader buffer;
	@SuppressWarnings("unused")
	private String log;
	private Token peeked = null;
	private List<Character> skipChars = Arrays.asList(' ', '\n', '\t', '\r', EOF);
	private List<Character> letters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
	private List<Character> numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	// HashMap caratteri TokenType per associazione fra '+', '-', '*', '/', '=', ';'
	// e TokenType
	HashMap<String, TokenType> operandToToken = new HashMap<String, TokenType>();
	
	// HashMap stringhe TokenType per associazione fra parole chiave "print",
	// "float", "int" e TokenType
	HashMap<String, TokenType> keyWordsToToken = new HashMap<String, TokenType>();
	
	

	public Scanner(String fileName) throws FileNotFoundException {
		this.buffer = new PushbackReader(new FileReader(fileName));
		riga = 1;
		insertOperandToToken();
		insertKeyWordToToken();
	}

	public Token peekToken() throws IOException, ScannerException { 
		if(peeked == null)
			peeked = nextToken();
		
		return peeked;
	}
	
	public Token nextToken() throws IOException, ScannerException {
		
		if(peeked!=null) {
			Token next = peeked;
			peeked = null;
			return next;
		}
		// nextChar contiene il prossimo carattere dell'input.
		while(true) {
			char nextChar = peekChar();
			
			// Avanza nel buffer leggendo i carattere in skipChars
			// incrementando riga se leggi '\n'.
			// Se raggiungi la fine del file ritorna il Token EOF
			if(nextChar == skipChars.get(1)) {
				readChar();
				riga++;
			}
			else if(nextChar == skipChars.get(4)) {
				return new Token(TokenType.EOF, riga);
			}
			else if(skipChars.contains(nextChar)) {
				readChar();
				//do nothing
			}
			
			// Se nextChar e' in numbers--->return scanNumber()
			// che legge sia un intero che un float e ritorna il Token INUM o FNUM i caratteri che leggete devono essere 
			//accumulati in una stringa che verra' assegnata al campo valore del Token
			else if(numbers.contains(nextChar)) {
				return scanNumber();
			}
			// Se nextChar e' in letters--->return scanId()
			// che legge tutte le lettere minuscole e ritorna un Token ID o il Token associato Parola Chiave (per generare i 
			//Token per le parole chiave usate l'HaskMap di corrispondenza)
			else if(letters.contains(nextChar)) {
				return scanId();
			}
			// Se nextChar e' un operatore o = o ;
			// ritorna il Token associato con l'operatore o il delimitatore .Anche qui per generare i Token usate l'HaskMap 
			//di corrispondenza
			else if(nextChar == '-') {
				readChar();
				return new Token(operandToToken.get("-"), riga, "-");
			}
			else if(nextChar == '+') {
				readChar();
				return new Token(operandToToken.get("+"), riga, "+");
			}
			else if(nextChar == '*') {
				readChar();
				return new Token(operandToToken.get("*"), riga, "*");
			}
			else if(nextChar == '/') {
				readChar();
				return new Token(operandToToken.get("/"), riga, "/");
			}
			else if(nextChar == '=') {
				readChar();
				return new Token(operandToToken.get("="), riga);
			}
			else if(nextChar == ';') {
				readChar();
				return new Token(operandToToken.get(";"), riga, ";");
			}
			// Altrimenti il carattere NON E' UN CARATTERE LEGALE
			// Potete:
			// (1) Cercare di recuperare l'errore scartando il carattere (segnalarlo
			// nella stringa log) e andando avanti
			// (2) Interrompere l'analisi lessicale sollevando una eccezione
			else throw new ScannerException("ScannerException (nextToken): IllegalValue alla riga " + riga);
		}
	}

	private Token scanId() throws IOException, ScannerException {
		StringBuilder value = new StringBuilder();
				
		while(letters.contains(peekChar())) {
			Character nextChar = readChar();
			value.append(nextChar);
		}
	
		char capitalErr = peekChar();
		//Se c'è un numero nell'Id --> ERRORE
		if(numbers.contains(peekChar())) {
			throw new ScannerException("ScanId: numberException");
		}
		else if(Character.isUpperCase(capitalErr)) {
			throw new ScannerException("ScanId: capitalLetterException");
		}
		
		
		if(value.toString().equals("print")) {
			return new Token(operandToToken.get("print"), riga, value.toString());
		} else if (value.toString().equals("float")) {
			return new Token(operandToToken.get("float"), riga, value.toString());
		} else if(value.toString().equals("int")) {
			return new Token(operandToToken.get("int"), riga, value.toString());
		}
		else {
			return new Token(TokenType.ID, riga, value.toString());
		}			
		
	}

	private Token scanNumber() throws ScannerException, IOException {
		StringBuilder value = new StringBuilder();
		int valAfterPointCount = 0;
		while(numbers.contains(peekChar())) {
			char nextChar = readChar();
			value.append(nextChar);
		}

		if(peekChar() == '.') {
			value.append(readChar());
			while(numbers.contains(peekChar())) {
				if(numbers.contains(peekChar())) {
					value.append(readChar());
					valAfterPointCount++;
				}					
			}
			
			if(letters.contains(peekChar())) {
				throw new ScannerException("ScanNumberException: wrongValueAfterPointException");
			}
			else if(valAfterPointCount > 4) {
				throw new ScannerException("ScanNumberExceptiopn: tooLongValueException");
			}
			else if(valAfterPointCount == 0) {
				throw new ScannerException("ScanNumberExceptiopn: incorrectFormatNovalueAfterPointException");
			}
			
			return new Token(TokenType.FLOAT, riga, value.toString());
		}
			
		return new Token(TokenType.INT, riga, value.toString());
	}

	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
	
	public void insertOperandToToken() {
		operandToToken.put("*", TokenType.TIMES);
		operandToToken.put("+", TokenType.PLUS);
		operandToToken.put("-", TokenType.MINUS);
		operandToToken.put("/", TokenType.DIV);
		operandToToken.put("=", TokenType.ASSIGN);
		operandToToken.put(";", TokenType.SEMI);
	}
	
	public void insertKeyWordToToken(){
		operandToToken.put("print", TokenType.PRINT);
		operandToToken.put("float", TokenType.TYFLOAT);
		operandToToken.put("int", TokenType.TYINT);
	}
}
