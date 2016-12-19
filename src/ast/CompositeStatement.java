/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class CompositeStatement extends Statement {

	public CompositeStatement() {
		statementList = new StatementList();
	}
	
	public void addStatement(Statement s) {
		statementList.addElement(s);
	}
	
	public void addStatementList(StatementList statementList) {
		this.statementList = statementList;
	}
	
	@Override
	public void genC(PW pw) {
		statementList.genC(pw);
	}

	@Override
	public void genKra(PW pw) {
		statementList.genKra(pw);	
	}

	private StatementList statementList;
}
