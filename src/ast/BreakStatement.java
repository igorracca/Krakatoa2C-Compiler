/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class BreakStatement extends Statement {

	public BreakStatement() {
		
	}
	
	@Override
	public void genC(PW pw) {
		pw.printlnIdent("break;");
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent("break;");
	}

}
