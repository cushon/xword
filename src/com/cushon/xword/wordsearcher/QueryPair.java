package com.cushon.xword.wordsearcher;

public class QueryPair {
	int charcode;
	int offset;
	
	/***
	 * Encapsulates a (character, offset) query pair.
	 * 
	 * @param character to search for
	 * @param offset at which the character is required
	 */
	public QueryPair(char character, int offset) {
		this.charcode = character - 'a';
		assert (charcode >= 0);
		this.offset = offset;
	}
	
	/***
	 * @return the character's distance from 'a'
	 */
	public int getCharcode() {
		return charcode;
	}
	
	/***
	 * @return the character's offset
	 */
	public int getOffset() {
		return offset;
	}
}
