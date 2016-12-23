package day23;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.FileIO;

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
		int value;
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
			value = (isRegister(inst[1])) ? registers.get(inst[1]) : Integer.parseInt(inst[1]);
			int value2 = (isRegister(inst[2]) ? registers.get(inst[2]) : Integer.parseInt(inst[2]));
			ip += (value != 0) ? value2 : 1;
			break;
		case "tgl":
			value = (isRegister(inst[1])) ? registers.get(inst[1]) : Integer.parseInt(inst[1]);
			int target = ip + value;
			if ((target < 0) || (target > program.size()-1)) {
				ip++;
				break;
			} else {
				String[] targetInst = program.get(target).split(" ");
				switch(targetInst[0]) {
				case "inc":
					targetInst[0] = "dec";
					break;
				case "dec":
				case "tgl":
					targetInst[0] = "inc";
					break;
				case "jnz":
					targetInst[0] = "cpy";
					break;
				case "cpy":
					targetInst[0] = "jnz";
					break;
				}
				String newInst = String.join(" ", targetInst);
				program.set(target, newInst);
				ip++;
				break;
			}
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


public class Day23 {
	public static final String FILENAME = "./resources/Input_Day23.txt";

	public int solvePart1() {
		List<String> program = FileIO.getFileAsList(FILENAME);
		CPU cpu = new CPU(program, 7, 0, 0, 0);
		if (cpu.run()) {
			return cpu.getRegisterValue("a");
		} 
		return -1;
	}
	
	public int solvePart2() {
		List<String> program = FileIO.getFileAsList(FILENAME);
		CPU cpu = new CPU(program, 12, 0, 0, 0);
		if (cpu.run()) {
			return cpu.getRegisterValue("a");
		} 
		return -1;
		
	}
	
	public static void main(String[] args) {
		
		Day23 day23 = new Day23();
		
		System.out.printf("Part 1: Register a: %d%n", day23.solvePart1());
		System.out.printf("Part 2: Register a: %d%n", day23.solvePart2());
		
	}
}
