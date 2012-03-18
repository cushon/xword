package com.cushon.util;

/***
 * 
 * A simple 2-tuple container class.
 * 
 * @author cushon
 *
 * @param <T1> first element type
 * @param <T2> second element type
 */
public class Tuple<T1, T2> {
	T1 first;
	T2 second;
	
	Tuple(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	public T1 getFirst() {
		return first;
	}
	
	public T2 getSecond() {
		return second;
	}
}