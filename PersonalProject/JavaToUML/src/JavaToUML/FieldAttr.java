package JavaToUML;

import java.util.ArrayList;

public class FieldAttr {
	String name; // Actual field name
	String type; // Base type name
	String typeClass; // "ArrayType", "ClassOrInterfaceType", "PrimitiveType"
	String elementType; // For ArrayType types
	String fullType;
	ArrayList<FieldAttr> argTypes; // arg types for collections
	String visiblity; // "private", "public", "protected"
}
