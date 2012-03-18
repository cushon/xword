package com.cushon.xword;

/***
 * 
 * Logger for interesting events during the solving of a crossword puzzle.
 * 
 * @author cushon
 *
 */
public class Logger {
	private int backTracks = 0;
	private int searchNodes = 0;
	private boolean searchOutcome = true;
	
	/***
	 * Record that the solution has failed.
	 */
	public void LogFailed() { searchOutcome = false; }
	
	/***
	 * Record that a candidate was abandoned, and we backtracked while
	 * searching the solution space.
	 */
	public void Backtracked() { backTracks++; }
	
	/***
	 * Record that a new node in the search graph was reached.
	 */
	public void NewSearchNode() { searchNodes++; }
	
	@Override
	public String toString() {
		return String.format(
				"Search completed %s.\n\tReached %d nodes.\n\tBacktracked %d times.\n",
				(searchOutcome ? "successfully" : "unsuccessfully"),
				searchNodes,
				backTracks);
						
	}
}
