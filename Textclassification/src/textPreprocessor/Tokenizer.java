package textPreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
#This is a new comment added to test Git with blue branch
public class Tokenizer {

	public static String[] getAllTokens(String stingToTokenize) {
		StringTokenizer javaTokenizer = new StringTokenizer(stingToTokenize,
				" :;,.");
		List<String> tokens = new ArrayList<String>();

		while (javaTokenizer.hasMoreTokens()) {
			String token = javaTokenizer.nextToken();
			tokens.add(token);
		}
		return (String[]) tokens.toArray(new String[0]);
	}
}
