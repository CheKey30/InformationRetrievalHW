package Indexing;

import Classes.Path;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PreProcessedCorpusReader {
	private BufferedReader bufferedReader;
	// path of the result of hw1
	private String path;
	
	
	public PreProcessedCorpusReader(String type) throws IOException {
		// This constructor opens the pre-processed corpus file, Path.ResultHM1 + type
		// You can use your own version, or download from http://crystal.exp.sis.pitt.edu:8080/iris/resource.jsp
		// Close the file when you do not use it any more
		this.path = Path.ResultHM1+type;
		this.bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.path)), StandardCharsets.UTF_8));

	}
	

	public Map<String, String> NextDocument() throws IOException {
		// read a line for docNo, put into the map with <"DOCNO", docNo>
		// read another line for the content , put into the map with <"CONTENT", content>

		// if the current line is not empty, then read two lines, the first line is docno, the second line is content
		String line = this.bufferedReader.readLine();
		if(line==null || line.equals("")){
			this.bufferedReader.close();
			return null;
		}
		// put the two lines into a map and return it
		HashMap<String,String> map = new HashMap<>();
		map.put("DOCNO",line);
		line = this.bufferedReader.readLine();
		map.put("CONTENT",line);
		return map;
	}

}
