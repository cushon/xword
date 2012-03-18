package com.cushon.xword.wordsearcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/***
 * A WordSearcherNode is the node class for a wide-branching lookup tree of
 * words. Each node contains a 2D array of children, where one dimension is
 * a possible lookup character, and another dimension is a possible offset.
 * 
 * The next (character, offset) tuple in the query set is used to determine
 * which child to expand.
 * 
 * When the query set is empty, all words associated with the current node
 * are returned.
 * 
 * It is assumed that query offsets are ordered to be strictly increasing,
 * so all nodes only handle query offsets strictly greater than the offset
 * that was used to reach them from their parent.
 * 
 * @author cushon
 */
public class WordSearcherNode {

	private boolean frozen = false;
	private int length;
	private WordSearcherNode[][] grid;
	private List<String> wordlist = new ArrayList<String>();
	private int roll = 0;
	
	public static int instantiationCount = 0;
	
	/***
	 * @param length - the length of the words in this tree
	 * @param roll - the minimum supported query offset
	 */
	public WordSearcherNode(int length, int roll) {
		this.grid = new WordSearcherNode[26][length - roll];
		this.length = length;
		this.roll = roll;
		++WordSearcherNode.instantiationCount;
	}
	
	/***
	 * Adds a word to this search tree.
	 * 
	 * @param word to be added
	 */
	public void addWord(String word) {
		assert(!this.frozen);
		this.wordlist.add(word);
	}
	
	/***
	 * Adds words to this search tree.
	 * 
	 * @param words - collection to be added.
	 */
	public void addWords(List<String> words) {
		assert(!frozen);
		this.wordlist.addAll(words);
	}
	
	/***
	 * @return all words that are valid upon reaching this node
	 */
	public List<String> getWords() {
		return this.wordlist;
	}
	
	/***
	 * Build out the search tree below this node for all possible (offset, character)
	 * searchs that can be performed from this node in the tree.
	 */
	public void freeze() {
		this.frozen = true;
		Iterator<String> it = this.wordlist.iterator();
		while (it.hasNext()) {
			String word = it.next();
			for (int i = roll; i < word.length(); ++i) {
				int charcode = word.charAt(i) - 'a';
				if (this.grid[charcode][i - roll] == null) {
					this.grid[charcode][i - roll] = new WordSearcherNode(this.length, i + 1);
				}
				this.grid[charcode][i - roll].addWord(word);
			}
		}
 	}
	
	/***
	 * Run a query rooted at this node in the tree. If the query contains
	 * additional query pairs, recurse down the tree. Otherwise return
	 * all words associated with this node.
	 * 
	 * @param query to be run
	 * @return the set of words that match the query
	 */
	public List<String> runQuery(Queue<QueryPair> query) {
		if (!frozen) freeze();
		
		if (query.isEmpty()) {
			return this.wordlist;
		}
		QueryPair curr = query.remove();
		
		WordSearcherNode next = this.grid[curr.getCharcode()][curr.getOffset() - roll];
		if (next == null) {
			return new ArrayList<String>();
		} else {
			return next.runQuery(query);
		}
	}
}
