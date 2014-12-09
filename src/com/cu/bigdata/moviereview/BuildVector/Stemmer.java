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
	
	public static String replacePunc = " ssstoppp ";
	
	public ArrayList<ArrayList<String>> stemInput(List<String> textInputs) {
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		for (String line:textInputs) {
			res.add(stemInput(line));
		}
		return res;
	}
	
	private static Boolean IsStopWord(String s) {
		List<String> stopWord = Arrays.asList("a", "about", "above", "after", "again", 
				"against", "all", "am", "an", "and", "any", "are", "as", "at", 
				"be", "because", "been", "before", "being", "below", "between", "both", 
				"but", "by", "could", "did", "do", 
				"does", "doing", "down", "during", "each", "few", 
				"for", "from", "further", "had", "has", "have", 
				"having", "he", "he'd", "he'll", "he's", "her", "here", 
				"here's", "hers", "herself", "him", "himself", "his", "how", "how's", 
				"i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", 
				"it", "it's", "its", "itself", "let's", "me", "more", "most", 
				"my", "myself", "of", "off", "on", "once", "only", 
				"or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", 
				"same", "she", "she'd", "she'll", "she's", "should", 
				"so", "some", "such", "than", "that", "that's", "the", "their", "theirs", 
				"them", "themselves", "then", "there", "there's", "these", "they", 
				"they'd", "they'll", "they're", "they've", "this", "those", "through", 
				"to", "too", "under", "until", "up", "very", "was", "we", "we'd", 
				"we'll", "we're", "we've", "were", "what", "what's", "when", 
				"when's", "where", "where's", "which", "while", "who", "who's", "whom", 
				"why", "why's", "with", "would", "you", "you'd", 
				"you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
		
		HashSet<String> stopWords = new HashSet<String>(stopWord);
		if (stopWords.contains(s)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Boolean IsNegation(String s) {
		List<String> negationWord = Arrays.asList("not", "can't", "cannot", "don't", "aren't", 
				"couldn't", "doesn't", "didn't", "hasn't", "haven't", "isn't", "musn't", 
				"no", "nor", "shan't", "shouldn't", "weren't", "won't", "wouldn't");
		HashSet<String> negationWords = new HashSet<String>(negationWord);
		if (negationWords.contains(s)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 1. Replace ".,?:;" with "ssstoppp"
	 * 2. Remove other punctuations except "'"
	 * 3. Split by space
	 * 4. Remove stopwords
	 * 5. Set negation flag
	 */
	public ArrayList<String> stemInput(String s) {
		SnowballStemmer stemmer = (SnowballStemmer) new englishStemmer();
		s = s.replaceAll("[\\.\\,\\:\\?]", replacePunc);
		s = s.replaceAll("[^a-zA-Z0-9'\\s]", "").replaceAll("\\s+", " ");
		String[] tmp = s.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		
		Boolean negate = false;
		
		for (String word: tmp) {
			if (IsStopWord(word)) {
				continue;
			}
			if (replacePunc.contains(word)) {
				negate = false;
				continue;
			}
			if (IsNegation(word)) {
				negate = !negate;
				continue;
			}
			stemmer.setCurrent(word);
			stemmer.stem();
			if (negate) {
				res.add("not-" + stemmer.getCurrent());
			} else {
				res.add(stemmer.getCurrent());
			}
		}
		return res;
	}

}
