package com.cushon.xword.model;

import java.util.Iterator;

/***
 * 
 * Iterates over the positions of the letters in a XWordWord in a 
 * LTR, top-down order.
 * 
 * @author cushon
 *
 */
public class WordIterator implements Iterator<Position> {
	Word word;
	int index = 0;
	
	/***
	 * @param word to iterate over
	 */
	public WordIterator(Word word) {
		this.word = word;
		this.index = 0;
	}
	
	@Override
	public boolean hasNext() {
		return index < word.getLength();
	}

	@Override
	public Position next() {
		PositionBuilder resultBuilder = Position.getBuilder();
		if (word.isAcross()) {
			resultBuilder.setX(word.start.getX() + index);
			resultBuilder.setY(word.start.getY());
		} else {
			resultBuilder.setX(word.start.getX());
			resultBuilder.setY(word.start.getY() + index);
		}
		++index;
		return resultBuilder.Build();
	}

	@Override
	public void remove() { }
}
