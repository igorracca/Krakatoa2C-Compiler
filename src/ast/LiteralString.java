/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("\"" + literalString + "\"");
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print(literalString);
		
	}
	
    private String literalString;
}
