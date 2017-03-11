package text;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceStringReader;


public class plant {
	public static void main(String[] args)throws IOException, InterruptedException{
		
	ByteArrayOutputStream png = new ByteArrayOutputStream();
	String plantUmlSource="@startuml\n";

      plantUmlSource+="classA--|>classB\n";

      plantUmlSource+="classc--|>classB\n";

    plantUmlSource+="@enduml";

    SourceStringReader reader = new SourceStringReader(plantUmlSource);
    String desc = reader.generateImage(png); 
    byte [] data = png.toByteArray();

    InputStream in = new ByteArrayInputStream(data);
    BufferedImage convImg = ImageIO.read(in);

    ImageIO.write(convImg, "png", new File("C:\\Users\\Pavana\\Desktop\\new\\image.png"));

    System.out.print(desc);
	}

}
