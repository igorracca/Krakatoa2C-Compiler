/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;

public class WritelnStatement extends Statement {

	public WritelnStatement() {
		exprList = new ExprList();
	}
	
	public void addExpr(Expr e) {
		exprList.addElement(e);
	}
	
	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}
	
	@Override
	public void genC(PW pw) {
        WriteStatement writeStmt = new WriteStatement();
        writeStmt.setExprList(exprList);
        
        writeStmt.genC(pw);
        pw.printlnIdent("printf(\"\\n\");");
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("write(");
		exprList.genKra(pw);
		pw.println(");");
		pw.printIdent("write(\"\n\")");
	}

	private ExprList exprList;
}

