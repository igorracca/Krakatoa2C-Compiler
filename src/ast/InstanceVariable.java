/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class InstanceVariable extends Variable {

    public InstanceVariable( String name, Type type) {
        super(name, type);
     }
	
	public InstanceVariable( String name, Type type , String kraClassName) {
       super(name, type);
       this.kraClassName = kraClassName;
    }
    
	public void genC(PW pw) {
		if(kraClassName != null) {
			pw.print(kraClassName);
		}
		pw.print("_" + name);
    }	

    public void genKra(PW pw) {
    	pw.print(name);
    }
    
    String kraClassName = null;
}
