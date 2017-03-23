package text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class connection {
	public static void main(String[] args) throws Exception{
		String filename = "C:\\Users\\Pavana\\Desktop\\New folder\\202\\yuml_image.png";
		try {
			URL url = new URL("https://yuml.me/diagram/class/draw/[A]<>->[B],[C]->[B]");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			InputStream instream = url.openStream();
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, len);
			}
			instream.close();
			byte[] data = outstream.toByteArray();
			File file = new File(filename);
			FileOutputStream file_out_stream = new FileOutputStream(file);
			file_out_stream.write(data);
			file_out_stream.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
