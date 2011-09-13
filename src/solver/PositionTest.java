package solver;

import static org.junit.Assert.*;

import org.junit.Test;

public class PositionTest {
	@Test
	public void testIntToPosition() {
		PositionPool pool = new PositionPool(4,3, SolverUtil.boardArray("123456789AB0"));

		Position p = pool.indexToPosition(9);
		assertEquals(1, p.getX());
		assertEquals(2, p.getY());
	}

	@Test
	public void testDistance() {
		PositionPool pool = new PositionPool(4,4, SolverUtil.boardArray("123456789ABCDEF0"));
		assertEquals(4, pool.position(0,0).distance(pool.position(2,2)));
	}

	@Test
	public void testTo_index() {
		PositionPool pool = new PositionPool(4,3,SolverUtil.boardArray("123456789AB0"));
		assertEquals(10, pool.position(2,2).toIndex());
	}

}
