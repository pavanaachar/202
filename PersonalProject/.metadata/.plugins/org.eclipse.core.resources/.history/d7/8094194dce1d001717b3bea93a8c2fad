package JavaToUML;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class RelationVisitor extends VoidVisitorAdapter<Object>  {
	
	public void visit(ClassOrInterfaceDeclaration n, Object obj) {
		System.out.print("\n" + n.getExtendedTypes().toString()+"\n");
	}

}
