package JavaToUML;

import java.util.ArrayList;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FieldVisitor extends VoidVisitorAdapter<Object>  {
	
	public ArrayList<String> FieldName;
	
	public FieldVisitor(){
		FieldName = null;
	}
	
	public void visit(FieldDeclaration n,Object obj){
		FieldName = new ArrayList<String>();
		if(n.getVariables().isEmpty()==false){
			
			FieldName.add(n.getVariables().get(0).toString());
			
			
			
		}
		else{
		FieldName.add("");
		}
	}
	
	public ArrayList<String> getFieldName(){
		return FieldName;
	}

}

