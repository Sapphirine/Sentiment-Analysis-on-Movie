package com.cu.bigdata.moviereview.BuildVector;

import java.util.ArrayList;
import java.util.Map;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class VectorBuilderForWeb {
	
	public Vector buildVector(String s, Integer numFeature) {
		
		s = s.toLowerCase();
		Map<String, Integer> dict = new FeatureDictBuilder().readDictWithMI(numFeature);
		Stemmer stemmer = new Stemmer();
		ArrayList<String> stemResult = stemmer.stemInput(s);
		
		Vector result = new RandomAccessSparseVector(dict.size());
		for(int i = 0; i < dict.size(); i++) {
			result.set(i, 0);
		}
		for(String word: stemResult) {
			if (dict.containsKey(word)) {
				result.set(dict.get(word), 1);
			}
		}
		return result;
	}
}
