package day14;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * --- Day 14: One-Time Pad ---
 * 
 * In order to communicate securely with Santa while you're on this mission, 
 * you've been using a one-time pad that you generate using a pre-agreed algorithm. 
 * Unfortunately, you've run out of keys in your one-time pad, and so you need to generate some more.
 * 
 * To generate keys, you first get a stream of random data by taking the MD5 of a 
 * pre-arranged salt (your puzzle input) and an increasing integer 
 * index (starting with 0, and represented in decimal); 
 * the resulting MD5 hash should be represented as a string of lowercase hexadecimal digits.
 * 
 * However, not all of these MD5 hashes are keys, and you need 64 new keys for your one-time pad. 
 * A hash is a key only if:
 * 
 * It contains three of the same character in a row, like 777. 
 * Only consider the first such triplet in a hash.
 * One of the next 1000 hashes in the stream contains that 
 * same character five times in a row, like 77777.
 * Considering future hashes for five-of-a-kind sequences does not cause those hashes to be skipped; 
 * instead, regardless of whether the current hash is a key, 
 * always resume testing for keys starting with the very next hash.
 * 
 * For example, if the pre-arranged salt is abc:
 * 
 * The first index which produces a triple is 18, 
 * because the MD5 hash of abc18 contains ...cc38887a5.... 
 * However, index 18 does not count as a key for your one-time pad, 
 * because none of the next thousand hashes (index 19 through index 1018) contain 88888.
 * 
 * The next index which produces a triple is 39; 
 * the hash of abc39 contains eee. It is also the first key: 
 * one of the next thousand hashes (the one at index 816) contains eeeee.
 * 
 * None of the next six triples are keys, but the one after that, 
 * at index 92, is: it contains 999 and index 200 contains 99999.
 * 
 * Eventually, index 22728 meets all of the criteria to generate the 64th key.
 * So, using our example salt of abc, index 22728 produces the 64th key.
 * 
 * Given the actual salt in your puzzle input, what index produces your 64th one-time pad key?
 * 
 * 
 * --- Part Two ---
 * 
 * Of course, in order to make this process even more secure, 
 * you've also implemented key stretching.
 * 
 * Key stretching forces attackers to spend more time generating hashes. 
 * Unfortunately, it forces everyone else to spend more time, too.
 * 
 * To implement key stretching, whenever you generate a hash, before you use it, 
 * you first find the MD5 hash of that hash, then the MD5 hash of that hash, and so on, 
 * a total of 2016 additional hashings. 
 * Always use lowercase hexadecimal representations of hashes.
 * 
 * For example, to find the stretched hash for index 0 and salt abc:
 * 
 * Find the MD5 hash of abc0: 577571be4de9dcce85a041ba0410f29f.
 * Then, find the MD5 hash of that hash: eec80a0c92dc8a0777c619d9bb51e910.
 * Then, find the MD5 hash of that hash: 16062ce768787384c81fe17a7a60c7e3.
 * ...repeat many times...
 * Then, find the MD5 hash of that hash: a107ff634856bb300138cac6568c0f24.
 * So, the stretched hash for index 0 in this situation is a107ff.... 
 * In the end, you find the original hash (one use of MD5), 
 * then find the hash-of-the-previous-hash 2016 times, for a total of 2017 uses of MD5.
 * 
 * The rest of the process remains the same, but now the keys are entirely different. Again for salt abc:
 * 
 * The first triple (222, at index 5) has no matching 22222 in the next thousand hashes.
 * The second triple (eee, at index 10) hash a matching eeeee at index 89, and so it is the first key.
 * Eventually, index 22551 produces the 64th key (triple fff with matching fffff at index 22859.
 * Given the actual salt in your puzzle input and using 2016 extra MD5 
 * calls of key stretching, what index now produces your 64th one-time pad key?
 */

public class Day14 {

	public static final String SALT = "cuanljph";
	private static final int BYTE_0_MASK = 0xFF; 	// since bytes are signed, they
													// can lead to 0xFFFF as
													// int.
	private static final int POSITIVE_OFFSET = 0x100;	// to compensate for
														// negative bytes, add
														// 256 to the value
	
	public static final String PATTERN_1 = "([a-f\\d])\\1\\1";
	public MessageDigest md;
	
	public Day14() {
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String md5ToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(Integer.toString((b & BYTE_0_MASK) + POSITIVE_OFFSET, 16)
					.substring(1));
		}
		return sb.toString().toLowerCase();
	}
	
	public String makeHash(String salt) {
		md.update(salt.getBytes());
		byte[] tmpHash = md.digest();
		String hash = md5ToHexString(tmpHash);
		return hash;
	}
	
	public String makeLongHash(String salt) {
		String hash = makeHash(salt);
		for(int i = 0; i < 2016; i++) {
			hash = makeHash(hash);
		}
		return hash;
	}
	
	
	public int solve(boolean part2) {
		int solution = 0;
		Pattern p = Pattern.compile(PATTERN_1);
		List<String> hashes = new ArrayList<>();
		List<Integer> actual = new ArrayList<>();
		int count = 0;
		int key = 0;
		for (key = 0; key <= 1000; key++) { // fill first 1000 keys
			String tmp = SALT + key;
			hashes.add((part2) ? makeLongHash(tmp) : makeHash(tmp));
		}
		int index = 0;
		while (count < 64) {
			// make the next hash in the series
			String tmp = SALT + (index + 1001);
			hashes.add((part2) ? makeLongHash(tmp) : makeHash(tmp));

			// Check if hash at index is a potential
			Matcher m = p.matcher(hashes.get(index));
			if (m.find()) { // is a potential
				Pattern p2 = Pattern.compile("([" + m.group(1) + "])\\1\\1\\1\\1");
				for(int i = index + 1; i <= index + 1001; i++) {
					Matcher m2 = p2.matcher(hashes.get(i));
					if (m2.find()) { // is a definite
						actual.add(index);
						count++;
						break;
					}
				}
			}
			index++;
		}
		
		solution = actual.get(actual.size()-1);
		return solution;
	}
	
	public int solvePart1() {
		return solve(false);
	}
	
	public int solvePart2() {
		return solve(true);
	}
	
	
	public static void main(String[] args){
		Day14 day14 = new Day14();
		
		System.out.println("Solving part 1 - please standby.");
		long start1 = System.currentTimeMillis();
		int part1 = day14.solvePart1();
		long end1 = System.currentTimeMillis();
		System.out.printf("Solution for part 1: %d - took %f seconds%n", part1, (end1-start1)/1000.0);

		System.out.println("\nSolving part 2 - please standby.");
		long start2 = System.currentTimeMillis();
		int part2 = day14.solvePart2();
		long end2 = System.currentTimeMillis();
		System.out.printf("Solution for part 2: %d - took %f seconds%n", part2, (end2-start2)/1000.0);
		
	}
	
}
