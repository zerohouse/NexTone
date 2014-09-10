package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Dummy {
	Stack<Card> items;

	Dummy() {
		items = new Stack<Card>();
	}

	public String shffleAdd(ArrayList<Card> dek, Random random) {

		String result = "";
		int size, rannum;
		Card card;

		while (dek.size() > 0) {
			size = dek.size();
			rannum = random.nextInt(size);
			result += rannum + ",";
			card = dek.get(rannum);
			items.push(card);
			dek.remove(rannum);
		}

		return result;
	}

	public void shffleAdd(ArrayList<Card> dek, String random) {

		Card card;
		String[] ranorder = random.split(",");
		int rannum;
		
		for (int i = 0; i < ranorder.length-1; i++) {
			rannum = Integer.parseInt(ranorder[i]);
			card = dek.get(rannum);
			items.push(card);
			dek.remove(rannum);
		}
		items.push(dek.get(0));
	}

	public int size() {
		return items.size();
	}

	public Card pop() {
		return items.pop();
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

}
