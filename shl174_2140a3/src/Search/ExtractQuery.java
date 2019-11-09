package Search;

import Classes.Path;
import Classes.Query;

import java.io.*;
import java.util.*;

public class ExtractQuery {
	private Queue<Query> queries = new LinkedList<>();
	private HashSet<String> stopWords = new HashSet<>();

	public ExtractQuery() {
		//you should extract the 4 queries from the Path.TopicDir
		//NT: the query content of each topic should be 1) tokenized, 2) to lowercase, 3) remove stop words, 4) stemming
		//NT: you can simply pick up title only for query, or you can also use title + description + narrative for the query content.
		try{
			// read the stop words
			File stopWordsFile = new File(Path.StopwordDir);
			FileReader fileReader = new FileReader(stopWordsFile);
			BufferedReader bf = new BufferedReader(fileReader);
			String line = bf.readLine();
			while (line != null){
				stopWords.add(line.trim());
				line = bf.readLine();
			}

			// read topics
			File topicFile = new File(Path.TopicDir);
			fileReader = new FileReader(topicFile);
			bf = new BufferedReader(fileReader);
			line = bf.readLine();
			while (line != null){
				if(line.equals("<top>")){
					line = bf.readLine();
					Query q = new Query();
					q.SetTopicId(line.split(" ")[2]);
					line = bf.readLine();
					StringBuilder sb = new StringBuilder();
					String[] title = line.toLowerCase().split(" ");
					for(String x: title){
						if(!stopWords.contains(x) && !x.equals("<title>")){
							sb.append(x).append(" ");
						}
					}
					q.SetQueryContent(sb.toString().trim());
					queries.add(q);
				}

				line = bf.readLine();
			}
		}catch (IOException e){
			e.printStackTrace();
		}

	}
	
	public boolean hasNext()
	{
		return !queries.isEmpty();
	}
	
	public Query next()
	{
		if(!this.queries.isEmpty()){
			return queries.poll();
		}else {
			return null;
		}

	}
}
