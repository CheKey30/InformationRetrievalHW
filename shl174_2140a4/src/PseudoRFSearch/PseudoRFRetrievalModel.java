package PseudoRFSearch;

import java.util.*;

import Classes.Document;
import Classes.Query;
import IndexingLucene.MyIndexReader;
import SearchLucene.QueryRetrievalModel;
import org.apache.lucene.index.Terms;

import javax.print.Doc;

public class PseudoRFRetrievalModel {

	MyIndexReader ixreader;
	int MIU = 2000;
	long totalLength;
	
	public PseudoRFRetrievalModel(MyIndexReader ixreader)
	{
		this.ixreader=ixreader;
		this.totalLength = ixreader.getTotalContentLength();
	}
	
	/**
	 * Search for the topic with pseudo relevance feedback in 2017 spring assignment 4. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<Document> RetrieveQuery( Query aQuery, int TopN, int TopK, double alpha) throws Exception {	
		// this method will return the retrieval result of the given Query, and this result is enhanced with pseudo relevance feedback
		// (1) you should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents
		// (2) implement GetTokenRFScore to get each query token's P(token|feedback model) in feedback documents
		// (3) implement the relevance feedback model for each token: combine the each query token's original retrieval score P(token|document) with its score in feedback documents P(token|feedback model)
		// (4) for each document, use the query likelihood language model to get the whole query's new score, P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
		
		
		//get P(token|feedback documents)
		HashMap<String,Double> TokenRFScore=GetTokenRFScore(aQuery,TopK);
		
		
		// sort all retrieved documents from most relevant to least, and return TopN
		List<Document> results = new ArrayList<Document>();
		QueryRetrievalModel model = new QueryRetrievalModel(ixreader);
		// original retrieval result
		results = model.retrieveQuery(aQuery,TopN);
		String[] tokens = aQuery.GetQueryContent().split(" ");
		for(Document d: results){
			// prob of this document in language model
			double newprob = 1.0;
			for(String token : tokens){
				//  P(token| D)
				double probA = 1.0;
				// P(token|feedback documents)
				double probB = TokenRFScore.get(token);
				// the frequency of token in this document
				int freq = ixreader.getFrequency(token,Integer.parseInt(d.docid()));
				// the frequency of token in collection
				long freqC = ixreader.CollectionFreq(token);
				int docLength = ixreader.docLength(Integer.parseInt(d.docid()));
				probA = ((double) freq+(double) this.MIU*((double)freqC/(double)totalLength))/((double)docLength+(double)this.MIU);
				newprob*=(alpha*probA+(1-alpha)*probB);
			}
			d.setScore(newprob);
		}
		results.sort(new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				if (o1.score() > o2.score()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		List<Document> res = new ArrayList<>();
		// remove 0 prob(too small prob)
		for(Document d: results){
			if(d.score()!=0){
				res.add(d);
			}
		}
		return res;
	}
	
	public HashMap<String,Double> GetTokenRFScore(Query aQuery,  int TopK) throws Exception
	{
		// for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing
		// save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String,Double> TokenRFScore=new HashMap<String,Double>();
		QueryRetrievalModel model = new QueryRetrievalModel(ixreader);
		// get the topK documents of this query
		List<Document> feedbacks = model.retrieveQuery(aQuery,TopK);
		String[] tokens = aQuery.GetQueryContent().split(" ");
		HashSet<String> docIds = new HashSet<>();
		int totalKLength = 0;
		// calculate the totalLength of the topK documents
		for(Document d: feedbacks){
			docIds.add(d.docid());
			totalKLength+=ixreader.docLength(Integer.parseInt(d.docid()));
		}

		for(String token: tokens){
			// the frequency of token in those topK documents c(w,D)
			int freqK = 0;
			// the frequency of token in collection c(w,REF)
			int freq = 0;
			int[][] postList = ixreader.getPostingList(token);
			if(postList != null){
				for(int[] x: postList){
					if(docIds.contains(String.valueOf(x[0]))){
						freqK+=x[1];
					}
					freq+=x[1];
				}
			}

			// calculate the prob by Dirichlet Prior Smoothing
			double prob = ((double) freqK+(double) this.MIU*((double)freq/(double)totalLength))/((double) totalKLength+(double)this.MIU);
			TokenRFScore.put(token,prob);
		}
		return TokenRFScore;
	}
	
	
}