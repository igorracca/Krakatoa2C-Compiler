/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class TypeKraClass extends Type {
    
    public TypeKraClass() {
    	super("kraClass");
    }
	
    public TypeKraClass(String name) {
        super(name);
    }
    
    @Override
   public String getCName() {
      return "_" + super.getName();
   }

	@Override
	public String getKraName() {
		return super.getName();
	}

}