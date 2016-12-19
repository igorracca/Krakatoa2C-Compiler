/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class DoWhileStatement extends Statement {

	public DoWhileStatement() {
	}
	
	public DoWhileStatement(Expr expr) {
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
		pw.printlnIdent("do {");
		pw.add();
			whileStatement.genC(pw);
		pw.sub();
		pw.printIdent("} while(");
		expr.genC(pw, false);
		pw.println(");");
	}
	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("do {");
		pw.add();
			whileStatement.genKra(pw);
		pw.sub();
		pw.printIdent("} while(");
		expr.genKra(pw, false);
		pw.println(");");
	}
	
	Expr expr;
	Statement whileStatement;

}
