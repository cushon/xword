package com.cushon.xword.wordsearcher;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/***
 * 
 * A WordSearcher is a collection of words that permits searching on sets of
 * (character, offset) tuples. This class is used to find words that could
 * be used to fill in an empty word in a crossword while respecting previously
 * filled in intersecting words.
 * 
 * @author cushon
 *
 */
public class WordSearcher {
	HashMap<Integer, WordSearcherNode> lengthToXWordNode;
	boolean frozen;
	
	/***
	 * A static constructor that populates the WordSearcher with the
	 * word list. 
	 * 
	 * @param wordlist - the set of words to search over
	 * @return a constructed WordSearcher
	 */
	public static WordSearcher ForWordList(List<String> wordlist) {
		WordSearcher searcher = new WordSearcher();
		
		for (String word : wordlist) {
			searcher.AddWord(word);
		}
		
		return searcher;
	}
	
	public WordSearcher() {
		lengthToXWordNode = new HashMap<Integer, WordSearcherNode>();
		frozen = false;
	}
	
	/***
	 * Add a word to the searchable set.
	 * 
	 * @param word to add
	 */
	public void AddWord(String word) {
		assert(frozen == false);
		if (lengthToXWordNode.containsKey(word.length())) {
			lengthToXWordNode.get(word.length()).addWord(word);
		} else {
			WordSearcherNode node = new WordSearcherNode(word.length(), 0);
			node.addWord(word);
			lengthToXWordNode.put(word.length(), node);
		}
	}
	
	/***
	 * Get a query builder to search for words of the given length.
	 * 
	 * @param length of word to search for
	 * @param seen - the set of words to exclude
	 * @return a new query builder
	 */
	public Query getQuery(int length, Set<String> seen) {
//		assert(frozen == true);
		return new Query(lengthToXWordNode.get(length), seen);
	}
}
