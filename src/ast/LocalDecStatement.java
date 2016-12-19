/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class LocalDecStatement extends AssignExprLocalDecStatement {

	public LocalDecStatement() {
		localDecStatement = new LocalVariableList();
	}
	
	public void addLocalVariable(Variable v) {
		this.localDecStatement.addElement(v);
	}
	
	public void setLocalVariableList(LocalVariableList localDecStatement) {
		this.localDecStatement = localDecStatement;
	}
	
	@Override
	public void genC(PW pw) {
		localDecStatement.genC(pw);
	}

	@Override
	public void genKra(PW pw) {
		localDecStatement.genKra(pw);
	}
	
	private LocalVariableList localDecStatement;
}
