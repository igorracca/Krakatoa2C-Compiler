/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

abstract public class AssignExprLocalDecStatement extends Statement {

	abstract public void genC(PW pw);
	abstract public void genKra(PW pw);
	
}
