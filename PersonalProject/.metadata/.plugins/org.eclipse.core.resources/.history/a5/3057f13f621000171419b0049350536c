package JavaToUML;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassOrInterfaceVisitor extends VoidVisitorAdapter<Object> {
	public void visit(ClassOrInterfaceDeclaration n, Object obj) {
		
		if(!n.isInterface()){
		System.out.print(" Class: "+n.getName()+"\n");
		}
		
		
		else
		{
			System.out.println("Interface: "+n.getName()+"\n");
		}
		

	}

}

