package textPreprocessor;

public class SpecialCharacterRemover {
	public static String removeSpecialCharacter(
			String stringWithSecialCharacters) {
		return stringWithSecialCharacters.toLowerCase().trim().replace("[", "")
				.replace("]", "").replace("-", "").replace(":", "")
				.replace(",", "").replace(";", "").replace("(", "")
				.replace(")", "").replace("+", "").replace("*", "")
				.replace("@", "").replace("á", "").replace("$", "")
				.replace("#", "").replace("Ò", "").replace("\\", "")
				.replace("?", "").replace("&", "").replace("'", "")
				.replace("\"", "").replace("!", "").replace("|", "")
				.replace("/", "").replace("<", "").replace(">", "")
				.replace("~", "").replace("!", "").replace("@", "")
				.replace("#", "").replace("$", "").replace("%", "")
				.replace("^", "").replace("&", "").replace("*", "")
				.replace("~", "").replace("_", "").replace("`", "")
				.replace("=", "").replace("+", "").replace("-", "")
				.replace("{", "").replace("}", "").replace("[", "")
				.replace("]", "");
	}
}
