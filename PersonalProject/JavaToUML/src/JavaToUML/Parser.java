package JavaToUML;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Parser {

	private static ArrayList<File> JavaFiles = new ArrayList<File>();

	ArrayList<CompilationUnit> compilationunits = new ArrayList<CompilationUnit>();

	public static ArrayList<String> ClassNames = new ArrayList<String>();

	public static ArrayList<String> InterfaceNames = new ArrayList<String>();

	private static ArrayList<String> UMLsource = new ArrayList<String>();

	HashMap<String, ArrayList<String>> ClassFieldsMap = new HashMap<String,ArrayList<String>>();

	HashMap<String, ArrayList<String>> ClassImplementsMap = new HashMap<String,ArrayList<String>>();

	HashMap<String, ArrayList<String>> InterfaceImplementsMap = new HashMap<String,ArrayList<String>>();

	HashMap<String, String> ClassExtendsMap = new HashMap<String,String>();
	
	HashMap<String, ArrayList<String>> ClassDependencyMap = new HashMap<String,ArrayList<String>>();

	// Track edges from Class to Class or interface
	class Edge {
		public String from;
		public String to;
		public int fromWeight;
		public int toWeight;


		Edge(String from, String to) {
			this.from = from;
			this.to = to;
			this.fromWeight = -1;
			this.toWeight = -1;
		}

		@Override
		public boolean equals(Object obj) {
			Edge e = (Edge)obj;

			if (obj == null) {
				return false;
			}

			if ((this.from.equals(e.from) && 
					this.to.equals(e.to)) ||
					(this.from.equals(e.to) &&
							this.to.equals(e.from))) {
				return true;
			}
			return false;
		}
	}

	public Parser(ArrayList<File> files){
		JavaFiles = files;
	}

	public ArrayList<String> parser() throws IOException {		


		HashMap<String, ArrayList<ObjCount>> classVarMap = new HashMap<String, ArrayList<ObjCount>>();

		ArrayList<Edge> edges = new ArrayList<Edge>();


		UMLsource.add("@startuml");

		for(int i=0; i<JavaFiles.size();i++){
			File file = new File(JavaFiles.get(i).getAbsolutePath());
			FileInputStream file_in = null;
			CompilationUnit compile_unit = null;
			try{
				file_in = new FileInputStream(file);
				compile_unit = JavaParser.parse(file_in);				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				file_in.close();
			}

			compilationunits.add(compile_unit);

			ClassOrInterfaceVisitor class_interface_visitor = new ClassOrInterfaceVisitor();

			class_interface_visitor.visit(compile_unit,null);

			if(class_interface_visitor.IsClass()){
				String classname = class_interface_visitor.getClassName();
				ClassNames.add(classname);

			}

			else if(class_interface_visitor.IsInterface()){
				String interfacename = class_interface_visitor.getInterfaceName();
				InterfaceNames.add(interfacename);

			}



		}
		ClassImplementsMap = ClassOrInterfaceVisitor.getClassImplementsMap();

		ClassExtendsMap = ClassOrInterfaceVisitor.getClassExtendsMap();

		InterfaceImplementsMap = ClassOrInterfaceVisitor.getInterfaceImplementsMap();


		for(int i =0;i<compilationunits.size();i++){
			CompilationUnit cu = compilationunits.get(i);
			ClassOrInterfaceVisitor class_interface_visitor = new ClassOrInterfaceVisitor();
			
			boolean isclass = false;
			boolean isInterface = false;
			
			String classname = "";
			String interfacename = "";
			

			class_interface_visitor.visit(cu,null);

			if(class_interface_visitor.IsClass()){
				
				isclass = true;
				
				classname = class_interface_visitor.getClassName();

				UMLsource.add("class "+classname+"{");

				FieldVisitor fieldvisitor = new FieldVisitor();
				fieldvisitor.visit(cu,null);
				ArrayList<String> fields = fieldvisitor.getFieldName();
				if(fields!=null){
					UMLsource.addAll(fieldvisitor.getFieldName());
				}
				ArrayList<String> Fieldtypes = fieldvisitor.getFieldTypes();
				ClassFieldsMap.put(classname, Fieldtypes);

				ArrayList<ObjCount> objCountList = fieldvisitor.getFieldObjCountList();
				classVarMap.put(classname, objCountList);
			}

			else if(class_interface_visitor.IsInterface()){
				isInterface = true;
				interfacename = class_interface_visitor.getInterfaceName();
				// InterfaceNames.add(interfacename);
				UMLsource.add("interface "+interfacename+"{");
			}


			MethodVisitor methodvisitor = new MethodVisitor();
			methodvisitor.visit(cu,null);
			
			UMLsource.addAll(methodvisitor.getMethods());
			if(isclass){
				ClassDependencyMap.put(classname, methodvisitor.gettypess());
			}

			UMLsource.add("}");

		}


		// Generate plantuml grammar for association between classes with cardinality


		for (Iterator<String> iterator = classVarMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			//System.out.println("Class: " + key);

			for (Iterator<ObjCount> iterator2 = classVarMap.get(key).iterator(); iterator2.hasNext();) {
				ObjCount o = iterator2.next();
				//System.out.println(" " + o.className);

				Edge tempEdge = new Edge(key, o.className);
				//System.out.println("Adding Edge");
				if (!edges.contains(tempEdge)) {
					tempEdge.fromWeight = o.count;
					edges.add(tempEdge);
				} else {
					Edge e = edges.get(edges.indexOf(tempEdge));
					if (e.from.equals(key)) {
						// We should never encounter the same classX -> classY mapping twice
						assert(false);
					} else {
						assert(e.to.equals(key));
						assert(e.fromWeight != -1 && e.toWeight == -1);
						e.toWeight = o.count;
					}
				}
			}
		}

		for (Edge e : edges) {
			String fromWeightStr = null;
			String toWeightStr = null;

			if (e.fromWeight >= ObjCount.MAX_OBJ_COUNT) {
				fromWeightStr = "\"*\"";
			} else if (e.fromWeight > -1) {
				fromWeightStr = "\"" + Integer.toString(e.fromWeight) + "\"";
			} else  {
				fromWeightStr = "";
			}

			if (e.toWeight >= ObjCount.MAX_OBJ_COUNT) {
				toWeightStr = "\"*\"";
			} else if (e.toWeight > -1) {
				toWeightStr = "\"" + Integer.toString(e.toWeight) + "\"";
			} else {
				toWeightStr = "";
			}

			if (InterfaceNames.contains(e.to)) {
				UMLsource.add("class " + e.from + " " + toWeightStr + " -- " +  fromWeightStr + " " + "interface " + e.to);
			} else {
				UMLsource.add("class " + e.from + " " + toWeightStr + " -- " +  fromWeightStr+ " " + "class " + e.to);
			}
		}
		for (Entry<String, ArrayList<String>> entry : ClassDependencyMap.entrySet()) {
			for(String name:entry.getValue()){
				UMLsource.add("class "+entry.getKey()+"\"uses\""+".."+"interface "+name);
				//UMLsource.add(+);

			}

		}

		for (Entry<String, ArrayList<String>> entry : ClassImplementsMap.entrySet()) {
			for(String name:entry.getValue()){

				UMLsource.add("interface "+name+"<|.."+"class "+entry.getKey());

			}

		}
		
		for (Entry<String, ArrayList<String>> entry : InterfaceImplementsMap.entrySet()) {
			for(String name:entry.getValue()){

				UMLsource.add("interface "+name+"<|.."+"interface "+entry.getKey());

			}

		}
		for (Entry<String, String> entry : ClassExtendsMap.entrySet()) {

			UMLsource.add("class "+entry.getValue()+"<|--"+"class "+entry.getKey());
		}

		UMLsource.add("@enduml");


		return UMLsource;

	}

}








