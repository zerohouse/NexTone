package Game;

import java.util.ArrayList;
import android.content.Context;

public class Hand extends ViewWrap {
	ArrayList<Card> items; // 통일성을 위해 아이템스로.
	Context context;

	public Hand(Context context) {
		super(context);
		this.context = context;
		this.setOrientation(VERTICAL);
		items = new ArrayList<Card>();

	}

	public void add(Card card) {
		items.add(card);
		addView(card);
	}
	
	public void remove(Card card){
		items.remove(card);
		removeView(card);
	}

	public int removeAndReturnToDek(ArrayList<Card> clonedek) {
		int removes = 0;
		for (int i = items.size() - 1; i > -1; i--) {
			if (items.get(i).selected()) {
				removeView(items.get(i));
				clonedek.add(items.get(i));
				items.remove(i);
				removes++;
			}
		}
		return removes;
	}

	public int size() {
		return items.size();
	}

	public boolean contains(Object object) {
		return items.contains(object);
	}

	public ArrayList<Card> selectedCards() {
		ArrayList<Card> selected = new ArrayList<Card>();
		for (Card card : items){
			if(card.selected() == true){
				selected.add(card);
			}
		}
		return selected;
	}


}
