package JavaToUML;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class MethodVisitor extends VoidVisitorAdapter<Object> {
	
	public ArrayList<String> Methods = new ArrayList<String>(); 
	public ArrayList<String> types = new ArrayList<String>(); 
	private HashMap<String, String> varVisiblity;
	private ArrayList<String> fieldNames;
	
	MethodVisitor(ArrayList<String> fieldNames, HashMap<String, String> varVisiblity) {
		this.varVisiblity = varVisiblity;
		this.fieldNames = fieldNames;
	}
	
	private boolean UpdateVarVisiblity(String name) {
		String varName;
		boolean ret = false;
		
		if (name.startsWith("get")) {
			varName = name.substring(name.indexOf("get") + 3 , name.length());
		} else if (name.startsWith("set")) {
			varName = name.substring(name.indexOf("set") + 3 , name.length());			
		} else {
			return ret;
		}

		if (fieldNames == null) {
			return ret;
		}
		
		if (fieldNames.contains(varName)) {
			varVisiblity.put(varName, "public");			
			ret = true;
		} else {
			String temp = varName.substring(0, 1).toLowerCase() + varName.substring(1);
			if (fieldNames.contains(temp)) {
				varVisiblity.put(temp, "public");
				ret = true;
			}
		}
		
		return ret;
	}
	
	@Override
	public void visit(MethodDeclaration n, Object obj) {
		
		super.visit(n, obj);

		boolean getterOrSetter = false;
		
		String name = n.getNameAsString();
		getterOrSetter = UpdateVarVisiblity(name);
		
		EnumSet<Modifier> mod = n.getModifiers();
		
		String modifier = "";
		switch(mod.toString()){
		case "[PRIVATE]": modifier+="-";
		break;						
		case "[PROTECTED]": modifier+="#";
		break;
		case "[PACKAGE]": modifier+="~";
		break;
		default: modifier += "+";
		}
		name = modifier + name;
		
		int params = n.getParameters().size();
		if(params==0){
		name = name + "()";
		}
		else
		{
			name += "(";
			for(int i= 0;i<params;i++)
			{
				name += n.getParameters().get(i).getNameAsString() + ":";
				name += n.getParameters().get(i).getType();
				String param_type = n.getParameters().get(i).getType().toString();
				if(Parser.InterfaceNames.contains(param_type)){
					if(!types.contains(param_type)){
						types.add(param_type);
					}
					
				}
				if(i<params-1){
					name+=",";
				}
				
			}
			name = name + ")";
			
		}
		name += ":"+ n.getType();
		
		if (!getterOrSetter) {
			Methods.add(name);
		}

	}

	public ArrayList<String> getMethods(){
		return Methods;
	}
	public ArrayList<String> gettypes(){
		return types;
	}

}
