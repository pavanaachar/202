package sample;

import java.io.File;
import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class sampleparser {

	public static void main(String[] args) throws Exception{
		String path = "/home/pavana/workspace/sample/src/sample/A.java";
		File file = new File(path);

		FileInputStream file_in = new FileInputStream(file);

		CompilationUnit compile_unit = null;

		try{
			compile_unit = JavaParser.parse(file_in);
			new MethodVisitor().visit(compile_unit,null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			file_in.close();
		}
		

	}
	private static class MethodVisitor extends VoidVisitorAdapter<Object> {
		public void visit(ClassOrInterfaceDeclaration n, Object arg) {

			super.visit(n, arg);
			System.out.print(" * "+n.getName()+"\n");

		}
	
		public void visit(MethodCallExpr n, Object arg) {

			super.visit(n, arg);
			System.out.print("\n" + n+"\n");

		}

	
	}
	
}

