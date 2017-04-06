package JavaToUML;

import java.util.ArrayList;

import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class MethodVisitor extends VoidVisitorAdapter<Object> {
	public ArrayList<String> Methods = new ArrayList<String>(); 
	
	public void visit(MethodDeclaration n, Object obj) {
		
		System.out.print("\n" +n.getNameAsString()+"\n");
		String name = n.getNameAsString();
		String methodname = name + "()";
		Methods.add(methodname);
		
	}
	
	public ArrayList<String> getMethods(){
		return Methods;
	}

}
