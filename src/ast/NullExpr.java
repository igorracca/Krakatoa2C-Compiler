/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class NullExpr extends Expr {
  
   public Type getType() {
	      return Type.nullType;
	   }
	
   public void genC( PW pw, boolean putParenthesis ) {
      pw.print("NULL");
   }
   
   @Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.print("null");
	}
}