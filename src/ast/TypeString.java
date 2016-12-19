/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeString extends Type {
    
    public TypeString() {
        super("String");
    }
    
   public String getCName() {
      return "char *";
   }
   
   public String getKraName() {
	   return "String";
   }

}