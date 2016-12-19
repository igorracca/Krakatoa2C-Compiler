/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeBoolean extends Type {

   public TypeBoolean() { super("boolean"); }

   @Override
   public String getCName() {
      return "boolean";
   }
   
   public String getKraName() {
	   return "boolean";
   }
}
