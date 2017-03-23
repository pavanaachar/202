package JavaToUML;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FieldVisitor extends VoidVisitorAdapter<Object>  {
	public void visit(FieldDeclaration n,Object obj){
		System.out.print("\n" + n.getVariables().toString()+"\n");
	}

}

