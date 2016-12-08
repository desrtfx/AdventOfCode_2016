package day8swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.FileIO;

/* --- Day 8: Two-Factor Authentication ---
 * 
 * You come across a door implementing what you can only assume is an implementation 
 * of two-factor authentication after a long game of requirements telephone.
 * 
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). 
 * Then, it displays a code on a little screen, and you type that code on a keypad. 
 * Then, presumably, the door unlocks.
 * 
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart 
 * and figured out how it works. Now you just have to work out what the screen would have displayed.
 * 
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen; 
 * these instructions are your puzzle input. 
 * The screen is 50 pixels wide and 6 pixels tall, all of which start off, 
 * and is capable of three somewhat peculiar operations:
 * 
 * rect AxB turns on all of the pixels in a rectangle 
 * at the top-left of the screen which is A wide and B tall.
 * 
 * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) 
 * right by B pixels. 
 * Pixels that would fall off the right end appear at the left end of the row.
 * 
 * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) 
 * down by B pixels. 
 * Pixels that would fall off the bottom appear at the top of the column.
 * 
 * For example, here is a simple sequence on a smaller screen:
 * 
 * rect 3x2 creates a small rectangle in the top-left corner:
 * 
 * ###....
 * ###....
 * .......
 * rotate column x=1 by 1 rotates the second column down by one pixel:
 * 
 * #.#....
 * ###....
 * .#.....
 * rotate row y=0 by 4 rotates the top row right by four pixels:
 * 
 * ....#.#
 * ###....
 * .#.....
 * rotate column x=1 by 1 again rotates the second column down by one pixel, 
 * causing the bottom pixel to wrap back to the top:
 * 
 * .#..#.#
 * #.#....
 * .#.....
 * As you can see, this display technology is extremely powerful, 
 * and will soon dominate the tiny-code-displaying-screen market. 
 * That's what the advertisement on the back of the display tries to convince you, anyway.
 * 
 * There seems to be an intermediate check of the voltage used by the display: 
 * after you swipe your card, if the screen did work, how many pixels should be lit?
 * 
 * 
 * --- Part Two ---
 * 
 * You notice that the screen is only capable of displaying capital letters; 
 * in the font it uses, each letter is 5 pixels wide and 6 tall.
 * 
 * After you swipe your card, what code is the screen trying to display?
 */



public class Day8Swing {

	public static final String FILENAME = "./resources/Input_Day8.txt";
	public static final String DELIMITER = " ";

	public static final int ROWS = 6;
	public static final int COLS = 50;
	public static char[][] mem = new char[ROWS][COLS];
	public static List<String[]> data;

	static {
		// get data
		data = FileIO.getFileLinesSplit(FILENAME, DELIMITER);
		for (int row = 0; row < mem.length; row++) {
			for (int col = 0; col < mem[row].length; col++)
				mem[row][col] = '.';
		}
	}

	public static void rect(int width, int height) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				mem[row][col] = '#';
			}
		}
	}

	public static void rotateRow(int row, int by) {
		for (int i = 0; i < by; i++) {
			char last = mem[row][mem[row].length - 1];
			for (int col = mem[row].length - 1; col > 0; col--) {
				mem[row][col] = mem[row][col - 1];
			}
			mem[row][0] = last;
		}
	}

	public static void rotateCol(int col, int by) {
		for (int i = 0; i < by; i++) {
			char last = mem[mem.length - 1][col];
			for (int row = mem.length - 1; row > 0; row--) {
				mem[row][col] = mem[row - 1][col];
			}
			mem[0][col] = last;
		}
	}

	public static void main(String[] args) {

		DisplayPanel disp = new DisplayPanel(mem);

		SwingUtilities.invokeLater(() -> {
			JFrame mainWin = new JFrame("--- Day 8: Two-Factor Authentication ---");
			mainWin.setPreferredSize(new Dimension(752, 162));
			mainWin.setSize(752, 162);
			mainWin.setLocationRelativeTo(null);
			mainWin.setLayout(new BorderLayout());
			mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWin.add(disp, BorderLayout.CENTER);
			mainWin.setVisible(true);
		});

		for (String[] line : data) {

			if ("rect".equals(line[0])) {
				String[] tmp = line[1].split("x");
				int width = Integer.parseInt(tmp[0]);
				int height = Integer.parseInt(tmp[1]);
				rect(width, height);
			} else {
				if ("row".equals(line[1])) {
					int row = Integer.parseInt(line[2].replace("y=", ""));
					int by = Integer.parseInt(line[4]);
					rotateRow(row, by);
				} else {
					int col = Integer.parseInt(line[2].replace("x=", ""));
					int by = Integer.parseInt(line[4]);
					rotateCol(col, by);
				}
			}
			disp.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}

class DisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static Map<Character, Color> colors = new HashMap<>();
	private static Color line = Color.GRAY;
	private static final Font FONT;
	private static final Rectangle R = new Rectangle(0, 100, 752, 20);

	static {
		colors.put('.', Color.DARK_GRAY);
		colors.put('#', Color.GREEN);
		FONT = new Font("Calibri", Font.PLAIN, 14);
	}

	char[][] mem;

	public DisplayPanel(char[][] mem) {
		super();
		this.mem = mem;
		this.setBackground(Color.BLACK);

	}

	int calcOn() {
		int count = 0;
		for (char[] row : mem) {
			for (char cell : row) {
				count += (cell == '#') ? 1 : 0;
			}
		}
		return count;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		int gridMultiplier = 15;
		int radius = gridMultiplier - 2;
		for (int row = 0; row < mem.length; row++) {
			for (int col = 0; col < mem[row].length; col++) {
				g.setColor(colors.get(mem[row][col]));
				int x = col * gridMultiplier + 1;
				int y = row * gridMultiplier + 1;
				g.fillOval(x, y, radius, radius);
				g.setColor(line);
				g.drawOval(x, y, radius, radius);

			}
		}
		String text = String.format("LEDs lit: %d", calcOn());
		drawCenteredString(g, text, R, FONT, Color.GREEN);

	}

	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font, Color color) {
		FontMetrics metrics = g.getFontMetrics(font);
		int x = (rect.width - metrics.stringWidth(text)) / 2 + rect.x;
		int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + rect.y;
		g.setFont(font);
		Color prev = g.getColor();
		g.setColor(color);
		g.drawString(text, x, y);
		g.setColor(prev);
	}

}
