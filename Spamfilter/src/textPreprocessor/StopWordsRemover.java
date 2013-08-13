package textPreprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StopWordsRemover {
	public static String[] removeStopWords(String[] data) {
		//Charset charset = Charset.forName("US-ASCII");
		List<String> dataWithoutStopWords = new ArrayList<String>();
		String currentDirectory = new File("").getAbsolutePath();
		//Path path = FileSystems.getDefault().getPath("Stopwords.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(
				currentDirectory + "/data/Stopwords.txt"))) {
			String line = null;
			String fileContent = "";
			while ((line = reader.readLine()) != null) {
				fileContent = fileContent + line;
			}
			if (data != null) {
				for (int i = 0; i < data.length; i++) {
					if (!fileContent.contains(data[i])) {
						dataWithoutStopWords.add(data[i]);
					}
				}
			}
		} catch (IOException ex) {
			System.err.format("IOException: %s%n", ex);
		}
		return (String[]) dataWithoutStopWords.toArray(new String[0]);
	}
}
