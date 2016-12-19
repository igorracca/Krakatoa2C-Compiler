/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;

public abstract class Method {
	
	public Method(String name, Type type) {
		this.name = name;
        this.type = type;
		pl = new ParamList();
		sl = new StatementList();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ParamList getParamList() {
		return pl;
	}

	public void setParamList(ParamList pl) {
		this.pl = pl;
	}
	
	public void setStatementList(StatementList sl) {
		this.sl = sl;
	}
	
	public StatementList getStatementList() {
		return sl;
	}
	
	public boolean noParams() {
		ArrayList<Parameter> pl = this.pl.getParamList();
		return (pl.size() == 0);
	}
	
	public boolean compareParams(ExprList exprList) {
		ArrayList<Parameter> pl = exprList.getParametersType();
		return (pl.equals(this.pl.getParamList()));
	}
	
	@Override public boolean equals(Object o) {
       return ((o instanceof Method) &&
        		(this.getName().equals(((Method) o).getName())) &&
        		(this.pl.equals(((Method) o).pl)));
    }
	
	abstract public void genC(PW pw, String className);
	abstract public void genKra(PW pw);

	private String name;
    private Type type;
	private ParamList pl;
	private StatementList sl;
	
}
