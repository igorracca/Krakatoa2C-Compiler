/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class PrivateMethodList {

	public PrivateMethodList(String className) {
    	this.className = className;
    	privateMethodList = new ArrayList<PrivateMethod>();
     }

	public ArrayList<PrivateMethod> getPrivateMethodList() {
		return privateMethodList;
	}

	public void setPrivateMethodList(ArrayList<PrivateMethod> privateMethodList) {
		this.privateMethodList = privateMethodList;
	}

	public void addElement(PrivateMethod pm) {
    	 privateMethodList.add(pm);
     }

	public Iterator<PrivateMethod> elements() {
         return privateMethodList.iterator();
     }

	public ArrayList<PrivateMethod> getprivateMethod() {
 		return privateMethodList;
 	}

	public int getSize() {
         return privateMethodList.size();
     }

    public void genC(PW pw) {
    	   for(PrivateMethod pm : privateMethodList) {
  		   pm.genC(pw, className);
  		   pw.println();
  	   }
   }
	
	public void genKra(PW pw) {
  	   for(PrivateMethod pm : privateMethodList) {
		   pm.genKra(pw);
	   }
     }
	
    private ArrayList<PrivateMethod> privateMethodList;
    private String className;
}
