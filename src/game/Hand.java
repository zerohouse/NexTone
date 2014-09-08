package game;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import animation.HideAndShow;

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
		setGravity(Gravity.BOTTOM);
		items = new ArrayList<Card>();

		scroll = new HorizontalScrollView(context);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 0f);
		params.height = Method.dpToPx(100);
		scroll.setLayoutParams(params);
		scroll.addView(this);
		scroll.setSmoothScrollingEnabled(true);

		// 애니메이션 세팅
		HideAndShow hideshow = new HideAndShow(this);
		hideshow.animate();

	}

	public void add(Card card) {
		items.add(card);
		marginCheck();
		addView(card);

	}

	private void marginCheck() {
		if (items.size() < 5) {
			for (Card card : items) {
				card.params.leftMargin = 5;
				card.params.rightMargin = 5;
				card.params.width = Method.dpToPx(70);
			}
			return;
		}
		for (Card card : items) {
			card.params.leftMargin = (int) (4 - (items.size() * 0.5 - 2)
					* Method.dpToPx(3));
			card.params.rightMargin = (int) (4 - (items.size() * 0.5 - 2)
					* Method.dpToPx(3));
			card.params.width = (int) (Method.dpToPx(70) - (items.size() * 0.5 - 2)
					* Method.dpToPx(3));
		}

	}

	public void remove(Card card) {
		items.remove(card);
		marginCheck();
		removeView(card);
	}

	public int removeAndReturnToDek(ArrayList<Card> clonedek) {
		int removes = 0;
		for (int i = items.size() - 1; i > -1; i--) {
			if (items.get(i).selected()) {
				items.get(i).toggleMultipleSelect();
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
