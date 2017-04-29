package JavaToUML;

import java.util.ArrayList;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConstructorVisitor extends VoidVisitorAdapter<Object> {
	public String constructor;
	public ArrayList<String> types = new ArrayList<String>(); 
	
	@Override
	public void visit(ConstructorDeclaration n,Object obj){
		super.visit(n, obj);
		
		String name = n.getNameAsString();
		NodeList<Parameter> params = n.getParameters();
		
		constructor = "+" + name + "(";
		for (int i = 0; i < params.size(); i++) {
			constructor += params.get(i).getNameAsString() + ": " + params.get(i).getType();
			String param_type = n.getParameters().get(i).getType().toString();
			if(Parser.interfaceNames.contains(param_type)) {
				if(!types.contains(param_type)) {
					types.add(param_type);
				}
			}
			if(i < params.size() - 1){
				constructor += ", ";
			}
		}
		constructor += ")";
	}
	
	public String getConstructor() {
		return constructor;
	}
	public ArrayList<String> gettypes(){
		return types;
	}
}
