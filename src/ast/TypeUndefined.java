/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeUndefined extends Type {
    // variables that are not declared have this type
    
   public TypeUndefined() { super("undefined"); }
   
   public String getCName() {
      return "";
   }

	@Override
	public String getKraName() {
		return "undefined";
	}
	   
}
