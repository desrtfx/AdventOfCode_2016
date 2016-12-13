package day10;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileIO;

/* 
 * --- Day 10: Balance Bots ---
 * 
 * You come upon a factory in which many robots are zooming 
 * around handing small microchips to each other.
 * 
 * Upon closer examination, you notice that each bot only proceeds 
 * when it has two microchips, and once it does, it gives each one to a different 
 * bot or puts it in a marked "output" bin. Sometimes, bots take microchips from "input" bins, too.
 * 
 * Inspecting one of the microchips, it seems like they each contain a single number; 
 * the bots must use some logic to decide what to do with each chip. 
 * You access the local control computer and download the bots' instructions (your puzzle input).
 * 
 * Some of the instructions specify that a specific-valued microchip 
 * should be given to a specific bot; the rest of the instructions indicate 
 * what a given bot should do with its lower-value or higher-value chip.
 * 
 * For example, consider the following instructions:
 * 
 * value 5 goes to bot 2
 * bot 2 gives low to bot 1 and high to bot 0
 * value 3 goes to bot 1
 * bot 1 gives low to output 1 and high to bot 0
 * bot 0 gives low to output 2 and high to output 0
 * value 2 goes to bot 2
 * Initially, bot 1 starts with a value-3 chip, 
 * and bot 2 starts with a value-2 chip and a value-5 chip.
 * Because bot 2 has two microchips, 
 * it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
 * Then, bot 1 has two microchips; 
 * it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
 * Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
 * In the end, output bin 0 contains a value-5 microchip, 
 * output bin 1 contains a value-2 microchip, and output bin 2 contains a value-3 microchip. 
 * In this configuration, bot number 2 is responsible for 
 * comparing value-5 microchips with value-2 microchips.
 * 
 * Based on your instructions, what is the number of the bot 
 * that is responsible for comparing value-61 microchips with value-17 microchips?
 */

enum BotState {
	INACTIVE, ACTIVE
};

enum InstType {
	INIT, ACTION
};

class Bot {
	
	private static Integer needleLow;
	private static Integer needleHigh;
	
	private final int no;
	private Integer low;
	private Integer high;
	private BotState state;

	public Bot(int no) {
		this.no = no;
		state = BotState.INACTIVE;
	}

	public static void setNeedles(Integer nLow, Integer nHigh) {
		needleLow = nLow;
		needleHigh = nHigh;
	}
	
	public int getNo() {
		return no;
	}

	public Integer getLow() {
		return low;
	}

	public Integer getHigh() {
		return high;
	}

	public BotState getState() {
		return state;
	}

	public void receive(Integer number) {
		if (number == null) {
			return;
		}

		if (low == null) {
			low = number;
			return;
		}

		if (number < low) {
			high = low;
			low = number;
		} else {
			high = number;
		}

		if ((low != null) && (high != null)) {
			state = BotState.ACTIVE; // indicates that Bot has two chips and
										// will handle other instructions
		}

	}

	public BotState checkBotState() {
		if ((low != null) && (high != null)) {
			state = BotState.ACTIVE;
		} else {
			state = BotState.INACTIVE;
		}
		return state;
	}

	public Integer giveLow() {
		Integer val = low;
		low = null;
		return val;
	}

	public Integer giveHigh() {
		Integer val = high;
		high = null;
		return val;
	}

	public void setCompleted() {
		state = BotState.INACTIVE;
	}

	@Override
	public String toString() {
		return "Bot: " + no + "\tlow: " + high + "\thigh: " + high;
	}

}

class Instruction {
	InstType type;
	int botNo;
	Integer value;
	int lowTarget;
	int highTarget;
	boolean lowToOutput;
	boolean highToOutput;

	static final String TXT_INIT = "value (?<value>\\d*) goes to bot (?<target>\\d*)";
	static final String TXT_ACTION = "bot (?<source>\\d*) gives low to (?<typeHigh>\\w*) (?<targHigh>\\d*) and high to (?<typeLow>\\w+) (?<targLow>\\d*)";
	Pattern pInit = Pattern.compile(TXT_INIT);
	Pattern pAction = Pattern.compile(TXT_ACTION);

	public Instruction(String line) {

		// Line format:

		// Init line
		// value 43 goes to bot 206
		// 0 1 2 3 4 5

		// Action line
		// bot 37 gives low to bot 145 and high to bot 25
		// 0 1 2 3 4 5 6 7 8 9 10 11

		if (line.startsWith("value")) {
			type = InstType.INIT;
			Matcher m = pInit.matcher(line);
			if (m.find()) {
				value = Integer.parseInt(m.group("value"));
				botNo = Integer.parseInt(m.group("target"));
				lowTarget = 0;
				highTarget = 0;
				lowToOutput = false;
				highToOutput = false;
			}
		} else {
			type = InstType.ACTION;
			Matcher m = pAction.matcher(line);
			if (m.find()) {
				botNo = Integer.parseInt(m.group("source"));
				highToOutput = ("output".equals(m.group("typeHigh")));
				highTarget = Integer.parseInt(m.group("targHigh"));
				lowToOutput = ("output".equals(m.group("typeLow")));
				lowTarget = Integer.parseInt(m.group("targLow"));
			}
		}
	}

	public InstType getType() {
		return type;
	}

	public int getBotNo() {
		return botNo;
	}

	public Integer getValue() {
		return value;
	}

	public int getLowTarget() {
		return lowTarget;
	}

	public int getHighTarget() {
		return highTarget;
	}

	public boolean isLowToOutput() {
		return lowToOutput;
	}

	public boolean isHighToOutput() {
		return highToOutput;
	}


}

public class Day10 {

	public static final String FILENAME = "./resources/Input_Day10.txt";

	private Map<Integer, Bot> bots;
	private Map<Integer, Integer> outputs;
	private List<Instruction> instructions;

	private static List<String> lines = FileIO.getFileAsList(FILENAME);

	public void parseInstructions() {
		for (String line : lines) {
			instructions.add(new Instruction(line));
		}
	}

}
