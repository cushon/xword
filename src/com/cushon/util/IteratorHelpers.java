package com.cushon.util;

/***
 * 
 * Collection of iterator utility functions.
 * 
 * @author cushon
 *
 */
public class IteratorHelpers {
	public static <A, C extends Iterable<A>> Iterable<Tuple<Integer, A>> Enumerate(C iterableCollection) {
		return new IndexedIterable<A>(iterableCollection);
	}
}
