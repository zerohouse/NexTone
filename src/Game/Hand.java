package Game;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Hand extends LinearLayout {
	ArrayList<Card> items; // 통일성을 위해 아이템스로.
	Context context;

	public Hand(Context context) {
		super(context);
		this.context = context;
		items = new ArrayList<Card>();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setOrientation(VERTICAL);
		setLayoutParams(params);

	}

	public void addCard(Card card) {
		items.add(card);
		addView(card);
	}

	public int removeSelectedCard() {
		int removes = 0;
		for (int i = items.size() - 1; i > -1; i--) {
			if (items.get(i).selected()) {
				removeView(items.get(i));
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

}
