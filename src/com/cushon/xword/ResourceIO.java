package com.cushon.xword;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.cushon.xword.model.Position;
import com.cushon.xword.model.Word;

/***
 * 
 * Collection of utility functions for dealing with input and output of the
 * problem specification.
 * 
 * @author cushon
 *
 */
public class ResourceIO {
	/***
	 * Read a list of words from a file
	 * 
	 * @param resourceName of the file
	 * @return a list of words
	 * @throws IOException
	 */
	static List<String> getWordList(String pathName) throws IOException {
		Set<String> wordset = new HashSet<String>();
		InputStream inputStream = new FileInputStream(pathName);
		@SuppressWarnings("unchecked")
		List<String> lines = IOUtils.readLines(inputStream);
		Iterator<String> it = lines.iterator();
		while (it.hasNext()) {
			String next = it.next();
			if (next.length() > 0) {
				wordset.add(next.toLowerCase());
			}
		}
		return new ArrayList<String>(wordset);
	}
	
	/***
	 * Read a file specifying the board.
	 * 
	 * @param resourceName of the file
	 * @return an array where entry (i, j) is true if square (i, j) of the board
	 * is part of a word
	 * @throws IOException
	 */
	static boolean[][] getBoard(String pathName) throws IOException {
		InputStream inputStream = new FileInputStream(pathName);
		@SuppressWarnings("unchecked")
		List<String> lines = IOUtils.readLines(inputStream);
		
		int width = lines.get(0).length();
		int height = lines.size();
		
		boolean[][] grid = new boolean[width][height];
		
		Iterator<String> it = lines.iterator();
		for (int row = 0; it.hasNext(); ++row) {
			String line = it.next();
			for (int column = 0; column < line.length(); ++column) {
				grid[column][row] = line.charAt(column) == '1';
			}
		}
		
		return grid;
	}
	
	/***
	 * Print a representation of the board.
	 * 
	 * @param boardLayout of the puzzle board
	 * @param board the puzzle board
	 */
	public static void printBoard(boolean[][] boardLayout, Board board) {
		int width = boardLayout.length;
		int height = boardLayout[0].length;
		
		char[][] buffer = new char[width][height];
		for (Word word : board.getWords()) {
			if (!word.hasValue()) continue;
			for (Position position : word) {
				buffer[position.getX()][position.getY()] = word.getChatAt(position);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < width; ++j) {
			for (int i = 0; i < height; ++i) {
				if (!Character.isLetter(buffer[i][j])) {
					sb.append((boardLayout != null && boardLayout[i][j]) ? '#' : '_');
				} else {
					sb.append(buffer[i][j]);
				}
			}
			sb.append('\n');
		}		
		System.out.println(sb.toString());
	}
}
