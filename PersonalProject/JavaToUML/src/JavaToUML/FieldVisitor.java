package JavaToUML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FieldVisitor extends VoidVisitorAdapter<Object>  {

	public ArrayList<String> FieldName;
	public ArrayList<String> types;
	public ArrayList<ObjCount> objCountList;
	
	List<String> collections = new ArrayList<String>(Arrays.asList("Collection","ArrayList","List","Hashmap","Set"));
	public FieldVisitor(){
		FieldName = new ArrayList<String>();
		types = new ArrayList<String>();
		
		objCountList = new ArrayList<ObjCount>();
	}

	@Override
	public void visit(FieldDeclaration n,Object obj){
		super.visit(n, obj);

		if(n.getVariables().isEmpty()==false)
		{
			NodeList<VariableDeclarator> fieldlist = n.getVariables();
			for(VariableDeclarator field: fieldlist)
			{	
				String name = "";
				String type = field.getType().toString();


				if(type.contains("<"))
				{
					//System.out.println(type.substring(0,type.indexOf("<")));
					if(collections.contains(type.substring(0,type.indexOf("<"))))
					{
						//System.out.println(type.substring(type.indexOf("<")+1,type.indexOf(">")));
						String Ctype = type.substring(type.indexOf("<")+1,type.indexOf(">"));
						if(Parser.ClassNames.contains(Ctype)||Parser.InterfaceNames.contains(Ctype))
						{
							
							boolean found = false;
							//System.out.println("HERE");
							for (ObjCount o : objCountList) {
								if (o.className.equals(Ctype) && o.count < ObjCount.MAX_OBJ_COUNT) {
									o.count += ObjCount.MAX_OBJ_COUNT;
									found = true;
								}
							}
							if (!found) {
								ObjCount o = new ObjCount(Ctype);
								o.count = ObjCount.MAX_OBJ_COUNT;
								objCountList.add(o);
							}
							
							
						}
					}
				}


				//TO-DO check if getter or setter methods exist
				
				else if(Parser.ClassNames.contains(type)==false && Parser.InterfaceNames.contains(type)==false)
				{
					if(n.isPublic()){
						name = name+"+";
					}
					else if(n.isProtected()){
						name = name+"#";
					}
					else if(n.isPrivate()){
						name = name+"-";
					}
					else{
						name = name+"-";
					}

					name = name+field.toString()+":"+field.getType().toString();
					FieldName.add(name);
					//System.out.println("field names : "+FieldName);
				}
				
				else
				{
					boolean found = false;
					for (Iterator<ObjCount> iterator = objCountList.iterator(); iterator.hasNext();) {
						ObjCount o = iterator.next();
						if (o.className.equals(type)) {
							o.count++;
							found = true;
						}
					}
					if (!found) {
						ObjCount o = new ObjCount(type);
						o.count = 1;
						objCountList.add(o);
					}
					
					types.add(type);
					//System.out.println("types : "+types);
				}
			}
		}
	}
	public ArrayList<String> getFieldName(){
		return FieldName;
	}

	public ArrayList<String> getFieldTypes(){
		return types;
	}
	
	// S
	public ArrayList<ObjCount> getFieldObjCountList(){
		return objCountList;
	}

}
