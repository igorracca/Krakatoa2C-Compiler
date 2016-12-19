/*
 * Nome: Igor Racca
 * RA: 511382
 */


package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {

		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		// Program ::= KraClass { KraClass }
		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		
		try {
			while ( lexer.token == Symbol.MOCall ) {
				metaobjectCallList.add(metaobjectCall());
			}
			kraClassList.add(classDec());
			while ( lexer.token == Symbol.CLASS )
				kraClassList.add(classDec());
			if ( lexer.token != Symbol.EOF ) {
				signalError.showError("End of file expected");
			}
		}
		catch( RuntimeException e) {
			// if there was an exception, there is a compilation signalError
		}
		
		Program program = new Program(kraClassList, metaobjectCallList, compilationErrorList);
		if(!containsProgram) {
			signalError.showError("Source code without a class 'Program'");
		}
		
		return program;
	}

	/**  parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     *     public void run() { } <br>
     * end <br>
     * </code>
     * 
	   
	 */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		String name = lexer.getMetaobjectName();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		if ( lexer.token == Symbol.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING ||
					lexer.token == Symbol.IDENT ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case IDENT:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Symbol.COMMA ) 
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Symbol.RIGHTPAR ) 
				signalError.showError("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if ( name.equals("nce") ) {
			if ( metaobjectParamList.size() != 0 )
				signalError.showError("Metaobject 'nce' does not take parameters");
		}
		else if ( name.equals("ce") ) {
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				signalError.showError("Metaobject 'ce' take three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  )
				signalError.showError("The first parameter of metaobject 'ce' should be an integer number");
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				signalError.showError("The second and third parameters of metaobject 'ce' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )  
				signalError.showError("The fourth parameter of metaobject 'ce' should be a literal string");
			
		}
			
		return new MetaobjectCall(name, metaobjectParamList);
	}

	private KraClass classDec() {
		// Note que os métodos desta classe não correspondem exatamente às
		// regras
		// da gramática. Este método classDec, por exemplo, implementa
		// a produção KraClass (veja abaixo) e partes de outras produções.

		/*
		 * KraClass ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
		 * MemberList ::= { Qualifier Member } 
		 * Member ::= InstVarDec | MethodDec
		 * InstVarDec ::= Type IdList ";" 
		 * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
		 * Qualifier ::= [ "static" ]  ( "private" | "public" )
		 */
		
		KraClass k = null;
		ArrayList<KraClass> kraClassList = new ArrayList<KraClass>();
				
		if ( lexer.token != Symbol.CLASS ) signalError.showError("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.IDENT )
			signalError.show(ErrorSignaller.ident_expected);
		
		String className = lexer.getStringValue();
		if(className.equals("Program")) {
			containsProgram = true;
		}
		k = new KraClass(className);
		setCurrentClass(k);
		symbolTable.putInGlobal(className, k);
		
		lexer.nextToken();
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show(ErrorSignaller.ident_expected);
			String superClassName = lexer.getStringValue();
			if(superClassName.equals(currentClass.getName())) {
				signalError.showError("Class '" + currentClass.getName() + "' is inheriting from itself");
			}
			
			KraClass superK = symbolTable.getInGlobal(superClassName);
			// semantic 
			if(superK == null) {
				signalError.showError("Superclass not defined");
			}
			k.setSuperClass(superK);
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.LEFTCURBRACKET )
			signalError.showError("'{' expected", true);
		lexer.nextToken();

		while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {

			Symbol qualifier;
			switch (lexer.token) {
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
			default:
				signalError.showError("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}
			String InstVarClassName = lexer.getStringValue();
			Type t = type();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			String name = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token == Symbol.LEFTPAR ) {
				Method m = null;
				// public
				if(qualifier == Symbol.PUBLIC) {
					if(name.equals("run") && currentClass.getName().equals("Program")) {
						containsRun = true;
					}
					m = (PublicMethod) methodDec(t, name, qualifier);
					checkMethod(m);
					currentClass.addPublicMethod((PublicMethod) m);
				}	
				// private
				if(qualifier == Symbol.PRIVATE) {
					if(name.equals("run")) {
						signalError.showError("Method 'run' of class 'Program' cannot be private");
					}
					m = (PrivateMethod) methodDec(t, name, qualifier);
					checkMethod(m);
					currentClass.addPrivateMethod((PrivateMethod) m);
				}
				symbolTable.removeLocalIdent();
			}
			else if ( qualifier != Symbol.PRIVATE )
				signalError.showError("Attempt to declare public instance variable '" + name + "'");
			else {
				// void function since it add the instance variables in the currentClass which is a instance variable of Compiler
				instanceVarDec(t, name, InstVarClassName);
			}
				
		}
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("'public', 'private' or \'}\' expected");
		
		if(currentClass.getName().equals("Program")) {
			Method m = currentClass.searchMethod("run");
			if(m == null) {
				signalError.showError("Method 'run' was not found in class 'Program'");
			}
		}
		
		lexer.nextToken();	
		
		
		return currentClass;
	}

	private void checkMethod(Method m) {
		if(currentClass.getSuperClass() != null) {
			if(currentClass.getSuperClass().findMethodDifferentSignatureSuperclass(m)) {
				signalError.showError("Method '" + m.getName() + "' of the subclass '" + currentClass.getName() + "' has a signature different from the same method of superclass '" + currentClass.getSuperClass().getName() + "'");
			}
		}
		if(currentClass.checkMethodInInstaceVariableList(m.getName())) {
			signalError.showError("Method '" + m.getName() + "' has name equal to an instance variable");
		}
		if(currentClass.checkMethodRedeclaration(m)) {
			signalError.showError("Method '" + m.getName() + "' is being redeclared");
		}
	}
	
	private void instanceVarDec(Type type, String name, String InstVarClassName) {
		// InstVarDec ::= [ "static" ] "private" Type IdList ";"
		InstanceVariable iv = null;
		
		// if type is KraclassType, v should be an object
		if(isType(type.getKraName())) {
			KraClass k = symbolTable.getInGlobal(InstVarClassName);		
			if(k == null) {
				signalError.showError("Undeclared Class " + InstVarClassName);
			};
			iv = new KraObject(name, type, k);
		// otherwise v should be a variable
		} else {
			iv = new InstanceVariable(name, type, currentClass.getCName());
		}	
		if(currentClass.instanceVariableAlreadyExists(name)) {
			signalError.showError("Variable '" + name + "' is being redeclared");
		}
		currentClass.addInstanceVariable(iv);
		
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			name = lexer.getStringValue();
			// if type is KraclassType, v should be an object
			if(isType(type.getKraName())) {
				KraClass k = symbolTable.getInGlobal(InstVarClassName);
				if(k == null) {
					signalError.showError("Undeclared Class " + InstVarClassName);
				}
				iv = new KraObject(name, type, k);
			// otherwise v should be a variable
			} else {
				iv = new InstanceVariable(name, type, currentClass.getName());
			}	
			if(currentClass.instanceVariableAlreadyExists(name)) {
				signalError.showError("Variable '" + name + "' is being redeclared");
			}
			currentClass.addInstanceVariable(iv);
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();

		
	}

	private Method methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{"
		 *                StatementList "}"
		 */
		Method m = null;
		
		if(qualifier == Symbol.PUBLIC) 
			m = new PublicMethod(name, type);
		if(qualifier == Symbol.PRIVATE)
			m = new PrivateMethod(name, type);
		
		setCurrentMethod(m);
		
		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ) {
			ParamList pl = formalParamDec();
			
			if(m.getName().equals("run") && pl.getSize() != 0) {
				signalError.showError("Method 'run' of class 'Program' cannot take parameters");
			}
			
			m.setParamList(pl);
		}
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");

		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.showError("'{' expected");

		if(m.getName().equals("run") && m.getType() != Type.voidType) {
			signalError.showError("Method 'run' of class 'Program' with a return value type different from 'void'");
		}
		
		lexer.nextToken();
		StatementList sl = statementList();
		m.setStatementList(sl);
		
		
		if(type != Type.voidType) {
			if(!sl.containsReturnStatement(type)) {
				signalError.showError("Missing 'return' statement in method '" + m.getName() + "'");
			}
		}
		
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.showError("} expected");

		symbolTable.removeLocalIdent();
		
		lexer.nextToken();

		return m;
	}

	private LocalDecStatement localDec() {
		// LocalDec ::= Type IdList ";"

		LocalDecStatement localDecStatement = new LocalDecStatement();
		
		String className = lexer.getStringValue();
		Type type = type();
		if ( lexer.token != Symbol.IDENT ) 
			signalError.showError("Identifier expected");
		
		String varName = lexer.getStringValue();
		Variable v = symbolTable.getInLocal(varName);
		if(v != null) {
			signalError.showError("Local variable " + varName + " has been already declared");
		}
		
		// if type is KraclassType, v should be an object
		if(isType(type.getKraName())) {
			KraClass k = symbolTable.getInGlobal(className);
			if(k == null) {
				signalError.showError("Undeclared Class " + className);
			}
			v = new KraObject(lexer.getStringValue(), type, k);
		// otherwise v should be a variable
		} else {
			v = new Variable(lexer.getStringValue(), type);
		}		
		// add the variable both in the list and in the local symbol table
		localDecStatement.addLocalVariable(v);
		symbolTable.putInLocal(v.getName(), v);	
		lexer.nextToken();
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			
			varName = lexer.getStringValue();
			v = symbolTable.getInLocal(varName);
			if(v != null) {
				signalError.showError("Local variable " + varName + " has been already declared");
			}
			// if type is KraclassType, v should be an object
			if(isType(type.getKraName())) {
				KraClass k = symbolTable.getInGlobal(className);
				if(k == null) {
					signalError.showError("Undeclared Class " + className);
				}
				v = new KraObject(lexer.getStringValue(), type, k);
			// otherwise v should be a variable
			} else {
				v = new Variable(lexer.getStringValue(), type);
			}		
			// add the variable both in the list and in the local symbol table
			localDecStatement.addLocalVariable(v);
			symbolTable.putInLocal(v.getName(), v);	
			
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.SEMICOLON ) signalError.showError("Missing ';'", true);
		lexer.nextToken();
		
		return localDecStatement;
	}

	private ParamList formalParamDec() {
		// FormalParamDec ::= ParamDec { "," ParamDec }
		
		ParamList pl = new ParamList();
		
		pl.addElement(paramDec());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			pl.addElement(paramDec());
		}
		
		return pl;
	}

	private Parameter paramDec() {
		// ParamDec ::= Type Id

		Type t = type();
		if ( lexer.token != Symbol.IDENT ) signalError.showError("Identifier expected");
		String name = lexer.getStringValue();		
		//semantic
		Parameter p = (Parameter) symbolTable.getInLocal(name);
		if(p != null) {
			signalError.showError("Duplicated parameter " + name);
		}
		p = new Parameter(name, t);
		symbolTable.putInLocal(name, p);
		
		lexer.nextToken();
		
		return p;
	}

	private Type type() {
		// Type ::= BasicType | Id
		Type result;

		switch (lexer.token) {
		case VOID:
			result = Type.voidType;
			break;
		case INT:
			result = Type.intType;
			break;
		case BOOLEAN:
			result = Type.booleanType;
			break;
		case STRING:
			result = Type.stringType;
			break;
		case IDENT:
			// # corrija: faça uma busca na TS para buscar a classe
			// IDENT deve ser uma classe.
			String name = lexer.getStringValue();
			if(!isType(name)) {
				signalError.showError("Identifier expected");
			}		
			result = new TypeKraClass(name);
			break;
		case NULL:
			result = Type.nullType;
			break;
		default:
			signalError.showError("Type expected");
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

	private CompositeStatement compositeStatement() {
		CompositeStatement cs = new CompositeStatement();
		
		lexer.nextToken();
		cs.addStatementList(statementList());
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("} expected");
		else
			lexer.nextToken();
		
		return cs;
	}

	private StatementList statementList() {
		// CompStatement ::= "{" { Statement } "}"
		StatementList sl = new StatementList();
		
		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
				&& tk != Symbol.ELSE)
			sl.addElement(statement());
		
		return sl;
	}

	private Statement statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 *                ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 *               ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */

		Statement s = null;
		
		switch (lexer.token) {
		case THIS:
		case IDENT:
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:	
			//s = new AssignStatemnt or LocalDecStatement
			s = assignExprLocalDec();
			break;
		case ASSERT:
			assertStatement();
			break;
		case RETURN:
			//s = new ReturnStatement
			s = returnStatement();
			break;
		case READ:
			//s = new ReadStatement
			s = readStatement();
			break;
		case WRITE:
			//s = new WriteStatement
			s = writeStatement();
			break;
		case WRITELN:
			//s = new WritelnStatement
			s = writelnStatement();
			break;
		case IF:
			//s = new IfStatement
			s = ifStatement();
			break;
		case BREAK:
			//s = new BreakStatement
			s = breakStatement();
			break;
		case DO:
			s = doWhileStatement();
			break;
		case WHILE:
			//s = new WhileStatement
			s = whileStatement();
			break;
		case SEMICOLON:
			//s = new NullStatement
			s = nullStatement();
			break;
		case LEFTCURBRACKET:
			//s = new CompositeStatement
			s = compositeStatement();
			break;
		default:
			signalError.showError("Statement expected");
		}
		
		return s;
	}

	private Statement assertStatement() {
		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		Expr e = expr();
		if ( e.getType() != Type.booleanType )
			signalError.showError("boolean expression expected");
		if ( lexer.token != Symbol.COMMA ) {
			this.signalError.showError("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Symbol.LITERALSTRING ) {
			this.signalError.showError("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Symbol.SEMICOLON )
			lexer.nextToken();
		
		return new StatementAssert(e, lineNumber, message);
	}

	/*
	 * retorne true se 'name' é uma classe declarada anteriormente. É necessário
	 * fazer uma busca na tabela de símbolos para isto.
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}

	/*
	 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec
	 */
	private AssignExprLocalDecStatement assignExprLocalDec() {

		AssignExprLocalDecStatement assignExprLocalDecStat = null;
		
		if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
				|| lexer.token == Symbol.STRING ||
				// token é uma classe declarada textualmente antes desta
				// instrução
				(lexer.token == Symbol.IDENT && isType(lexer.getStringValue())) ) {
			/*
			 * uma declaração de variável. 'lexer.token' é o tipo da variável
			 * 
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec 
			 * LocalDec ::= Type IdList ``;''
			 */
			assignExprLocalDecStat = new LocalDecStatement();
			assignExprLocalDecStat = localDec();
		}
		else {
			/*
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ]
			 */
			Expr left = expr(); 
			if(left instanceof MessageSend) {
				if(((MessageSend) left).getType() != Type.voidType && !(left instanceof MessageSendToSelf)) {
					signalError.showError("Message send '" + ((MessageSendToVariable) left).getKraObject().getName() + "." + ((MessageSendToVariable) left).getMethod().getName() + "()' returns a value that is not used");
				} 	
				if(!(left instanceof MessageSendToSelf)) {
					lexer.nextToken();
					return assignExprLocalDecStat = new MessageSendStatement((MessageSend) left);
				}
			}
			
			// get the name of the declared class to analyze the instantiation in Factor()
			if(isType(left.getType().getKraName())) {
				KraObject k = (KraObject) ((VariableExpr) left).getV();
				classDecName = k.getKraClass().getName();
			}
			
			if ( lexer.token == Symbol.ASSIGN ) {
				
				lexer.nextToken();
				Expr right = expr(); 
				
				if(right.getType() == Type.voidType) {
					signalError.showError("Expression expected in the right-hand side of assignment");
				}
				if(left.getType() != right.getType() && !right.getType().getKraName().equals("new") && !isType(left.getType().getKraName())) {
					signalError.showError("Type error: value of the right-hand side is not subtype of the variable of the left-hand side.");
				}

				left = new CompositeExpr(left, Symbol.ASSIGN, right);
				
				if ( lexer.token != Symbol.SEMICOLON )
					signalError.showError("';' expected", true);
				else
					lexer.nextToken();
			}
			
			if(left instanceof MessageSendToSelf) {
				lexer.nextToken();
				return assignExprLocalDecStat = new MessageSendStatement((MessageSendToSelf) left);
				
			}
			assignExprLocalDecStat = new AssignStatement(left);
		}
		return assignExprLocalDecStat;
	}

	private ExprList realParameters() {
		ExprList anExprList = null;

		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		if ( startExpr(lexer.token) ) anExprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		return anExprList;
	}

	private DoWhileStatement doWhileStatement() {
		DoWhileStatement dws = new DoWhileStatement();
		
		isInAWhileStatement = true;
		lexer.nextToken();
		dws.setWhileStatement(statement());
		if(!isInAWhileStatement) {
			signalError.showError("'break' statement found outside a 'while' statement");
		}
		isInAWhileStatement = false;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		Expr e = expr();
		
		if(e.getType() != Type.booleanType) {
			signalError.showError("boolean expression expected in a do-while statement");
		}
		
		dws.setExpr(e);
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
			
		return dws;
	}
	
	private WhileStatement whileStatement() {
		WhileStatement ws = null;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		Expr e = expr();
		
		if(e.getType() != Type.booleanType) {
			signalError.showError("non-boolean expression in 'while' command");
		}
		
		ws = new WhileStatement(e);
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		
		isInAWhileStatement = true;
		ws.setWhileStatement(statement());
		if(!isInAWhileStatement) {
			signalError.showError("'break' statement found outside a 'while' statement");
		}
		isInAWhileStatement = false;
		
		return ws;
	}

	private IfStatement ifStatement() {
		IfStatement ifStatement = null;
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		ifStatement = new IfStatement(expr());
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		ifStatement.setIfStatement(statement());
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			ifStatement.setElseStatement(statement());
		}
		
		return ifStatement;
	}

	private ReturnStatement returnStatement() {	
		lexer.nextToken();
		Expr e = expr();
		ReturnStatement r = new ReturnStatement(e);
		
		if(currentMethod.getType().getCName().equals(Symbol.VOID.toString() )) 
			signalError.showError("Illegal 'return' statement. Method returns 'void'");
		
		if(currentMethod.getType() != e.getType())
			signalError.showError("Return type is different from method return type");
			
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return r;
	}

	private ReadStatement readStatement() {
		ReadStatement rs = new ReadStatement();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("'(' expected after 'read' command");
		lexer.nextToken();
		while (true) {
			if ( lexer.token == Symbol.THIS ) {
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) signalError.showError(". expected");
				lexer.nextToken();
			}
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Command 'read' expects a variable");

			String name = lexer.getStringValue();
			Variable v = symbolTable.getInLocal(name);
			if(v == null) {
				signalError.showError("Variable '" + name + "' was not declared");
			}
			if(v.getType() != Type.stringType && v.getType() != Type.intType) {
				signalError.showError("'int' or 'String' expression expected");
			}
			VariableExpr ve = new VariableExpr(v);
			if(ve == null) {
				signalError.showError("Command 'read' without arguments");
			
			}
			if(ve.getType() == Type.booleanType) {
				signalError.showError("Command 'read' does not accept 'boolean' variables");
			
			}
			rs.addExpr(ve);
			
			lexer.nextToken();
			if ( lexer.token == Symbol.COMMA )
				lexer.nextToken();
			else
				break;
		}

		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return rs;
	}

	private WriteStatement writeStatement() {
		WriteStatement ws = new WriteStatement();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();

		ExprList exprList = exprList();
		if(exprList == null) {
			signalError.showError("Command 'write' without arguments");
		}
		for(Variable p : exprList.getParametersType()) {
			if(p.getType() == Type.booleanType) {
				signalError.showError("Command 'write' does not accept 'boolean' expressions");
			}
			if(p instanceof KraObject) {
				signalError.showError("Command 'write' does not accept objects");
			}
		}
		ws.setExprList(exprList);
		
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return ws;
	}

	private WritelnStatement writelnStatement() {
		WritelnStatement wls = new WritelnStatement();
		
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		ExprList exprList = exprList();
		if(exprList == null) {
			signalError.showError("Command 'write' without arguments");
		}
		for(Parameter p : exprList.getParametersType()) {
			if(p.getType() == Type.booleanType) {
				signalError.showError("Command 'write' does not accept 'boolean' expressions");
			}
		}
		wls.setExprList(exprList);
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return wls;
	}

	private BreakStatement breakStatement() {
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
		
		return new BreakStatement();
	}

	private NullStatement nullStatement() {
		lexer.nextToken();
		return new NullStatement();
	}

	private ExprList exprList() {
		// ExpressionList ::= Expression { "," Expression }

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
	}

	private Expr expr() {

		Expr left = simpleExpr();
		
		Symbol op = lexer.token;
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			
			lexer.nextToken();
			Expr right = simpleExpr();

			
			if((op == Symbol.EQ || op == Symbol.NEQ) && (left.getType() != right.getType())) {
				signalError.showError("Incompatible types cannot be compared with '" + op + "' because the result will always be 'false'");
			}
			
			left = new CompositeExpr(left, op, right);
		}
		return left;
		
	}

	private Expr simpleExpr() {
		Symbol op;

		Expr left = term();

		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			lexer.nextToken();
			Expr right = term();
			if(left.getType() == right.getType() && right.getType() == Type.booleanType && (op.equals("+") || op.equals("-") )) {
				signalError.showError("type boolean does not support operation '" + op + "'");
			}
			
			if(left.getType() == Type.intType && right.getType() == Type.booleanType) {
				signalError.showError("operator '" + op + "' of 'int' expects an 'int' value");
			}
			
			left = new CompositeExpr(left, op, right);
			
		}
		return left;
	}

	private Expr term() {
		Symbol op;

		Expr left = signalFactor();
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
			lexer.nextToken();
			Expr right = signalFactor();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr signalFactor() {
		Symbol op;
		if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
			lexer.nextToken();
			Expr f = factor();
			if(f.getType() == Type.booleanType) {
				signalError.showError("Operator '" + op + "' does not accepts 'boolean' expressions");
			}
			return new SignalExpr(op, f);
		}
		else
			return factor();
	}

	/*
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 *      ObjectCreation | PrimaryExpr
	 * 
	 * BasicValue ::= IntValue | BooleanValue | StringValue 
	 * BooleanValue ::=  "true" | "false" 
	 * ObjectCreation ::= "new" Id "(" ")" 
	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 Id  |
	 *                 Id "." Id | 
	 *                 Id "." Id "(" [ ExpressionList ] ")" |
	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
	 *                 "this" | 
	 *                 "this" "." Id | 
	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {

		Expr anExpr;
		ExprList exprList;
		String messageName, id;
		

		switch (lexer.token) {
		// IntValue
		case LITERALINT:
			return literalInt();
			// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
			// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
			// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
			// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			anExpr = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(anExpr);

			// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
			// "!" Factor
		case NOT:
			lexer.nextToken();
			anExpr = expr();
			if(anExpr.getType() != Type.booleanType) {
				signalError.showError("Operator '!' does not accepts '" + anExpr.getType().getKraName() + "' values");
			}
			return new UnaryExpr(anExpr, Symbol.NOT);
			// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Class expected");

			String className = lexer.getStringValue();
			/*
			 * // encontre a classe className in symbol table KraClass 
			 *      aClass = symbolTable.getInGlobal(className); 
			 *      if ( aClass == null ) ...
			 */
			NewObjectExpr newObjExpr = null;
			KraClass k = symbolTable.getInGlobal(className); 
			
			if(k == null) {
				signalError.showError("Class '" + className + "' was not found");
			}		
			if(k.isClassOrSuperclass(classDecName)) {
				newObjExpr = new NewObjectExpr(k);
			} else {
				signalError.showError("Type error: type of the expression returned is not subclass of the method return type");
			}

			lexer.nextToken();
			if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
			lexer.nextToken();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			return newObjExpr;
			/*
          	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
		case SUPER:
			// "super" "." Id "(" [ ExpressionList ] ")"
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				signalError.showError("'.' expected");
			}
			else
				lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			
			/*
			 * para fazer as conferências semânticas, procure por 'messageName'
			 * na superclasse/superclasse da superclasse etc
			 */
			if(currentClass.getName().equals("Program")) {
				signalError.showError("'super' used in class 'Program' that does not have a superclass");
			}
			
			messageName = lexer.getStringValue();
			KraClass superclass = currentClass.getSuperClass();
			Method superMethod = superclass.searchMethod(messageName);
			if(superMethod == null) {
				signalError.showError("Method '" + messageName + "' was not found in class '" + superclass.getName() + "' or its superclasses");				
			}
			
			lexer.nextToken();
			exprList = realParameters();
			
			if( (exprList == null && !superMethod.noParams()) || (exprList != null && superMethod.noParams()) ) {
				signalError.showError("Parameter type mismatch");
			}
			
			if(exprList != null && !superMethod.compareParams(exprList)) {
				signalError.showError("Parameter type mismatch");
			}
			
			MessageSendToSuper messageSendToSuper = new MessageSendToSuper(currentClass, superMethod, exprList);
			
			return messageSendToSuper;
		case IDENT:
			/*
          	 * PrimaryExpr ::=  
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
			 */

			String firstId = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// Id
				// retorne um objeto da ASA que representa um identificador
				
				// procura pelo identificador na tabela de simbolos
				Variable v = symbolTable.getInLocal(firstId);	
				
				if ( v == null ) {
					signalError.showError("Variable '" + firstId + "' was not declared");	
				}		
				
				// if V is a parameter, create a Variable class to it
				if ( v instanceof Parameter) {
					Variable var = new Variable(v.getName(), v.getType());
					VariableExpr varExpr = new VariableExpr(var);
					return varExpr;
				}
				
				VariableExpr varExpr = new VariableExpr(v);
				return varExpr;
			}
			else { // Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.showError("Identifier expected");
				}
				else {
					// Id "." Id
					lexer.nextToken();
					id = lexer.getStringValue();
					
					Variable v = symbolTable.getInLocal(firstId);
					if(!isType(v.getType().getName())) {
						signalError.showError("Message send to a non-object receiver");
					}
					
					if ( lexer.token == Symbol.DOT ) {
						// Id "." Id "." Id "(" [ ExpressionList ] ")"
						/*
						 * se o compilador permite variáveis estáticas, é possível
						 * ter esta opção, como
						 *     Clock.currentDay.setDay(12);
						 * Contudo, se variáveis estáticas não estiver nas especificações,
						 * sinalize um erro neste ponto.
						 */signalError.showError("The compiler does not support static variables.");
						
						lexer.nextToken();
						if ( lexer.token != Symbol.IDENT )
							signalError.showError("Identifier expected");
						messageName = lexer.getStringValue();
						lexer.nextToken();
						exprList = this.realParameters();

					}
					else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
						
						String methodName = lexer.getStringValue();
						KraObject kraObj = (KraObject) symbolTable.getInLocal(firstId);				
						
						Method method = kraObj.getKraClass().searchMethod(methodName);
						if(method == null) {
							signalError.showError("Method '" + methodName + "' was not found in class '" + kraObj.getKraClass().getName() + "' or its superclasses");				
						}
						
						exprList = this.realParameters();
							
						if( (exprList == null && !method.noParams()) || (exprList != null && method.noParams()) ) {
							signalError.showError("Parameter type mismatch");
						}
						
						if(exprList != null && !method.compareParams(exprList)) {
							signalError.showError("Parameter type mismatch");
						}
						
						MessageSendToVariable messageSendToVariable = new MessageSendToVariable(kraObj, method, exprList);
						
						return messageSendToVariable;
						
						/*
						 * para fazer as conferências semânticas, procure por
						 * método 'ident' na classe de 'firstId'
						 */
					}
					else {
						// retorne o objeto da ASA que representa Id "." Id
					}
				}
			}
			break;
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: 
          	 * PrimaryExpr ::= 
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// only 'this'
				// retorne um objeto da ASA que representa 'this'
				// confira se não estamos em um método estático
				MessageSendToSelf messageSend = new MessageSendToSelf(currentClass);
				return messageSend;
			}
			else {
				lexer.nextToken();
				if ( lexer.token != Symbol.IDENT )
					signalError.showError("Identifier expected");
				id = lexer.getStringValue();
				lexer.nextToken();
				// já analisou "this" "." Id
				if ( lexer.token == Symbol.LEFTPAR ) {
					// "this" "." Id "(" [ ExpressionList ] ")"
					/*
					 * Confira se a classe corrente possui um método cujo nome é
					 * 'ident' e que pode tomar os parâmetros de ExpressionList
					 */
					String methodName = lexer.getStringValue();
					Method method = currentClass.searchMethod(methodName);
					if(method == null) {
						signalError.showError("Method '" + methodName + "' was not found in class '" + currentClass.getName() + "' or its superclasses");				
					}
								
					exprList = this.realParameters();

					if( (exprList == null && !method.noParams()) || (exprList != null && method.noParams()) ) {
						signalError.showError("Parameter type mismatch");
					}
					
					if(exprList != null && !method.compareParams(exprList)) {
						signalError.showError("Parameter type mismatch");
					}
					
					MessageSendToSelf messageSendToSelf = new MessageSendToSelf(currentClass, method, exprList);
					
					return messageSendToSelf;
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					lexer.nextToken();
					if ( lexer.token != Symbol.IDENT )
						signalError.showError("Identifier expected");
					lexer.nextToken();
					exprList = this.realParameters();
				}
				else {
					// retorne o objeto da ASA que representa "this" "." Id
					/*
					 * confira se a classe corrente realmente possui uma
					 * variável de instância 'ident'
					 */
					String name = lexer.getStringValue();
					
					InstanceVariable instanceVariable = currentClass.searchInstanceVariable(name);
					if(instanceVariable == null) {
						signalError.showError(currentClass.getName() + " does not contain instance variable " + name);				
					}
					
					MessageSendToSelf messageSendToSelf = new MessageSendToSelf(currentClass, instanceVariable);
					return messageSendToSelf;
				}
			}
			break;
		default:
			signalError.showError("Expression expected");
		}
		return null;
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE
				|| token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.LITERALINT || token == Symbol.SUPER
				|| token == Symbol.LEFTPAR || token == Symbol.NULL
				|| token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}
	
	public KraClass getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(KraClass currentClass) {
		this.currentClass = currentClass;
	}

	public Method getCurrentMethod() {
		return currentMethod;
	}

	public void setCurrentMethod(Method currentMethod) {
		this.currentMethod = currentMethod;
	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	
	private KraClass currentClass;
	private Method currentMethod;
	private String classDecName;
	private boolean containsProgram = false;
	private boolean containsRun = false;
	private boolean isInAWhileStatement;

}
