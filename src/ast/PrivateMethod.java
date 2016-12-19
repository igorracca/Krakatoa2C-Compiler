/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

public class PrivateMethod extends Method {

	public PrivateMethod(String name, Type type ) {
		super(name, type);
	}

	@Override
	public void genC(PW pw, String className) {
		pw.printIdent(super.getType().getCName() + " _" + className + "_" + super.getName() + "( " + "_class_" + className + " *this");
		getParamList().genC(pw);
		pw.println(" ) {");
		pw.add();
			getStatementList().genC(pw);
		pw.sub();
		pw.printlnIdent("}");
	}
	
	@Override
	public void genKra(PW pw) {
		pw.printIdent("private " + super.getType().getCName() + " " + super.getName() + "(");
		getParamList().genKra(pw);
		pw.println(") {");
		pw.add();
			getStatementList().genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
	}

}
