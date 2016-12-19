/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class AssignStatement extends AssignExprLocalDecStatement {

	public AssignStatement(Expr expr) {
		this.expr = expr;
	}
	
	public void setExpr(Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public void genC(PW pw) {

			CompositeExpr ce = (CompositeExpr) expr;
			// string
			if(ce.getLeft().getType() == Type.stringType) {
				Variable vLeft = ((VariableExpr) ce.getLeft()).getV();
				
				pw.printIdent("strcpy(");
				vLeft.genC(pw);
				pw.print(", ");
				// var = "literalStr";
				// strcpy(var, "literalStr");
				if(ce.getRight() instanceof LiteralString) {
					LiteralString literalStr = ((LiteralString) ce.getRight());	
					literalStr.genC(pw, false);
				// var = varStr;
				// strcpy(var, varStr);
				} else {
					Variable vRight = ((VariableExpr) ce.getRight()).getV();
					vRight.genC(pw);	
				}
				pw.println(");");
			} else {
				pw.printIdent("");
				expr.genC(pw, false);
				pw.println(";");
			}
		}	
	

	@Override
	public void genKra(PW pw) {
		pw.printIdent("");
		expr.genKra(pw, false);
		pw.println(";");
	}
	
	private Expr expr;

}
