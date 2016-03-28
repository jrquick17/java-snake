import java.util.Random;


public class Bonus {
	Random gen = new Random();
	int position, type;
	
	public Bonus(int pos) { 
		position = pos;
		type = gen.nextInt(1);
	}
	
	public int getPosition() {
		return position;
	}
	
	public void activate() {
		if (type == 0) {
			System.out.println("2x MULTIPLIER");
		}
	}
}
