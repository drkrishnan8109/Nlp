package scoringAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import classifier.Document;

public class TfIdfNormalized {
	private List<HashMap<String, Double>> tfidf = new ArrayList<HashMap<String, Double>>();
	private HashMap<String, Integer> df = new HashMap<String, Integer>();
	private HashMap<String, Double> idf = new HashMap<String, Double>();
	private List<String> uniqueTokens = new ArrayList<String>();
	private List<Document> dataSet = new ArrayList<Document>();
	private Integer totalNumberOfDocuments = 0;

	public TfIdfNormalized(List<Document> dataSet) {
		this.dataSet = dataSet;
		this.totalNumberOfDocuments = dataSet.size();
		createUniqueTokens();
		calculateTermFrequency();
		calculateDocumentFrequency();
		calculateNormalizedTfIdf();
		prepareDataInputForWeka();
	}

	private void createUniqueTokens() {
		for (int i = 0; i < dataSet.toArray().length; i++) {
			String[] allTokens = dataSet.get(i).getTokensRawData();
			for (String token : allTokens) {
				if (!uniqueTokens.contains(token.trim())) {
					uniqueTokens.add(token);
				}
			}
		}

	}

	private void calculateTermFrequency() {
		for (Document document : dataSet) {
			HashMap<String, Integer> allTokenFrequency = new HashMap<String, Integer>();
			String[] allTokens = document.getTokensRawData();
			for (int i = 0; i < allTokens.length; i++) {
				String token = allTokens[i];
				int count = 1;
				if (uniqueTokens.contains(token)) {
					if (allTokenFrequency.containsKey(token)) {
						count = allTokenFrequency.get(token) + 1;
					}
				}
				allTokenFrequency.put(token, count);
			}
			document.setAllTokenFrequency(allTokenFrequency);
		}
	}

	private void calculateDocumentFrequency() {
		System.out.print("Please wait");
		for (String token : uniqueTokens) {
			Integer totalTokenFrequency = 0;
			totalTokenFrequency = getTokenFrequency(token);
			df.put(token, totalTokenFrequency);
			Double value = 0.0;
			try {
				value = Math
						.log10(totalNumberOfDocuments / totalTokenFrequency);
			} catch (Exception e) {
				// TODO: handle exception
				value = 0.0;
			}
			idf.put(token, value);
		}
	}

	private Integer getTokenFrequency(String token) {
		Integer totalTokenFrequency = 0;
		for (Document document : dataSet) {
			if (document.hasToken(token)) {
				totalTokenFrequency = totalTokenFrequency + 1;
			}
		}
		return totalTokenFrequency;
	}

	private void calculateNormalizedTfIdf() {
		// tfidf (t,d,D) = tf(t,d) * idf (t,D)
		for (Document document : dataSet) {
			HashMap<String, Double> allTokenTfIdf = new HashMap<String, Double>();
			String[] allToken = document.getTokensRawData();
			for (String token : allToken) {
				allTokenTfIdf.put(token,
						document.getMaximizedTokenFrequencyNormalized(token)
								* idf.get(token));
				System.out.println("Terms :" + token + "  Ifidf Normalized"
						+ allTokenTfIdf.get(token).toString() + "\n");
			}

			document.setAllTokenTfIdf(allTokenTfIdf);
		}
	}

	public void prepareDataInputForWeka() {
		File arffFile = new File("tfidfNormalized.arff");
		try {
			arffFile.createNewFile();
			BufferedWriter bwTrain = new BufferedWriter(
					new FileWriter(arffFile));
			// Prepare training arff
			bwTrain.write("@RELATION X");
			bwTrain.newLine();
			bwTrain.newLine();
			String wekaAttrString = createWekaAttributeString();
			bwTrain.write(wekaAttrString);
			bwTrain.newLine();
			bwTrain.newLine();
			bwTrain.write("@DATA");
			bwTrain.newLine();
			bwTrain.newLine();
			for (Document document : dataSet) {
				bwTrain.write(createWekaDataString(document));
				bwTrain.newLine();
			}
			bwTrain.close();
			System.out.println("Finished writing data in arff file");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String createWekaDataString(Document document) {
		StringBuffer dataString = new StringBuffer();
		for (String uniqueToken : uniqueTokens) {
			if (document.hasToken(uniqueToken)) {
				dataString.append(document.getTokenTfIdf(uniqueToken));
			} else {
				dataString.append("0");
			}
			dataString.append(",");
		}
		String category = "Spam";
		category = document.getCategory();
		dataString.append(category);
		return dataString.toString();
	}

	public String createWekaAttributeString() {
		StringBuffer attrString = new StringBuffer();

		String wekaAttr = "@ATTRIBUTE";
		String wekaDataType = "NUMERIC";

		if (uniqueTokens.contains("null")) {
			uniqueTokens.remove("null");
		}

		for (String attribute : uniqueTokens) {
			attrString.append(wekaAttr);
			attrString.append(" ");
			if (attribute != null) {
				attrString.append(attribute);
			}
			attrString.append(" ");
			attrString.append(wekaDataType);
			attrString.append(System.getProperty("line.separator"));
		}
		attrString.append(wekaAttr + " subbu {");
		attrString.append("Spam,NoSpam");
		attrString.append("}");

		return attrString.toString();
	}
}
