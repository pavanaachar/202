package JavaToUML;

import java.util.ArrayList;
import java.util.EnumSet;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Object> {
	public ArrayList<FieldAttr> fieldAttrs;
	public ArrayList<String> Methods = new ArrayList<String>(); 
	public ArrayList<String> types = new ArrayList<String>(); 

	MethodVisitor(ArrayList<FieldAttr> fieldAttrs) {
		this.fieldAttrs = fieldAttrs;
	}

	private boolean UpdateVarVisiblity(EnumSet<Modifier> mod, String name) {
		String varName;
		boolean ret = false;

		for (Modifier m : mod) {
			if (m.toString().contains("PRIVATE") || m.toString().contains("PROTECTED")) {
				return false;
			}
		}

		if (name.startsWith("get")) {
			varName = name.substring(name.indexOf("get") + 3 , name.length());
		} else if (name.startsWith("set")) {
			varName = name.substring(name.indexOf("set") + 3 , name.length());			
		} else {
			return ret;
		}

		// Interfaces have no fields
		if (fieldAttrs == null) {
			return ret;
		}

		for (FieldAttr fAttr : fieldAttrs) {
			if (fAttr.name.equals(varName)) {
				fAttr.visiblity = "public";
				ret = true;
				break;
			}
		}
		if (ret != true) {
			for (FieldAttr fAttr : fieldAttrs) {
				if (fAttr.name.equals(varName.substring(0, 1).toLowerCase() + varName.substring(1))) {
					fAttr.visiblity = "public";
					ret = true;
					break;
				}
			}
		}

		return ret;		
	}

	@Override
	public void visit(MethodDeclaration n, Object obj) {
		super.visit(n, obj);

		boolean getterOrSetter = false;
		boolean isPublic = false;

		String name = n.getNameAsString();

		EnumSet<Modifier> mod = n.getModifiers();
		getterOrSetter = UpdateVarVisiblity(mod, name);

		String modifier = "";
		switch (mod.toString()) {
			case "[PRIVATE]": modifier+="-";
			break;						
			
			case "[PROTECTED]": modifier+="#";
			break;
			
			case "[PACKAGE]": modifier+="~";
			break;
			
			default: modifier += "+"; isPublic = true;
		}
		name = modifier + name;

		int params = n.getParameters().size();
		if (params==0) {
			name = name + "()";
		} else {
			name += "(";
			for(int i= 0;i<params;i++) {
				name += n.getParameters().get(i).getNameAsString() + ":";
				name += n.getParameters().get(i).getType();
				String param_type = n.getParameters().get(i).getType().toString();
				if(Parser.interfaceNames.contains(param_type)) {
					if(!types.contains(param_type)) {
						types.add(param_type);
					}
				}
				if(i<params-1) {
					name+=", ";
				}
			}
			name = name + ")";
		}
		
		name += ":"+ n.getType();

		if (!getterOrSetter && isPublic) {
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
