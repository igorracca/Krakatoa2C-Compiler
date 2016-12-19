/*
 * Nome: Igor Racca
 * RA: 511382
 */

package ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/*
 * Krakatoa Class
 */
public class KraClass extends Type {
	
   public KraClass( String name ) {
      super(name);
      instanceVariableList = new InstanceVariableList(name);
      publicMethodList = new PublicMethodList(name);
      privateMethodList = new PrivateMethodList(name);
   }
   
   @Override
   public String getKraName() {
	   return name;
   }
   
   public String getCName() {
      return ("_" + name);
   }
   
   public void setSuperClass(KraClass superclass) {
	   this.superClass = superclass;
   }
   
   public KraClass getSuperClass() {
	   return this.superClass;
   }
   
   public void setInstanceVariableList(InstanceVariableList instanceVariableList) {
	   this.instanceVariableList = instanceVariableList;
   }
   
   public InstanceVariableList getInstanceVariableList() {
	   return this.instanceVariableList;
   }
   
   public PublicMethodList getPublicMethodList() {
	   return publicMethodList;
   }
   
	public void setPublicMethodList(PublicMethodList publicMethodList) {
		   this.publicMethodList = publicMethodList;
	   }
	
	public PrivateMethodList getPrivateMethodList() {
		return privateMethodList;
	}
  
   public void setPrivateMethodList(PrivateMethodList privateMethodList) {
	   this.privateMethodList = privateMethodList;
   }
   
   public void addInstanceVariable(InstanceVariable v) {
	   instanceVariableList.addElement(v);
   }
   
   public void addPublicMethod(PublicMethod m) {
	   publicMethodList.addElement(m);
   }
   
   public void addPrivateMethod(PrivateMethod m) {
	   privateMethodList.addElement(m);
   }
   
   public boolean isClassOrSuperclass(String kName) {
	   if(kName == this.getName())
		   return true;
	   if(superClass == null)	
		   return false;
	   return superClass.isClassOrSuperclass(kName);
   }
   
   public boolean instanceVariableAlreadyExists(String InstanceVariable) {
	   ArrayList<InstanceVariable> a = instanceVariableList.getInstanceVariableList();
	   for(Variable v : a) {
		   if(v.getName().equals(InstanceVariable)) {
			   return true;
		   }
	   }
	   return false;
   }
   
   public boolean checkMethodInInstaceVariableList(String methodName) {
	   if(instanceVariableAlreadyExists(methodName)) {
		   return true;
	   }
	   return false;
   }
   
   public boolean findMethodDifferentSignatureSuperclass(Method method) {
	   
	   ArrayList<PublicMethod> puml = publicMethodList.getPublicMethodList();
	   for(Method m : puml) {
		   if(m.getName().equals(method.getName()))
			   if(!m.equals(method) || method.getType()!=m.getType()) {
				   return true;
			   }
	   }
	   
	   ArrayList<PrivateMethod> prml = privateMethodList.getPrivateMethodList();
	   for(Method m : prml) {
		   if(m.getName().equals(method.getName()))
			   if(!m.equals(method) || method.getType()!=m.getType()) {
				   return true;
			   }
	   }
	   
	   if(superClass == null)	   
		   return false;
	   else
		   return superClass.findMethodDifferentSignatureSuperclass(method);
   }
   
   public boolean checkMethodRedeclaration(Method method) {
   
	   ArrayList<PublicMethod> puml = publicMethodList.getPublicMethodList();
	   for(Method m : puml) {
		   if(m.equals(method)) {
			   return true;
		   }
	   }
	   
	   ArrayList<PrivateMethod> prml = privateMethodList.getPrivateMethodList();
	   for(Method m : prml) {
		   if(m.equals(method)) {
			   return true;
		   }
	   }
	   
	   return false;
   }
   
   public boolean checkInstanceVariableinMethodList(String InstanceVariable) {
	     
	   ArrayList<PublicMethod> pml = publicMethodList.getPublicMethodList();
	   for(Method m : pml) {
		   if(m.getName().equals(InstanceVariable)) {
			   return true;
		   }
	   }
	   
	   ArrayList<PrivateMethod> prml = privateMethodList.getPrivateMethodList();
	   for(Method m : prml) {
		   if(m.getName().equals(InstanceVariable)) {
			   return true;
		   }
	   }
	   
	   return false;
   }
   
   public InstanceVariable searchInstanceVariable(String instanceVariableName) {	   
	   for(InstanceVariable v : instanceVariableList.getInstanceVariableList()) {
		   if(v.getName().equals(instanceVariableName)) {
			   return v;
		   }
	   }  
	   if(superClass == null)	   
		   return null;
	   else
		   return superClass.searchInstanceVariable(instanceVariableName);
   }
   
   public Method searchMethod(String methodName) {
	   ArrayList<Method> ml = new ArrayList<Method>();
	   ml.addAll( publicMethodList.getPublicMethodList());
	   ml.addAll( privateMethodList.getPrivateMethodList());
	   
	   for(Method m : ml) {
		   if(m.getName().equals(methodName)) {
			   return m;
		   }
	   }  
	   
	   if(superClass == null)	   
		   return null;
	   else
		   return superClass.searchMethod(methodName);
   }
   
