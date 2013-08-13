package classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import textPreprocessor.SpecialCharacterRemover;
import textPreprocessor.StopWordsRemover;
import textPreprocessor.Tokenizer;

public class SearchResult {

	private String title;
	private String[] tokensInTitle;
	private String snippet;
	private String[] tokensInSnippet;
	private String label;
	private List<String> allTokens = new ArrayList<String>();
	private HashMap<String, Integer> allTokenFrequency = new HashMap<String, Integer>();

	public HashMap<String, Integer> getAllTokenFrequency() {
		return allTokenFrequency;
	}

	public Integer getTokenFrequency(String token) {
		return allTokenFrequency.get(token);
	}

	public void setAllTokenFrequency(HashMap<String, Integer> allTokenFrequency) {
		this.allTokenFrequency = allTokenFrequency;
	}

	public String[] getTokensInTitle() {
		return tokensInTitle;
	}

	public void setTokensInTitle(String[] tokensInTitle) {
		this.tokensInTitle = tokensInTitle;
	}

	public String[] getTokensInSnippet() {
		return tokensInSnippet;
	}

	public void setTokensInSnippet(String[] tokensInSnippet) {
		this.tokensInSnippet = tokensInSnippet;
	}

	public String[] getAllToken() {
		//System.out.println(this.toString() + "\n\n");
		for (int i = 0; i < tokensInTitle.length; i++) {
			allTokens.add(tokensInTitle[i]);
		}
		for (int i = 0; i < tokensInSnippet.length; i++) {
			allTokens.add(tokensInSnippet[i]);
		}
		return (String[]) allTokens.toArray(new String[0]);
	}

	public boolean hasToken(String token) {
		String[] allToken = this.getAllToken();
		boolean returnValue = false;
		for (int i = 0; i < allToken.length; i++) {
			if (allToken[i] == token) {
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public SearchResult() {
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getTitle() + getSnippet();
	}

	public void removeSpecialCharacters() {
		this.title = SpecialCharacterRemover.removeSpecialCharacter(this.title);
		this.snippet = SpecialCharacterRemover
				.removeSpecialCharacter(this.snippet);
	}

	public void tokenizeSearchResult() {
		this.tokensInTitle = Tokenizer.getAllTokens(title);
		this.tokensInSnippet = Tokenizer.getAllTokens(snippet);
	}

	public void removeStopwords() {
		this.tokensInTitle = StopWordsRemover
				.removeStopWords(this.tokensInTitle);
		this.tokensInSnippet = StopWordsRemover
				.removeStopWords(this.tokensInSnippet);
	}
}
