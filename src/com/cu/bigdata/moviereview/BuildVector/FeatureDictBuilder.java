package com.cu.bigdata.moviereview.BuildVector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FeatureDictBuilder {
	public FeatureDictBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public Map<String,Double> calculateMI(String filePath){
		Map<String, ArrayList<Integer>> MIMap = new HashMap<String, ArrayList<Integer>>();
		Map<String, Double> featureScoreMap = new HashMap<String, Double>();
		Map<String, Double> featureScoreMapSorted = new TreeMap<String, Double>(new MyComparator(featureScoreMap));
		
		double epsilon = 0.00001;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		// total number of positive & total number of negative docs
		int totalPos = 0;
		int totalNeg = 0;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] splitedline = line.split(cvsSplitBy);
				Integer lable = Integer.parseInt(splitedline[0]);
				Set<String> lineHashSet = new HashSet<String>();
				for(int i = 1;i<splitedline.length;i++){
					if(!lineHashSet.contains(splitedline[i])) {
						lineHashSet.add(splitedline[i]);
					}
				}
				for(String s:lineHashSet){
					if(MIMap.get(s)==null){
						ArrayList<Integer> tmpList = new ArrayList<Integer>(Collections.nCopies(2, 0));
						MIMap.put(s, tmpList);
					}
					/* lable 0&1 -> negative -> 0
					 *       2&3 -> positive -> 1
					 * 0 -> 00 1 -> 01 2 -> 10 3 -> 11
					 *
					 **/
					if(lable<=1){
						totalNeg++;
						ArrayList<Integer> updateArr = MIMap.get(s);
						updateArr.set(0, updateArr.get(0)+1);
						MIMap.put(s, updateArr);
					}
					else{
						totalPos++;
						ArrayList<Integer> updateArr = MIMap.get(s);
						updateArr.set(1, updateArr.get(1)+1);
						MIMap.put(s, updateArr);
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
		
		for(String s:MIMap.keySet()){
			int negContained = MIMap.get(s).get(0);
			int posContained = MIMap.get(s).get(1);
			double n00 = totalNeg-negContained;
			double n01 = negContained;
			double n10 = totalPos-posContained;
			double n11 = posContained;
			double total = totalNeg+totalPos;
			double score = n11/total*log2((total*n11+epsilon)/((n10+n11)*(n01+n11)+epsilon))
					+n01/total*log2((total*n01+epsilon)/((n01+n00)*(n01+n11)+epsilon))
					+n10/total*log2((total*n10+epsilon)/((n10+n11)*(n00+n10)+epsilon))
					+n00/total*log2((total*n00+epsilon)/((n01+n00)*(n10+n00)+epsilon));
			featureScoreMap.put(s, score);
		}
		
		featureScoreMapSorted.putAll(featureScoreMap);
		return featureScoreMapSorted;
	}
	
	private double log2(double x){
		return Math.log(x) / Math.log(2);
	}
	
	public Map<String, Integer> buildDictWithMI(String filePath,int numFeture){
		Map<String, Integer> dict = new HashMap<String, Integer>();
		Map<String, Double> MIScrore = calculateMI(filePath);
		//this part is not done yet...
		int i=0;
		for (Map.Entry<String, Double> entry : MIScrore.entrySet()) {
			if (i>=numFeture)
				break;
			dict.put(entry.getKey(), i);
//			System.out.println("Word: " + entry.getKey() + ", score: " + entry.getValue() + ", index: " + i);
			i++;
		}
		
		// Serialization map
		try {
			FileOutputStream fos = new FileOutputStream("data/dict/dict_" + numFeture + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dict);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dict;
	}
	
	public Map<String, Integer> readDictWithMI(Integer numFeture) {
		Map<String, Integer> dict = new HashMap<String, Integer>();
		
		try {
			FileInputStream fis = new FileInputStream("data/dict/dict_" + numFeture + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			dict = (HashMap<String, Integer>) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dict;
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
