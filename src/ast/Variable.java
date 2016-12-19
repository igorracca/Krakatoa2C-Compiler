/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class Variable {

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }

    public Type getType() {
        return type;
    }
    
	public void genC(PW pw) {
		pw.print("_" + name);
    }	

    public void genKra(PW pw) {
    	pw.print(name);
    }
    
    protected String name;
    private Type type;
}