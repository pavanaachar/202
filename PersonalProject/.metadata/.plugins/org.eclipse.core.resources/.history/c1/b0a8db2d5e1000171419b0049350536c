package JavaToUML;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class DirExplorer {
	ArrayList<File> directory= new ArrayList<File>();
	File Dir;
	public DirExplorer(File file){
		this.Dir=file;
	}

	FilenameFilter JavaFiles = new FilenameFilter() {
		public boolean accept(File file, String str) {
			String lowercaseName = str.toLowerCase();
			if (lowercaseName.endsWith(".txt")) {
				return true;
			} else {
				return false;
			}
		}
	};

	public ArrayList<File> listFiles(){
		for(File file: Dir.listFiles(JavaFiles)){
			directory.add(file);
		}
		return directory;


	}
}
