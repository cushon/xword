package com.cushon.xword;

import java.util.PriorityQueue;
import java.util.Stack;


import com.cushon.xword.model.Word;

/***
 * 
 * A solver for crossword puzzles with constrained word lists and no clues.
 * 
 * @author cushon
 *
 */
public class Solver {
	Board board;
	Logger logger;
	
	public Solver(Board board, Logger logger) {
		this.board = board;
		this.logger = logger;
	}
	
	/***
	 * Attempts to find valid assignments to all words in the puzzle.
	 */
	public void Solve() {
		
		// Create a collection of all unsatisfied words, ordered using the
		// minimum remaining value heuristic (fewest legal assignments
		// remaining). To break ties, the word with the most intersections
		// is taken (which approximates the most constraining variable).
		PriorityQueue<Word> pq = new PriorityQueue<Word>(board.getWords());
		
		// A stack of words that were previously assigned to. If we run out
		// of possible assignments and must backtrack, the word at the top
		// of the stack is the first to be unassigned.
		Stack<Word> stack = new Stack<Word>();
		
		// The word currently under consideration.
		Word cand = pq.remove();
		
		search_loop:
		while (true) {
			logger.NewSearchNode();
			
			// First, find all possible values for the current candidate
			// that respect existing assignments, and then decide which
			// one to try first. The least constraining value heuristic
			// is applied: the assignment that causes the smallest percentage
			// reduction in legal moves for each child of the assigned
			// word is taken.
			cand.clearWeightedPossibilities();
			heuristic_loop:
			for (String string : cand.getPossibilities()) {
				// Temporarily assign the value we are evaluating to the
				// current word
				cand.setValue(string);
				double weight = 0;
				for (Word child : cand.getChildren()) {
					if (child.hasValue()) continue;
					int newRemainingValues = child.getPossibilities().size();
					// If any child has no remaining assignments, do
					// not consider this assignment of the parent.
					if (newRemainingValues == 0) {
						cand.removeValue();
						continue heuristic_loop;
					}
					// Otherwise, contribute the percentage of the remaining
					// values for this child to the weighting of this assignment.
					weight += (newRemainingValues / child.getRemainingValues());
				}
				cand.addWeightedPossibility(new WordCandidate(weight, string));
				cand.removeValue();
			}

			//If there are no possible assignments, then we need to backtrack.
			while (!cand.hasWeightedPossibilities()) {
				logger.Backtracked();
				
				// Unassign the current candidate
				cand.removeValue();
				
				// Update the sort attributes of the candidate's unassigned
				// intersectees. An implementation of a PQ with a fast
				// change-key (a fibonacci heap or similar) would improve
				// the asymptotic complexity, but is unlikely to make any
				// practical improvement.
				for (Word child : cand.getChildren()) {
					if (!pq.contains(child)) continue;
					pq.remove(child);
					child.setRemainingValues(child.getPossibilities().size());
					child.setDegree(child.getDegree() + 1);
					pq.add(child);
				}
				pq.add(cand);
				
				// If we can't backtrack any more, then the search space has
				// been exhausted and no satisfying assignment was found.
				// Abort the search.
				if (stack.isEmpty()) {
					logger.LogFailed();
					break search_loop;
				}
				
				// Backtrack to the last decision and undo the assignment that
				// was made.
				cand = stack.pop();
				cand.removeValue();	
			}
			
			// If flow reaches this point, then the current candidate has
			// >= 1 legal assignments.
			
			cand.setValue(cand.bestPossibility().getValue());
			
			stack.push(cand);

			if (pq.isEmpty()) {
				// There are no more candidates, so we have found a
				// satisfying assignment.
				break;
			}
			
			// Rekey each intersectee of the candidate, so the sort order
			// accounts for the new assignment. A priority queue with a proper
			// decrease-key would improve the asymptotic complexity of this
			// operation, but performance isn't limited by this operation...
			for (Word child : cand.getChildren()) {
				if (!pq.contains(child)) continue;
				pq.remove(child);
				child.setRemainingValues(child.getPossibilities().size());
				child.setDegree(child.getDegree() - 1);
				pq.add(child);
			}
			
			// Take the most promising of the remaining unassigned candidates.
			cand = pq.remove();
		}
	}
}
