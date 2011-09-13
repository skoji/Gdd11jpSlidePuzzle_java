package solver;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AstarSolver implements AbstractSolver {
	
	public PriorityQueue<BoardState> forwardPq = new PriorityQueue<BoardState>();
	public Map<String, BoardState> forwardVisitedMap = new HashMap<String, BoardState>();

	public PriorityQueue<BoardState> backwordPq = new PriorityQueue<BoardState>();
	public Map<String, BoardState> backwordVisitedMap = new HashMap<String, BoardState>();

	public Boolean bidirection = true;
	public Boolean backwordOnly = false;
	public String result = null;
	public Boolean endNotified = false;
	
	public int threshold = 1000;
	public AstarSolver(int w, int h, Integer[] data, int threshold) {
		this.threshold = threshold;
		Board b = new Board(data, w, h);
		Board goal =  SolverUtil.createGoal(b);
		forwardPq.add(new BoardState(b, goal));
		backwordPq.add(new BoardState(goal, b));
	}
	
	public String step(PriorityQueue<BoardState> pq, Map<String, BoardState> visitedMap, Map<String, BoardState> revVisitedMap, Boolean isForward) {
		BoardState s = pq.poll();
		if (!s.open) return null;
		for (BoardState newState: s.nextStates()) {
			if (newState.board.isGoal) {
				return newState.moveHistoryString();
			}
			BoardState revVisited = revVisitedMap.get(newState.key());
			if (revVisited != null) {
				if (isForward)
					return newState.moveHistoryString() + revVisited.reverseMoveHistoryString();
				else 
					return revVisited.moveHistoryString() + newState.reverseMoveHistoryString();
			}
			BoardState visited = visitedMap.get(newState.key());
			if (visited != null) {
				if (visited.moves > newState.moves) {
					visited.open = false;
					pq.add(newState);
					visitedMap.put(newState.key(), newState);
				}
			} else {
				pq.add(newState);
				visitedMap.put(newState.key(), newState);
			}
		}
		s.open = false;
		return null;
	}

	public String step()
	{
		String s = null;
		if (! backwordOnly) 
			s = step(forwardPq, forwardVisitedMap, backwordVisitedMap, true);
		if (s == null && (bidirection||backwordOnly)) {
			s = step(backwordPq, backwordVisitedMap, forwardVisitedMap, false);
		}
		return s;
	}
	
	/* (non-Javadoc)
	 * @see solver.AbstactSolver#run()
	 */
	@Override
	public void run()
	{
		while (!forwardPq.isEmpty()) {
			String solved = step();
			if (solved != null) {
				result = solved;
				return;
			}
			if (forwardPq.size()> threshold && threshold > -1) {
				result = null;
				return;
			}
			if (getEndNotified()) {
				System.err.printf("timeout. aborting \n");
				result = null;
				return;
			}
		}
		result = null;
		return;
	}
	
	@Override
	public String getResult() {
		return result;
	}

	@Override
	public void nofityEnd() {
		synchronized(this) {
			endNotified = true;
		}
	}
	public Boolean getEndNotified() {
		synchronized(this) {
			return endNotified;
		}
	}

	
}
