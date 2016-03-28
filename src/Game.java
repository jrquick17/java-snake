import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Game {
	static Random gen = new Random();

	static JFrame frame;
	static JPanel board;
	static JButton cells[];
	static int boardSize, sqrt, size;

	static Snake snakes[];
	static int totalPoints[];

	static Apple apples[];
	static Mouse mice[];
	static Bonus bonuses[];

	public static void init(int sq) {
		sqrt = sq;
		size = sqrt*sqrt;
		boardSize = sqrt*sqrt;

		frame = new JFrame("Snake");
		frame.setSize(500, 500);

		board = listeners();
		board.setSize(500, 500);

		GridLayout layout = new GridLayout();
		layout.setColumns(sqrt);
		layout.setRows(sqrt);
		board.setLayout(layout);

		cells = new JButton[sqrt*sqrt];
		for (int i = 0; i < boardSize; i++)
			cells[i] = new JButton();

		for (int i = 0; i < cells.length; i++)
		{
			cells[i].setBorder(BorderFactory.createEmptyBorder());
			cells[i].setBackground(Color.WHITE);
			cells[i].setFocusable(false);
			board.add(cells[i]);
		}

		frame.add(board);
		frame.setVisible(true);
		
		mice = new Mouse[(int)(size*.001+.5)];
		apples = new Apple[(int)(size*.005+.5)];
		
	}

	public static LinkedList<Integer> getAvailable() {
		LinkedList<Integer> available = new LinkedList<Integer>();

		//ADD ALL CELLS
		for (int i = 0; i < sqrt*sqrt; i++) 
			available.add(i);

		//DELETES TAKEN CELLS; SNAKES, APPLES, MICE, BONUS
		try {
			for (int s = 0; s < snakes.length; s++) 
				for (int i = 0; i < snakes[s].positions.size(); i++) 
					available.remove(snakes[s].positions.get(i));

			for (int a = 0; a < apples.length; a++)
				available.remove(apples[a].getPosition());

			for (int m = 0; m < mice.length; m++)
				available.remove(mice[m].getPosition());

			for (int b = 0; b < bonuses.length; b++)
				available.remove(bonuses[b].getPosition());
		} catch (Exception e) {};

		return available;
	}

	public static void addSnakes() {
		snakes = new Snake[1];
		totalPoints = new int[1];
		snakes[0] = new Snake(Color.BLACK, getAvailable().getFirst());
		for (int s = 0; s < snakes.length; s++)
			for (int p = 0; p < snakes[s].positions.size(); p++) {
				cells[snakes[s].positions.get(p)].setBackground(snakes[s].color);
				cells[snakes[s].positions.get(p)].repaint();
			}
		
		totalPoints[0] = 0;
	}

	public static JPanel listeners() {
		JPanel panel = new JPanel();
		panel.setFocusable(true);
		panel.addKeyListener(new KeyListener() { 
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				switch (e.getKeyCode()){
				case KeyEvent.VK_LEFT:
					if (snakes[0].getDirection() != 2 && snakes[0].getHead() != snakes[0].getHead()-1)
						snakes[0].changeDirection(4);
					break;
				case KeyEvent.VK_RIGHT:
					if (snakes[0].getDirection() != 4 && snakes[0].getHead() != snakes[0].getHead()+1)
						snakes[0].changeDirection(2);
					break;
				case KeyEvent.VK_UP:
					if (snakes[0].getDirection() != 3 && snakes[0].getHead() != snakes[0].getHead()-sqrt)
						snakes[0].changeDirection(1);
					break;
				case KeyEvent.VK_DOWN:
					if (snakes[0].getDirection() != 1 && snakes[0].getHead() != snakes[0].getHead()+sqrt)
						snakes[0].changeDirection(3);
					break;
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		}); 

		return panel;
	}

	public static void replaceMice(){

	}

	public static void moveMice() { 
		//ERASE MICE TEMPORARILY, IN CASE MOVEMENT OCCURS
		for (int i = 0; i < mice.length; i++) {
			cells[mice[i].getPosition()].setBackground(Color.WHITE);
			cells[mice[i].getPosition()].repaint();
		}

		for (int i = 0; i < mice.length; i++)
			//RANDOMIZES MOVEMENT
			if (gen.nextInt(3) % 3 == 0)
				mice[i].progress(getAvailable());

		//REPAINT MICE, CREATES APPEARANCE OF MOVEMENT
		for (int i = 0; i < mice.length; i++) {
			cells[mice[i].getPosition()].setBackground(Color.GRAY);
			cells[mice[i].getPosition()].repaint();
		}
	}

	public static void moveSnakes() {
		for (int i = 0; i < snakes.length; i++) {
			snakes[0].progress();
			cells[snakes[0].positions.getFirst()].setBackground(snakes[0].color);
			cells[snakes[0].positions.getLast()].setBackground(Color.WHITE);
			cells[snakes[0].positions.getFirst()].repaint();
			cells[snakes[0].positions.removeLast()].repaint();
		}
	}

	public static void addApples() {
		for (int i = 0; i < apples.length; i++) {
			apples[i] = new Apple(getAvailable().get(gen.nextInt(getAvailable().size())));
			cells[apples[i].position].setBackground(Color.RED);
			cells[apples[i].position].repaint();
		}
	}

	public static void addMice() {
		for (int i = 0; i < mice.length; i++) 
			mice[i] = new Mouse(getAvailable().get(gen.nextInt(getAvailable().size())));
	}

	public static void addBonuses() {

	}
	
	public static void replaceApples() {
		for (int i = 0; i < apples.length; i++)
			if (snakes[0].positions.getFirst() == apples[i].position){
				System.out.println(apples[i]);
				snakes[0].ate(apples[i].eaten(getAvailable().get(gen.nextInt(getAvailable().size()))));
				snakes[0].progress();
				cells[apples[i].position].setBackground(Color.RED);
				cells[apples[i].position].repaint();
			}
	}
}

