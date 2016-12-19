/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class ReturnStatement extends Statement {

	public ReturnStatement(Expr expr) {
		this.expr = expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}
	
	public Expr getExpr() {
		return expr;
	}
	
	public Type getReturnType() {
		return expr.getType();
	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("return (");
		expr.genC(pw, false);
		pw.println(");");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("return (");
		expr.genKra(pw, false);
		pw.println(");");
	}
	
	private Expr expr;

}
