package text;

import java.io.File;
import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class sampleparser2 {

	public static void main(String[] args) throws Exception{
		String path = "C:\\Users\\Pavana\\workspace1\\text\\src\\text\\";

		File file = new File(path);
		DirExplorer dir_explore = new DirExplorer(file);
		for(File f:dir_explore.listfiles()){
			FileInputStream file_in = new FileInputStream(f);

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

