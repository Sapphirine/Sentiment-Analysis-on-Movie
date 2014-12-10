package com.cu.bigdata.moviereview.BuildVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StemmedTextWriter {
	
	public void writeFile(List<ArrayList<String>> stemmedTextInputs, 
			List<String> lableInputs) throws IOException {
		Path path = Paths.get("src/main/resources/data/temp/StemmedText.csv");
		try {
			Files.deleteIfExists(path);
			Files.createFile(path);
		} catch (Exception e) {}
		
		for (int i = 0; i < stemmedTextInputs.size(); i++) {
			String content = lableInputs.get(i) + ",";
			for (String s: stemmedTextInputs.get(i)) {
				content = content + s + ",";
			}
			content = content + "\n";
			Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
		}
	}

}
