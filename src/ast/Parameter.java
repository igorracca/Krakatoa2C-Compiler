/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;


public class Parameter extends Variable {

    public Parameter( String name, Type type ) {
        super(name, type);
    }
    
    @Override public boolean equals(Object o) {
    	return ((o instanceof Parameter) &&
    			this.getType() == ((Variable) o).getType());
    }
    
    public void genC(PW pw) {
		pw.print(getType().getCName() + " _" + name);
    }

    public void genKra(PW pw) {
    	pw.print(getType().getKraName() + " " + name);
    }

}