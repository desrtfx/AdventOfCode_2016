package day19;

import java.util.LinkedList;

/*
 * --- Day 19: An Elephant Named Joseph ---
 * 
 * The Elves contact you over a highly secure emergency channel. Back at the 
 * North Pole, the Elves are busy misunderstanding White Elephant parties.
 * 
 * Each Elf brings a present. They all sit in a circle, numbered starting with 
 * position 1. Then, starting with the first Elf, they take turns stealing all 
 * the presents from the Elf to their left. An Elf with no presents is removed 
 * from the circle and does not take turns.
 * 
 * For example, with five Elves (numbered 1 to 5):
 * 
 *      1
 *    5   2
 *     4 3
 *  - Elf 1 takes Elf 2's present.
 *  - Elf 2 has no presents and is skipped.
 *  - Elf 3 takes Elf 4's present.
 *  - Elf 4 has no presents and is also skipped.
 *  - Elf 5 takes Elf 1's two presents.
 *  - Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
 *  - Elf 3 takes Elf 5's three presents.
 * 
 * So, with five Elves, the Elf that sits starting in position 3 gets all the 
 * presents.
 * 
 * With the number of Elves given in your puzzle input, which Elf gets all the presents?
 * 
 * 
 * --- Part Two ---
 * 
 * Realizing the folly of their present-exchange rules, the Elves agree to 
 * instead steal presents from the Elf directly across the circle. If two 
 * Elves are across the circle, the one on the left (from the perspective of 
 * the stealer) is stolen from. The other rules remain unchanged: Elves with 
 * no presents are removed from the circle entirely, and the other elves move 
 * in slightly to keep the circle evenly spaced.
 * 
 * For example, with five Elves (again numbered 1 to 5):
 * 
 *  - The Elves sit in a circle; Elf 1 goes first:
 *      1
 *    5   2
 *     4 3
 *  - Elves 3 and 4 are across the circle; Elf 3's present is stolen, being 
 *    the one to the left. Elf 3 leaves the circle, and the rest of the 
 *    Elves move in:
 *      1           1
 *    5   2  -->  5   2
 *     4 -          4
 *  - Elf 2 steals from the Elf directly across the circle, Elf 5:
 *      1         1 
 *    -   2  -->     2
 *      4         4 
 *  - Next is Elf 4 who, choosing between Elves 1 and 2, steals from Elf 1:
 *     -          2  
 *        2  -->
 *     4          4
 *  - Finally, Elf 2 steals from Elf 4:
 *     2
 *        -->  2  
 *     -
 *    - So, with five Elves, the Elf that sits starting in position 2 gets all the 
 *      presents.
 * 
 * With the number of Elves given in your puzzle input, which Elf now gets all 
 * the presents?
 */

public class Day19 {
	
	public static final int INPUT = 3012210;
	
	
	public int solvePart1() {
		// Josephus Problem (Numberphile: https://www.youtube.com/watch?v=uCsD3ZGzMgE) - the solution is
		// to convert to binary, strip the MSB
		// append to LSB
		String binPart = Integer.toBinaryString(INPUT);
		String resolved = binPart.substring(1) + binPart.charAt(0);
		int solved = Integer.parseInt(resolved, 2);
		return solved;
	}
	
	public int solvePart2() {
		// make 2 queues - 1st half and 2nd half
		// if for first half add to h1, for second half add to h2
		
        LinkedList<Integer> half1 = new LinkedList<>();
        LinkedList<Integer> half2 = new LinkedList<>();
        // fill the lists
        int size = INPUT;
        for(int i = 1; i<=size; i++) {
            if(i<=size/2) half1.addLast(i);
            else half2.addLast(i);
        }
        
        
        // removal starts
        while(half1.size() + half2.size() != 1) {
        	// get first element from h1
            int x = half1.pollFirst();
            // if sizes are equal, remove last from h1
            // else remove first from h2
            if(half1.size() == half2.size()) {
                half1.pollLast();
            }else {
                half2.pollFirst();
            }
            // add the first element from h1 (retrieved previously)
            // to the end of h2
            half2.addLast(x);
            // add the first element from h2 to the end of h1
            int a = half2.pollFirst();
            half1.addLast(a);
        }
        // first element in h1 is the solution
        return half1.pollFirst();
		
		
	}
	
	

	public static void main(String[] args) {
		Day19 day19 = new Day19();
		
		System.out.println("Part 1: " + day19.solvePart1());
		System.out.println("Part 2: " + day19.solvePart2());

		
		
	}

}
