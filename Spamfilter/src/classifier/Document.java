package classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import textPreprocessor.SpecialCharacterRemover;
import textPreprocessor.StopWordsRemover;
import textPreprocessor.Tokenizer;

public class Document {

	private String[] tokensRawData;
	private String category;
	private List<String> allTokens = new ArrayList<String>();
	private HashMap<String, Integer> allTokenFrequency = new HashMap<String, Integer>();
	private String rawData;
	private int documentID;
	private HashMap<String, Double> allTokenTfIdf = new HashMap<String, Double>();

	public HashMap<String, Double> getAllTokenTfIdf() {
		return allTokenTfIdf;
	}

	public void setAllTokenTfIdf(HashMap<String, Double> allTokenTfIdf) {
		this.allTokenTfIdf = allTokenTfIdf;
	}

	public Double getTokenTfIdf(String token) {
		return this.allTokenTfIdf.get(token);
	}

	public Integer getDocumentId() {
		return documentID;
	}

	public void setDocumentId(int docId) {
		this.documentID = docId;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public HashMap<String, Integer> getAllTokenFrequency() {
		return allTokenFrequency;
	}

	public Integer getTokenFrequency(String token) {
		return allTokenFrequency.get(token);
	}

	double max = 0.0;

	private void getMax() {
		List<Integer> allCount = new ArrayList<Integer>();
		String[] allTerms = getTokensRawData();
		for (int i = 0; i < allTerms.length; i++) {
			allCount.add(allTokenFrequency.get(allTerms[i]));
		}
		max = Collections.max(allCount);
	}

	public double getMaximizedTokenFrequencyNormalized(String token) {
		if (max == 0.0) {
			getMax();
		}
		double maxTokenFrequency = 0.0;
		if (max != 0.0) {
			maxTokenFrequency = (allTokenFrequency.get(token) / max);
		}
		return maxTokenFrequency;
	}

	public void setAllTokenFrequency(HashMap<String, Integer> allTokenFrequency) {
		this.allTokenFrequency = allTokenFrequency;
	}

	public String[] getTokensRawData() {
		return tokensRawData;
	}

	public void setTokensRawData(String[] tokensRawData) {
		this.tokensRawData = tokensRawData;
	}

	public String[] getAllToken() {
		// System.out.println(this.toString() + "\n\n");
		for (int i = 0; i < tokensRawData.length; i++) {
			allTokens.add(tokensRawData[i]);
		}
		return (String[]) allTokens.toArray(new String[0]);
	}

	public boolean hasToken(String token) {
		boolean returnValue = false;
		returnValue = this.allTokens.contains(token);
		return returnValue;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Document() {
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return rawData;
	}

	public void removeSpecialCharacters() {
		this.rawData = SpecialCharacterRemover
				.removeSpecialCharacter(this.rawData);
	}

	public void tokenizeSearchResult() {
		this.tokensRawData = Tokenizer.getAllTokens(rawData);
	}

	public void removeStopwords() {
		this.tokensRawData = StopWordsRemover
				.removeStopWords(this.tokensRawData);
	}

	public void removeDigits() {
		List<String> tmp = new ArrayList<String>();
		for (int i = 0; i < this.tokensRawData.length; i++) {
			try {
				Integer.parseInt(this.tokensRawData[i]);
			} catch (Exception e) {
				// TODO: handle exception
				tmp.add(this.tokensRawData[i]);
			}
		}
		this.tokensRawData = tmp.toArray(new String[0]);
	}
}
