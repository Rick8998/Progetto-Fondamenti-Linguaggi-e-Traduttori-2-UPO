package token;

import org.junit.platform.commons.util.ToStringBuilder;

public class Token {

	private int riga;
	private TokenType tipo;
	private String val;
	
	public Token(TokenType tipo, int riga, String val) {
		this.riga = riga;
		this.tipo = tipo;
		this.val = val;
	}
	
	public Token(TokenType tipo, int riga) {
		this.tipo = tipo;
		this.riga = riga;
	}
    
	public String toString() {
		//<Tipo, riga, valore (se c'è)>
		ToStringBuilder stringBuilder = new ToStringBuilder(this);
		//stringBuilder.append(null, ">");
		stringBuilder.append("tipo", tipo);
		stringBuilder.append("riga", riga);
		stringBuilder.append("val", val);
		//stringBuilder.append(null, ">");
		
		return stringBuilder.toString();
	}

	public TokenType getTipo() {
		return tipo;
	}

	public void setTipo(TokenType tipo) {
		this.tipo = tipo;
	}

	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

     

}
