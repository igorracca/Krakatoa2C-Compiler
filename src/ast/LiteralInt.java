/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print("" + value);
    }
    
    public Type getType() {
        return Type.intType;
    }
    
	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print("" + value);	
	}
	
    private int value;
}
