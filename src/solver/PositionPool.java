package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PositionPool {
	private int w, h;
	private Integer[] data;
	private PositionImpl [] indexToPosList = {};

	public Integer[][] distances;
	
	protected class PositionImpl implements Position {
		private int x;
		private int y;
		
		@Override	
		public int getX() {
			return x;
		}
		
		@Override	
		public int getY() {
			return y;
		}

		private PositionImpl(int x, int y) {
			this.x = x; this.y = y;
		}

		@Override
		public int distance(Position other) {
			return distances[toIndex()][other.toIndex()];
		}

		@Override
		public int toIndex() {
			return PositionPool.toIndex(x,y,w);
		}

		@Override
		public List<Integer> nexts() {
			List<Integer> r = new ArrayList<Integer>();
			int i = toIndex();
			if (y > 0 && data[i - w] > 0) r.add(-w);
			if (y < h - 1 && data[i + w] > 0) r.add(w);
			if (x > 0 && data[i - 1] > 0) r.add(-1);
			if (x < w - 1 && data[i + 1] > 0) r.add(1);
			return r;
		}
		
	}

	private static int toIndex(int x,int y, int w) {
		return x + y * w;
	}

	public PositionPool(int w, int h, Integer [] data) {
		this.w = w; this.h = h;
		this.data = data;
		indexToPosList = new PositionImpl[data.length];
		Arrays.fill(indexToPosList, null);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j ++ ) {
				indexToPosList[toIndex(i,j,w)] = new PositionImpl(i,j);
			}
		}
		
		// calc distances
		distances = new Integer[data.length][data.length];
		for (int i = 0; i < data.length; i++) {
			Queue<Integer []> q = new LinkedList<Integer []>();
			Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
			q.add(new Integer[] {i, 0});
			while (!q.isEmpty()) {
				Integer [] current = q.poll();
				int index = current[0];
				int step = current[1];
				visited.put(index, true);
				distances[i][index] = step;
				for (int next: indexToPosition(index).nexts()) {
					int nextIndex = index + next;
					if (visited.get(nextIndex) == null) {
						q.add(new Integer[] {nextIndex, step + 1});
					}
				}
			}
		}
	}
	
	static Boolean between(int x, int y, int s) {
		int a = x - s;
		int b = s - y;
		int c = x - y;
		return ((a<=0&&b<=0&&c<=0)||(a>=0&&b>=0&&c>=0))&&
				Math.abs(c)>=Math.abs(a)&&
				Math.abs(c)>=Math.abs(b);
	}
	
	public void weightDistance() {
		List<Position> walls = new ArrayList<Position>();
		for (int i = 0; i < data.length; i ++) {
			if (data[i] < 0) {
				walls.add(indexToPosition(i));
			}
		}
		for (int i = 0; i < data.length; i ++) {
			for (int j = 0; j < data.length; j ++) {
				if (distances[i][j] == null) {
					continue;
				}
				Position p1 = indexToPosition(i);
				Position p2 = indexToPosition(j);
				for (Position w: walls) {
					if (between(p1.getX(), p2.getX(), w.getX()) &&
						between(p1.getY(), p2.getY(), w.getY())) {
						distances[i][j] += 1;
					}
				}
			}
		}
	}
	
	public Position indexToPosition(int x) {
		return indexToPosList[x];
	}

	public PositionImpl position(int x, int y) {
		return indexToPosList[toIndex(x,y,w)];
	}

}
