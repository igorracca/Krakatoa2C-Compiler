/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeNull extends Type {
    
    public TypeNull() {
        super("null");
    }
    
    @Override
   public String getCName() {
      return "NULL";
   }

	@Override
	public String getKraName() {
		return "null";
	}

}