/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;

public class PublicMethod extends Method {

	public PublicMethod(String name, Type type ) {
		super(name, type);
	}

	// genC for method calls
	public void genCparamsType(PW pw) {
		ArrayList<Parameter> pl = getParamList().getParamList();
    	for(Parameter p : pl) {
    		pw.print(", " + p.getType().getCName());
    	}
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
		pw.printIdent("public " + super.getType().getKraName() + " " + super.getName() + "(");
		getParamList().genKra(pw);
		pw.println(") {");
		pw.add();
			getStatementList().genKra(pw);
		pw.sub();
		pw.printlnIdent("}");
	}

}
