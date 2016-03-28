import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OriginalSnake {
	//BOARD VARIABLES
	static JFrame frame;
	static JPanel board;
	static JButton cells[];
	static OriginalSnake snake;

	//EDIBLE VARIABLES
	static int MICEMAX, APPLESMAX, BONUSMAX;
	static LinkedList<Integer> apples = new LinkedList<Integer>();
	static LinkedList<Integer> mice = new LinkedList<Integer>();
	static LinkedList<Bonus> bonus = new LinkedList<Bonus>();

	//SNAKE VARIABLES
	static LinkedList<Integer> position;
	Color color;
	int points;

	//OTHER VARIABLES
	static Random gen = new Random();
	static int direction = 2, size, sqrt;

	public static void init() {
		sqrt = 70;
		size = sqrt*sqrt;

		APPLESMAX = (int)(size*.005);
		MICEMAX = (int)(size*.001);
		BONUSMAX = (int)(size*.0005);

		System.out.println(APPLESMAX);
		System.out.println(MICEMAX);

		frame = new JFrame("Snake");
		frame.setSize(500, 500);

		board = snakeListeners();
		board.setSize(500, 500);

		GridLayout layout = new GridLayout();
		layout.setColumns(sqrt);
		layout.setRows(sqrt);
		board.setLayout(layout);

		cells = new JButton[sqrt*sqrt];
		for (int i = 0; i < size; i++)
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
	}

	public static JPanel snakeListeners() {
		JPanel panel = new JPanel();
		panel.setFocusable(true);
		panel.addKeyListener(new KeyListener() { 
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()){
				case KeyEvent.VK_LEFT:
					if (getDirection() != 2 && position.get(1) != position.peek()-1)
						changeDirection(4);
					break;
				case KeyEvent.VK_RIGHT:
					if (getDirection() != 4 && position.get(1) != position.peek()+1)
						changeDirection(2);
					break;
				case KeyEvent.VK_UP:
					if (getDirection() != 3 && position.get(1) != position.peek()-sqrt)
						changeDirection(1);
					break;
				case KeyEvent.VK_DOWN:
					if (getDirection() != 1 && position.get(1) != position.peek()+sqrt)
						changeDirection(3);
					break;
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		}); 

		return panel;
	}

	public OriginalSnake(Color color) {
		position = new LinkedList<Integer>();
		position.push((int) (gen.nextInt(100)));

		this.color = color;
		points = 0;
	}

	public static void changeDirection(int direct) {
		direction = direct;
	}

	public static int getDirection() {
		return direction;
	}

	//FOOD
	public static int getAvailable(LinkedList<Integer> position) {
		int possible = 0;
		boolean works = false;

		while (!works) {
			possible = gen.nextInt(size);

			if (!position.contains(possible))
				works = true;
		}

		return possible;
	}

	//MICE
	public static void moveMice() {
		int direct;

		//EARSE MICE INCASE OF MOVEMENT
		for (int i = 0; i < mice.size(); i++) {
			cells[mice.get(i)].setBackground(Color.WHITE);
			cells[mice.get(i)].repaint();
		}

		//RANDOMLY GENERATE MOVEMENT
		if (gen.nextInt(3) % 3 == 0)
			for (int i = 0; i < mice.size(); i++) {
				direct = gen.nextInt(5);

				if (direct == 1) {
					if (!position.contains(mice.get(i)-sqrt) && mice.get(i)-sqrt > 0)
						mice.add(mice.remove(i)-sqrt);
				}
				else if (direct == 2) {
					if (!position.contains(mice.get(i)+1) && mice.get(i)+1 < size)
						mice.add(mice.remove(i)+1);
				}
				else if (direct == 3) {
					if (!position.contains(mice.get(i)+sqrt) && mice.get(i)+sqrt < size)
						mice.add(mice.remove(i)+sqrt);
				}
				else if (direct == 4) {
					if (!position.contains(mice.get(i)-1) && mice.get(i)-1 > 0)
						mice.add(mice.remove(i)-1);
				}
			}

		//REPAINT MICE
		for (int i = 0; i < mice.size(); i++) {
			cells[mice.get(i)].setBackground(Color.GRAY);
			cells[mice.get(i)].repaint();
		}
	}

	public void moveSnake(int direction) {
		//DETERMINE MOVEMENT
		//0=STAY 1=UP 2=RIGHT 3=DOWN 4=LEFT
		if (direction == 1)
		{
			this.position.push((this.position.getFirst())-sqrt);
			if (this.position.getFirst() < 0)
				this.position.addFirst(this.position.removeFirst()+size-sqrt);
		}
		else if (direction == 2)
		{
			this.position.push((this.position.getFirst())+1);
			if (((this.position.getFirst())%sqrt) == 0)
				this.position.addFirst(this.position.removeFirst()-sqrt);
		}
		else if (direction == 3)
		{
			this.position.push((this.position.getFirst())+sqrt);
			if (this.position.getFirst() > size)
				this.position.addFirst(this.position.removeFirst()-size+sqrt);
			if (this.position.getFirst() == size)
				this.position.addFirst(this.position.removeFirst()-1);
		}
		else if (direction == 4)
		{
			this.position.push((this.position.getFirst())-1);
			if (((this.position.getFirst()+1)%sqrt) == 0)
				this.position.addFirst(this.position.removeFirst()+sqrt);
		}

		//REPAINT SNAKE
		for (int i = 0; i < position.size(); i++)
		{
			cells[position.get(i)].setBackground(color);
			cells[position.get(i)].repaint();
		}

		/*CHECK FOR COLLISION AND EATEN
		 * THREE SEPERATE CALLS IN CASE OF OVERLAPPING ELEMENTS*/
		//APPLES
		points += this.eaten();
		//MICE
		points += this.eaten();
		//BONUS
		points += this.eaten();
	}

	public int eaten() {
		if (apples.contains(position.getFirst())) {
			apples.remove(position.getFirst());
			return 1;
		}
		if (mice.contains(position.getFirst())) {
			mice.remove(position.getFirst());
			return 5;
		}
		for (int i = 0; i < bonus.size(); i++)
			if (bonus.get(i).position == position.getFirst())
				bonus.remove(i).activate();

		return 0;
	}

	public static void repopulate() {
		//REPLACES EATEN APPLES
		if (apples.size() < APPLESMAX) {
			apples.addLast(getAvailable(position));
			cells[apples.getLast()].setBackground(Color.RED);
		}
		//REPLACES EATEN MICE
		else if (mice.size() < MICEMAX) {
			mice.addLast(getAvailable(position));
			cells[mice.getLast()].setBackground(Color.GRAY);
		}
		//REPLACES EATEN BONUSES (RANDOMLY)
		else if (bonus.size() < BONUSMAX && gen.nextInt() % 100 == 0) 
		{
			bonus.addLast(new Bonus(getAvailable(position)));
			cells[bonus.getLast().position].setBackground(Color.BLUE);

		}
		/*POPS THE END OF THE SNAKE IF NOTHING WAS CONSUME
		 *CREATES THE ILLUSION OF GROWTH */
		else {
			cells[position.getLast()].setBackground(Color.WHITE);
			cells[position.removeLast()].repaint();
		}
	}

	public boolean collision() {
		//CHECKS FOR COLLISION
		int head = position.removeFirst();
		if (position.contains(head)) {
			position.addFirst(head);
			return true;
		}
		else
			position.addFirst(head);

		return false;
	}

	public static void main(String[]args) throws InterruptedException {
		int totalPoints = 0;
		int speed = 35;
		LinkedList<OriginalSnake> snakes = new LinkedList<OriginalSnake>();
		snakes.add(new OriginalSnake(Color.BLACK));

		snakes.getFirst().init();

		while (true) {
			totalPoints = 0;

			for (int i = 0; i < snakes.size(); i++)
				snakes.get(i).moveSnake(snakes.get(i).getDirection());

			repopulate();
			moveMice();

			for (int i = 0; i < snakes.size(); i++)
				totalPoints += snakes.get(i).points;

			if (snakes.get(0).collision())
				break;

			Thread.sleep(speed);
		}

		for (int i = 0; i < snakes.getFirst().position.size(); i++)
			System.out.println(snakes.getFirst().position.pop());

		System.out.println("YOU DIED!\nPoints: " + snakes.getFirst().points);
	}
}
