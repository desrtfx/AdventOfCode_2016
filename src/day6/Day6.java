package day6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.FileIO;

public class Day6 {
	
	public static final String FILENAME = "./resources/Input_Day6.txt";
	public static final String FILENAME_TEST = "./resources/Test_Input_Day6.txt";

	public static List<String> testData;
	public static List<String> data;
	
	public static final String TEST_EXPECTED_1 = "easter";
	public static final String TEST_EXPECTED_2 = "advent";
	
	public List<Map<Character,Integer>> freq = new ArrayList<Map<Character, Integer>>();
	
	static {
		testData = FileIO.getFileAsList(FILENAME_TEST);
		data = FileIO.getFileAsList(FILENAME);
	}
			
	public void calcFreq(List<String> data) {
		// Initialize Maps
		freq.clear();
		String d = data.get(0);
		for(int i = 0; i < d.length(); i++) {
			freq.add(new HashMap<Character, Integer>());
		}
		// parse actual data
		for(String s : data) {
			for(int i = 0; i < s.length(); i++) {
				freq.get(i).put(s.charAt(i), freq.get(i).getOrDefault(s.charAt(i),0) + 1);
			}
		}
	}
	
	public String getWord(boolean part2) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < freq.size(); i++) {
			String s = freq.get(i).entrySet().stream()
					.sorted(Map.Entry.comparingByKey())
					.sorted((part2 ? Map.Entry.comparingByValue() : Collections.reverseOrder(Map.Entry.comparingByValue())))
					.limit(1L)
					.map(Map.Entry::getKey)
					.map(c -> Character.toString(c))
					.collect(Collectors.joining(""));
			sb.append(s);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public String part1(List<String> data) {
		calcFreq(data);
		return getWord(false);
	}
	
	public String part2(List<String> data) {
		calcFreq(data);
		return getWord(true);
	}
	
			
	public static void main(String[] args) {
		Day6 day6 = new Day6();
		
		String testResult = day6.part1(testData);
		System.out.printf("Test run part 1:\texpected: %s\tgenerated: %s\tPassed: %b%n",TEST_EXPECTED_1,testResult,TEST_EXPECTED_1.equals(testResult));
		
		String part1 = day6.part1(data);
		System.out.printf("Part 1:\tgenerated:%s%n",part1);

		testResult = day6.part2(testData);
		System.out.printf("Test run part 2:\texpected: %s\tgenerated: %s\tPassed: %b%n",TEST_EXPECTED_2,testResult,TEST_EXPECTED_2.equals(testResult));
		
		String part2 = day6.part2(data);
		System.out.printf("Part 2:\tgenerated:%s%n",part2);

		
	}

}
