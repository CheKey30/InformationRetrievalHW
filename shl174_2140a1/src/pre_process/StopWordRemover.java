package pre_process;

import classes.Path;

import java.io.*;
import java.util.HashSet;
import java.util.regex.Pattern;

public class StopWordRemover {
	// Essential private methods or variables can be added.

	// use a set to save all stop words
	private HashSet<String> stopWords = new HashSet<>();

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public StopWordRemover() {
		// Load and store the stop words from the fileinputstream with appropriate data
		// structure.
		// NT: address of stopword.txt is Path.StopwordDir
		try{
			//read stop words from file and add them into set
			initStopWords();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public boolean isStopword(String word) {
		// Return true if the input word is a stopword, or false if not.
		return this.stopWords.contains(word);
	}

	public void initStopWords() throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Path.StopwordDir))));
		String line;
		while ((line=bufferedReader.readLine())!=null){
			this.stopWords.add(line);
		}
	}
}
