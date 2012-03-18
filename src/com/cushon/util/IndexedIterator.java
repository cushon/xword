package com.cushon.util;

import java.util.Iterator;

/***
 * Wraps an iterator and adds an index to each value. (Think python's
 * enumerate()).
 * 
 * @author cushon
 *
 */
public class IndexedIterator<T> implements Iterator<Tuple<Integer, T>> {
	
	Iterator<T> baseIterator;
	int index;
	
	public IndexedIterator(Iterator<T> baseIterator) {
		this.baseIterator = baseIterator;
		this.index = 0;
	}

	@Override
	public boolean hasNext() {
		return baseIterator.hasNext();
	}

	@Override
	public Tuple<Integer, T> next() {
		return new Tuple<Integer, T>(index++, baseIterator.next());
	}

	@Override
	public void remove() {
		baseIterator.remove();
	}
}
