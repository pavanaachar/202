package JavaToUML;

import java.io.File;
import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Parser {
	
	public static void main(String[] args) throws Exception{
		String path = "C:\\Users\\Pavana\\workspace1\\text\\src\\text\\A.java";
		

		File file = new File(path);

		FileInputStream file_in = new FileInputStream(file);

		CompilationUnit compile_unit = null;

		try{
			compile_unit = JavaParser.parse(file_in);
			new FieldVisitor().visit(compile_unit,null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			file_in.close();
		}
	}

}







