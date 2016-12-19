/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.*;

public class InstanceVariableList {

    public InstanceVariableList(String className) {
        this.className = className;
    	instanceVariableList = new ArrayList<InstanceVariable>();
    }

    public ArrayList<InstanceVariable> getInstanceVariableList() {
		return instanceVariableList;
	}

	public void setInstanceVariableList(ArrayList<InstanceVariable> instanceVariableList) {
		this.instanceVariableList = instanceVariableList;
	}

	public void addElement(InstanceVariable instanceVariable) {
       instanceVariableList.add( instanceVariable );
    }
    
    public void add(InstanceVariableList ivl) {
    	instanceVariableList.addAll( ivl.instanceVariableList );
     }

    public Iterator<InstanceVariable> elements() {
    	return this.instanceVariableList.iterator();
    }

    public int getSize() {
        return instanceVariableList.size();
    }

    public void genC(PW pw) {  	
    	for(InstanceVariable v : instanceVariableList) {
		   pw.printIdent(v.getType().getCName() + " "); 	      
		   v.genC(pw);
		   pw.println(";");
    	}
    }
    
    public void genKra(PW pw) {
    	for(Variable v : instanceVariableList) {
   		   pw.printIdent("private " + v.getType().getKraName() + " ");
 		   v.genKra(pw);
 		   pw.println(";");
    	}
    }
    
    private ArrayList<InstanceVariable> instanceVariableList;
    private String className;
}
