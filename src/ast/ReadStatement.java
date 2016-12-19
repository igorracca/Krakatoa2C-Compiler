/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;

public class ReadStatement extends Statement {

	public ReadStatement() {
		exprList = new ExprList();
	}
	
	public void addExpr(Expr e) {
		exprList.addElement(e);
	}
	
	@Override
	public void genC(PW pw) {
		
		ArrayList<Expr> exprArrayList = exprList.getExprList();
		for ( Expr e : exprArrayList ) {
			Variable v = (Variable) ((VariableExpr)e).getV();
			
			pw.printlnIdent("{");
			pw.add();
				pw.printlnIdent("char __s[512];");
				pw.printlnIdent("gets(__s);");
						
			if(e.getType() == Type.stringType) {
				pw.printIdent("");
				v.genC(pw);
				pw.println(" = malloc(strlen(__s) + 1);");
				pw.printIdent("strcpy(");
				v.genC(pw);
				pw.println(", __s);");
    		} else {
    			pw.printlnIdent("sscanf(__s, \"%d\", &_" + v.getName() + ");");
    		}
			
			pw.sub();
			pw.printlnIdent("}");
		}
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("read(");
		exprList.genKra(pw);
		pw.println(");");
	}

	private ExprList exprList;
}
