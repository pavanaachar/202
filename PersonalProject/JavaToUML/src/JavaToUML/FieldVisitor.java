package JavaToUML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FieldVisitor extends VoidVisitorAdapter<Object>  {

	public ArrayList<String> FieldName;
	public ArrayList<String> types;

	public static final String[] collections = {"Set","List","Collection","ArrayList","LinkedList","HashSet","queue","dequeue"};

	public FieldVisitor(){
		FieldName = new ArrayList<String>();
		types = new ArrayList<String>();
	}

	@Override
	public void visit(FieldDeclaration n,Object obj){
		super.visit(n, obj);
		if(n.getVariables().isEmpty()==false){
			NodeList<VariableDeclarator> fieldlist = n.getVariables();
			
			for(VariableDeclarator field: fieldlist){	
				
				String name = "";
				
				String type = field.getType().toString();
				for(int i=0;i<collections.length;i++){
					if(type.contains(collections[i])){
						types.add("*"+type);
						System.out.println("field contains :"+collections[i]);
						break;
					}
				}
				
				//check if getter or setter methods exist

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
					name = name+"-";
				}


				name = name+field.toString()+":"+field.getType().toString();
				FieldName.add(name);
				types.add(field.getType().toString());



			}

		}
		else{
			FieldName.add("");
		}
	}
	public ArrayList<String> getFieldName(){
		return FieldName;
	}

	public ArrayList<String> getFieldTypes(){
		return types;
	}
}
