package com.cushon.util;

import java.util.Iterator;

/***
 * Wraps an iterable collection so that iterating over it returns an
 * indexed enumerator.
 * 
 * @author cushon
 */
public class IndexedIterable<T> implements Iterable<Tuple<Integer, T>> {

	Iterable<T> baseIterable;
	
	public IndexedIterable(Iterable<T> baseIterable) {
		this.baseIterable = baseIterable;
	}
	
	@Override
	public Iterator<Tuple<Integer, T>> iterator() {
		return new IndexedIterator<T>(baseIterable.iterator());
	}

}
