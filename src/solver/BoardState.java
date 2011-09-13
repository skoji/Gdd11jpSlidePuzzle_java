package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardState implements Comparable<BoardState> {
	public Board board;
	public Board goal;
	public List<Integer> moveHistory;
	public Short moves = -1;
	public Integer lastMove = -999;
	public boolean open = true;
	public Integer score = 0;
	
	public String key() {
		return SolverUtil.boardString(board.data);
	}
	
	public BoardState(Board board, Board goal) {
		this.board = board;
		moves = 0;
		moveHistory = new ArrayList<Integer>();
		this.goal = goal;
		board.calcDistances(goal);
		score = moves + board.cost;
	}

	@SuppressWarnings("unchecked")
	public BoardState(Board board, Integer move, BoardState prevState) {
		this.board = board;
		moves = (short) (prevState.moves + 1);
		moveHistory = 
			(List<Integer>)
			((ArrayList<Integer>)prevState.moveHistory).clone();
		moveHistory.add(move);
		lastMove = move;
		goal = prevState.goal;
		// do not need to calculate distances.
		score = moves + board.cost;
	}
	
	public List<BoardState> nextStates() {
		List<Integer> nextMove = board.nexts();
		nextMove.remove(Integer.valueOf(- lastMove));
		List<BoardState> r = new ArrayList<BoardState>();
		for (Integer i: nextMove) {
			r.add(new BoardState(board.nextBoard(i, goal), i, this));
		}
		return r;
	}

	@Override
	public int compareTo(BoardState o) {
		return score.compareTo(o.score);
	}
	
	public String moveHistoryString()
	{
		char [] tmp = new char[moveHistory.size()];
		Arrays.fill(tmp, 'x');
		for (int i = 0; i < tmp.length; i ++) {
			Integer m = moveHistory.get(i);
			if (m == -1) tmp[i] = 'L';
			if (m == 1) tmp[i] = 'R';
			if (m == board.w) tmp[i] = 'D';
			if (m == -board.w) tmp[i] = 'U';
		}
		return String.valueOf(tmp);
	}
	
	public String reverseMoveHistoryString()
	{
		char [] tmp = new char[moveHistory.size()];
		Arrays.fill(tmp, 'x');
		for (int i = 0; i < tmp.length; i ++) {
			Integer m = moveHistory.get(i);
			if (m == -1) tmp[tmp.length - i -1] = 'R';
			if (m == 1) tmp[tmp.length - i - 1] = 'L';
			if (m == board.w) tmp[tmp.length - i - 1] = 'U';
			if (m == -board.w) tmp[tmp.length - i - 1] = 'D';
		}
		return String.valueOf(tmp);
	}
}
