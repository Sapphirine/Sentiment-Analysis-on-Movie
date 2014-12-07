package com.cu.bigdata.moviereview;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

public class CSVHelper {
	
	public List<DataInstance> ReadFromCSV(String filePath){
		List<DataInstance> dataset = new ArrayList<DataInstance>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				String[] splitedline = line.split(cvsSplitBy);
				Integer lable = Integer.parseInt(splitedline[0]);
				Vector features = new RandomAccessSparseVector(splitedline.length-1);
				for(int i = 1;i<splitedline.length;i++){
					features.set(i-1,Double.parseDouble(splitedline[i]));
				}
				/*
				for(int i = 0;i<features.size();i++){
					System.out.print(features.get(i)+",");
				}
				System.out.println();
				*/
				DataInstance instance = new DataInstance(features, lable);
				dataset.add(instance);
				
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
	 
		System.out.println("[CSVHelper] Done");
		return dataset;
	}
	
	public static void main(String[] args) {
		String filePath = "data/test.csv";
		CSVHelper helper = new CSVHelper();
		List<DataInstance> ds = helper.ReadFromCSV(filePath);
		System.out.println(ds.size());
		for(DataInstance instance : ds){
			System.out.print(instance.lable+":");
			for(int i = 0;i<instance.features.size();i++){
				System.out.print(instance.features.get(i)+",");
			}
			System.out.println();
		}
	}
	
}
