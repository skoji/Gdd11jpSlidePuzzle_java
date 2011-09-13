package solver;

import static org.junit.Assert.*;

import org.junit.Test;

public class SlidePuzzleSolverTest {

	@Test
	public void testStep() {
	}

	@Test
	public void testRun() throws InterruptedException {
		AbstractSolver solver = 
			new AstarSolver(3, 2, SolverUtil.boardArray("130425"), 100);
		Thread t = new Thread(solver);
		t.start();
		t.join();
		String solved = solver.getResult();
		assertEquals("LDR", solved);
		
	}

	@Test
	public void testRun2() throws InterruptedException {
		AstarSolver solver = 
			new AstarSolver(3, 3, SolverUtil.boardArray("168452=30"), 10000);
		solver.bidirection = false;
		Thread t = new Thread(solver);
		t.start();
		t.join();
		String solved = solver.getResult();
		assertEquals("ULDRUULDDRUULDDR", solved);
		
	}
	@Test
	public void testRun3() throws InterruptedException {
		AstarSolver solver = 
				new AstarSolver(3, 3, SolverUtil.boardArray("168452=30"), 10000);
			solver.bidirection = false;
			solver.backwordOnly = true;
			Thread t = new Thread(solver);
			t.start();
			t.join();
			String solved = solver.getResult();
			assertEquals("ULDRUULDDRUULDDR", solved);
		
	}
	
	@Test
	public void testRun4() throws InterruptedException {
		IddfsSolver solver = 
				new IddfsSolver(3, 3, SolverUtil.boardArray("168452=30"), 128, 1024, false);
		Thread t = new Thread(solver);
		t.start();
		t.join();
		String solved = solver.getResult();
		assertEquals("ULDRUULDDRUULDDR", solved);
	}
	@Test
	public void testRun5() throws InterruptedException {
		IddfsSolver solver = 
				new IddfsSolver(3, 3, SolverUtil.boardArray("168452=30"), 128, 1024, true);
		Thread t = new Thread(solver);
		t.start();
		t.join();
		String solved = solver.getResult();
		assertEquals("LUURDDLUURDDLURD", solved);
	}
	
	@Test
	public void testRun6() throws InterruptedException {
		IddfsSolver solver = 
				new IddfsSolver(3, 3, SolverUtil.boardArray("168452=30"), 128, 1024, false);
		Thread t = new Thread(solver);
		t.start();
		t.join();
		String solved = solver.getResult();
		assertEquals("ULDRUULDDRUULDDR", solved);
	}
	@Test
	public void testRun7() throws InterruptedException {
		IddfsSolver solver = 
				new IddfsSolver(3, 3, SolverUtil.boardArray("168452=30"), 128, 1024, false);

		solver.reverse = true;
		solver.recordDeadEnds = true;
		Thread t = new Thread(solver);
		t.start();
		t.join();
		String solved = solver.getResult();
		assertEquals("LUURDDLUURDDLURD", solved);
	}
}
