package day7;

import java.util.ArrayList;
import java.util.List;

import util.FileIO;

/* --- Day 7: Internet Protocol Version 7 ---
 * 
 * While snooping around the local network of EBHQ, you compile a list of IP addresses 
 * (they're IPv7, of course; IPv6 is much too limited). 
 * You'd like to figure out which IPs support TLS (transport-layer snooping).
 * 
 * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. 
 * An ABBA is any four-character sequence which consists of a pair of two different characters 
 * followed by the reverse of that pair, such as xyyx or abba. 
 * However, the IP also must not have an ABBA within any hypernet sequences, 
 * which are contained by square brackets.
 * 
 * For example:
 * 
 * abba[mnop]qrst supports TLS (abba outside square brackets).
 * abcd[bddb]xyyx does not support TLS (bddb is within square brackets, 
 * even though xyyx is outside square brackets).
 * aaaa[qwer]tyui does not support TLS (aaaa is invalid; 
 * the interior characters must be different).
 * ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, 
 * even though it's within a larger string).
 * How many IPs in your puzzle input support TLS?
 * 
 * --- Part Two ---
 * 
 * You would also like to know which IPs support SSL (super-secret listening).
 * 
 * An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences 
 * (outside any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, 
 * anywhere in the hypernet sequences. An ABA is any three-character sequence which consists of the 
 * same character twice with a different character between them, such as xyx or aba. 
 * A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.
 * 
 * For example:
 * 
 * aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
 * xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
 * aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; 
 * the aaa sequence is not related, because the interior character must be different).
 * zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, 
 * even though zaz and zbz overlap).
 * 
 * How many IPs in your puzzle input support SSL?
 */

public class Day7 {

	public static final String FILENAME = "./resources/Input_Day7.txt";

	public static final String DELIMITER = "\\[|\\]";
	public static final String PART2_PATTERN = "(?<super1>\\w*)(?<hyper>\\[\\w*\\])(?<super2>\\w*)";

	public static List<String> data;

	static {
		data = FileIO.getFileAsList(FILENAME);
	}

	public static boolean hasABBA(String text) {
		if (text == null) {
			return false;
		}
		if (text.length() == 0) {
			return false;
		}
		char[] data = text.toCharArray();
		for (int i = 0; i < data.length - 3; i++) {
			if (data[i] != data[i + 1]) {
				if ((data[i] == data[i + 3]) && (data[i + 1] == data[i + 2])) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean supportsTLS(String address) {
		if (address == null) {
			return false;
		}
		if (address.length() == 0) {
			return false;
		}
		String[] parts = address.split(DELIMITER);
		if (parts.length == 0) {
			return false;
		}
		boolean found = false;
		for (int i = 0; i < parts.length; i++) {
			boolean hasABBA = hasABBA(parts[i]);
			if ((i % 2 == 1) && (hasABBA)) {
				return false;
			}
			if (hasABBA) {
				found = true;
			}
		}
		return found;
	}

	public static List<String> getABA(String text) {
		List<String> aba = new ArrayList<>();
		char[] data = text.toCharArray();
		for (int i = 0; i < data.length - 2; i++) {
			if (data[i] != data[i + 1]) {
				if (data[i] == data[i + 2]) {
					aba.add(new String(new char[] { data[i], data[i + 1] }));
				}
			}
		}
		return aba;
	}

	public static boolean hasBAB(String text, String aba) {
		String bab = "" + aba.charAt(1) + aba.charAt(0) + aba.charAt(1);
		return text.contains(bab);
	}

	public static boolean supportsSSL(String address) {
		if (address == null) {
			return false;
		}
		if (address.length() == 0) {
			return false;
		}
		String[] parts = address.split(DELIMITER);
		if (parts.length == 0) {
			return false;
		}

		List<String> superNet = new ArrayList<>();
		List<String> hyperNet = new ArrayList<>();
		for (int i = 0; i < parts.length; i++) {
			if (i % 2 == 0) {
				superNet.add(parts[i]);
			} else {
				hyperNet.add(parts[i]);
			}
		}

		List<String> abas = new ArrayList<>();
		for (String s : superNet) {
			abas.addAll(getABA(s));
		}
		boolean found = false;
		if (abas.size() > 0) {
			for (String s : hyperNet) {
				for (String aba : abas) {
					if (hasBAB(s, aba)) {
						found = true;
					}
				}
			}
		}

		return found;
	}

	public static int part1() {
		int count = 0;
		for (String s : data) {
			count += supportsTLS(s) ? 1 : 0;
		}

		return count;
	}

	public static int part2() {
		int count = 0;
		for (String s : data) {
			count += supportsSSL(s) ? 1 : 0;

		}
		return count;
	}

	public static void main(String[] args) {
		System.out.println("Part 1: Count=" + part1());
		System.out.println("Part 2: Count=" + part2());

	}

}
