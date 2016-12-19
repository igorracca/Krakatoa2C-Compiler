/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import lexer.Symbol;

public class IfStatement extends Statement {

	public IfStatement() {
	}
	
	public IfStatement(Expr expr) {
		this.expr = expr;
	}
	
	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public Statement getIfStatement() {
		return ifStatement;
	}

	public void setIfStatement(Statement ifStatement) {
		this.ifStatement = ifStatement;
	}

	public Statement getElseStatement() {
		return elseStatement;
	}

	public void setElseStatement(Statement elseStatement) {
		this.elseStatement = elseStatement;
	}

	@Override
	public void genC(PW pw) {
		pw.printIdent("if(");
	
		if(expr instanceof VariableExpr) {
			expr.genC(pw, false);
			pw.print(" != false");
		} else
		if(expr instanceof UnaryExpr && ((UnaryExpr)expr).getOp() == Symbol.NOT) {
			pw.print("(");
			((UnaryExpr)expr).getExpr().genC(pw, false);
			pw.print(") == false");
		} else {
			expr.genC(pw, false);
		}
		
		pw.println(") {");
		pw.add();
			ifStatement.genC(pw);
		pw.sub();
		pw.printlnIdent("}");
		if(elseStatement != null) {
			pw.printlnIdent("else {");
			pw.add();
				elseStatement.genC(pw);
			pw.sub();
			pw.printlnIdent("}");
		}	
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("if(");
		expr.genKra(pw, false);
		pw.println(") {");
		pw.add();
			ifStatement.genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
		if(elseStatement != null) {
			pw.printlnIdent("else {");
			pw.add();
				elseStatement.genKra(pw);
			pw.sub();
			pw.printlnIdent("}");
		}	
	}
	
	Expr expr;
	Statement ifStatement;
	Statement elseStatement;
}
