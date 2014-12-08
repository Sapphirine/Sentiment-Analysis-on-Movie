package com.cu.bigdata.moviereview.BuildVector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class VectorWriter {
	
	public void writeCSV(Map<String, Integer> dict, 
			List<ArrayList<String>> stemmedTextInputs, List<String> lableInputs) throws IOException{
		
		Path path = Paths.get("Vector.csv");
		try {
			Files.deleteIfExists(path);
			Files.createFile(path);
		} catch (Exception e) {}
		
		for (int i = 0; i < stemmedTextInputs.size(); i++) {
			String content = lableInputs.get(i) + ",";
			ArrayList<Integer> vector = new ArrayList<Integer>(Collections.nCopies(dict.size(), 0));
			for (String s: stemmedTextInputs.get(i)) {
				if (dict.containsKey(s)) {
					vector.set(dict.get(s), 1);
				}
			}
			for (int pos: vector) {
				content = content + pos + ",";
			}
			content = content + "\n";
			Files.write( path, content.getBytes(), StandardOpenOption.APPEND);
		}
	}

}
