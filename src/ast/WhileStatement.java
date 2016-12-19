/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class WhileStatement extends Statement {

	public WhileStatement() {
	}
	
	public WhileStatement(Expr expr) {
		this.expr = expr;
	}
	
	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public Statement getWhileStatement() {
		return whileStatement;
	}

	public void setWhileStatement(Statement whileStatement) {
		this.whileStatement = whileStatement;
	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("while(");
		expr.genC(pw, false);
		pw.println(") {");
		pw.add();
			whileStatement.genC(pw);
		pw.sub();
		pw.printlnIdent("}");
	}
	@Override
	public void genKra(PW pw) {
		pw.printIdent("while(");
		expr.genKra(pw, false);
		pw.println(") {");
		pw.add();
			whileStatement.genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
	}
	
	Expr expr;
	Statement whileStatement;

}
