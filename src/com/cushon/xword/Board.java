package com.cushon.xword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cushon.xword.model.Grid;
import com.cushon.xword.model.Position;
import com.cushon.xword.model.Word;
import com.cushon.xword.wordsearcher.WordSearcher;

/***
 * 
 * Represents a crossword board: specifically, a collection of words that
 * need to be filled.
 * 
 * @author cushon
 *
 */
public class Board {
	private Grid<Word> across;
	private Grid<Word> down;
	private Set<Word> words;
	
	/***
	 * Sets up a Board class from a 2D array indicating where the words
	 * lie in the puzzle.
	 * 
	 * @param grid an array where element (i, j) is true if the square in the
	 * crossword at index (i, j) is a word, false otherwise
	 * @return an instantiated Board class
	 */
	public static Board FromBoard(boolean[][] grid, WordSearcher searcher) {
		int width = grid.length;
		int height = grid[0].length;
		
		Board board = new Board(width, height);
		
		for (int row = 0; row < height; ++row) {
			int from, to;
			for (from = 0; from < width;) {
				for (to = from; to < width && !grid[to][row]; ++to);
				if (from < (to - 1)) {
					board.add(new Word(from, row, to - 1, row, searcher));
				}
				for (from = to; from < width && grid[from][row]; ++from);
			}
		}		

		for (int column = 0; column < width; ++column) {
			int from, to;
			for (from = 0; from < height;) {
				for (to = from; to < height && !grid[column][to]; ++to);
				if (from < (to - 1)) {
					board.add(new Word(column, from, column, to - 1, searcher));
				}
				for (from = to; from < height && grid[column][from]; ++from);
			}
		}
		
		for (Word word : board.words) {
			word.addChildren(board.getIntersections(word));
		}
		
		for (Word word : board.words) {
			word.setRemainingValues(word.getPossibilities().size());
		}
		
		return board;
	}
	
	public Board(int width, int height) {
		this.across = new Grid<Word>(width, height);
		this.down = new Grid<Word>(width, height);
		this.words = new HashSet<Word>();
	}
	
	/***
	 * Retrieves all words in the crossword.
	 * 
	 * @return all words in the crossword
	 */
	public Collection<Word> getWords() {
		return words;
	}
	
	/***
	 * Calculates the intersectees for a given word.
	 * 
	 * @param parent the intersecter
	 * @return list of intersectees
	 */
	private List<Word> getIntersections(Word parent) {
		List<Word> children = new ArrayList<Word>();
		Grid<Word> intersects = parent.isAcross() ? this.down : this.across;
		for (Position position : parent) {
			if (intersects.IsSet(position)) {
				children.add(intersects.Get(position));
			}
		}
		return children;
	}
	
	/***
	 * Adds a word to the crossword.
	 * 
	 * @param word to be added
	 */
	private void add(Word word) {
		words.add(word);
		((word.isAcross()) ? across : down).Set(word, word);
	}
}