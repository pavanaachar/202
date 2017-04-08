package JavaToUML;

import java.util.ArrayList;
import java.util.Iterator;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FieldVisitor extends VoidVisitorAdapter<Object>  {

	public ArrayList<String> FieldName;

	public FieldVisitor(){
		FieldName = null;

	}
	public void visit(FieldDeclaration n,Object obj){
		FieldName = new ArrayList<String>();
		if(n.getVariables().isEmpty()==false){
			NodeList<VariableDeclarator> fieldlist = n.getVariables();
			String name = "";
			for(VariableDeclarator field: fieldlist){	
				if(n.isPublic()){
					name = name+"+";
				}
				else if(n.isProtected()){
					name = name+"#";
				}
				else if(n.isPrivate()){
					name = name+"-";
				}
				else{
					name = name+"+";
				}
				name = name+field.toString();
				FieldName.add(name);
				System.out.println(name);
			}

		}
		else{
			FieldName.add("");
		}
	}
	public ArrayList<String> getFieldName(){
		return FieldName;
	}
}
