package Game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Dummy {
	Stack<Card> items;

	Dummy() {
		items = new Stack<Card>();
	}

	public void shffleAdd(ArrayList<Card> clonedek, Random random) {

		int size, rannum;
		Card card;

		while (clonedek.size() > 1) {
			size = clonedek.size();
			rannum = random.nextInt(size - 1);
			card = clonedek.get(rannum);
			items.push(card);
			clonedek.remove(rannum);
		}
		items.push(clonedek.get(0));
	}

	public int size() {
		return items.size();
	}

}
