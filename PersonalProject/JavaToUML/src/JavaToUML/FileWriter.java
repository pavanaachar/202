package JavaToUML;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWriter {
	public static void writer(String path,ArrayList<String> string){
		PrintWriter filewriter = null;

		try {
			filewriter = new PrintWriter(path);
			for(int i = 0; i<string.size();i++){
				filewriter.println(string.get(i));
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			filewriter.close();
		}
	}
}
