package textPreprocessor;

public class SpecialCharacterRemover {
	public static String removeSpecialCharacter(
			String stringWithSecialCharacters) {
		return stringWithSecialCharacters.trim().replace("[", "")
				.replace("]", "").replace("-", "").replace(":", "")
				.replace(",", "").replace(";", "").replace("(", "")
				.replace(")", "").replace("+", "").replace("*", "")
				.replace("@", "").replace("á", "").replace("$", "")
				.replace("#", "").replace("Ò", "").replace("\\", "")
				.replace("?", "").replace("&", "").replace("'", "")
				.replace("\"", "").replace("!", "").replace("|", "")
				.replace("/", "").replace("<", "").replace(">", "").replace("~", "")
				.replace("!", "").replace("@", "").replace("#", "").replace("$", "")
				.replace("%", "").replace("^", "").replace("&", "").replace("*", "");
	}
}
