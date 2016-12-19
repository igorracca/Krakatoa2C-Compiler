/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;


public class MessageSendToSelf extends MessageSend {
    
	public MessageSendToSelf (KraClass kraClass) {
		this.kraClass = kraClass;
	}
	
	public MessageSendToSelf (KraClass kraClass, Variable instanceVariable) {
		this.kraClass = kraClass;
		this.instanceVariable = instanceVariable;
	}
	
	public MessageSendToSelf (KraClass kraClass, Method method, ExprList exprList) {
		this.kraClass = kraClass;
		this.method = method;
		this.exprList = exprList;
	}
	
    public void setType(Type type) { 
    	method.setType(type);
    }
	
    public Type getType() { 
		if(method == null) {
			return instanceVariable.getType();
		}
        return method.getType();
    }
    
	@Override
	public Method getMethod() {
		return method;
	}
	
    public void genC( PW pw, boolean putParenthesis ) {		
		if(instanceVariable != null) {
			pw.print("this");
			pw.print("->");
			instanceVariable.genC(pw);		
		} else 
		if(method != null) {
			if(method instanceof PublicMethod) {
				pw.print("( (" + method.getType().getCName() + " (*)(_class" + kraClass.getCName() + " *");    	
				((PublicMethod) method).genCparamsType(pw);
				pw.print(")) this");
				pw.print("->vt[" + kraClass.findPublicMethodIndex(method.getName(), kraClass.getName()) + "] ) (");
				pw.print(" (_class" + kraClass.getCName() + " *) this");
				if(exprList != null) {
					pw.print(", ");
					exprList.genC(pw);
				}
				pw.print(")");
			} else
			if(method instanceof PrivateMethod) {			
				pw.print("_" + kraClass.getName() + "_" + method.getName() + "(this"); 
				if(exprList != null) {
					pw.print(", ");
					exprList.genC(pw);
				}
				pw.print(")");
			}		
		}
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.printIdent("this");
		if(instanceVariable != null) {
			pw.print(".");
			instanceVariable.genKra(pw);		
		}
		if(method != null) {
			pw.print("." + method.getName() + "(");
			if(exprList != null) {
				exprList.genKra(pw);
			}
			pw.print(")");
		}
		pw.println(";");
	}
    
	KraClass kraClass;
	Variable instanceVariable = null;
	Method method = null;
	ExprList exprList = null;

}