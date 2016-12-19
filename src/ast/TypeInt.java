/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeInt extends Type {
    
    public TypeInt() {
        super("int");
    }
    
    @Override
   public String getCName() {
      return "int";
   }

	@Override
	public String getKraName() {
		return "int";
	}

}