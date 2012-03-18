package com.cushon.xword;

import org.apache.commons.cli.Options;

import com.cushon.xword.wordsearcher.WordSearcher;

/***
 * 
 * Reads in a wordlist and crossword puzzle and solves it.
 * 
 * @author cushon
 *
 */
public class XWord {	
	
	public static void usage() {
		System.out.println("Usage: xword [-v] grid word-list");
		System.exit(1);
	}
	
	public static void main(String[] args) throws Exception {
		
		boolean verbose = false;
		String gridFilePath = "";
		String wordListFilePath = "";
		
		switch (args.length) {
		case 3:
			if (args[0].equals("-v")) {
				verbose = true;
				gridFilePath = args[1];
				wordListFilePath = args[2];
				break;
			} else {
				usage();
				break;
			}
		case 2:
			gridFilePath = args[0];
			wordListFilePath = args[1];
			break;
		default:
			usage();
		}
		
		boolean[][] rawBoard = ResourceIO.getBoard(gridFilePath);
		
		long start = java.lang.System.nanoTime();
		
		WordSearcher searcher = WordSearcher.ForWordList(ResourceIO.getWordList(wordListFilePath));
		
		Board board = Board.FromBoard(rawBoard, searcher);
		
		Logger logger = new Logger();
		
		new Solver(board, logger).Solve();
		
		if (verbose) {
			long elapsed = java.lang.System.nanoTime() - start;
			System.err.print(logger);
			System.err.printf("\tElapsed time: %fs\n", Math.pow(10, -9) * (double)elapsed);
		}
		
		ResourceIO.printBoard(rawBoard, board);
	}
}