package turtlefont.grammar;

import java.io.File;
import java.io.FileInputStream;

public class Utils{
	public static String readFileContent(File file) throws Exception {

		FileInputStream fileInputStream;

		fileInputStream = new FileInputStream(file);
		byte[] crunchifyValue = new byte[(int) file.length()];
		fileInputStream.read(crunchifyValue);
		fileInputStream.close();

		String fileContent = new String(crunchifyValue, "UTF-8");
		return fileContent;

	}
}
