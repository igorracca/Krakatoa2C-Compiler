/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;


public class MessageSendToVariable extends MessageSend { 

    public MessageSendToVariable(KraObject KraObject, Method method, ExprList exprList) {
    	this.kraObject = KraObject;
    	this.method = method;
    	this.exprList = exprList;
	}

	public Type getType() { 
        return method.getType();
    }
    
    public KraObject getKraObject() {
		return kraObject;
	}

	public void setKraObject(KraObject kraObject) {
		kraObject = kraObject;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public ExprList getExprList() {
		return exprList;
	}

	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}
	
	@Override
	public Method getMethod() {
		return method;
	}
	
    // a.put(5);
    // ( (void (*)(_class_A *, int)) _a->vt[1] )(_a, 5); 
    
    // k = a.get();
    // k = ( (int (*)(_class_A *)) _a->vt[0] )(_a); 
	public void genC( PW pw, boolean putParenthesis ) {
		if(method instanceof PublicMethod) {
			pw.print("( (" + method.getType().getCName() + " (*)(_class" + kraObject.getKraClass().getCName() + " *");    	
			((PublicMethod) method).genCparamsType(pw);
			pw.print(")) ");
			kraObject.genC(pw);
			pw.print("->vt[" + kraObject.getKraClass().findPublicMethodIndex(method.getName(), kraObject.getKraClass().getName()) + "] ) (");
			kraObject.genC(pw);		
			if(exprList != null) {
				pw.print(", ");
				exprList.genC(pw);
			}
			pw.print(")");
		} else
		if(method instanceof PrivateMethod) {			
			pw.print("_" + kraObject.getKraClass().getName() + "_" + method.getName() + "(_" + kraObject.getName()); 
			if(exprList != null) {
				pw.print(", ");
				exprList.genC(pw);
			}
			pw.print(")");
		}	
    	
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.printIdent(kraObject.getName());
		if(method != null) {
			pw.print("." + method.getName() + "(");
			if(exprList != null) {
				exprList.genKra(pw);
			}
			pw.print(")");
		}
		pw.println(";");
	}
    
	private KraObject kraObject; 
	private Method method; 
	private ExprList exprList;
}    