package com.cu.bigdata.moviereview.BuildVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StemmedTextWriter {
	
	public void writeFile(List<ArrayList<String>> stemmedTextInputs) throws IOException {
		Path path = Paths.get("StemmedText.csv");
		try {
			Files.deleteIfExists(path);
			Files.createFile(path);
		} catch (Exception e) {}
		
		for (int i = 0; i < stemmedTextInputs.size(); i++) {
			String content = "";
			for (String s: stemmedTextInputs.get(i)) {
				content = content + s + ",";
			}
			content = content + "\n";
			Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
		}
	}

}
