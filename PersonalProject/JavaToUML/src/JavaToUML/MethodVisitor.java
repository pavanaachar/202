package JavaToUML;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Object> {
	public void visit(MethodDeclaration n, Object obj) {
		
		System.out.print("\n" +n.getBody()+"\n");

	}

}
