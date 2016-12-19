/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;
import java.util.Iterator;

public class PublicMethodList {

	public PublicMethodList(String className) {
    	this.className = className;
    	publicMethodList = new ArrayList<PublicMethod>();
    }

     public ArrayList<PublicMethod> getPublicMethodList() {
		return publicMethodList;
	}

	public void setPublicMethodList(ArrayList<PublicMethod> publicMethodList) {
		this.publicMethodList = publicMethodList;
	}

	public void addElement(PublicMethod pm) {
    	 publicMethodList.add(pm);
     }

     public Iterator<PublicMethod> elements() {
         return publicMethodList.iterator();
     }

     public ArrayList<PublicMethod> getPublicMethod() {
 		return publicMethodList;
 	}

 	public int getSize() {
         return publicMethodList.size();
     }
 	
    public void genC(PW pw) {
   	   for(PublicMethod pm : publicMethodList) {
 		   pm.genC(pw, className);
 		   pw.println();
 	   }
      }
 	
     public void genKra(PW pw) {
  	   for(PublicMethod pm : publicMethodList) {
		   pm.genKra(pw);
	   }
     }
	
    private ArrayList<PublicMethod> publicMethodList;
    private String className;
}
