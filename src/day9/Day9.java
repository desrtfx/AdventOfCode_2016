package day9;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileIO;

public class Day9 {

	public enum State {
		IN_REPEAT, IN_TOKEN;
	}

	public static final String FILENAME = "./resources/Input_Day9.txt";
	public static final String DATA;
	public static final String REGEX = "(?<pattern>\\((?<num>\\d*)x(?<repeat>\\d*)\\))";
	public static final Pattern TOKEN = Pattern.compile(REGEX);
	
	
	// part 1 tests
	public static final String TEST_1_1 = "ADVENT";
	public static final String TEST_1_2 = "A(1x5)BC";
	public static final String TEST_1_3 = "(3x3)XYZ";
	public static final String TEST_1_4 = "A(2x2)BCD(2x2)EFG";
	public static final String TEST_1_5 = "(6x1)(1x3)A";
	public static final String TEST_1_6 = "X(8x2)(3x3)ABCY";
	public static final String EXPECTED_1_1 = "ADVENT";
	public static final String EXPECTED_1_2 = "ABBBBBC";
	public static final String EXPECTED_1_3 = "XYZXYZXYZ";
	public static final String EXPECTED_1_4 = "ABCBCDEFEFG";
	public static final String EXPECTED_1_5 = "(1x3)A";
	public static final String EXPECTED_1_6 = "X(3x3)ABC(3x3)ABCY";
	
	// part 2 tests
	public static final String TEST_2_1 = "(3x3)XYZ";
	public static final String TEST_2_2 = "X(8x2)(3x3)ABCY";
	public static final String TEST_2_3 = "(27x12)(20x12)(13x14)(7x10)(1x12)A";
	public static final String TEST_2_4 = "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN";
	public static final long EXPECTED_2_1 = 9L;
	public static final long EXPECTED_2_2 = 20L;
	public static final long EXPECTED_2_3 = 241920L;
	public static final long EXPECTED_2_4 = 445L;

	
	

	static {
		DATA = FileIO.getFileAsString(FILENAME).replace(" ", "");
	}

	public static String parsePart1(String input) {
		EnumSet<State> parseState = EnumSet.noneOf(State.class);
		StringBuilder parsed = new StringBuilder();
		StringBuilder repeat = new StringBuilder();
		StringBuilder token = new StringBuilder();
		int numChar = 0;
		int numRepeat = 0;
		char[] text = input.toCharArray();
		for (char c : text) {
			if ((c == '(') && (!parseState.contains(State.IN_TOKEN) && !parseState.contains(State.IN_REPEAT))) {
				parseState.add(State.IN_TOKEN);
				continue;
			}
			if ((c == ')') && (!parseState.contains(State.IN_REPEAT) && (parseState.contains(State.IN_TOKEN)))) {
				parseState.remove(State.IN_TOKEN);
				parseState.add(State.IN_REPEAT);
				String[] decomp=token.toString().split("x");
				numChar = Integer.parseInt(decomp[0]);
				numRepeat = Integer.parseInt(decomp[1]);
				token.setLength(0); // reset token
				continue;
			}
			if(parseState.contains(State.IN_TOKEN)) {
				token.append(c);
				continue;
			}
			if(numChar >= 1) {
				numChar--;
				repeat.append(c);
				
				if (numChar == 0) { // last character, let's repeat
					String tmp = repeat.toString();
					for(int i = 0; i < numRepeat; i++) {
						parsed.append(tmp);
					}
					repeat.setLength(0); // reset repetition text
					// token.setLength(0); // reset token
					numChar = 0; // reset char counter
					numRepeat = 0; // reset repeat counter
					parseState.remove(State.IN_REPEAT);
				}
			} else { // regular character, just append
				parsed.append(c);
			}
		}

		return parsed.toString();

	}
	
