package JavaToUML;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassOrInterfaceVisitor extends VoidVisitorAdapter<Object> {

	private String ClassName ;
	private String InterfaceName;
	private boolean IsClass;
	private boolean IsInterface;

	static HashMap<String,ArrayList<String>> ClassImplementsMap = new HashMap<String,ArrayList<String>>();

	static HashMap<String,ArrayList<String>> InterfaceImplementsMap = new HashMap<String,ArrayList<String>>();

	ArrayList<String> InterfaceTypes = new ArrayList<String>();
	static HashMap<String, String> ClassExtendsMap = new HashMap<String,String>();

	public ClassOrInterfaceVisitor(){
		this.IsClass= false;
		this.IsInterface = false;
		this.ClassName = null;
		this.InterfaceName = null;

	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Object obj) {
		super.visit(n, obj);


		if(!n.isInterface()){
			this.ClassName = n.getName().toString();
			this.IsClass = true;
		}


		if(n.isInterface())
		{
			this.InterfaceName = n.getName().toString();
			this.IsInterface = true;
		}


		if(!n.getImplementedTypes().isEmpty()){
			System.out.println(n.getImplementedTypes());
			NodeList<ClassOrInterfaceType> implementedTypes = n.getImplementedTypes();
			for(ClassOrInterfaceType types: implementedTypes){
				InterfaceTypes.add(types.getNameAsString());
			}
			if(n.isInterface()){
				InterfaceImplementsMap.put(InterfaceName, InterfaceTypes);
			}
			else
			{
				ClassImplementsMap.put(ClassName, InterfaceTypes);
				
			}
				System.out.println(ClassImplementsMap);

			}

			//System.out.println(n.getExtendedTypes().isEmpty());

			if(!n.getExtendedTypes().isEmpty()){
				//System.out.println(n.getExtendedTypes().get(0).getNameAsString());
				ClassExtendsMap.put(ClassName, n.getExtendedTypes().get(0).getNameAsString());

			}


		}

		public String getClassName(){
			return this.ClassName;
		}

		public String getInterfaceName(){
			return this.InterfaceName;
		}

		public boolean IsClass(){
			return this.IsClass;
		}

		public boolean IsInterface(){
			return this.IsInterface;
		}

		public static HashMap<String, ArrayList<String>> getClassImplementsMap(){
			return ClassImplementsMap;
		}
		public static HashMap<String, ArrayList<String>> getInterfaceImplementsMap(){
			return InterfaceImplementsMap;
		}

		public static HashMap<String, String> getClassExtendsMap(){
			return ClassExtendsMap;
		}


	}

