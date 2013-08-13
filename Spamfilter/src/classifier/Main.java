package classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

import scoringAlgorithms.TfIdf;
import scoringAlgorithms.TfIdfNormalized;
import textPreprocessor.SpecialCharacterRemover;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {
	// Public variables
	public List<Document> dataset;
	TfIdf tfIdf;

	/**
	 * @param args
	 */
	// Static methods
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main mainApplication = new Main();
		mainApplication.startApplication();
	}

	// Public methods
	public void startApplication() {
		if (dataset == null) {
			dataset = new ArrayList<Document>();
		}
		dataset = getDocuments();
		preprocessDataSet();
		applyScoringAlgorithm();
		// buildWekaModel();
	}

	private void buildWekaModel() {
		// get arff data into the memory
		try {
			Instances trainingData = DataSource
					.read("documentTermMatrixTraining.arff");
			// define which attribute should be predicted
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			// build the model

			J48 myTree = new J48();
			myTree.buildClassifier(trainingData);

			Instances testingData = DataSource
					.read("documentTermMatrixTesting.arff");
			testingData.setClassIndex(testingData.numAttributes() - 1);
			System.out.println("Classfying Instances:");
			System.out.println("Classfying Instances:");
			// get predicted label for one data instance
			for (int i = 0; i < testingData.numInstances(); i++) {

				double pred = myTree.classifyInstance(testingData.instance(i));
				String predictedInString = testingData.classAttribute().value(
						(int) pred);
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
				dataset.get(i).removeDigits();
				System.out.println("Printing tokens for Dcouments: "
						+ dataset.get(i).getRawData() + "\n");
				for (int j = 0; j < dataset.get(i).getTokensRawData().length; j++) {
					System.out.println(dataset.get(i).getTokensRawData()[j]
							+ "\n");
				}
			}
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

	private List<Document> getDocuments() {
		// Directory path here
		String spamDocumentPath = "/home/subramanya/Documents/01 Study/SemanticSearch/SPAM corpus/spam";
		String nonSpamDocumentPath = "/home/subramanya/Documents/01 Study/SemanticSearch/SPAM corpus/ling";
		// Read Spam Documents
		readDocuments(spamDocumentPath, true);
		// Read Spam Documents
		readDocuments(nonSpamDocumentPath, false);
		return dataset;
	}

	private void readDocuments(String path, Boolean isSpamDocumentSet) {
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			String fileContent = "";
			Document document = new Document();
			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				if (files.endsWith(".txt") || files.endsWith(".TXT")) {
					try (BufferedReader reader = new BufferedReader(
							new FileReader(path + "/" + files))) {
						String line = null;
						while ((line = reader.readLine()) != null) {
							fileContent = fileContent + line;
						}
					} catch (IOException ex) {
						System.err.format("IOException: %s%n", ex);
					}
				}
				document.setRawData(fileContent);
				if (isSpamDocumentSet) {
					document.setCategory("Spam");
				} else {
					document.setCategory("NoSpam");
				}
				document.setDocumentId(dataset.size() + 1);
				dataset.add(document);
				System.out.println(dataset.get(i).getRawData());
			}
		}
	}

	private void applyScoringAlgorithm() {
		// documentTermMatrix = new DocumentTermMatrix(dataset);
		tfIdf = new TfIdf(dataset);
		TfIdfNormalized tfidfNormalizedTf= new TfIdfNormalized(dataset);
	}
}
