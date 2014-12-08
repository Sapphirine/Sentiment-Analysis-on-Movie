package com.cu.bigdata.moviereview.BuildVector;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileReader {
	
	public List<String> readText(String filePath) throws IOException {
		
		Path path = Paths.get(filePath);
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		List<String> res = new ArrayList<String>();
        String line = null;
        while ( (line = reader.readLine()) != null ) { 
        	res.add(line);
        	//System.out.println(line);
        }
        return res;
	}
	
	public List<String> readLable(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		List<String> res = Files.readAllLines(path, StandardCharsets.UTF_8);
		//System.out.print(res);
		return res;
	}
}
