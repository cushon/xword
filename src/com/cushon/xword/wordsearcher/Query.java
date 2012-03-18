package com.cushon.xword.wordsearcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Query {
	WordSearcherNode searcher;
	Queue<QueryPair> query;
	int startOffset;
	Set<String> seen;
	
	/***
	 * A query object allows searches to be built and run against
	 * a wordsearcher, honouring the specified exclusion list.
	 * 
	 * @param searcher - the wordsearcher to query
	 * @param seen - the set of results to exclude
	 */
	public Query(WordSearcherNode searcher, Set<String> seen) {
		this.searcher = searcher;
		this.query = new LinkedList<QueryPair>();
		this.startOffset = -1;
		this.seen = seen;
	}
	
	/***
	 * Add as a (character, offset) requirement to the query. All
	 * words returned by the query will have the specified character
	 * at the specified offset.
	 * 
	 * @param character - to search for
	 * @param offset - offset character is required at
	 * @return the query object the param was added to
	 */
	public Query AddParam(char character, int offset) {
		assert(offset > startOffset);
		startOffset = offset;
		query.add(new QueryPair(character, offset));
		return this;
	}
	
	/***
	 * Run the query, and filter the list of results to exclude query
	 * results as necessary. 
	 * 
	 * @return the result set
	 */
	public List<String> Run() {
		List<String> filteredResults = new ArrayList<String>();
		List<String> queryResults = searcher.runQuery(query);
		if (seen == null) {
			return queryResults;
		}
		for (String s : queryResults) {
			if (!seen.contains(s)) {
				filteredResults.add(s);
			}
		}
		return filteredResults;
	}
}