   public int findPublicMethodIndex(String methodName) {
	   int i=-1;
	   KraClass k = this;
	   ArrayList<methodNameAndClass> methodsNameList = new ArrayList<methodNameAndClass>();
	   while(k != null) {
		   ArrayList<PublicMethod> pml = new ArrayList<PublicMethod>();
		   pml = k.getPublicMethodList().getPublicMethodList();
		   ArrayList<methodNameAndClass> ml = new ArrayList<methodNameAndClass>();
		   
		   for(PublicMethod m : pml) {
			   ml.add(new methodNameAndClass(m.getName(), k.name));
		   }
		   methodsNameList.addAll(0, ml);
		   k = k.superClass;
	   }
	   
	   for(methodNameAndClass m: methodsNameList) {
		   i++;
		   if(m.methodName.equals(methodName)) {
			   break;
		   }
	   }
	   return i;
   }
   
   public int findPublicMethodIndex(String methodName, String className) {
	   int i=-1;
	   KraClass k = this;
	   ArrayList<methodNameAndClass> methodsNameList = new ArrayList<methodNameAndClass>();
	   while(k != null) {
		   ArrayList<PublicMethod> pml = new ArrayList<PublicMethod>();
		   pml = k.getPublicMethodList().getPublicMethodList();
		   ArrayList<methodNameAndClass> ml = new ArrayList<methodNameAndClass>();
		   
		   for(PublicMethod m : pml) {
			   ml.add(new methodNameAndClass(m.getName(), k.name));
		   }
		   methodsNameList.addAll(0, ml);
		   k = k.superClass;
	   }
	   
	   for(methodNameAndClass m: methodsNameList) {
		   i++;
		   if(m.methodName.equals(methodName) && m.className.equals(className)) {
			   break;
		   }
	   }
	   return i;
   }
   
   public String firstClassThatHaveMethod(String methodName) {
	   ArrayList<PublicMethod> pml = publicMethodList.getPublicMethod();
	   int pmlSize = pml.size();
	   
	   for(PublicMethod m : pml) {
			if(m.getName().equals(methodName)) {
				return this.name;
			}
		}
	   
	   return superClass.firstClassThatHaveMethod(methodName);
   }
   
   public void printInstanceVariablesList_C(PW pw) {
	   if(superClass != null) {
		  superClass.printInstanceVariablesList_C(pw);
	   }
	   
	   instanceVariableList.genC(pw);
   }
   
   public void printVetorDeFuncoes_C(PW pw) {
	   if(superClass != null) {
		  superClass.printVetorDeFuncoes_C(pw);
		  pw.println(", ");
	   }
	   
	   ArrayList<PublicMethod> pml = publicMethodList.getPublicMethod();
	   int pmlSize = pml.size();
	   
	   for(PublicMethod m : pml) {
			pw.printIdent("( void (*)() ) _" + name + "_" + m.getName());
			if(--pmlSize > 0) {
				pw.println(", ");
			}
		}
   }
   
   public void genC(PW pw) {

	   pw.println("typedef");
		pw.add();
			pw.printlnIdent("struct _St_" + name + " {");
			pw.add();
				pw.printlnIdent("Func *vt;");
				// variáveis de instancia
				printInstanceVariablesList_C(pw);
			pw.sub();
			pw.printlnIdent("} _class_" + name + ";");
		pw.sub();
		pw.println();
		pw.println("_class_" + name + " *new_" + name + "(void);");
		pw.println();
		//métodos
		publicMethodList.genC(pw);
		privateMethodList.genC(pw);
		//tabela de métodos públicos
		pw.println("Func VTclass_" + name + "[] = {");
		pw.add();
			printVetorDeFuncoes_C(pw);
			pw.println("");
		pw.sub();
		pw.println("};");
		pw.println();
		pw.println("_class_" + name + " *new_" + name + "()");
		pw.println("{");
			pw.add();
				pw.printlnIdent("_class_" + name + " *t;");
				pw.println();
				pw.printlnIdent("if ( (t = malloc(sizeof(_class_" + name + "))) != NULL )");
				pw.add();
					pw.printlnIdent("t->vt = VTclass_" + name + ";");
				pw.sub();
				pw.printlnIdent("return t;");
			pw.sub();
		pw.println("}");

   }
   
   public void genKra(PW pw) {
	   pw.printIdent("class " + getName());
	   if(superClass != null) {
		   pw.print(" extends " + superClass.getName());
	   }
       pw.println(" {");
	   pw.add();
	   instanceVariableList.genKra(pw);
	   publicMethodList.genKra(pw);
	   privateMethodList.genKra(pw);
	   pw.sub();
	   pw.printlnIdent("}");
   }
   
   private String name=super.getName();
   private KraClass superClass = null;
   private InstanceVariableList instanceVariableList;
   private PublicMethodList publicMethodList;
   private PrivateMethodList privateMethodList;
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos

   class methodNameAndClass {
	   methodNameAndClass(String methodName, String className) {
		   this.methodName = methodName;
		   this.className = className;
	   }
	   
	   private String methodName; 
	   private String className;      
   }
   
}
