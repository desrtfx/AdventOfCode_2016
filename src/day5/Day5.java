package day5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
