package solver;

import java.util.Arrays;
import java.util.List;

public class Board {
	public Integer [] indexList = {};
	public Integer [] distances = null;
	public Integer [] data = null;
	public PositionPool posPool = null;
	public int w, h;
	public boolean isGoal = false;
	public int cost = -1; 
	public Board(
			Integer [] data,
			int w, int h) {
		this.data = data; this.w = w; this.h = h;
		indexList = new Integer[37];
		posPool = new PositionPool(w,h,data);
		Arrays.fill(indexList, null);
	}
	
	public Board(
			Integer [] data,
			int w, int h,
			PositionPool posPool, Integer[] indexList, Integer[] distances) {
		this.data = data; this.w = w; this.h = h;
		this.posPool = posPool; 
		this.indexList = indexList;
		this.distances = distances;
		cost = 0; for (int d: distances) { cost = cost + d; };
		isGoal = (cost == 0);
	}
	
	public Integer [] calcDistances(Board other) {
		distances = new Integer[data.length];
		for (int i = 0; i < data.length; i ++) {
			int x = data[i];
			if (x > 0 && x != 36) {
				distances[i] = posPool.indexToPosition(i).distance(other.posOf(x));
			} else {
				distances[i] = 0;
			}
		}
		cost = 0; for (int d: distances) { cost += d; };
		isGoal = (cost == 0);
		return distances;
	}
	
	public Position posOf(int x) {
		return posPool.indexToPosition(indexOf(x));
	}
	
	public  int indexOf(int x) {
		if (indexList[x] == null) {
			int idx = -1;
			for (int i = 0 ; i < data.length; i ++) { if (data[i] == x) { idx = i; break; } }
			indexList[x] = idx;
		}
		return indexList[x];
	}
	
	public Integer indexOfSpc()
	{
		return indexOf(36);
	}
	
	public Position posOfSpc() {
		return posOf(36);
	}
	
	Position indexToPos(int index) {
		return posPool.indexToPosition(index);
	}
	
	public List<Integer> nexts() {
		return posOfSpc().nexts();
	}
	
	public Board nextBoard(Integer dir, Board goal) {
		Integer i = indexOfSpc();
		Integer x = data[i + dir];
		Integer [] newData = Arrays.copyOf(data, data.length);
		newData[i + dir] = 36;
		newData[i] = x;
		Integer [] newIndexList = Arrays.copyOf(indexList, indexList.length);
		newIndexList[36] = i + dir;
		newIndexList[x] = i;
		
		Integer [] newDistances = Arrays.copyOf(distances, distances.length);
		newDistances[i] = indexToPos(i).distance(goal.posOf(x));
		newDistances[i + dir] = 0;
		return new Board(newData, w, h, posPool, newIndexList, newDistances);
	}
}
