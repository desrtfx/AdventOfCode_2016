package day2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.FileIO;

/*--- Day 2: Bathroom Security ---
 * 
 * You arrive at Easter Bunny Headquarters under cover of darkness. 
 * However, you left in such a rush that you forgot to use the bathroom! 
 * Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.
 * 
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written down. 
 * Instead, please memorize and follow the procedure below to access the bathrooms."
 * 
 * The document goes on to explain that each button to be pressed can be found by starting on the previous button and 
 * moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. 
 * Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button); 
 * press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.
 * 
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You picture a keypad like this:
 * 
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * Suppose your instructions are:
 * 
 * ULL
 * RRDDD
 * LURDL
 * UUUUD
 * 
 * You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
 * Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at "9" after two moves and ignoring the third), ending up with 9.
 * Continuing from "9", you move left, up, right, down, and left, ending with 8.
 * Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 * So, in this example, the bathroom code is 1985.
 * 
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
 */

public class Day2 {
	
	private static final String FILENAME = "./resources/Input_Day2.txt";
	private static final Map<Character,Point> DIRECTIONS = new HashMap<>();
	private static List<String> data;
	private static List<String> testData = new ArrayList<>();

	static {
		DIRECTIONS.put('U', new Point(0, -1));
		DIRECTIONS.put('D', new Point(0, 1));
		DIRECTIONS.put('L', new Point(-1, 0));
		DIRECTIONS.put('R', new Point(1, 0));
		
		data = FileIO.getFileAsList(FILENAME);
		
		testData.add("ULL");
		testData.add("RRDDD");
		testData.add("LURDL");
		testData.add("UUUUD");
	}
	
	
	
	
	public int part1(List<String> data) {
		int row = 2;
		int col = 2;
		
		int keys = 0;
		
		for(String line : data) {
			for(char c : line.toCharArray()) {
				int dx = DIRECTIONS.get(c).x;
				int dy = DIRECTIONS.get(c).y;
				
				int newRow = row + dy;
				int newCol = col + dx;
				
				row = (newRow < 1) ? row : (newRow > 3) ? row : newRow;
				col = (newCol < 1) ? col : (newCol > 3) ? col : newCol;
			}
			keys = (keys * 10) + (((row - 1 ) * 3) + (col));
		}
		
		return keys;
	    
	}
	
	public String part2(List<String> data) {
		char[][] keyPad = 
			{ { ' ', ' ', '1', ' ',' ' },
			  { ' ', '2', '3', '4', ' ' },
			  { '5', '6', '7', '8', '9' },
			  { ' ', 'A', 'B', 'C', ' ' },
			  { ' ', ' ', 'D', ' ', ' ' }
			};
		
		StringBuilder keys = new StringBuilder();
		
		int row = 2;
		int col = 0;
		
		System.out.println(keyPad[row][col]);
		
		for(String line : data) {
			for(char c : line.toCharArray()) {
				int dx = DIRECTIONS.get(c).x;
				int dy = DIRECTIONS.get(c).y;
				
				int newRow = row + dy;
				int newCol = col + dx;
				
				newRow = (newRow < 0) ? row : (newRow > 4) ? row : newRow;
				newCol = (newCol < 0) ? col : (newCol > 4) ? col : newCol;
			
				if (keyPad[newRow][newCol] == ' ') {
					newRow = row;
					newCol = col;
				}
				row = newRow;
				col = newCol;
				
			}
			keys.append(keyPad[row][col]);
		}
		
		
		return keys.toString();
	}
	
	
	
	public static void main(String[] args) {

		Day2 day2 = new Day2();
		
		System.out.printf("The test code for part 1 is: %d%n", day2.part1(testData));
		System.out.printf("The code for part 1 is: %d%n", day2.part1(data));
		System.out.printf("The test code for part 2 is: %s%n", day2.part2(testData));
		System.out.printf("The code for part 2 is: %s%n", day2.part2(data));
		
		
	}

}
