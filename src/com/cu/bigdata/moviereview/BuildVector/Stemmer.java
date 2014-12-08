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
	
	private ArrayList<String> stemInput(String s) {
		SnowballStemmer stemmer = (SnowballStemmer) new englishStemmer();
		s = s.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", " ");
		String[] tmp = s.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		for (String word: tmp) {
			stemmer.setCurrent(word);
			stemmer.stem();
		    res.add(stemmer.getCurrent());
		}
		return res;
	}

}
