package JavaToUML;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FieldVisitor extends VoidVisitorAdapter<Object>  {

	public ArrayList<FieldAttr> fieldAttrs;

	public FieldVisitor(){
		fieldAttrs = new ArrayList<FieldAttr>();
	}

	private void AddVarToFieldAttrs(FieldDeclaration n, VariableDeclarator field) {
		String typeClass = field.getType().getClass().toString();
		FieldAttr fAttr = new FieldAttr();
 
		fAttr.name = new String(field.toString());
		fAttr.fullType = new String(field.getType().toString());
		
		if (typeClass.contains("ArrayType")) {
			fAttr.type = new String(field.getType().toString());
			fAttr.typeClass = new String(typeClass);
			fAttr.elementType = new String(field.getType().getElementType().toString());

		} else if (typeClass.contains("ClassOrInterfaceType")) {
			fAttr.type = new String(((ClassOrInterfaceType)(field.getType())).getName().toString());
			fAttr.typeClass = new String(typeClass);

			Optional<NodeList<Type>> tList = ((ClassOrInterfaceType)(field.getType())).getTypeArguments();
			if (tList.isPresent()) {
				fAttr.argTypes = new ArrayList<FieldAttr>();
				for (Iterator<Type> iterator = tList.get().iterator(); iterator.hasNext();) {
					Type t = iterator.next();
					FieldAttr fAttrInt = new FieldAttr();
					fAttrInt.type = t.getElementType().toString();
					fAttrInt.typeClass = t.getElementType().getClass().getName();
					fAttrInt.fullType = t.getElementType().toString();
					
					// The fields fAttrInt.name, fAttrInt.argTypes, fAttrInt.elementType
					// are not used in this case.
					
					fAttr.argTypes.add(fAttrInt);
				}
			}
		} else if (typeClass.contains("PrimitiveType")) {
			fAttr.type = new String(field.getType().toString());
			fAttr.typeClass = new String(typeClass);
		} else {
			// We are not expecting or handling any other type 
			assert(false);
		}

		// Set visibility
		if (n.isPublic()) {
			fAttr.visiblity = new String("public");
		}
		else if (n.isProtected()) {
			fAttr.visiblity = new String("protected");
		}
		else {
			fAttr.visiblity = new String("private");
		}
		
		fieldAttrs.add(fAttr);
	}

	@Override
	public void visit(FieldDeclaration n,Object obj){
		super.visit(n, obj);

		if(n.getVariables().isEmpty()==false)
		{
			NodeList<VariableDeclarator> fieldlist = n.getVariables();
			for(VariableDeclarator field: fieldlist)
			{
				AddVarToFieldAttrs(n, field);			
			}
		}
	}

	public ArrayList<FieldAttr> getFieldAttrs(){
		return fieldAttrs;
	}
}
