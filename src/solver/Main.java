package solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

enum SolverType {
	Astar,
	Iddfs
}

public class Main {
	private static int threshold = 1000;
	private static int sc = 0;
	private static int tc = 0;
	private static int asc = 0;
	private static InputStream alreadySolved = null;
	private static BufferedReader aBuf = null;

	private static Runtime runtime = Runtime.getRuntime();

	private static int timeoutSec = 120;
	
	// params for ASTAR: should refactor
	private static boolean bidirection = true;
	private static boolean forward = false;
	private static SolverType solverType = SolverType.Astar;
	// params for iddfs;
	private static Integer maxdepth = 150;
	private static long maxvisit = 1024 * 1024 * 1024;
	private static Integer fromNth = 1;
	private static Integer toNth = Integer.MAX_VALUE;
	private static Boolean  reverse = false;
	private static boolean checkVisited = false;
	private static boolean purgeDeadEnds = false;
	
	private static AbstractSolver createSolver(int w, int h, Integer[] data) {
		switch (solverType) {
		case Astar:
			AstarSolver s =  new AstarSolver(w, h, data, threshold);
			s.bidirection = bidirection;
			s.backwordOnly = !forward;
			return s;
		case Iddfs:
			IddfsSolver is =  new IddfsSolver(w, h, data, maxdepth, maxvisit, reverse);
			is.checkVisited = checkVisited;
			is.recordDeadEnds = purgeDeadEnds;
			return is;
		}
		return null;
	}
	public static void processLine(String line) throws InterruptedException {
		String [] splitted = line.split(",");
		Integer w = Integer.valueOf(splitted[0]);
		Integer h = Integer.valueOf(splitted[1]);
		Integer [] data = SolverUtil.boardArray(splitted[2]);
		AbstractSolver solver = createSolver(w,h,data);
				
		if (runtime.freeMemory() < 1024 * 1024) {
			System.err.printf("invoking gc.\n");
			runtime.gc();
		}
		Thread t = new Thread(solver);
		t.start();
		t.join(timeoutSec * 1000);
		if (t.isAlive()) {
			solver.nofityEnd();
			t.join();
		}
		String solved = solver.getResult();
		if (solved == null) {
			System.out.printf("\n");
		} else {
			System.out.printf("%s\n", solved);
			sc ++;
		}
		tc ++;

		if (tc % 100 == 0) {
			System.err.printf("%d / %d (%d)\n", sc, tc, sc - asc);
		}

	}

	public static String getSolvedLine() {
		try {
			if (alreadySolved != null) 
				return aBuf.readLine();
			else 
				return "";
		} catch (IOException e) {
			System.err.printf(e.getMessage());
			return "";
		} 
	}

	public static void openAlreadySolvedResult(String filename) throws FileNotFoundException {
		alreadySolved = new FileInputStream(new File(filename));
		aBuf = new BufferedReader(new InputStreamReader(alreadySolved));
	}

	public static void closeAlreadySolvedResult() throws IOException {
		if (alreadySolved != null) alreadySolved.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Options options = new Options();
		options.addOption("i", true, "input data file");
		options.addOption("e", true, "existing answer file");
		options.addOption("s", true, "strategy: ASTAR, IDDFS");
		options.addOption("d", true, "direction for ASTAR: B (bidirection), F(forward), R(backword)");
		options.addOption("t", true, "threshold: queue size for ASTAR");
		options.addOption("m", true, "maxdepth for IDDFS");
		options.addOption("v", true, "maxvisit for IDDFS");
		options.addOption("r", false, "search from goal in IDDFS");
		options.addOption("c", false, "check visited node cost in IDDFS");
		options.addOption("p", false, "purge dead ends in IDDFS");
		options.addOption("l", true, "timeout seconds for one problem");
		options.addOption("f", true, "start from n");
		options.addOption("g", true, "end at m");
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine;
		try {
		    commandLine = parser.parse(options, args);
		} catch (ParseException e) {
		    System.err.println("can't parse command line options.");
		    HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp( "slide", options );
		    return;
		}
		if (commandLine.getOptions().length == 0) {
		    HelpFormatter formatter = new HelpFormatter();
		    formatter.printHelp( "slide", options );
		    return;
		}
			
		InputStream stream = null;
		try {
			stream = System.in;
			if (commandLine.hasOption('i')) {
				stream = new FileInputStream(commandLine.getOptionValue('i'));
			}
			if (commandLine.hasOption('t')) {
				threshold = Integer.valueOf(commandLine.getOptionValue('t'));
			}
			if (commandLine.hasOption('m')) {
				maxdepth = Integer.valueOf(commandLine.getOptionValue('m'));
			}
			if (commandLine.hasOption('v')) {
				maxvisit = Long.valueOf(commandLine.getOptionValue('m'));
			}
			if (commandLine.hasOption('l')) {
				timeoutSec = Integer.valueOf(commandLine.getOptionValue('l'));
			}
			if (commandLine.hasOption('f')) {
				fromNth = Integer.valueOf(commandLine.getOptionValue('f'));
			}
			if (commandLine.hasOption('g')) {
				toNth = Integer.valueOf(commandLine.getOptionValue('g'));
			}
			if (commandLine.hasOption('r')) {
				reverse = true;
			}
			if (commandLine.hasOption('c')) {
				checkVisited = true;
			}
			if (commandLine.hasOption('p')) {
				purgeDeadEnds = true;
			}
			if (commandLine.hasOption('e')) {
				openAlreadySolvedResult(commandLine.getOptionValue('e'));
			}
			if (commandLine.hasOption('s')) {
				if (commandLine.getOptionValue('s').equals("ASTAR")) {
					solverType = SolverType.Astar;
				} else if (commandLine.getOptionValue('s').equals("IDDFS")){
					solverType = SolverType.Iddfs;
				} else {
					System.err.println("unknown searty type" + commandLine.getOptionValue('s'));
				}
			}
			if (commandLine.hasOption('d')) {
				String d = commandLine.getOptionValue('d');
				if (d == "B") {
					bidirection = true;
				}
				if (d == "F") {
					bidirection = false;
					forward = true;
				}
				if (d == "R") {
					bidirection = false;
					forward = false;
				}
			}
			
			System.err.printf(
					"strategy: %s, threshold: %d, maxdepth: %d, maxvisit: %d, checkVisit: %s, purgeDeadEnd: %s, input: %s, alreadySolved: %s timeout: %d, from: %d, to: %d\n",
					solverType,
					threshold,
					maxdepth,
					maxvisit,
					checkVisited,
					purgeDeadEnds, 
					commandLine.getOptionValue('i'),
					commandLine.getOptionValue('e'),
					timeoutSec,
					fromNth, toNth);
			BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
			buf.readLine();
			buf.readLine();
			for (int i = 1; i < fromNth; i++) {
				buf.readLine();
				String s = getSolvedLine();
				System.out.printf("%s\n", s);
				if (s.length() > 0) {
					sc ++; asc ++;
				}
				tc ++;
				if (tc % 100 == 0) {
					System.err.printf("%d / %d (%d)\n", sc, tc, sc - asc);
				}
			}
			String line;
			String s;
			while ((line = buf.readLine()) != null && tc < toNth) {
				if ((s = getSolvedLine()).length() > 0) {
					System.out.printf("%s\n", s);
					sc ++;
					asc ++;
					tc ++;
					if (tc % 100 == 0) {
						System.err.printf("%d / %d (%d)\n", sc, tc, sc - asc);
					}
				} else {
					processLine(line);	
				}
			}

		} finally {
			if (stream != null) stream.close();
			closeAlreadySolvedResult();
		}

	}


}
