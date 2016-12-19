/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;

public class WriteStatement extends Statement {

	public WriteStatement() {
		exprList = new ExprList();
	}
	
	public void addExpr(Expr e) {
		exprList.addElement(e);
	}
	
	public void setExprList(ExprList exprList) {
		this.exprList = exprList;
	}
	
	@Override
	public void genC(PW pw) {
		ArrayList<Expr> exprArrayList = exprList.getExprList();
        int size = exprArrayList.size();     
        // just a literal string or one String var
        if(size == 1 && exprArrayList.get(0).getType() == Type.stringType) {  	
        	if(exprArrayList.get(0) instanceof LiteralString) {
            	pw.printIdent("puts(");
            	exprArrayList.get(0).genC(pw, false);
            	pw.println(");");
        	} else {
        		Variable v = (Variable) ((VariableExpr)exprArrayList.get(0)).getV();
        		
            	pw.printIdent("printf(\"%s\", ");
            	v.genC(pw);
            	pw.println(");");
        	}
        // one or more int and/or String variables
        } else {
      	
        	pw.printIdent("printf(\"");
        	for ( Expr e : exprArrayList ) {
        		
        		if(e.getType() == Type.intType) {
        			pw.print("%d ");
        		} else
    			if(e.getType() == Type.stringType) {
        			pw.print("%s ");
        		}

        	}
        	 pw.print("\", ");
        	 size = exprArrayList.size();   
        	 for ( Expr e : exprArrayList ) {
//        		 Variable v = (Variable) ((VariableExpr)e).getV();
        		 e.genC(pw, false); 
       
				if ( --size > 0 )
			        pw.print(", ");
    	 	}  	
            pw.println(");");
        }
	}

	@Override
	public void genKra(PW pw) {
		pw.printIdent("write(");
		exprList.genKra(pw);
		pw.println(");");
	}

	private ExprList exprList;
}

