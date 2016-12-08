package day8;

import java.util.List;

import util.FileIO;

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
