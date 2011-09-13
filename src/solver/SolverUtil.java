package solver;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class SolverUtil {
	enum Direction { forward, backword };
	
	static public int boardNumber(char c) {
		c = Character.toUpperCase(c);
		if (c == '=') return -1;
		if ('1' <= c && c <='9') return ((int)c) - 48;
		if (c == '0') return 36;
		if (c >= 'A' && c <= 'Z') {
			return ((int)c) - 55;
		}
		return -9999;
	}
	
	static public Integer [] boardArray(String s) {
		Integer [] result = new Integer[s.length()];
		for (int i = 0;  i < s.length(); i ++) {
			result[i] = boardNumber(s.charAt(i));
		}
		return result;
	}
	
	static public char boardChar(int i) {
		if (i == -1) return '=';
		if (1 <= i && i <= 9) return (char)(i + 48);
		if (i == 36) return '0';
		if (10 <= i && i <= 35) return (char)(i + 55);
		return '$';
	}
	
	static public String boardString(Integer [] arr) {
		char [] temp = new char[arr.length];
		for (int i = 0; i < arr.length; i ++) {
			temp[i] = boardChar(arr[i]);
		}
		return String.valueOf(temp);
	}
	
	static public Board createGoal(Board board) {
		Queue<Integer> temp = new PriorityQueue<Integer>();
		for (Integer i: board.data) {
			if (i > 0) temp.add(i);
		}
		Integer [] newData = Arrays.copyOf(board.data, board.data.length);
		for (int i = 0; i < newData.length; i ++) {
			if (board.data[i] > 0) {
				newData[i] = temp.poll();
			}
		}
		return new Board(newData, board.w, board.h);
	}

}
