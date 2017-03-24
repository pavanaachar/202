package JavaToUML;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class Parser {

	private static ArrayList<File> JavaFiles = new ArrayList<File>();

	public static void main(String[] args) throws Exception{
		String path = "C:\\Users\\Pavana\\workspace1\\text\\src\\text\\";

		File inputFile = new File(path);
		if(inputFile.isDirectory()){
			DirExplorer dir_explorer = new DirExplorer(inputFile);

			JavaFiles = dir_explorer.listFiles();

			for(int i=0; i<JavaFiles.size();i++){
				File file = new File(JavaFiles.get(i).getAbsolutePath());
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
	}
}






