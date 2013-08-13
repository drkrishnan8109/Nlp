package classifier;

import idavoll.searchTools.WebSearchServiceTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scoringAlgorithms.DocumentTermMatrix;
import textPreprocessor.SpecialCharacterRemover;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {
	// Public variables
	public String delimiter = "::";
	public List<SearchResult> trainingDataset;
	public List<SearchResult> dataset;
	int trainingDataCount;
	DocumentTermMatrix documentTermMatrix;

	/**
	 * @param args
	 */
	// Static methods
	public static void main(String[] args) {
		Main mainApplication = new Main();
		mainApplication.startApplication();
	}

	// Public methods
	public void startApplication() {
		if (dataset == null) {
			dataset = new ArrayList<SearchResult>();
		}
		dataset = getSearchResults();
		preprocessDataSet();
		System.out.println("\nEnter number of traning data to be used:\n");
		trainingDataCount = Integer.parseInt(getUserInput());
		createTrainingDataset(trainingDataCount);
		getLabelForTrainDataFromUser();
		applyScoringAlgorithm();
		//buildWekaModel();
	}

	private void buildWekaModel() {
		// get arff data into the memory
		try {
			Instances trainingData = DataSource
					.read("documentTermMatrixTraining.arff");
			// define which attribute should be predicted
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			// build the model
			ZeroR zr = new ZeroR();
			J48 myTree = new J48();
			zr.buildClassifier(trainingData);

			Instances testingData = DataSource
					.read("documentTermMatrixTesting.arff");
			testingData.setClassIndex(testingData.numAttributes() - 1);
			System.out.println("Classfying Instances:");
			System.out.println("Classfying Instances:");
			// get predicted label for one data instance
			for (int i = 0; i < testingData.numInstances(); i++) {

				double pred = zr.classifyInstance(testingData.instance(i));
				String predictedInString = testingData.classAttribute().value(
						(int) pred);
				// System.out.print("ID: " + testingData.instance(i).value(0));
				// System.out.print(", actual: " +
				// testingData.classAttribute().value((int)
				// testingData.instance(i).classValue()));
				System.out.println((i + 1) + " : " + dataset.get(i).toString());
				System.out.println("classified as: " + predictedInString);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void preprocessDataSet() {
		if (dataset != null) {
			for (int i = 0; i < dataset.size(); i++) {
				dataset.get(i).removeSpecialCharacters();
				dataset.get(i).tokenizeSearchResult();
				dataset.get(i).removeStopwords();			
			}
		}
	}

	private void getLabelForTrainDataFromUser() {
		// TODO Auto-generated method stub
		for (int i = 0; i < trainingDataset.size(); i++) {
			System.out.println(trainingDataset.get(i).toString());
			System.out.println("\nEnter label for the training data:\n");
			String label = getUserInput();
			trainingDataset.get(i).setLabel(label.toLowerCase());
		}
	}

	private String getUserInput() {
		// Get input
		// open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		try {
			input = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}

	private void createTrainingDataset(int trainingDataCount) {
		if (trainingDataset == null) {
			trainingDataset = new ArrayList<SearchResult>();
		}
		for (int i = 0; i < trainingDataCount; i++) {
			trainingDataset.add(dataset.get(i));
			dataset.remove(i);
		}
		for (int i = 0; i < trainingDataset.size(); i++) {
			System.out.println(trainingDataset.get(i).getTitle() + ">>>>"
					+ trainingDataset.get(i).getSnippet());
		}
	}

	private List<SearchResult> getSearchResults() {
		// TODO Auto-generated method stub
		WebSearchServiceTest searchService = new WebSearchServiceTest();
		System.out.println("Enter your query : \n");
		String query = getUserInput();
		List<Map<String, String>> resultsList = searchService.testWebServices2(
				Arrays.asList(query), 5);
		List<SearchResult> dataset = new ArrayList<SearchResult>();
		for (int j = 0; j < resultsList.size(); j++) {
			SearchResult searchresult = new SearchResult();
			Map<String, String> aMap = resultsList.get(j);
			String resultTitle = aMap.get("title");
			String resultSnippet = aMap.get("snippet");
			searchresult.setTitle(resultTitle.toLowerCase());
			searchresult.setSnippet(resultSnippet.toLowerCase());
			dataset.add(searchresult);
		}
		return dataset;
	}

	private void applyScoringAlgorithm() {
		documentTermMatrix = new DocumentTermMatrix(dataset);
		documentTermMatrix.prepareDataInputForWeka(trainingDataset, dataset);
	}
}
