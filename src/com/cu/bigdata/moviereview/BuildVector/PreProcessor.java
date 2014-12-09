package com.cu.bigdata.moviereview.BuildVector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cu.bigdata.moviereview.data.ModelConfig;

public class PreProcessor {
	
	public void toVector(String textFPath, String labelFPath, String outputPath) {
		try {
			FileReader reader = new FileReader();
//			List<String> lableInputs = reader.readLable("data/James+Berardinelli/label.4class.James+Berardinelli");
//			List<String> textInputs = reader.readText("data/James+Berardinelli/subj.James+Berardinelli");
			List<String> lableInputs = reader.readLable(labelFPath);
			List<String> textInputs = reader.readText(textFPath);

			List<ArrayList<String>> stemmedTextInputs = new Stemmer().stemInput(textInputs);
			
			new StemmedTextWriter().writeFile(stemmedTextInputs, lableInputs);
			Map<String, Integer> dict = new FeatureDictBuilder().buildDictWithMI("data/temp/StemmedText.csv", ModelConfig.FeatureNumber);
			new VectorWriter().writeCSV(dict, stemmedTextInputs, lableInputs, outputPath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {

//		VectorBuilderForWeb test = new VectorBuilderForWeb();
//		System.out.println(test.buildVector("This is a really bad movie", 1000));
//		System.out.println(test.buildVector("this is a really bad movie not good at all, but i like it somehow.", 10000));
//		System.out.println(test.buildVector("what the fuck sad amazing?", 5000));
		
		
//		String tp = "data/James+Berardinelli/subj.James+Berardinelli";
//		String lp = "data/James+Berardinelli/label.4class.James+Berardinelli";
//		String tp = "data/Dennis+Schwartz/subj.Dennis+Schwartz";
//		String lp = "data/Dennis+Schwartz/label.4class.Dennis+Schwartz";
		
//		String to = "data/train_log_1000_James.csv";
//		
//		ModelConfig.FeatureNumber = 1000;
//		PreProcessor processor = new PreProcessor();
//		processor.toVector(tp, lp, to);
		
//		FileReader reader = new FileReader();
//		List<String> lableInputs = reader.readLable(".\\data\\James+Berardinelli\\label.4class.James+Berardinelli");
//		List<String> textInputs = reader.readText(".\\data\\James+Berardinelli\\subj.James+Berardinelli");
//		List<String> lableInputs = reader.readLable("data/James+Berardinelli/label.4class.James+Berardinelli");
//		List<String> textInputs = reader.readText("data/James+Berardinelli/subj.James+Berardinelli");
//	
//		List<ArrayList<String>> stemmedTextInputs = new Stemmer().stemInput(textInputs);
//		System.out.println(stemmedTextInputs.size());
//		System.out.println(textInputs.get(50));
//		System.out.println(stemmedTextInputs.get(50));
//		
//		new StemmedTextWriter().writeFile(stemmedTextInputs, lableInputs);
//		Map<String, Integer> dict = new FeatureDictBuilder().buildDictWithMI("StemmedText.csv", 1000);
//		new VectorWriter().writeCSV(dict, stemmedTextInputs, lableInputs);
//		
////		HashMap<String, Integer> dict = new HashMap<String, Integer>();
////		dict.put("this", 0);
////		dict.put("is", 1);
////		dict.put("a", 2);
////		dict.put("cat", 3);
////		List<List<String>> stemmedTextInputs = new ArrayList<List<String>>();
////		stemmedTextInputs.add(Arrays.asList("this", "is", "a"));
////		stemmedTextInputs.add(Arrays.asList("is", "a"));
////		stemmedTextInputs.add(Arrays.asList("this", "is", "cat"));
////		List<String> lableInputs = Arrays.asList("5", "3", "4");
//		
////		System.out.println(dict);
////		System.out.println(stemmedTextInputs);
////		System.out.println(lableInputs);
////		
////		new VectorWriter().writeCSV(dict, stemmedTextInputs, lableInputs);
	
	}
}