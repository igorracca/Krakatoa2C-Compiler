/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class KraObject extends InstanceVariable {

	public KraObject(String name, Type type, KraClass kraClass) {
		super(name, type);
		this.kraClass = kraClass;
	}
	
	public KraClass getKraClass() {
		return kraClass;
	}

	public void setKraClass(KraClass kraClass) {
		this.kraClass = kraClass;
	}
	
    public void genKra(PW pw) {
    	pw.print(name);
    }

	private KraClass kraClass;
	
}
