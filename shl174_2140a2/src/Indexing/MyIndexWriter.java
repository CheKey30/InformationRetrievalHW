package Indexing;

import Classes.Path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class MyIndexWriter {
	// I suggest you to write very efficient code here, otherwise, your memory cannot hold our corpus...
	// writer to write indexing
	private BufferedWriter writer;

	// writer to write docID and docNo
	private BufferedWriter idWriter;
	private String indexpath;
	private String idpath;
	// map to save token and its posting
	private HashMap<String,StringBuilder> map = new HashMap<>();
	// assign id to each document
	private int docID = 1;
	
	
	public MyIndexWriter(String type) throws IOException {
		// This constructor should initiate the FileWriter to output your index files
		// remember to close files if you finish writing the index
		if(type.equals("trectext")){
			this.indexpath = Path.IndexTextDir+"//TextIndex.txt";
			this.idpath = Path.IndexTextDir+"//TextDocMap.txt";
		}else {
			this.indexpath = Path.IndexWebDir+"//WebIndex.txt";
			this.idpath = Path.IndexWebDir+"//WebDocMap.txt";
		}
		this.writer = new BufferedWriter(new FileWriter(this.indexpath));
		this.idWriter = new BufferedWriter(new FileWriter(this.idpath));
		
	}
	
	public void IndexADocument(String docno, String content) throws IOException {
		// you are strongly suggested to build the index by installments
		// you need to assign the new non-negative integer docId to each document, which will be used in MyIndexReader
		this.idWriter.write(docID+":"+docno);
		this.idWriter.newLine();
		this.idWriter.flush();
		String[] words = content.split(" ");
		HashMap<String,Integer> count = new HashMap<>();
		for(String s:words){
			count.put(s,count.getOrDefault(s,0)+1);
		}
		for(String key: count.keySet()){
			if(this.map.containsKey(key)){
				this.map.get(key).append(docID).append(":").append(count.get(key)).append(",");
			}else {
				StringBuilder sb = new StringBuilder();
				this.map.put(key, sb.append(docID).append(":").append(count.get(key)).append(","));
			}
		}
		docID++;
	}
	
	public void Close() throws IOException {
		// close the index writer, and you should output all the buffered content (if any).
		// if you write your index into several files, you need to fuse them here.
		for(String key: this.map.keySet()){
			String tonkenList = this.map.get(key).toString();
			this.writer.write(key+";"+tonkenList.substring(0,tonkenList.length()-1));
			this.writer.newLine();
			this.writer.flush();
		}
		this.writer.close();
		this.idWriter.close();
	}
	
}