	public static long decompress(String data) {
		if (data.isEmpty()) {
			return 0L;
		}
		long count = 0L;
		Matcher m = TOKEN.matcher(data);
		if (m.find()) { // token found, process
			int preText = data.indexOf('('); // find the first occurrence of the pattern
			count += preText;

			// text before the pattern is accounted for, treat the pattern
			int numChars = Integer.parseInt(m.group("num")); // how many characters
			int repeat = Integer.parseInt(m.group("repeat")); // repeat how many times

			// Subgroup starts at
			int start = data.indexOf(')') + 1; // subgroup starts after the end of the token
			int end = start + numChars; // end of the subgroup
			
			// decompress subgroup & count
			count += decompress(data.substring(start,end)) * repeat;
			
			// process remaining data
			count += decompress(data.substring(end));
			
		} else { // no token, just return length
			return (long)data.length();
		}
		return count;
	}
	

	public static int part1() {
		int count = parsePart1(DATA).length();

		return count;
	}

	public static long part2() {
		return decompress(DATA);
	}
	
	public static void main(String[] args) {
		
		// Part 1 Tests
		System.out.println("--- Part 1 Tests ---");
		String resultP1T1 = parsePart1(TEST_1_1);
		String resultP1T2 = parsePart1(TEST_1_2);
		String resultP1T3 = parsePart1(TEST_1_3);
		String resultP1T4 = parsePart1(TEST_1_4);
		String resultP1T5 = parsePart1(TEST_1_5);
		String resultP1T6 = parsePart1(TEST_1_6);
		System.out.printf("Test 1:\tData: %-20s\tExpected: %-20s\tResult: %-20s\t Passed: %b%n", TEST_1_1, EXPECTED_1_1,resultP1T1,EXPECTED_1_1.equals(resultP1T1));
		System.out.printf("Test 2:\tData: %-20s\tExpected: %-20s\tResult: %-20s\t Passed: %b%n", TEST_1_2, EXPECTED_1_2,resultP1T2,EXPECTED_1_2.equals(resultP1T2));
		System.out.printf("Test 3:\tData: %-20s\tExpected: %-20s\tResult: %-20s\t Passed: %b%n", TEST_1_3, EXPECTED_1_3,resultP1T3,EXPECTED_1_3.equals(resultP1T3));
		System.out.printf("Test 4:\tData: %-20s\tExpected: %-20s\tResult: %-20s\t Passed: %b%n", TEST_1_4, EXPECTED_1_4,resultP1T4,EXPECTED_1_4.equals(resultP1T4));
		System.out.printf("Test 5:\tData: %-20s\tExpected: %-20s\tResult: %-20s\t Passed: %b%n", TEST_1_5, EXPECTED_1_5,resultP1T5,EXPECTED_1_5.equals(resultP1T5));
		System.out.printf("Test 6:\tData: %-20s\tExpected: %-20s\tResult: %-20s\t Passed: %b%n", TEST_1_6, EXPECTED_1_6,resultP1T6,EXPECTED_1_6.equals(resultP1T6));

		// Part 1 Solution
		System.out.println("\n\n--- Part 1 Solution ---");
		System.out.printf("Part 1: result: %d%n%n%n",part1());

		// Part 2 Tests 
		System.out.println("--- Part 2 Tests ---");
		long resultP2T1 = decompress(TEST_2_1);
		long resultP2T2 = decompress(TEST_2_2);
		long resultP2T3 = decompress(TEST_2_3);
		long resultP2T4 = decompress(TEST_2_4);
		System.out.printf("Test 1:\tData: %-60s\tExpected: %8d\tResult: %8d\t Passed: %b%n", TEST_2_1, EXPECTED_2_1,resultP2T1,(EXPECTED_2_1 == resultP2T1));
		System.out.printf("Test 2:\tData: %-60s\tExpected: %8d\tResult: %8d\t Passed: %b%n", TEST_2_2, EXPECTED_2_2,resultP2T2,(EXPECTED_2_2 == resultP2T2));
		System.out.printf("Test 3:\tData: %-60s\tExpected: %8d\tResult: %8d\t Passed: %b%n", TEST_2_3, EXPECTED_2_3,resultP2T3,(EXPECTED_2_3 == resultP2T3));
		System.out.printf("Test 4:\tData: %-60s\tExpected: %8d\tResult: %8d\t Passed: %b%n", TEST_2_4, EXPECTED_2_4,resultP2T4,(EXPECTED_2_4 == resultP2T4));
		
		// Part 2 Solution
		System.out.println("\n\n--- Part 2 Solution ---");
		System.out.printf("Part 2: result: %d%n",part2());
		

	}

}
