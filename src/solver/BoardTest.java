package solver;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testBoardIntegerArrayIntIntPositionArrayIntegerArrayIntegerArray() {
	}

	@Test
	public void testCalcDistances() {
		Board a = new Board(SolverUtil.boardArray("123450"), 3,2);
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		Integer [] distances = b.calcDistances(a);
		assertEquals(6, distances.length);
		assertEquals(Integer.valueOf(0), distances[0]);
		assertEquals(Integer.valueOf(0), distances[1]);
		assertEquals(Integer.valueOf(0), distances[2]);
		assertEquals(Integer.valueOf(0), distances[3]);
		assertEquals(Integer.valueOf(0), distances[4]);
		assertEquals(Integer.valueOf(1), distances[5]);
		assertEquals(1, b.cost);
	}
	
	@Test
	public void testIndexOf() {
		Board b = new Board(new Integer [] {1,3,4,10,-1, 2}, 3, 2);
		assertEquals(3, b.indexOf(10));
	}
	
	@Test
	public void testNexts() {
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		List<Integer> i = b.nexts();
		
		assertEquals(3, i.size());
		assertEquals(-3, (int)i.get(0));
		assertEquals(-1, (int)i.get(1));
		assertEquals(1, (int)i.get(2));
	}
	@Test
	public void testNextBoard() {
		Board a = new Board(SolverUtil.boardArray("123450"), 3,2);
		Board b = new Board(SolverUtil.boardArray("123405"), 3,2);
		b.calcDistances(a);
		Board n = b.nextBoard(-3, a);
		assertEquals("103425", SolverUtil.boardString(n.data));
	}

}
