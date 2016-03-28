import java.awt.Color;
import java.util.LinkedList;


public class Snake {
	LinkedList<Integer> positions = new LinkedList<Integer>();
	Color color;
	int sqrt, size;
	
	int direction;
	
	int points = 0;
	
	public Snake(Color clr, int pos) {
		sqrt = Game.sqrt;
		size = Game.size;
		direction = 2;
		color = clr;
		
		//Creates Snake of size 5
		positions.push(pos);
		while (positions.size() < 5)
			positions.add(++pos);
	}
	
	public void progress() {
		//DETERMINE MOVEMENT
		//0=STAY 1=UP 2=RIGHT 3=DOWN 4=LEFT
		if (direction == 1)
		{
			positions.push((positions.getFirst())-sqrt);
			if (positions.getFirst() < 0)
				positions.addFirst(positions.removeFirst()+size-sqrt);
		}
		else if (direction == 2)
		{
			positions.push((positions.getFirst())+1);
			if (((positions.getFirst())%sqrt) == 0)
				positions.addFirst(positions.removeFirst()-sqrt);
		}
		else if (direction == 3)
		{
			positions.push((positions.getFirst())+sqrt);
			if (positions.getFirst() > size)
				positions.addFirst(positions.removeFirst()-size+sqrt);
			if (positions.getFirst() == size)
				positions.addFirst(positions.removeFirst()-1);
		}
		else if (direction == 4)
		{
			positions.push((positions.getFirst())-1);
			if (((positions.getFirst()+1)%sqrt) == 0)
				positions.addFirst(positions.removeFirst()+sqrt);
		}
	}
	
	public void pushNextPosition(int next) {
		positions.push(next);
	}
	
	public int removeEndPosition() {
		return positions.removeLast();
	}
	
	public int getHead() {
		return positions.peek();
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void ate(int p) {
		points += p;
		System.out.println(points);
	}
	
	public void changeDirection(int direct) {
		direction = direct;
	}
}
