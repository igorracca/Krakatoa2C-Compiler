/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;


public abstract class MessageSend  extends Expr  {
	
	public abstract void genC(PW pw, boolean putParenthesis);
	public abstract void genKra(PW pw, boolean putParenthesis);
	
	public abstract Method getMethod();
}

