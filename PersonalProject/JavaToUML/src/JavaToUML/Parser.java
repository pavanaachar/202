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

	public static ArrayList<String> classNames = new ArrayList<String>();

	public static ArrayList<String> interfaceNames = new ArrayList<String>();

	private static ArrayList<String> UMLsource = new ArrayList<String>();

	HashMap<String, ArrayList<String>> ClassImplementsMap = new HashMap<String,ArrayList<String>>();

	HashMap<String, ArrayList<String>> InterfaceImplementsMap = new HashMap<String,ArrayList<String>>();

	HashMap<String, String> ClassExtendsMap = new HashMap<String,String>();

	HashMap<String, ArrayList<String>> ClassDependencyMap = new HashMap<String,ArrayList<String>>();

	// ArrayList<CompilationUnit> CompilationUnits = new ArrayList<CompilationUnit>();

	class MyCompilationUnit {
		CompilationUnit cu;
		String name;
		String type; // "class" or "interface"
	}

	ArrayList<MyCompilationUnit> myCompilationUnits = new ArrayList<MyCompilationUnit>();

	public Parser(ArrayList<File> files){
		JavaFiles = files;
	}

	private int GetIndexOfClassOrIfaceName(String key) {
		for (int i = 0; i < myCompilationUnits.size(); i++) {
			if (myCompilationUnits.get(i).name.equals(key)) {
				return i;
			}
		}
		return -1;
	}

	private String[] GetNClassRefCounts(String name, int N, ArrayList<ClassRefCount> cRefs) {
		String[] counts = new String[N];
		int idx = 0;

		for (ClassRefCount cRef : cRefs) {
			if (cRef.name.equals(name)) {
				if (cRef.count >= ClassRefCount.MAX_COUNT) {
					counts[idx] = "\"*\"";
				} else {
					counts[idx] = "";
				}
				idx++;
			}
		}
		return counts;
	}

	private int max(int i, int j) {
		return i > j? i : j;
	}

	//
	class ClassRefCount {
		public static final int MAX_COUNT = 20000;
		String name;
		int count;
	}

	public ArrayList<String> parser() throws IOException {
		HashMap<String, ArrayList<FieldAttr>> classFieldMap = new HashMap<String, ArrayList<FieldAttr>>();
		HashMap<String, ArrayList<ClassRefCount>> classRefCountMap = new HashMap<String, ArrayList<ClassRefCount>>();

		UMLsource.add("@startuml");

		for (int i = 0; i < JavaFiles.size(); i++) {
			File file = new File(JavaFiles.get(i).getAbsolutePath());
			FileInputStream fileIn = null;
			MyCompilationUnit myCompilationUnit = new MyCompilationUnit();
			CompilationUnit compileUnit = null;

			try {
				fileIn = new FileInputStream(file);
				compileUnit = JavaParser.parse(fileIn);				
				myCompilationUnit.cu = compileUnit;  
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			} finally {
				fileIn.close();
			}

			ClassOrInterfaceVisitor classOrInterfaceVisitor = new ClassOrInterfaceVisitor();
			classOrInterfaceVisitor.visit(compileUnit, null);

			if (classOrInterfaceVisitor.IsClass()) {
				String className = classOrInterfaceVisitor.getClassName();
				classNames.add(className);
				myCompilationUnit.type = "class";
				myCompilationUnit.name = className;

			} else if (classOrInterfaceVisitor.IsInterface()) {
				String interfaceName = classOrInterfaceVisitor.getInterfaceName();
				interfaceNames.add(interfaceName);
				myCompilationUnit.type = "interface";
				myCompilationUnit.name = interfaceName;
			}
			myCompilationUnits.add(myCompilationUnit);
		}		

		ClassImplementsMap = ClassOrInterfaceVisitor.getClassImplementsMap();
		ClassExtendsMap = ClassOrInterfaceVisitor.getClassExtendsMap();
		InterfaceImplementsMap = ClassOrInterfaceVisitor.getInterfaceImplementsMap();		

		for(int i = 0; i < myCompilationUnits.size(); i++) {
			MyCompilationUnit myCu = myCompilationUnits.get(i);
			CompilationUnit cu = myCu.cu;
			String name = myCu.name;
			String type = myCu.type;

			if (type.equals("class")) {
				UMLsource.add("class "+ name + "{");
				FieldVisitor fieldVisitor = new FieldVisitor();
				fieldVisitor.visit(cu, null);

				ArrayList<FieldAttr> fAttrs = fieldVisitor.getFieldAttrs();
				classFieldMap.put(name, fAttrs);

				MethodVisitor methodVisitor = new MethodVisitor(fAttrs);
				methodVisitor.visit(cu, null);

				ArrayList<String> types = methodVisitor.gettypes();			

				ConstructorVisitor constructorVisitor = new ConstructorVisitor();
				constructorVisitor.visit(cu, null);

				for(String t: constructorVisitor.gettypes()){
					if(!types.contains(types)){
						types.add(t);
					}
				}

				ClassDependencyMap.put(name, types);



				// Add fields
				for (FieldAttr fAttr : fAttrs) {
					boolean proceed = true;
					if (fAttr.typeClass.contains("ArrayType")) {
						if(classNames.contains(fAttr.elementType)||interfaceNames.contains(fAttr.elementType)){
							proceed = false;
							break;
						}	
					}
					if (!proceed) continue;

					if (fAttr.typeClass.contains("ClassOrInterfaceType")) {
						if (fAttr.argTypes != null) {
							for (FieldAttr f : fAttr.argTypes) {
								if(classNames.contains(f.type)||interfaceNames.contains(f.type)){
									proceed = false;
									break;
								}								
							}
						} else {
							if(classNames.contains(fAttr.type)||interfaceNames.contains(fAttr.type)){
								proceed = false;
								break;
							}	
						}
					}
					if (!proceed) continue;

					String varName = fAttr.name;

					if (fAttr.visiblity.equals("private")) {
						varName = "-" + varName;
					} else if (fAttr.visiblity.equals("protected")) {
						varName = "#" + varName;
					} else {
						varName = "+" + varName;
					}
					varName = varName + ":" + fAttr.fullType;
					UMLsource.add(varName);
				}

				// Add constructor
				String constr = constructorVisitor.getConstructor();
				if (constr != null) {
					UMLsource.add(constr);
				}

				// Add methods
				UMLsource.addAll(methodVisitor.getMethods());

			} else if (type.equals("interface")) {
				UMLsource.add("interface "+ name +"{");
				MethodVisitor methodvisitor = new MethodVisitor(null);
				methodvisitor.visit(cu, null);

				// Add methods
				UMLsource.addAll(methodvisitor.getMethods());

			} else {
				// Type should only be "class" or "interface"
				assert(false);
			}
			UMLsource.add("}");
		}

		// Draw Association lines
		for (Iterator<String> iterator = classFieldMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			ArrayList<FieldAttr> fAttrs = classFieldMap.get(key);

			for (FieldAttr fAttr : fAttrs) {
				if (fAttr.typeClass.contains("ClassOrInterfaceType")) {
					if (fAttr.argTypes != null) {

						for (FieldAttr f : fAttr.argTypes) {
							if (classNames.contains(f.type) || interfaceNames.contains(f.type)) {
								ClassRefCount cRef = new ClassRefCount();
								cRef.name = new String(f.type);
								cRef.count = ClassRefCount.MAX_COUNT;

								ArrayList<ClassRefCount> list = classRefCountMap.get(key);
								if (list == null) {
									classRefCountMap.put(key, new ArrayList<ClassRefCount>());
								}
								list = classRefCountMap.get(key);
								list.add(cRef);
							}
						}
					} else {
						if (classNames.contains(fAttr.type) || interfaceNames.contains(fAttr.type)) {
							ClassRefCount cRef = new ClassRefCount();
							cRef.name = new String(fAttr.type);
							cRef.count = 1;

							ArrayList<ClassRefCount> list = classRefCountMap.get(key);
							if (list == null) {
								classRefCountMap.put(key, new ArrayList<ClassRefCount>());
							}
							list = classRefCountMap.get(key);
							list.add(cRef);
						}
					}
				}
			}
		}

		// Draw association between classes with cardinality
		int[][] edgeCountMatrix = new int[myCompilationUnits.size()][myCompilationUnits.size()];
		for (int i = 0; i < myCompilationUnits.size(); i++) {
			for (int j = 0; j < myCompilationUnits.size(); j++) {
				edgeCountMatrix[i][j] = 0;
			}
		}

		for (Iterator<String> iterator = classRefCountMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			int idx = GetIndexOfClassOrIfaceName(key);
			assert(idx != -1);

			for (Iterator<ClassRefCount> iterator2 = classRefCountMap.get(key).iterator(); iterator2.hasNext();) {
				ClassRefCount c = iterator2.next();
				int idx1 = GetIndexOfClassOrIfaceName(c.name);
				assert(idx1 != -1);

				edgeCountMatrix[idx][idx1]++;
			}
		}

		// Draw association between classes with cardinality
		for (int i = 0; i < myCompilationUnits.size(); i++) {
			for (int j = 0; j < myCompilationUnits.size(); j++) {
				int ijCount = edgeCountMatrix[i][j];
				int jiCount = edgeCountMatrix[j][i];
				int m = max(ijCount, jiCount);

				String iName = myCompilationUnits.get(i).name;
				String jName = myCompilationUnits.get(j).name;

				String ij[] = null;
				String ji[] = null;

				if (ijCount > 0) {
					ij = GetNClassRefCounts(jName, ijCount, classRefCountMap.get(iName));
				}
				if (jiCount > 0) {
					ji = GetNClassRefCounts(iName, jiCount, classRefCountMap.get(jName));
				}

				for (int k = 0; k < m; k++) {
					String iWeight = null;
					String jWeight = null;

					if (ijCount > 0) {
						iWeight = ij[k];
						ijCount--;
						edgeCountMatrix[i][j]--;
					} else {
						iWeight = "";
					}

					if (jiCount > 0) {
						jWeight = ji[k];
						jiCount--;
						edgeCountMatrix[j][i]--;
					} else {
						jWeight = "";
					}

					UMLsource.add(iName + " " + jWeight + "--" + iWeight + " " + jName);
				}
				assert(ijCount == 0 && jiCount == 0 && m == 0);
				assert(edgeCountMatrix[i][j] == 0 && edgeCountMatrix[j][i] == 0);
			}
		}		



		// Draw inheritance relation between class and interface
		for (Entry<String, ArrayList<String>> entry : ClassImplementsMap.entrySet()) {
			for(String name:entry.getValue()){
				UMLsource.add(name + " <|.. " + entry.getKey());
			}
		}

		// Draw inheritance relation between interfaces
		for (Entry<String, ArrayList<String>> entry : InterfaceImplementsMap.entrySet()) {
			for(String name:entry.getValue()){
				UMLsource.add(name + " <|.. " + entry.getKey());
			}
		}

		// Draw inheritance relation between classes		
		for (Entry<String, String> entry : ClassExtendsMap.entrySet()) {
			UMLsource.add(entry.getValue() + " <|-- " + entry.getKey());
		}
		
		// Draw dependency to interface
		for (Entry<String, ArrayList<String>> entry : ClassDependencyMap.entrySet()) {
			for(String name : entry.getValue()){
				UMLsource.add(entry.getKey() + "..>" + name);
				//UMLsource.add(+);
			}
		}

		UMLsource.add("@enduml");
		return UMLsource;
	}
}