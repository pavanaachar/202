package JavaToUML;

import java.util.ArrayList;
import java.util.EnumSet;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class MethodVisitor extends VoidVisitorAdapter<Object> {
	
	public ArrayList<String> Methods = new ArrayList<String>(); 
	public ArrayList<String> types = new ArrayList<String>(); 

	@Override
	public void visit(MethodDeclaration n, Object obj) {
		
		super.visit(n, obj);

		//System.out.print("\n" +n.getNameAsString()+"\n");
		EnumSet<Modifier> mod = n.getModifiers();
		
		String name = "";
		switch(mod.toString()){
		case "[PRIVATE]": name+="-";
		break;						
		case "[PROTECTED]": name+="#";
		break;
		case "[PACKAGE]": name+="~";
		break;
		default: name += "+";
		}
		name += n.getNameAsString();
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
		
	
		Methods.add(name);

	}

	public ArrayList<String> getMethods(){
		return Methods;
	}
	public ArrayList<String> gettypess(){
		return types;
	}

}
