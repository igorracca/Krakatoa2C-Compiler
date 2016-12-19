/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class NullStatement extends Statement {

	public NullStatement(){}
	
	@Override
	public void genC(PW pw) {
		pw.printlnIdent(";");
	}

	@Override
	public void genKra(PW pw) {
		pw.printlnIdent(";");
	}
}
