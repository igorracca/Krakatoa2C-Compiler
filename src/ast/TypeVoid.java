/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeVoid extends Type {
    
    public TypeVoid() {
        super("void");
    }
    
   public String getCName() {
      return "void";
   }
   
   public String getKraName() {
	      return "void";
	   }

}