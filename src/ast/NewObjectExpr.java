/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class NewObjectExpr extends Expr {

	public NewObjectExpr(KraClass kraClass) {
		this.kraClass = kraClass;
	}
	
	@Override
	public void genC(PW pw, boolean putParenthesis) {
		pw.print("new" + kraClass.getCName() + "()");  
	}

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print("new " + kraClass.getKraName() + "()");
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return new TypeKraClass("new");
	}
	
	private KraClass kraClass;
}
