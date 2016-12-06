package day5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* --- Day 5: How About a Nice Game of Chess? ---
* 
* You are faced with a security door designed by Easter Bunny engineers 
* that seem to have acquired most of their security knowledge by watching hacking movies.
* 
* The eight-character password for the door is generated one character 
* at a time by finding the MD5 hash of some Door ID (your puzzle input) 
* and an increasing integer index (starting with 0).
* 
* A hash indicates the next character in the password if 
* its hexadecimal representation starts with five zeroes. 
* If it does, the sixth character in the hash is the next character of the password.
* 
* For example, if the Door ID is abc:
* 
* The first index which produces a hash that starts with five zeroes is 3231929, 
* which we find by hashing abc3231929; the sixth character of the hash, 
* and thus the first character of the password, is 1.
* 5017308 produces the next interesting hash, which starts with 000008f82..., 
* so the second character of the password is 8.
* The third time a hash starts with five zeroes is for abc5278568, 
* discovering the character f.
* In this example, after continuing this search a total of eight times, 
* the password is 18f47a30.
* 
* Given the actual Door ID, what is the password?
* 
* 
* --- Part Two ---
* 
* As the door slides open, you are presented with a second door that uses 
* a slightly more inspired security mechanism. Clearly unimpressed by the last 
* version (in what movie is the password decrypted in order?!), 
* the Easter Bunny engineers have worked out a better solution.
* 
* Instead of simply filling in the password from left to right, 
* the hash now also indicates the position within the password to fill. 
* You still look for hashes that begin with five zeroes; however, now, 
* the sixth character represents the position (0-7), 
* and the seventh character is the character to put in that position.
* 
* A hash result of 000001f means that f is the second character in the password. 
* Use only the first result for each position, and ignore invalid positions.
* 
* For example, if the Door ID is abc:
* 
* The first interesting hash is from abc3231929, 
* which produces 0000015...; so, 5 goes in position 1: _5______.
* In the previous method, 5017308 produced an interesting hash; 
* however, it is ignored, because it specifies an invalid position (8).
* The second interesting hash is at index 5357525, 
* which produces 000004e...; so, e goes in position 4: _5__e___.
* You almost choke on your popcorn as the final character 
* falls into place, producing the password 05ace8e3.
* 
* Given the actual Door ID and this new method, what is the password? 
* Be extra proud of your solution if it uses a cinematic "decrypting" animation.
* 
*/

public class Day5 {

	private static final String INPUT = "uqwqemis";
	private static final String TEST_INPUT = "abc";
	private static final String TEST_EXPECTED_1 = "18f47a30";
	private static final String TEST_EXPECTED_2 = "05ace8e3";

	private static final int BYTE_0_MASK = 0xFF; // since bytes are signed, they
													// can lead to 0xFFFF as
													// int.
	private static final int POSITIVE_OFFSET = 0x100; // to compensate for
														// negative bytes, add
														// 256 to the value

	public static String part1(String input) {
		try {
			return getPassword1(input);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return "";
	}

	public static String part2(String input) {
		try {
			return getPassword2(input);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return "";
	}

	public static String getPassword1(String input)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		StringBuilder password = new StringBuilder();

		int key = 0;

		for (int l = 0; l < 8; l++) { // iterate over the password length
			boolean found = false;
			while (!found) {
				String tmp = input + key;
				md.update(tmp.getBytes());
				byte[] tmpHash = md.digest();
				String hash = md5ToHexString(tmpHash);
				if (hash.startsWith("00000")) {
					password.append(hash.substring(5, 6));
					found = true;
				}
				key++;
			}
		}
		return password.toString();
	}

	public static String getPassword2(String input)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		// StringBuilder password = new StringBuilder();

		char[] password = "--------".toCharArray();

		int key = 0;

		while (!isFilled(password)) { // iterate over the password length
			boolean found = false;
			while (!found) {
				String tmp = input + key;
				md.update(tmp.getBytes());
				byte[] tmpHash = md.digest();
				String hash = md5ToHexString(tmpHash);
				if (hash.startsWith("00000")) {
					int pos = hash.charAt(5) - '0';
					char c = hash.charAt(6);
					if ((pos >= 0) && (pos <= 7)) { // valid position
						if (password[pos] == '-') { // position is unoccupied
							password[pos] = c;
							found = true;
						}
					}
				}
				key++;
			}
		}
		return new String(password);
	}

	public static boolean isFilled(char[] chars) {
		for (char c : chars) {
			if (c == '-') {
				return false;
			}
		}
		return true;
	}

	public static String md5ToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(Integer.toString((b & BYTE_0_MASK) + POSITIVE_OFFSET, 16)
					.substring(1));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String testPass1 = part1(TEST_INPUT);
		String pass1 = part1(INPUT);
		System.out.printf(
				"Test run 1:\texpected: %s\t produced: %s\t passed: %b%n",
				TEST_EXPECTED_1, testPass1, TEST_EXPECTED_1.equals(testPass1));
		System.out.printf("Part 1 password: %s%n", pass1);

		String testPass2 = part2(TEST_INPUT);
		String pass2 = part2(INPUT);

		System.out.printf(
				"Test run 2:\texpected: %s\t produced: %s\t passed: %b%n",
				TEST_EXPECTED_2, testPass2, TEST_EXPECTED_2.equals(testPass2));
		System.out.printf("Part 1 password: %s%n", pass2);

	}

}
