package day4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import util.FileIO;

/*--- Day 4: Security Through Obscurity ---
 * 
 * Finally, you come across an information kiosk with a list of rooms. 
 * Of course, the list is encrypted and full of decoy data, 
 * but the instructions to decode the list are barely hidden nearby. 
 * Better remove the decoy data first.
 * 
 * Each room consists of an encrypted name (lowercase letters separated by dashes) 
 * followed by a dash, a sector ID, and a checksum in square brackets.
 * 
 * A room is real (not a decoy) if the checksum is the five most common letters
 *  in the encrypted name, in order, with ties broken by alphabetization. For example:
 * 
 * aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are 
 * a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
 * a-b-c-d-e-f-g-h-987[abcde] is a real room because although 
 * the letters are all tied (1 of each), the first five are listed alphabetically.
 * not-a-real-room-404[oarel] is a real room.
 * totally-real-room-200[decoy] is not.
 * 
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 * 
 * What is the sum of the sector IDs of the real rooms?
 * 
 * 
 * --- Part Two ---
 * 
 * With all the decoy data out of the way, it's time to decrypt this list and get moving.
 * 
 * The room names are encrypted by a state-of-the-art shift cipher, 
 * which is nearly unbreakable without the right software. 
 * However, the information kiosk designers at Easter Bunny HQ 
 * were not expecting to deal with a master cryptographer like yourself.
 * 
 * To decrypt a room name, rotate each letter forward through the alphabet 
 * a number of times equal to the room's sector ID. A becomes B, B becomes C, 
 * Z becomes A, and so on. Dashes become spaces.
 * 
 * For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.
 * 
 * What is the sector ID of the room where North Pole objects are stored?
*/

public class Day4 {

	private static class Room {
		private String rawName;
		private String name;
		private String decryptedName;
		private int number;
		private String checksum;
		private boolean valid;
		private String top5;

		private Map<Character, Integer> freq = new HashMap<>();

		public Room(String line) {
			Matcher m = pattern.matcher(line);
			if (m.find()) {
				rawName = m.group(1);
				name = m.group(1).replace("-", "");
				number = Integer.parseInt(m.group(2));
				checksum = m.group(3);
				valid = (checksum.length() == 5);
				calcFreq();
				top5();
				checkValid();
				decrypt();
			}
		}

		public int getNumber() {
			return number;
		}

		public String getDecryptedName() {
			return decryptedName;
		}

		public boolean isValid() {
			return valid;
		}

		private void calcFreq() {
			for (char c : name.toCharArray()) {
				freq.put(c, freq.getOrDefault(c, 0) + 1);
			}
		}

		private void top5() {
			top5 = freq.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(5).map(Map.Entry::getKey)
					.map(c -> Character.toString(c)).collect(Collectors.joining(""));
		}

		private void checkValid() {
			valid = checksum.equals(top5);
		}

		private void decrypt() {
			char[] encrypted = rawName.toCharArray();

			for (int i = 0; i < encrypted.length; i++) {
				encrypted[i] = (encrypted[i] == '-') ? ' ' : (char) ((((encrypted[i] - 'a') + number) % 26) + 'a');
			}
			decryptedName = new String(encrypted);
		}
	}

	private static final String FILENAME = "./resources/Input_Day4.txt";

	private static List<String> rawData;
	private static List<Room> processedData;

	private static final String GROUPS = "(.*)-(\\d+)\\[(\\w+)\\]";
	private static Pattern pattern = Pattern.compile(GROUPS);

	static {
		rawData = FileIO.getFileAsList(FILENAME);
		processedData = new ArrayList<>();

		for (String line : rawData) {
			processedData.add(new Room(line));
		}

	}

	public int part1() {
		int sum = 0;
		for (Room r : processedData) {
			sum += (r.isValid() ? r.getNumber() : 0L);
		}
		return sum;
	}

	public int part2() {
		int sector = 0;
		for (Room r : processedData) {
			if (r.getDecryptedName().startsWith("northpole object storage")) {
				return r.getNumber();
			}
		}
		return sector;
	}

	public static void main(String[] args) {

		Day4 day4 = new Day4();
		System.out.printf("The result for part 1 is: %d%n", day4.part1());
		System.out.printf("The result for part 2 is: %d%n", day4.part2());

	}
}
