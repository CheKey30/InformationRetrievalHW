package pre_process;

import classes.Path;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is for INFSCI 2140 in 2019
 *
 */
public class TrectextCollection implements DocumentCollection {
	// Essential private methods or variables can be added.

	// path for the trectext file
	private  String trecttextpath;

	// the reader of trectext file
	private BufferedReader textReader;

	//the pattern of the documentID
	public final Pattern docNoPattern = Pattern.compile("<DOCNO>(.*)</DOCNO>");

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public TrectextCollection() throws IOException {
		// 1. Open the file in Path.DataTextDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!
		this.trecttextpath = Path.DataTextDir;
		this.textReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.trecttextpath)),StandardCharsets.UTF_8));
	}

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public Map<String, Object> nextDocument() throws IOException {
		// 1. When called, this API processes one document from corpus, and returns its
		// doc number and content.
		// 2. When no document left, return null, and close the file.
		String line;
		while ((line=this.textReader.readLine()) != null){
			// find the start point of one document
			if(!line.equals("<DOC>")){
				continue;
			}
			String docID="XXXXXX";
			Map<String, Object> res = new HashMap<>();
			StringBuilder sb = new StringBuilder();
			// start reading one document
			while(!(line=this.textReader.readLine()).equals("</DOC>")){
				// get the documentID
				Matcher matcher = this.docNoPattern.matcher(line);
				if(matcher.matches()){
					docID = matcher.group(1).trim();
				}
				// read the text part
				if(line.equals("<TEXT>")){
					while (!(line=this.textReader.readLine()).equals("</TEXT>")){
						sb.append(line);
						sb.append(' ');
					}
				}
			}
			// one document is finished, return a map, if there is not docID, it will be saved as XXXXXX
			res.put(docID,sb.toString());
			return res;
		}

		// the whole file has been read, close it and return null
		this.textReader.close();
		return null;
	}

}
