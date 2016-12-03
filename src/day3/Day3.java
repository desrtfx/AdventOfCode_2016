package day3;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileIO;

/* 
 * --- Day 3: Squares With Three Sides ---
 * 
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture
 * that makes up this part of Easter Bunny HQ. 
 * This must be a graphic design department; the walls are covered in specifications for triangles.
 * 
 * Or are they?
 * 
 * The design document gives the side lengths of each triangle it describes, 
 * but... 5 10 25? Some of these aren't triangles. 
 * You can't help but mark the impossible ones.
 * 
 * In a valid triangle, the sum of any two sides must be larger than the remaining side. 
 * For example, the "triangle" given above is impossible, 
 * because 5 + 10 is not larger than 25.
 * 
 * In your puzzle input, how many of the listed triangles are possible?
 * 
 * --- Part Two ---
 * 
 * Now that you've helpfully marked up their design documents, 
 * it occurs to you that triangles are specified in groups of three vertically. 
 * Each set of three numbers in a column specifies a triangle. Rows are unrelated.
 * 
 * For example, given the following specification, 
 * numbers with the same hundreds digit would be part of the same triangle:
 * 
 * 101 301 501
 * 102 302 502
 * 103 303 503
 * 201 401 601
 * 202 402 602
 * 203 403 603
 * 
 * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
 */


public class Day3 {
	
	
	private static final String FILENAME = "./resources/Input_Day3.txt";
	
	private static final String REGEX = "\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)";
	private static final Pattern PATTERN = Pattern.compile(REGEX);
	
	
	private static List<String> rawData;
	
	static {
		rawData = FileIO.getFileAsList(FILENAME);
	}
	
	private boolean isTriangleLine(String line) {
		Matcher m = PATTERN.matcher(line);
        int[] lengths = new int[3];
		if (m.find()) {
			for(int i = 1; i < 4; i++) {
				lengths[i-1] = Integer.parseInt(m.group(i));
			}
			return isTriangle(lengths);
		}
		return false;
	}
	
	
	private boolean isTriangle(int[] lengths) {
		Arrays.sort(lengths);
		return (lengths[0] + lengths[1] > lengths[2]);
	}
	
	
	public int part1() {
		int count = 0;
		for(String l : rawData) {
			count += isTriangleLine(l) ? 1 : 0;
		}
		return count;
	}
	
	public int part2() {
		int count = 0;
		if (rawData.size() % 3 == 0) {
			for(int i = 0; i < rawData.size(); ) { // no need for increment here
				Matcher m1 = PATTERN.matcher(rawData.get(i++));
				Matcher m2 = PATTERN.matcher(rawData.get(i++));
				Matcher m3 = PATTERN.matcher(rawData.get(i++));
				if(m1.find() && m2.find() && m3.find()) {
					for (int g = 1; g < 4; g++) {
						count += isTriangle(new int[]{
								Integer.parseInt(m1.group(g)), 
								Integer.parseInt(m2.group(g)), 
								Integer.parseInt(m3.group(g))}) ? 1 : 0;
					}
				}
			}
			return count;
		} 
		
		return -1;
	}
	

	public static void main(String[] args) {
		Day3 day3 = new Day3();
		
		System.out.printf("The code for part 1 is: %d%n", day3.part1());
		System.out.printf("The code for part 2 is: %d%n", day3.part2());
	}

}
