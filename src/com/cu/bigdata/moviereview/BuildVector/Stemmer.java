package com.cu.bigdata.moviereview.BuildVector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.*;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {
	
	public ArrayList<ArrayList<String>> stemInput(List<String> textInputs) {
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		for (String line:textInputs) {
			res.add(stemInput(line));
		}
		return res;
	}
	
	private static Boolean IsStopWord(String s) {
		List<String> stopWord = Arrays.asList("a", "about", "above", "after", "again", 
				"against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", 
				"be", "because", "been", "before", "being", "below", "between", "both", 
				"but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", 
				"does", "doesn't", "doing", "don't", "down", "during", "each", "few", 
				"for", "from", "further", "had", "hadn't", "has", "hasn't", "have", 
				"haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", 
				"here's", "hers", "herself", "him", "himself", "his", "how", "how's", 
				"i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", 
				"it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", 
				"my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", 
				"or", "other", "ought", "our", "ours	ourselves", "out", "over", "own", 
				"same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", 
				"so", "some", "such", "than", "that", "that's", "the", "their", "theirs", 
				"them", "themselves", "then", "there", "there's", "these", "they", 
				"they'd", "they'll", "they're", "they've", "this", "those", "through", 
				"to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", 
				"we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", 
				"when's", "where", "where's", "which", "while", "who", "who's", "whom", 
				"why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", 
				"you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
		
		HashSet<String> stopWords = new HashSet<String>(stopWord);
		if (stopWords.contains(s)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private ArrayList<String> stemInput(String s) {
		SnowballStemmer stemmer = (SnowballStemmer) new englishStemmer();
		s = s.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", " ");
		String[] tmp = s.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		for (String word: tmp) {
			if (!IsStopWord(word)) {
				stemmer.setCurrent(word);
				stemmer.stem();
			    res.add(stemmer.getCurrent());
			}
		}
		return res;
	}

}
