package com.cushon.xword;

/***
 * 
 * A tuple of (string, int) corresponding to a possible value
 * assignment of some word in the cross word puzzle, and a
 * weight that indicates how favorable it is.
 * 
 * @author cushon
 *
 */
public class WordCandidate implements Comparable<WordCandidate> {
	double weight;
	String value;
	
	/***
	 * @param weight of the assignment
	 * @param value of the assignment
	 */
	public WordCandidate(double weight, String value) {
		this.weight = weight;
		this.value = value;
	}
	
	/***
	 * @return assignment value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public int compareTo(WordCandidate o) {
		if (weight != o.weight) {
			return new Double(weight).compareTo(o.weight);
		} else {
			return this.value.compareTo(o.value);
		}
	}
}
