package pre_process;

import classes.Path;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is for INFSCI 2140 in 2019
 *
 */
public class TrecwebCollection implements DocumentCollection {
	// Essential private methods or variables can be added.

	// path of the trecweb file
	private String trecWebPath;

	// the reader of the trecweb file
	private BufferedReader webReader;

	//pattern of the documentID
	public final Pattern docNoPattern = Pattern.compile("<DOCNO>(.*)</DOCNO>");

	//pattern of the html tags
	public final Pattern tags = Pattern.compile("<.+?>");


	// YOU SHOULD IMPLEMENT THIS METHOD.
	public TrecwebCollection() throws IOException {
		// 1. Open the file in Path.DataWebDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!
		this.trecWebPath = Path.DataWebDir;
		this.webReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(this.trecWebPath)), StandardCharsets.UTF_8));


	}

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public Map<String, Object> nextDocument() throws IOException {
		// 1. When called, this API processes one document from corpus, and returns its
		// doc number and content.
		// 2. When no document left, return null, and close the file.
		// 3. the HTML tags should be removed in document content.
		String line;
		while((line=this.webReader.readLine())!=null){
			//find the start point of one document
			if(!line.equals("<DOC>")){
				continue;
			}
			Map<String, Object> res = new HashMap<>();
			String docID = "XXXXXX";
			StringBuilder sb = new StringBuilder();
			// start reading one document in the file
			while((line=webReader.readLine())!=null){
				//get the documentID
				Matcher matcher = this.docNoPattern.matcher(line);
				if(matcher.matches()){
					docID = matcher.group(1).trim();
				}
				// read the content part
				if(line.equals("</DOCHDR>")){
					while (!(line=this.webReader.readLine()).equals("</DOC>")){
						sb.append(line.trim());
						sb.append(' ');
					}
					// one document is finished, break
					break;
				}
			}
			// one documnet is finished ,put it into map and return
			String part = RemoveTags(sb.toString());
			res.put(docID,part);
			return res;
		}

		// the whole file has been read, close it and return null
		this.webReader.close();
		return null;
	}

	// remove all html tags in the content
	public String RemoveTags(String s){
		if (s==null || s.equals("")){
			return " ";
		}
		return this.tags.matcher(s).replaceAll(" ");
	}

}
