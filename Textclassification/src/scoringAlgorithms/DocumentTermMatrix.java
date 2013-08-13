package scoringAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import classifier.SearchResult;

public class DocumentTermMatrix {
	private List<HashMap<String, Integer>> documentTermMatrix = new ArrayList<HashMap<String, Integer>>();
	private List<String> uniqueTokens = new ArrayList<String>();
	private List<String> uniqueLabel = new ArrayList<String>();

	public DocumentTermMatrix() {

	}

	public DocumentTermMatrix(List<SearchResult> dataSet) {
		for (SearchResult searchResult : dataSet) {
			createUniqueTokens(searchResult);
			calculateFrequency(searchResult);
		}
		// display();
	}

	private void display() {
		for (int i = 0; i < documentTermMatrix.size(); i++) {
			HashMap<String, Integer> data = documentTermMatrix.get(i);
			for (Integer value : data.values()) {
				System.out.println("Document ID: " + i + "Frequency Values: "
						+ value);
			}
		}
	}

	private void createUniqueTokens(SearchResult searchResult) {
		for (String token : searchResult.getTokensInSnippet()) {
			if (!uniqueTokens.contains(token)) {
				uniqueTokens.add(token);
			}
		}
		for (String token : searchResult.getTokensInTitle()) {
			if (!uniqueTokens.contains(token)) {
				uniqueTokens.add(token);
			}
		}
	}

	private void createUniqueLabels(List<SearchResult> trainingDataset) {
		for (SearchResult searchResult : trainingDataset) {
			if (!uniqueLabel.contains(searchResult.getLabel())) {
				uniqueLabel.add(searchResult.getLabel());
			}
		}
	}

	public void prepareDataInputForWeka(List<SearchResult> trainingDataset,
			List<SearchResult> testDataset) {
		File trainingFile = new File("documentTermMatrixTraining.arff");
		File testFile = new File("documentTermMatrixTesting.arff");
		createUniqueLabels(trainingDataset);
		try {
			trainingFile.createNewFile();
			testFile.createNewFile();
			BufferedWriter bwTrain = new BufferedWriter(new FileWriter(
					trainingFile));
			BufferedWriter bwTest = new BufferedWriter(new FileWriter(
					testFile));
			//Prepare training arff
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
			for (SearchResult result : trainingDataset) {
				bwTrain.write(createWekaDataString(result));
				bwTrain.newLine();
			}
			//for (SearchResult result : testDataset) {
			//	bwTrain.write(createWekaDataString(result));
			//	bwTrain.newLine();
			//}
			bwTrain.close();
			System.out.println("Finished writing data in arff file");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String createWekaDataString(SearchResult result) {
		StringBuffer dataString = new StringBuffer();
		for (String uniqueToken : uniqueTokens) {
			if (result.hasToken(uniqueToken)) {
				dataString.append(result.getTokenFrequency(uniqueToken));
			} else {
				dataString.append("0");
			}
			dataString.append(", ");
		}
		String label = "?";
		if (result.getLabel() != null) {
			label = result.getLabel();
		}
		dataString.append(label);
		return dataString.toString();
	}

	public String createWekaAttributeString() {
		StringBuffer attrString = new StringBuffer();

		String wekaAttr = "@attribute";
		String wekaDataType = "NUMERIC";

		for (String attribute : uniqueTokens) {
			attrString.append(wekaAttr);
			attrString.append(" ");
			attrString.append(attribute);
			attrString.append(" ");
			attrString.append(wekaDataType);
			attrString.append(System.getProperty("line.separator"));
		}
		attrString.append(wekaAttr + " topredict {");
		for (int i = 0; i < uniqueLabel.size(); i++) {
			if (i == uniqueLabel.size() - 1) {
				attrString.append(uniqueLabel.get(i));
			} else {
				attrString.append(uniqueLabel.get(i) + ",");
			}
		}
		attrString.append("}");

		return attrString.toString();
	}

	private void calculateFrequency(SearchResult searchResult) {
		HashMap<String, Integer> allTokenFrequency = new HashMap<String, Integer>();
		String[] allTokens = searchResult.getAllToken();
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
		searchResult.setAllTokenFrequency(allTokenFrequency);
	}
}
