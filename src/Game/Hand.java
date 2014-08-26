package Game;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class Hand extends LinearLayout {
	ArrayList<Card> items; // 통일성을 위해 아이템스로.
	Context context;
	HorizontalScrollView scroll;
	LinearLayout.LayoutParams params;

	public Hand(Context context) {
		super(context);
		this.context = context;
		params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER);
		items = new ArrayList<Card>();

		scroll = new HorizontalScrollView(context);
		params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,0f);
		
		scroll.setLayoutParams(params);
		scroll.addView(this);
		

	}
	


	public void add(Card card) {
		items.add(card);
		addView(card);
	}

	public void remove(Card card) {
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
		for (Card card : items) {
			if (card.selected() == true) {
				selected.add(card);
			}
		}
		return selected;
	}

	public View ScrollView() {
		return scroll;
	}

}
