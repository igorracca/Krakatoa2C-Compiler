/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }
  
    public Variable getV() {
		return v;
	}

	public void setV(Variable v) {
		this.v = v;
	}

	public void genC( PW pw, boolean putParenthesis ) {
		v.genC(pw);
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print( v.getName() );
	}
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;

}