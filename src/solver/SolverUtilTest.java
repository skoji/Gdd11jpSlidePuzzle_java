package solver;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolverUtilTest {

	@Test
	public void testBoardNumber() {
		assertEquals(36, SolverUtil.boardNumber('0'));
		assertEquals(10, SolverUtil.boardNumber('A'));
		assertEquals(35, SolverUtil.boardNumber('Z'));
		assertEquals(9, SolverUtil.boardNumber('9'));
		assertEquals(-1, SolverUtil.boardNumber('='));
	}
	
	@Test 
	public void testBoardArray() {
		Integer [] a = SolverUtil.boardArray("12=AZ0");
		assertEquals(6, a.length);
		assertEquals(Integer.valueOf(1), a[0]);
		assertEquals(Integer.valueOf(2), a[1]);
		assertEquals(Integer.valueOf(-1), a[2]);
		assertEquals(Integer.valueOf(10), a[3]);
		assertEquals(Integer.valueOf(35), a[4]);
		assertEquals(Integer.valueOf(36), a[5]);
	}
	
	@Test
	public void testBoardChar() {
		assertEquals('0', SolverUtil.boardChar(36));
		assertEquals('A', SolverUtil.boardChar(10));
		assertEquals('Z', SolverUtil.boardChar(35));
		assertEquals('1', SolverUtil.boardChar(1));
		assertEquals('9', SolverUtil.boardChar(9));
	}
	
	@Test
	public void testBoardString() {
		String s = SolverUtil.boardString(new Integer [] {1,2,-1, 10,35,36});
		assertEquals("12=AZ0", s);
	}
}
