/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.*;

public class LocalVariableList {

    public LocalVariableList() {
       localList = new ArrayList<Variable>();
    }

    public void addElement(Variable v) {
       localList.add(v);
    }

    public Iterator<Variable> elements() {
        return localList.iterator();
    }

    public int getSize() {
        return localList.size();
    }
    
    public ArrayList<Variable> getLocalList() {
		return localList;
	}

	public void setLocalList(ArrayList<Variable> localList) {
		this.localList = localList;
	}

	public void genC(PW pw) {
	   	for(Variable v : localList) {
	   		pw.printIdent("");
	   		if(v instanceof KraObject) {
	   			pw.print("_class" + v.getType().getCName() + " *");
	   		} else {
	   			pw.print(v.getType().getCName() + " ");
	   		}   			
    		v.genC(pw);
    		pw.println(";");
    	}
	}
	
    public void genKra( PW pw ) {
    	for(Variable v : localList) {
    		pw.printIdent(v.getType().getKraName() + " ");
    		v.genKra(pw);
    		pw.println(";");
    	}
	}
	
	private ArrayList<Variable> localList;
}
