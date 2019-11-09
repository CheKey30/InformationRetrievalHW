package pre_process;

import java.util.StringTokenizer;

/**
 * This is for INFSCI 2140 in 2019
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	// Essential private methods or variables can be added.

	// save all contents in this document
	private StringTokenizer stringTokenizer;

	//all of these characters can split a sentence into two parts
	private String delim = " \r\t\n.,;:\"()?!+-*/[]_";

	// YOU MUST IMPLEMENT THIS METHOD.
	public WordTokenizer(String texts) {
		// Tokenize the input texts.
		// use the StringTokenizer class to split the String
		this.stringTokenizer = new StringTokenizer(texts,this.delim);
	}

	// YOU MUST IMPLEMENT THIS METHOD.
	public String nextWord() {
		// Return the next word in the document.
		// Return null, if it is the end of the document.
		while(this.stringTokenizer.hasMoreTokens()){
			char[] current = this.stringTokenizer.nextToken().toCharArray();
			// if the current word contain non-character items, skip it.
			if(!validWord(current))continue;
			return String.valueOf(current);
		}
			return null;
	}

	// check whether a word only contains letters
	public boolean validWord(char[] current){
		for(char c:current){
			// word like “it's” is allowed
			if(Character.isAlphabetic(c) || c=='\''){
				continue;
			}else {
				return false;
			}
		}
		return true;
	}

}
