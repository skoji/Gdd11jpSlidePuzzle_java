package solver;

import java.util.List;

public interface Position {
	public int distance(Position other);
	public int toIndex();
	public int getX();
	public int getY();
	public List<Integer> nexts();
}