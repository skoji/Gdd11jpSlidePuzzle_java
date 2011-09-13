package solver;

import static org.junit.Assert.*;

import org.junit.Test;

public class PositionPoolTest {

	@Test
	public void testPositionPool() {
		PositionPool pool = new PositionPool(6,4, SolverUtil.boardArray("1A=56H8294CB7=KMNLD0GJFI"));
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 24; i ++) {
			if (pool.distances[0][i] != null) {
				b.append(String.format("%d", pool.distances[0][i]));
			} else {
				b.append("x");
			}
		}
		assertEquals("01x5671234562x4567345678", b.toString());

		b = new StringBuilder();
		
		for (int i = 0; i < 24; i ++) {
			if (pool.distances[6][i] != null) {
				b.append(String.format("%d", pool.distances[6][i]));
			} else {
				b.append("x");
			}
		}
		assertEquals("12x4560123451x3456234567", b.toString());

	}

}
