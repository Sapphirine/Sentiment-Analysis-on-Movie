package com.cu.bigdata.moviereview.BuildVector;

import java.io.IOException;
import java.util.*;

public class PreProcessor {

	public static void main(String[] args) throws IOException {

		FileReader reader = new FileReader();
		List<String> lableInputs = reader.readLable(".\\data\\James+Berardinelli\\label.4class.James+Berardinelli");
		List<String> textInputs = reader.readText(".\\data\\James+Berardinelli\\subj.James+Berardinelli");
	
		List<ArrayList<String>> stemmedTextInputs = new Stemmer().stemInput(textInputs);
//		System.out.println(stemmedTextInputs.size());
//		System.out.println(textInputs.get(5));
//		System.out.println(stemmedTextInputs.get(5));
		
		new StemmedTextWriter().writeFile(stemmedTextInputs);
		
//		HashMap<String, Integer> dict = new HashMap<String, Integer>();
//		dict.put("this", 0);
//		dict.put("is", 1);
//		dict.put("a", 2);
//		dict.put("cat", 3);
//		List<List<String>> stemmedTextInputs = new ArrayList<List<String>>();
//		stemmedTextInputs.add(Arrays.asList("this", "is", "a"));
//		stemmedTextInputs.add(Arrays.asList("is", "a"));
//		stemmedTextInputs.add(Arrays.asList("this", "is", "cat"));
//		List<String> lableInputs = Arrays.asList("5", "3", "4");
		
//		System.out.println(dict);
//		System.out.println(stemmedTextInputs);
//		System.out.println(lableInputs);
//		
//		new VectorWriter().writeCSV(dict, stemmedTextInputs, lableInputs);
	
	}
	
	

}
