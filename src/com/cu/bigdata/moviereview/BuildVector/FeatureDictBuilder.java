package com.cu.bigdata.moviereview.BuildVector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import com.cu.bigdata.moviereview.DataInstance;

public class FeatureDictBuilder {
	public FeatureDictBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String, Integer> buildDict(String filePath){
		Map<String, Integer> dict = new HashMap<String, Integer>();
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int idxCounter = 0;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] splitedline = line.split(cvsSplitBy);
				for(String s:splitedline){
					if(dict.get(s)==null){
						dict.put(s, idxCounter++);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return dict;
	}
}
