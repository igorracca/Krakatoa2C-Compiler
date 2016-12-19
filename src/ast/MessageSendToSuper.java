/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class MessageSendToSuper extends MessageSend { 

	public MessageSendToSuper (KraClass kraClass) {
		this.kraClass = kraClass;
	}
	
	public MessageSendToSuper (KraClass kraClass, Variable instanceVariable) {
		this.kraClass = kraClass;
		this.instanceVariable = instanceVariable;
	}
	
	public MessageSendToSuper (KraClass kraClass, Method method, ExprList exprList) {
		this.kraClass = kraClass;
		this.method = method;
		this.exprList = exprList;
	}
	
    public Type getType() { 
        return method.getType();
    }
      
	@Override
	public Method getMethod() {
		return method;
	}
    
    public void genC( PW pw, boolean putParenthesis ) {	
		pw.print("_" + kraClass.getSuperClass().firstClassThatHaveMethod(method.getName()) + "_" + method.getName() + "( (_class_" + kraClass.getSuperClass().getName() + " *) this"); 
		if(exprList != null) {
			pw.print(", ");
			exprList.genC(pw);
		}
		pw.print(")");	
    }

	@Override
	public void genKra(PW pw, boolean putParenthesis) {
		pw.printIdent("super");
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