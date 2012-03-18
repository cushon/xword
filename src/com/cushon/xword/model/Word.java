package com.cushon.xword.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.cushon.xword.WordCandidate;
import com.cushon.xword.wordsearcher.Query;
import com.cushon.xword.wordsearcher.WordSearcher;

/***
 * 
 * A word in a crossword puzzle.
 * 
 * @author cushon
 * 
 */
public class Word implements Comparable<Word>, Iterable<Position> {
	
	private static Set<String> seen = new HashSet<String>();;
	
	// The comparator uses instantiation order as a fall back for stable comparisons.
	private static int gid = 0;
	private int id;
	
	Position start;
	Position end;
	
	int remainingValues;
	int degree;

	String value;
	boolean set = false;
	
	PriorityQueue<WordCandidate> weightedPossibilities;
	WordSearcher searcher;

	private List<Word> children;
	
	/***
	 * @param startX starting x coordinate
	 * @param startY starting y coordinate
	 * @param endX ending x coordinate
	 * @param endY ending y coordinate
	 * @param searcher 
	 */
	public Word(int startX, int startY, int endX, int endY, WordSearcher searcher) {
		start = new Position(startX, startY);
		end = new Position(endX, endY);
		this.children = new ArrayList<Word>();
		this.id = gid++;
		this.searcher = searcher;
	}
	
	/***
	 * @return the recorded number of possible values remaining for this
	 * word
	 */
	public int getRemainingValues() {
		return remainingValues;
	}

	/***
	 * @param remainingValues - the recorded number of possible values remaining for this
	 * word
	 */
	public void setRemainingValues(int remainingValues) {
		this.remainingValues = remainingValues;
	}
	
	/***
	 * @param children - words that this word intersects with
	 */
	public void addChildren(Collection<Word> children) {
		this.children.addAll(children);
	}
	
	/***
	 * @return the words that this word intersect with
	 */
	public Collection<Word> getChildren() {
		return children;
	}
	
	/***
	 * @return true if this word goes across the crossword
	 */
	public boolean isAcross() {
		return start.getY() == end.getY();
	}
	
	/***
	 * @return true if this word goes down the crossword
	 */
	public boolean isDown() {
		return start.getX() == end.getX();
	}
	
	/***
	 * @return the word length
	 */
	public int getLength() {
		return Math.max(end.getX() - start.getX(), end.getY() - start.getY()) + 1;
	}
	
	/***
	 * @return the number of unsatisfied intersecting words
	 */
	public int getDegree() {
		return degree;
	}

	/***
	 * @param degree - the number of unsatisfied intersecting words
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	/***
	 * Assigns a value to this word in the crossword, and marks that
	 * value has having been used.
	 * 
	 * @param value - the current value assigned to this word
	 */
	public void setValue(String value) {
		set = true;
		this.value = value;
		seen.add(value);
	}

	/***
	 * Clears the assigned value from this word in the crossword, and
	 * frees the formerly assigned value for use in another space.
	 */
	public void removeValue() {
		set = false;
		seen.remove(value);
		value = null;
	}

	/***
	 * @return true if the word has an assigned value
	 */
	public boolean hasValue() {
		return set;
	}

	/***
	 * Finds the position at which two words in the crossword intersect.
	 * 
	 * @param other - the word to compute the intersection with
	 * @return the position at which the two words intersect
	 */
	public Position getIntersection(Word other) {
		PositionBuilder builder = Position.getBuilder();
		builder.setX(Math.max(start.getX(), other.start.getX()));
		builder.setY(Math.max(start.getY(), other.start.getY()));
		return builder.Build();
	}
	
	/***
	 * Finds the offset into the word where a given position occurs.
	 * 
	 * @param position to compute the offset of
	 * @return offset into the word
	 */
	public int getOffset(Position position) {
		if (isAcross()) {
			return position.getX() - start.getX();
		} else {
			return position.getY() - start.getY();
		}
	}
	
	/***
	 * Returns the character in this the value assigned to this word
	 * that occupies the square with the specified position.
	 * 
	 * @param position of the character to return
	 * @return the character at the specified position
	 */
	public char getChatAt(Position position) {
		return value.charAt(getOffset(position));
	}

	/***
	 * Returns all possible assignments to the current word that
	 * respect existing assignments to intersecting words.
	 * 
	 * @return the set of possible assignents
	 */
	public List<String> getPossibilities() {
		Query query = searcher.getQuery(getLength(), seen);

		for (Word intersectee : children) {
			if (!intersectee.hasValue()) continue;
			Position position = getIntersection(intersectee);
			query.AddParam(intersectee.getChatAt(position), getOffset(position));
		}
		
		return query.Run();
	}
	
	/***
	 * Reset the collection of possible assignments that are being
	 * considered for this word.
	 */
	public void clearWeightedPossibilities() {
		weightedPossibilities = new PriorityQueue<WordCandidate>(1, Collections.reverseOrder());
	}
	
	/***
	 * Add a possible assignment to the set of assignments being considered.
	 * 
	 * @param weightedPossibility - the weighed candidate assignment to add to consideration
	 */
	public void addWeightedPossibility(WordCandidate weightedPossibility) {
		this.weightedPossibilities.add(weightedPossibility);
	}
	
	/***
	 * @return true if the word has any valid assignments still being considered
	 */
	public boolean hasWeightedPossibilities() {
		return !weightedPossibilities.isEmpty();
	}
	
	/***
	 * @return the candidate assignment with the most favourable weight
	 */
	public WordCandidate bestPossibility() {
		return weightedPossibilities.remove();
	}
	
	@Override
	public Iterator<Position> iterator() {
		return new WordIterator(this);
	}
	
	/***
	 * Specify the sort order that will be used to determine which word in the crossword
	 * to expand next while searching the solution space.
	 */
	@Override
	public int compareTo(Word o) {

		// If possible, order using the minimum remaining value heuristic 
		// (fewest legal assignments remaining).
		if (remainingValues != o.remainingValues) {
			return new Integer(remainingValues).compareTo(o.remainingValues);
		}
		
		// To break ties, the word with the most intersections
		// is taken (which approximates the most constraining variable).
		if (degree != o.degree) {
			return new Integer(degree).compareTo(o.degree);
		}
		
		// If the words are equally good, fall back to instantiation order.
		return (this == o) ? 0 : new Integer(id).compareTo(o.id);
	}
	
	@Override
	public String toString() {
		return String.format("%d, %d - %d, %d", start.getX(), start.getY(), end.getX(), end.getY());
	}
}
