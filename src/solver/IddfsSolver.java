package solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IddfsSolver implements AbstractSolver {

	public int maxdepth = 512;
	public BoardState root = null;
	public int widthCount = 0;
	public long visited = 0;
	public long maxvisit = Integer.MAX_VALUE;
	public String result = null;
	public Boolean endNotified = false;
	public Boolean reverse = false;
	public Map<String, Short> visitedMoves = new HashMap<String, Short>();
	public Map<String, Boolean> deadEnds = new HashMap<String, Boolean>();
	public Boolean recordDeadEnds = false;
		
	public Boolean checkVisited = false;
	public Runtime r = Runtime.getRuntime();

	
	public IddfsSolver(int w, int h, Integer [] data, int maxdepth, long maxvisit) {
		this.maxdepth = maxdepth;
		this.maxvisit = maxvisit;
		Board b = new Board(data, w, h);
		Board goal =  SolverUtil.createGoal(b);
		root = new BoardState(b, goal);
	}

	public IddfsSolver(int w, int h, Integer [] data, int maxdepth, long maxvisit, Boolean reverse) {
		this.maxdepth = maxdepth;
		this.maxvisit = maxvisit;
		Board b = new Board(data, w, h);
		Board goal =  SolverUtil.createGoal(b);
		this.reverse = reverse;
		if (reverse)
			root = new BoardState(goal, b);
		else	
			root = new BoardState(b, goal);
	}

	@Override
	public void run() {
		int currentmaxdepth = root.score + 1;
		String s = null;

		while (maxdepth >= currentmaxdepth) {
			widthCount = 0;
			visited = 0;
			s = dfs(root, 0, currentmaxdepth);
			if (s != null) {
				if (s.length() == 0) {
					System.err.printf("something wrong: all nodes is dead end.\n");
					result = null;
					return;
				}
				if (s.equals("nomem")) {
					System.err.printf("no memory: abort: visited : %d\n", visited);
					r.gc();
					result = null;
					return;
				} 
				result =  s;
				return;
			}
			if (visited >= maxvisit) {
				System.err.printf("aborting: visited too many nodes.\n");
				result =  null;
				return;
			}
			currentmaxdepth += 2;
			if (getEndNotified()) {
				System.err.printf("timeout. aborting \n");
				result = null;
				return;
			}
		}
		result = null;
		return;
	}

	private String dfs(BoardState state, int depth, int currentmaxdepth) {
		if (state.board.isGoal) {
			System.err.printf("goal found at depth: %d, visited: %d\n",depth, visited);
			if (reverse)
				return state.reverseMoveHistoryString();
			else
				return state.moveHistoryString();
		}

		if (r.freeMemory() < 1024 * 640) {
			return "nomem";
		}
		
		if (visited >= maxvisit) {
			return null;
		}

		if (getEndNotified()) {
			return null;
		}
		visited ++;
		if (depth == currentmaxdepth) {
			widthCount ++;
			return null;
		}
		String key = state.key();
		
		if (recordDeadEnds) {
			if (deadEnds.containsKey(key)) 
				return "";
		}

		if (checkVisited) {
			Short bs = visitedMoves.get(key);
			if (bs != null && bs < state.moves) {
				return null;
			}
			visitedMoves.put(key, state.moves);
		}
		
		List<BoardState> nexts = state.nextStates();
		if (nexts.size() == 0) return "";
		
		Integer deadEndsCount = 0;
		String s = null;
		for (BoardState next: nexts) {
			if (next.score <= currentmaxdepth) {
				s = dfs(next, depth + 1, currentmaxdepth);
				if (s != null) 
					if (s.length() > 0)
						return s;
					else
						deadEndsCount ++;
			}
		}
		if (nexts.size() == deadEndsCount) {
			if (recordDeadEnds) {
				deadEnds.put(key, true);
			}
			return "";
		}
		return null;
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
