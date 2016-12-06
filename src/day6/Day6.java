package day6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import util.FileIO;

/* --- Day 6: Signals and Noise ---
* 
* Something is jamming your communications with Santa. 
* Fortunately, your signal is only partially jammed, 
* and protocol in situations like this is to switch 
* to a simple repetition code to get the message through.
* 
* In this model, the same message is sent repeatedly. 
* You've recorded the repeating message signal (your puzzle input), 
* but the data seems quite corrupted - almost too badly to recover. Almost.
* 
* All you need to do is figure out which character is most frequent 
* for each position. For example, suppose you had recorded the following messages:
* 
* eedadn
* drvtee
* eandsr
* raavrd
* atevrs
* tsrnev
* sdttsa
* rasrtv
* nssdts
* ntnada
* svetve
* tesnvt
* vntsnd
* vrdear
* dvrsen
* enarar
* The most common character in the first column is e; in the second, a;
* in the third, s, and so on. 
* Combining these characters returns the error-corrected message, easter.
* 
* Given the recording in your puzzle input, 
* what is the error-corrected version of the message being sent?
* 
* 
* --- Part Two ---
* 
* Of course, that would be the message - if you hadn't agreed 
* to use a modified repetition code instead.
* 
* In this modified code, the sender instead transmits what looks like random data, 
* but for each character, the character they actually want to send is slightly 
* less likely than the others. 
* Even after signal-jamming noise, you can look at the letter distributions 
* in each column and choose the least common letter to reconstruct the original message.
* 
* In the above example, the least common character in the first column is a; 
* in the second, d, and so on. 
* Repeating this process for the remaining characters produces the original message, advent.
* 
* Given the recording in your puzzle input and this new decoding methodology, 
* what is the original message that Santa is trying to send?
* 
*/

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
		data.forEach(new Consumer<String>() {

			@Override
			public void accept(String s) {
				for(int i = 0; i < s.length(); i++) {
					freq.get(i).put(s.charAt(i), freq.get(i).getOrDefault(s.charAt(i),0) + 1);
				}				
			}
			
		});

	}
	
	public String getWord(boolean part2) {
		StringBuilder sb = new StringBuilder();
		freq.stream().forEach(new Consumer<Map<Character, Integer>>() {

			@Override
			public void accept(Map<Character, Integer> m) {
				String s = m.entrySet().stream()
						.sorted(Map.Entry.comparingByKey())
						.sorted((part2 ? Map.Entry.comparingByValue() : Collections.reverseOrder(Map.Entry.comparingByValue())))
						.limit(1L)
						.map(Map.Entry::getKey)
						.map(c -> Character.toString(c))
						.collect(Collectors.joining(""));
				sb.append(s);
			}
		});
		return sb.toString();
	}
	
	public String part1() {
		//calcFreq(data);
		return getWord(false);
	}
	
	public String part2() {
		//calcFreq(data);
		return getWord(true);
	}
	
			
	public static void main(String[] args) {
		Day6 day6 = new Day6();

		// Test data
		day6.calcFreq(testData);
		String testResult = day6.part1();
		System.out.printf("Test run part 1:\texpected: %s\tgenerated: %s\tPassed: %b%n",TEST_EXPECTED_1,testResult,TEST_EXPECTED_1.equals(testResult));
		testResult = day6.part2();
		System.out.printf("Test run part 2:\texpected: %s\tgenerated: %s\tPassed: %b%n",TEST_EXPECTED_2,testResult,TEST_EXPECTED_2.equals(testResult));
		
		// real data
		day6.calcFreq(data);
		String part1 = day6.part1();
		System.out.printf("Part 1:\tgenerated:%s%n",part1);
		String part2 = day6.part2();
		System.out.printf("Part 2:\tgenerated:%s%n",part2);

		
	}

}
