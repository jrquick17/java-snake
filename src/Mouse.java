import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;


public class Mouse {
	Random gen = new Random();
	int points = 5, position, sqrt;

	public Mouse(int pos) {
		position = pos;
		sqrt = Game.sqrt;
	}

	public int getPosition() {
		return position;
	}

	public void progress(LinkedList<Integer> available) {
		int direct, pos = -1;

		//RANDOMLY GENERATE MOVEMENT
		direct = gen.nextInt(5);

		if (direct == 1) {
			if (!available.contains(position-sqrt) && position-sqrt > 0)
				pos = position - sqrt;
		}
		else if (direct == 2) {
			if (!available.contains(position+1) && position+1 < sqrt*sqrt)
				pos = position + 1;
		}
		else if (direct == 3) {
			if (!available.contains(position+sqrt) && position+Game.sqrt < sqrt*sqrt)
				pos = position + sqrt;
		}
		else if (direct == 4) {
			if (!available.contains(position-1) && position-1 > 0)
				pos = position - 1;
		}
		
		if (available.contains(pos) && pos != -1)
			position = pos;
	}

	public int eaten() {
		return points;
	}
}
