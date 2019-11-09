package Search;

import java.io.IOException;
import java.util.*;

import Classes.Query;
import Classes.Document;
import IndexingLucene.MyIndexReader;

public class QueryRetrievalModel {
	
	protected MyIndexReader indexReader;

	// the total number of tokens in the document collection
	protected long totalLength;

	// miu in the  Dirichlet smoothing method
	protected int MIU = 2000;
	
	public QueryRetrievalModel(MyIndexReader ixreader) {
		indexReader = ixreader;
		totalLength = indexReader.getTotalContentLength();
	}
	
	/**
	 * Search for the topic information. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * TopN specifies the maximum number of results to be returned.
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @return
	 */
	
	public List<Document> retrieveQuery( Query aQuery, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the documents based on their relevance score, from high to low

		// get tokens from one query
		String[] tokens = aQuery.GetQueryContent().split(" ");
		List<Document> res = new ArrayList<>();
		HashMap<Integer,Document> map = new HashMap<>();

		// use a priorityQueue to save the results
		PriorityQueue<Document> queue = new PriorityQueue<>(TopN, new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				if(o1.score()<o2.score()){
					return 1;
				}else {
					return -1;
				}
			}
		});


		// calculate the score based on language model
		for(String x: tokens){
			int[][] posting = this.indexReader.getPostingList(x);
			if(posting == null){
				continue;
			}
			for(int[] post:posting){
				double prob = (post[1]+(double)this.MIU*this.indexReader.CollectionFreq(x)/this.totalLength)/(this.indexReader.docLength(post[0])+(double)this.MIU);
				if(map.containsKey(post[0])){
					Document d = map.get(post[0]);
					d.setScore(d.score()*prob);
					map.put(post[0],d);
				}else {
					Document d = new Document(Integer.toString(post[0]),this.indexReader.getDocno(post[0]),prob);
					map.put(post[0],d);
				}
			}
		}

		// add each document into the priority queue
		for(Integer key: map.keySet()){
			queue.add(map.get(key));
		}

		// select the top N from the priority queue
		for(int i=0;i<TopN && i<queue.size();i++){
			res.add(queue.poll());
		}
		return res;
	}
	
}