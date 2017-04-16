package JavaToUML;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	HashMap<String, String> ClassImplementsMap = new HashMap<String,String>();

	HashMap<String, String> ClassExtendsMap = new HashMap<String,String>();


	public Parser(ArrayList<File> files){
		JavaFiles = files;
	}

	public ArrayList<String> parser() throws IOException {		

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

			ClassImplementsMap = class_interface_visitor.getClassImplementsMap();

			ClassExtendsMap = class_interface_visitor.getClassExtendsMap();


		}

		for(int i =0;i<compilationunits.size();i++){
			CompilationUnit cu = compilationunits.get(i);
			ClassOrInterfaceVisitor class_interface_visitor = new ClassOrInterfaceVisitor();

			class_interface_visitor.visit(cu,null);

			if(class_interface_visitor.IsClass()){
				String classname = class_interface_visitor.getClassName();

				UMLsource.add("class "+classname+"{");

				FieldVisitor fieldvisitor = new FieldVisitor();
				fieldvisitor.visit(cu,null);
				ArrayList<String> fields = fieldvisitor.getFieldName();
				if(fields!=null){
					UMLsource.addAll(fieldvisitor.getFieldName());
				}
				ArrayList<String> Fieldtypes = fieldvisitor.getFieldTypes();
				ClassFieldsMap.put(classname, Fieldtypes);

			}

			else if(class_interface_visitor.IsInterface()){
				String interfacename = class_interface_visitor.getInterfaceName();
				InterfaceNames.add(interfacename);
				UMLsource.add("interface "+interfacename+"{");

			}


			MethodVisitor methodvisitor = new MethodVisitor();
			methodvisitor.visit(cu,null);
			UMLsource.addAll(methodvisitor.getMethods());



			UMLsource.add("}");

		}


		// Generate plantuml grammar for relation between classes

		for (Entry<String, ArrayList<String>> entry : ClassFieldsMap.entrySet()) {
			//System.out.println(entry.getValue());
			for(String s: entry.getValue()){
				String t = "";
				if(s.startsWith("*")){
					System.out.println(s);
					t = s.replace("*", "");
					System.out.println(t);
					if(ClassNames.contains(t)){
						UMLsource.add("class "+entry.getKey()+"--"+"\"*\""+"class "+t);

					}
					else
						if(InterfaceNames.contains(t) ){
							UMLsource.add("class "+entry.getKey()+"--"+"\"*\""+"interface "+t);
						}

				}


				else {
					if(ClassNames.contains(s)){

						if(!UMLsource.contains("class "+s+"--"+"class "+entry.getKey()))
						{
							UMLsource.add("class "+entry.getKey()+"--"+"class "+s);
						}
						//System.out.println("class "+s+"--"+"class "+entry.getKey());
					}
					else if(InterfaceNames.contains(s) ){
						UMLsource.add("interface "+s+".."+"class "+entry.getKey());
						//System.out.println("class "+entry.getKey()+"--"+"class "+s);
					}
				}

			}

		}
		for (Entry<String, String> entry : ClassImplementsMap.entrySet()) {

			UMLsource.add("interface "+entry.getValue()+"<|.."+"class "+entry.getKey());

		}

		for (Entry<String, String> entry : ClassExtendsMap.entrySet()) {

			UMLsource.add("class "+entry.getValue()+"<|--"+"class "+entry.getKey());
		}

		UMLsource.add("@enduml");


		return UMLsource;

	}

}








