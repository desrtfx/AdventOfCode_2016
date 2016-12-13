package day12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.FileIO;

/* 
 * --- Day 12: Leonardo's Monorail ---
 * 
 * You finally reach the top floor of this building: 
 * a garden with a slanted glass ceiling. 
 * Looks like there are no more stars to be had.
 * 
 * While sitting on a nearby bench amidst some tiger lilies, 
 * you manage to decrypt some of the files you extracted from the servers downstairs.
 * 
 * According to these documents, Easter Bunny HQ isn't just this building - 
 * it's a collection of buildings in the nearby area. 
 * They're all connected by a local monorail, and there's another building not far from here! 
 * Unfortunately, being night, the monorail is currently not operating.
 * 
 * You remotely connect to the monorail control systems and discover that 
 * the boot sequence expects a password. The password-checking logic (your puzzle input) 
 * is easy to extract, but the code it uses is strange: 
 * it's assembunny code designed for the new computer you just assembled. 
 * You'll have to execute the code and get the password.
 * 
 * The assembunny code you've extracted operates on four registers (a, b, c, and d) 
 * that start at 0 and can hold any integer. 
 * However, it seems to make use of only a few instructions:
 * 
 * cpy x y copies x (either an integer or the value of a register) into register y.
 * inc x increases the value of register x by one.
 * dec x decreases the value of register x by one.
 * jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
 * The jnz instruction moves relative to itself: 
 * an offset of -1 would continue at the previous instruction, 
 * while an offset of 2 would skip over the next instruction.
 * 
 * For example:
 * 
 * cpy 41 a
 * inc a
 * inc a
 * dec a
 * jnz a 2
 * dec a
 * The above code would set register a to 41, increase its value by 2, decrease its value by 1, 
 * and then skip the last dec a (because a is not zero, so the jnz a 2 skips it), 
 * leaving register a at 42. When you move past the last instruction, the program halts.
 * 
 * After executing the assembunny code in your puzzle input, 
 * what value is left in register a?
 * 
 * To begin, get your puzzle input.
 * 
 * --- Part Two ---
 *
 * As you head down the fire escape to the monorail, you notice it didn't start; 
 * register c needs to be initialized to the position of the ignition key.
 *
 * If you instead initialize register c to be 1, what value is now left in register a?
 */

class CPU {
	Map<String, Integer> registers = new HashMap<>(); // CPU registers
	List<String> program; // CPU program
	int ip; // Instruction Pointer
	
	public CPU(List<String> program) { // New CPU with a program
		this(program,0,0,0,0);
		
	}
	
	public CPU(List<String> program, int a, int b, int c, int d) { // New CPU with a program and preset registers
		this.program = program;
		registers.put("a", a);
		registers.put("b", b);
		registers.put("c", c);
		registers.put("d", d);
	}
	
	public int getRegisterValue(String register) {
		return registers.get(register);
	}
	
	public void process(String instruction) {
		String[] inst = instruction.split(" ");
		switch(inst[0]) {
		case "cpy":
			if (isRegister(inst[1])) { // reg to reg copy
				registers.put(inst[2], registers.get(inst[1]));
			} else { // direct to register
				registers.put(inst[2], Integer.parseInt(inst[1]));
			}
			ip++;
			break;
		case "inc":
			registers.put(inst[1], registers.get(inst[1]) + 1);
			ip++;
			break;
		case "dec":
			registers.put(inst[1], registers.get(inst[1]) - 1);
			ip++;
			break;
		case "jnz":
			int value = (isRegister(inst[1])) ? registers.get(inst[1]) : Integer.parseInt(inst[1]);
			ip += (value != 0) ? Integer.parseInt(inst[2]) : 1;
			break;
		}
	}
	
	private boolean isRegister(String data) {
		return (data.charAt(0) >= 'a' && data.charAt(0) <= 'd');
	}
	
	public boolean run() {
		if (program == null) {
			return false;
		}
		ip = 0;
		while (ip < program.size()) {
			process(program.get(ip));
		}
		
		return true;
	}
	
}

public class Day12 {

	public static final String FILENAME = "./resources/Input_Day12.txt";

	public int solvePart1() {
		List<String> program = FileIO.getFileAsList(FILENAME);
		CPU cpu = new CPU(program);
		if (cpu.run()) {
			return cpu.getRegisterValue("a");
		} 
		return -1;
	}
	
	public int solvePart2() {
		List<String> program = FileIO.getFileAsList(FILENAME);
		CPU cpu = new CPU(program, 0, 0, 1, 0);
		if (cpu.run()) {
			return cpu.getRegisterValue("a");
		} 
		return -1;
		
	}
	
	public static void main(String[] args) {
		
		Day12 day12 = new Day12();
		
		System.out.printf("Part 1: Register a: %d%n", day12.solvePart1());
		System.out.printf("Part 2: Register a: %d%n", day12.solvePart2());
		
	}
	
}
