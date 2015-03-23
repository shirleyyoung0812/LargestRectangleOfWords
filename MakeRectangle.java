package RectangleWords;
/**
 * Given a dictionary of millions of words, give an algorithm to find the largest 
 * possible rectangle of letters such that every row forms a word (reading left to right) 
 * and every column forms a word (reading top to bottom).
 * http://shirleyisnotageek.blogspot.com/2015/03/largest-rectangle-of-words.html
 */
import java.util.*;
public class MakeRectangle {
	
	public List<String> largestRectangle(Set<String> dicts){
		if(dicts == null || dicts.size() == 0)
			return null;
		int L = getMax(dicts);
		List<String> rst = new ArrayList<String> ();
		List<String> max = null;
		int maxArea = 0;
		for(int r = L; r >= 1; r--){
			for(int c = L; c >= 1; c--){
				Set<String> rows = findWords(dicts, r);
				if(r == c)
					rst = rectangle(rows, r, rows, c);
				else{
					Set<String> cols = findWords(dicts, c);
					rst = rectangle(rows, r, cols, c);
				}
				if(rst != null){
					//if the longest words can form a rectangle
					//it is guaranteed to be the largest one, return the rst
					if(r == L && c == L)
						return rst;
					//otherwise we wouldn't know which one has the largest area 
					//until we find all valid area
					int currArea = rst.size() * rst.get(0).length();
					if(max == null || maxArea < currArea){
						max = new ArrayList<String> (rst);
						maxArea = currArea;
					}
				}
			}
		}
		return rst;
	}
	private int getMax(Set<String> dicts){
		int length = 0;
		for(String s : dicts)
			length = Math.max(length, s.length());
		return length;
	}
	private Set<String> findWords(Set<String> dicts, int length){
		Set<String> rst = new HashSet<String> ();
		for(String s : dicts){
			if(s.length() == length)
				rst.add(s);
		}
		return rst;
	}
	//m: length of each word in cols
	//n: length of each word in rows
	public List<String> rectangle(Set<String> rows, int n, Set<String> cols, int m){
		//since we need to build a rectangle, if number of words in rows 
		//is smaller than the length of the words in cols, then we will not have 
		//have enough words to build the rectangle, return null
		if(rows.size() < m || cols.size() < n)
			return null;
		List<String> matrix = new ArrayList<String> (m);
		Trie columns = new Trie();
		columns.add(cols);
		List<String> previous = new ArrayList<String> (n);
		for(int i = 0; i < n; i++)
			previous.add("");
		if(build(rows, columns, matrix, 0, previous, m))
			return matrix;
		return null;
	}
	private boolean build(Set<String> rows, Trie columns, List<String> matrix, int row, List<String> previous, int m){
		if(row == m){
			//for the condition where rows and columns are from the same set, we need to avoid duplicates in rows and cols)
			if(previous.get(0).length() == m){
				for(String s : previous){
					if(matrix.contains(s))
						return false;
				}
			}
			return true;
		}
		for(String s : rows){
			if(matrix.contains(s))
				continue;
			boolean valid = true;
			int indexS = 0;
			int indexL = 0;
			while(indexS < s.length() && indexL < previous.size() && valid){
				String tmp = previous.get(indexL++) + s.charAt(indexS++);
				if(!columns.contains(tmp)){
					valid = false;
					break;
				}
			}
			if(!valid)
				continue;
			matrix.add(s);
			indexS = 0;
			indexL = 0;
			while(indexS < s.length() && indexL < previous.size()){
				previous.set(indexL, previous.get(indexL) + s.charAt(indexS));
				indexL++;
				indexS++;
			}
			if(build(rows, columns, matrix, row + 1, previous, m))
				return true;
			matrix.remove(matrix.size() - 1);
			indexL = 0;
			while(indexL < previous.size()){
				String tmp = previous.get(indexL);
				previous.set(indexL, tmp.substring(0, tmp.length() - 1));
			}
		}
		return false;
	}
	
}
