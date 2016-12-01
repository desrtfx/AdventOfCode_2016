package day1;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.FileIO;

/*
 * Santa's sleigh uses a very high-precision clock to guide its movements, 
 * and the clock's oscillator is regulated by stars. 
 * Unfortunately, the stars have been stolen... by the Easter Bunny. 
 * To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. 
 * Two puzzles will be made available on each day in the advent calendar; 
 * the second puzzle is unlocked when you complete the first. 
 * Each puzzle grants one star. Good luck!
 * 
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", 
 * unfortunately, is as close as you can get - 
 * the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, 
 * and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates (where you just landed) and face North. 
 * Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, 
 * then walk forward the given number of blocks, ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though, 
 * so you take a moment and work out the destination. 
 * Given that you can only walk on the street grid of the city, 
 * how far is the shortest path to the destination?
 *
 * For example:
 *
 * Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 * R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 * R5, L5, R5, R3 leaves you 12 blocks away.
 * How many blocks away is Easter Bunny HQ?
 *
 * --- Part Two ---
 *
 * Then, you notice the instructions continue on the back of the Recruiting Document. 
 * Easter Bunny HQ is actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8, 
 * the first location you visit twice is 4 blocks away, due East.
 *
 * How many blocks away is the first location you visit twice?
 */

public class Day1 {

	private static final String FILENAME = "./resources/Input_Day1.txt";

	private static final Map<Character,Integer> TURNS = new HashMap<>(); 
	private static final Map<Character,Point> CARDINALS = new HashMap<>();
	private static final char[] HEADINGS = {'N', 'E' ,'S' ,'W' };
	private static final int NUM_HEADINGS = HEADINGS.length;
	
	private static final int HEADING_N = 0;
	private static final int HEADING_E = 1;
	private static final int HEADING_S = 2;
	private static final int HEADING_W = 3;

	private static String data;
	
	private Point start;;
	private Point current;
	
	private int heading;

	
	static {
		TURNS.put('R', 1);
		TURNS.put('L', -1);
		
		CARDINALS.put('N', new Point(0, -1));
		CARDINALS.put('E', new Point(1, 0));
		CARDINALS.put('S', new Point(0,1));
		CARDINALS.put('W', new Point(-1, 0));
		
		data = FileIO.getFileAsString(FILENAME);
	}
	
	public void part1() {
		
		start = new Point(0, 0);
		current = new Point(0, 0);
		
		heading = HEADING_N;
		
		String[] instructions = data.split(", ");
		
		for(String s : instructions) {
			walk1(s);
		}
		
		int finalDistance = Math.abs(start.x - current.x) + Math.abs(start.y - current.y);
		System.out.println("Part 1: ");
		System.out.println("Final distance: " + finalDistance);
		
		
	}
	
	public void part2() {
		
		Set<Point> visited = new HashSet<>();
		
		start = new Point(0, 0);
		current = new Point(0, 0);
		
		heading = HEADING_N;
		
		String[] instructions = data.split(", ");
		
		// start point
		visited.add(current);
		
		for(String s : instructions) {
			if(walk2(visited,s)) {
				break;
			}
		}
		int finalDistance = Math.abs(start.x - current.x) + Math.abs(start.y - current.y);
		System.out.println("Part 2: ");
		System.out.println("Final distance: " + finalDistance);		
	}
	
	
	
	public void turn(char direction) {
		heading = (heading + NUM_HEADINGS + TURNS.get(direction)) % NUM_HEADINGS;
	}
	
	public void walk1(String instruction) {
		char dir = instruction.charAt(0);
		int dist = Integer.parseInt(instruction.substring(1).trim());
		
		turn(dir);
		Point offset = CARDINALS.get(HEADINGS[heading]);
		int xOffs = offset.x * dist;
		int yOffs = offset.y * dist;
		current.translate(xOffs, yOffs);
		
		
		
	}
	
	public boolean walk2(Set<Point> p, String instruction) {
		char dir = instruction.charAt(0);
		int dist = Integer.parseInt(instruction.substring(1).trim());

		turn(dir);
		Point offset = CARDINALS.get(HEADINGS[heading]);
		
		for(int i=0; i < dist; i++) {
			current.translate(offset.x, offset.y);
			Point n = new Point(current.x, current.y);
			if(p.contains(n)) {
				return true;
			} else {
				p.add(n);
			}
			
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		Day1 day1 = new Day1();
		day1.part1();
		System.out.println("\n");
		day1.part2();
	}

}
