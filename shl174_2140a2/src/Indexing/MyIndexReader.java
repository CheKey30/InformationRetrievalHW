package Indexing;

import Classes.Path;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class MyIndexReader {
	//you are suggested to write very efficient code here, otherwise, your memory cannot hold our corpus...
	// path of the txt file saved id to docno mapping
	private String docIdPath;
	// path of the index file
	private String indexPath;
	// map to find id based on docno
	private HashMap<String,Integer> docToID = new HashMap<>();
	// map to find docno based on id
	private HashMap<Integer,String> iDToDoc = new HashMap<>();
	// reader for the id docno mapping
	private BufferedReader idReader;


	public MyIndexReader( String type ) throws IOException {
		//read the index files you generated in task 1
		//remember to close them when you finish using them
		//use appropriate structure to store your index

		//get paths based on type
		if(type.equals("trecweb")){
			this.docIdPath = Path.IndexWebDir+"//WebDocMap.txt";
			this.indexPath = Path.IndexWebDir+"//WebIndex.txt";
		}else {
			this.docIdPath = Path.IndexTextDir+"//TextDocMap.txt";
			this.indexPath = Path.IndexTextDir+"//TextIndex.txt";
		}

		// read the id docno mapping line by line and store the result as two hashmap
		this.idReader = new BufferedReader(new FileReader(this.docIdPath));
		String line = this.idReader.readLine();
		while (line != null && !line.equals("")){
			String[] onedoc = line.split(":");
			this.docToID.put(onedoc[1],Integer.parseInt(onedoc[0]));
			this.iDToDoc.put(Integer.parseInt(onedoc[0]),onedoc[1]);
			line = this.idReader.readLine();
		}
	}
	
	//get the non-negative integer dociId for the requested docNo
	//If the requested docno does not exist in the index, return -1
	public int GetDocid( String docno ) {
		return this.docToID.getOrDefault(docno,-1);
	}

	// Retrieve the docno for the integer docid
	public String GetDocno( int docid ) {
		if(this.iDToDoc.containsKey(docid)){
			return this.iDToDoc.get(docid);
		}
		return null;
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] GetPostingList( String token ) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(this.indexPath));
		String line = bf.readLine();
		/* read the indexing line by line, if find one key equals the token,
		the then value of this line can be transformed to 2-dimension array
		 */
		while (line !=null && !line.equals("")){
			String[] oneToken = line.split(";");
			if(oneToken[0].equals(token)){
				String[] docs = oneToken[1].split(",");
				int[][] posting = new int[docs.length][2];
				for(int i=0;i<posting.length;i++){
					String[] onedoc = docs[i].split(":");
					posting[i][0] = Integer.parseInt(onedoc[0]);
					posting[i][1] = Integer.parseInt(onedoc[1]);
				}
				bf.close();
				return posting;
			}
			line = bf.readLine();
		}

		bf.close();
		return null;
	}

	// Return the number of documents that contains the token.
	public int GetDocFreq( String token ) throws IOException {
		// use the getPostingList function to find the list of documents including this token
		int[][] posting = GetPostingList(token);
		if(posting==null){
			return 0;
		}
		// the length of the posting list is the result
		return posting.length;
	}
	
	// Return the total number of times the token appears in the collection.
	public long GetCollectionFreq( String token ) throws IOException {
		// also use getPostingList to get the list first
		int[][] posting = GetPostingList(token);
		if(posting==null){
			return 0;
		}
		// the result would be the sum of all counts in different documents
		int count = 0;
		for(int i=0;i<posting.length;i++){
			count+=posting[i][1];
		}
		return count;
	}
	
	public void Close() throws IOException {
		this.idReader.close();
	}
	
}