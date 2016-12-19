/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.*;

public class ParamList {

    public ParamList() {
       paramList = new ArrayList<Parameter>();
    }

    public void addElement(Parameter p) {
       paramList.add(p);
    }

    public Iterator<Parameter> elements() {
        return paramList.iterator();
    }

    public ArrayList<Parameter> getParamList() {
		return paramList;
	}

	public void setParamList(ArrayList<Parameter> paramList) {
		this.paramList = paramList;
	}

	public int getSize() {
        return paramList.size();
    }
    
    public boolean checkParams(ParamList pl) {
    	ArrayList<Parameter> paramList = pl.getParamList();
    	return (this.paramList == paramList);   	
    }
    
    @Override public boolean equals(Object o) {
    	return ((o instanceof ParamList) &&
    			(this.getSize() == ((ParamList) o).getSize()) &&
    			this.paramList.equals(((ParamList) o).getParamList()));
    }
    
    public void genC(PW pw) {
    	for(Parameter p : paramList) {
    		pw.print(", ");
    		p.genC(pw);
    	}
    }
    
    public void genKra(PW pw) {
    	int size = paramList.size();
    	for(Parameter p : paramList) {
    		p.genKra(pw);
            if ( --size > 0 )
                pw.print(", ");
    	}
    }
    
    private ArrayList<Parameter> paramList;

}
