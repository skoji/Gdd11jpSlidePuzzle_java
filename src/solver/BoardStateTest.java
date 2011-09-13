package solver;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BoardStateTest {

	@Test
	public void testNextStates() {
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		BoardState s = new BoardState(b, SolverUtil.createGoal(b));
		List<BoardState> states = s.nextStates();
		
		assertEquals(3, states.size());
		assertEquals("103425", states.get(0).key());
		assertEquals("123045", states.get(1).key());
		assertEquals("123450", states.get(2).key());
	}
	@Test
	public void testNextStates2() {
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		BoardState s = new BoardState(b, SolverUtil.createGoal(b));
		List<BoardState> states = s.nextStates();
		assertEquals("123045", states.get(1).key());
		
		states = states.get(1).nextStates();
		assertEquals(1, states.size());
		assertEquals("023145", states.get(0).key());
	}
	@Test
	public void testNextStates3() {
		Board b = new Board(SolverUtil.boardArray("123045"), 3,2);
		BoardState s = new BoardState(b, SolverUtil.createGoal(b));
		List<BoardState> states = s.nextStates();
		assertEquals(2, states.size());
		assertEquals("023145", states.get(0).key());
		assertEquals("123405", states.get(1).key());
	}

	@Test
	public void testMoveHistoryString() {
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		BoardState s = new BoardState(b, SolverUtil.createGoal(b));
		List<BoardState> states = s.nextStates();
		states = states.get(1).nextStates();
		assertEquals("LU", states.get(0).moveHistoryString());
	}

	@Test
	public void testReverseMoveHistoryString() {
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		BoardState s = new BoardState(b, SolverUtil.createGoal(b));
		List<BoardState> states = s.nextStates();
		states = states.get(1).nextStates();
		assertEquals("DR", states.get(0).reverseMoveHistoryString());
	}

}
