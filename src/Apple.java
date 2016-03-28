import java.util.LinkedList;
import java.util.Random;


public class Apple {
	Random gen = new Random();
	int points = 1, position;
	
	public Apple(int pos) {
		position = pos;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int eaten(int available) {
		position = available;
		
		return points;
	}
}
