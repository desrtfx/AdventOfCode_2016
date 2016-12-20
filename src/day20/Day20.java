package day20;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.FileIO;

/*
 * --- Day 20: Firewall Rules ---
 * 
 * You'd like to set up a small hidden computer here so you can use it to get 
 * back into the network later. However, the corporate firewall only allows 
 * communication with certain external IP addresses.
 * 
 * You've retrieved the list of blocked IPs from the firewall, but the list 
 * seems to be messy and poorly maintained, and it's not clear which IPs are 
 * allowed. Also, rather than being written in dot-decimal notation, they are 
 * written as plain 32-bit integers, which can have any value from 0 through 
 * 4294967295, inclusive.
 * 
 * For example, suppose only the values 0 through 9 were valid, and that you 
 * retrieved the following blacklist:
 * 
 * 5-8
 * 0-2
 * 4-7
 * The blacklist specifies ranges of IPs (inclusive of both the start and end 
 * value) that are not allowed. Then, the only IPs that this firewall allows 
 * are 3 and 9, since those are the only numbers not in any range.
 * 
 * Given the list of blocked IPs you retrieved from the firewall (your puzzle 
 * input), what is the lowest-valued IP that is not blocked?
 * 
 * 
 * --- Part Two ---
 * 
 * How many IPs are allowed by the blacklist?
 */ 

public class Day20 {

	public static final String FILENAME = "./resources/Input_Day20.txt";
	public static final List<String> RAW_DATA = FileIO.getFileAsList(FILENAME);

	public static final List<FwRule> RAW_RULES = new ArrayList<>();
	public static List<FwRule> optRules = new ArrayList<>();
	public static final long MAX_IP = 4294967295L;

	public static void readData() {
		for (String s : RAW_DATA) {
			RAW_RULES.add(new FwRule(s));
		}
	}
	
	

	public static void optimizeRules() {
		Collections.sort(RAW_RULES);

		FwRule curr = RAW_RULES.get(0);
		for (int i = 1; i < RAW_RULES.size(); i++) {
			FwRule test = RAW_RULES.get(i);
			if (curr.minInRange(test)) {
				curr.extendRange(test);
			} else {
				// Minimum is not in range,
				// need new FwRule object
				optRules.add(curr); // add to optimized ruleset
				curr = new FwRule(test);
			}
		}
		// Append the last rule
		optRules.add(curr);
		Collections.sort(optRules);

	}

	public static long freeIP() {
		long cnt = 0;
		for (int i = 1; i < optRules.size(); i++) {
			cnt += (optRules.get(i).getPortMin() - optRules.get(i - 1).getPortMax() - 1L);
		}
		return cnt;
	}

	public static void main(String[] args) {
		readData();
		optimizeRules();
		System.out.println("Part 1 solution: " + (optRules.get(0).getPortMax() + 1));
		System.out.println("Part 2 solution: " + freeIP());

	}

}

class FwRule implements Comparable<FwRule> {

	long portMin;
	long portMax;

	public FwRule(long portMin, long portMax) {
		this.portMin = portMin;
		this.portMax = portMax;
	}

	public FwRule(String line) {
		String[] tmp = line.split("-");
		portMin = Long.parseLong(tmp[0]);
		portMax = Long.parseLong(tmp[1]);
	}

	public FwRule(FwRule copy) {
		portMin = copy.getPortMin();
		portMax = copy.getPortMax();
	}

	public void setPortMin(long portMin) {
		this.portMin = portMin;
	}

	public long getPortMin() {
		return portMin;
	}

	public void setPortMax(long portMax) {
		this.portMax = portMax;
	}

	public long getPortMax() {
		return portMax;
	}

	public boolean minInRange(FwRule other) {
		long pOther = other.getPortMin();
		return ((pOther >= portMin) && (pOther <= portMax + 1));
	}

	public void extendRange(FwRule other) {
		long pOther = other.getPortMax();
		if (portMax < pOther) {
			portMax = pOther;
		}
	}

	public int compareTo(FwRule other) {
		return (portMin < other.getPortMin() ? -1 : portMin == other.getPortMin() ? 0 : 1);
	}

	public String toString() {
		return "" + portMin + "-" + portMax;
	}

}
