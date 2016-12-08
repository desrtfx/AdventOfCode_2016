package day8;

import java.util.List;

import util.FileIO;

/* --- Day 8: Two-Factor Authentication ---
 * 
 * You come across a door implementing what you can only assume is an implementation 
 * of two-factor authentication after a long game of requirements telephone.
 * 
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). 
 * Then, it displays a code on a little screen, and you type that code on a keypad. 
 * Then, presumably, the door unlocks.
 * 
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart 
 * and figured out how it works. Now you just have to work out what the screen would have displayed.
 * 
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen; 
 * these instructions are your puzzle input. 
 * The screen is 50 pixels wide and 6 pixels tall, all of which start off, 
 * and is capable of three somewhat peculiar operations:
 * 
 * rect AxB turns on all of the pixels in a rectangle 
 * at the top-left of the screen which is A wide and B tall.
 * 
 * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) 
 * right by B pixels. 
 * Pixels that would fall off the right end appear at the left end of the row.
 * 
 * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) 
 * down by B pixels. 
 * Pixels that would fall off the bottom appear at the top of the column.
 * 
 * For example, here is a simple sequence on a smaller screen:
 * 
 * rect 3x2 creates a small rectangle in the top-left corner:
 * 
 * ###....
 * ###....
 * .......
 * rotate column x=1 by 1 rotates the second column down by one pixel:
 * 
 * #.#....
 * ###....
 * .#.....
 * rotate row y=0 by 4 rotates the top row right by four pixels:
 * 
 * ....#.#
 * ###....
 * .#.....
 * rotate column x=1 by 1 again rotates the second column down by one pixel, 
 * causing the bottom pixel to wrap back to the top:
 * 
 * .#..#.#
 * #.#....
 * .#.....
 * As you can see, this display technology is extremely powerful, 
 * and will soon dominate the tiny-code-displaying-screen market. 
 * That's what the advertisement on the back of the display tries to convince you, anyway.
 * 
 * There seems to be an intermediate check of the voltage used by the display: 
 * after you swipe your card, if the screen did work, how many pixels should be lit?
 * 
 * 
 * --- Part Two ---
 * 
 * You notice that the screen is only capable of displaying capital letters; 
 * in the font it uses, each letter is 5 pixels wide and 6 tall.
 * 
 * After you swipe your card, what code is the screen trying to display?
 */


public class Day8 {

	public static final String FILENAME = "./resources/Input_Day8.txt";
	public static final String DELIMITER = " ";
	
	public static final int ROWS = 6;
	public static final int COLS = 50;

	public static List<String[]> data;
	
	private static char[][] disp = new char[ROWS][COLS];
	
	
	
	static {
		// get data
		data = FileIO.getFileLinesSplit(FILENAME, DELIMITER);
		
		// initialize display
		for(int row = 0; row < disp.length; row++) {
			for(int col = 0; col < disp[row].length; col++) {
				disp[row][col] = '.';
			}
		}
	}
	
	public static void display() {
		for(int row = 0; row < disp.length; row++) {
			System.out.println(new String(disp[row]));
		}
	}
	
	public static void rect(int width, int height) {
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				disp[row][col] = '#';
			}
		}
	}
	
	public static void rotateRow(int row, int by) {
		for(int i = 0; i < by; i++) {
			char last = disp[row][disp[row].length - 1];
			for (int col = disp[row].length-1; col > 0; col--) {
				disp[row][col] = disp[row][col - 1];
			}
			disp[row][0] = last;
		}
	}

	public static void rotateCol(int col, int by) {
		for(int i = 0; i < by; i++) {
			char last = disp[disp.length - 1][col];
			for(int row = disp.length - 1; row > 0; row--) {
				disp[row][col] = disp[row - 1][col];
			}
			disp[0][col] = last;
		}
	}
	
	public static int countLit() {
		int count = 0;
		for(int row = 0; row < disp.length; row++) {
			for(int col = 0; col < disp[row].length; col++) {
				count += disp[row][col] == '#' ? 1 : 0;
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		
		for(String[] line : data) {
			
			if ("rect".equals(line[0])) {
				String[] tmp = line[1].split("x");
				int width = Integer.parseInt(tmp[0]);
				int height = Integer.parseInt(tmp[1]);
				rect(width, height);
			} else {
				if ("row".equals(line[1])) {
					int row = Integer.parseInt(line[2].replace("y=",""));
					int by = Integer.parseInt(line[4]);
					rotateRow(row, by);
				} else {
					int col = Integer.parseInt(line[2].replace("x=", ""));
					int by = Integer.parseInt(line[4]);
					rotateCol(col, by);
				}
			}
			display();
			System.out.printf("%nLit are: %d LEDs%n%n", countLit());
		}
		

	}

}
