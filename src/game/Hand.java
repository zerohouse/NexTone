package game;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import animation.HideAndShow;

@SuppressLint("ClickableViewAccessibility")
public class Hand extends RelativeLayout {
	ArrayList<Card> items; // 통일성을 위해 아이템스로.
	Context context;
	RelativeLayout.LayoutParams params;
	boolean center = true;
	final int windowWidth, windowHeight;

	@SuppressLint("NewApi")
	public Hand(Context context) {
		super(context);
		this.context = context;
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		items = new ArrayList<Card>();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		windowWidth = size.x;
		windowHeight = size.y;
		marginCheck();

		// 애니메이션 세팅
		HideAndShow hideshow = new HideAndShow(this);
		hideshow.animate(false);

	}

	public void add(Card card) {
		items.add(card);
		marginCheck();
		addView(card);

	}

	@SuppressLint("NewApi")
	void marginCheck() {
		int size = items.size();
		if (size == 0)
			return;
		int block;
		float degree;
		if (size == 1) {
			items.get(0).params.width = Method.dpToPx(70);
			items.get(0).params.leftMargin = windowWidth / 2
					- Method.dpToPx(35);
			items.get(0).params.topMargin = windowHeight - Method.dpToPx(105);
			return;
		} else
			degree = 40 / (size - 1);
		if (size == 0)
			return;
		int width = Method.dpToPx(70);
		int startposition = (windowWidth - size * width) / 2;

		int itemwidth = Method.dpToPx(70) - (size - 5) * 2;

		if (width * size > windowWidth) {
			block = (windowWidth - itemwidth) / (size - 1);

			for (int i = 0; i < items.size(); i++) {
				items.get(i).params.width = itemwidth;
				items.get(i).params.leftMargin = i * block;
				items.get(i).params.topMargin = windowHeight
						- Method.dpToPx(105) + Math.abs(size / 2 - i) * 15;
				items.get(i).setRotation(degree * i - 20);
				items.get(i).rotate = degree * i - 20;
			}
			return;
		}

		for (int i = 0; i < items.size(); i++) {
			Log.i("card", "update" + i);
			items.get(i).params.leftMargin = startposition - (size - 1) * 5 + i
					* (width + 5);
			if (items.get(0).getWidth() == 0)
				items.get(i).params.topMargin = windowHeight / 2
						- Method.dpToPx(15);
			else {
				items.get(i).params.topMargin = windowHeight
						- Method.dpToPx(105) + Math.abs(size / 2 - i) * 15;
				items.get(i).setRotation(degree * i - 20);
				items.get(i).rotate = degree * i - 20;
			}
		}
	}

	public void remove(Card card) {
		items.remove(card); // remove card
		card.setVisibility(View.GONE);
		marginCheck();
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
			if (card.selected()) {
				selected.add(card);
			}
		}
		return selected;
	}

	public Card selectedCard() {
		for (Card card : items) {
			if (card.selected()) {
				return card;
			}
		}
		Log.i("null", "선택된 카드 없음");
		return null;
	}

	public void lostCards(int card) {
		int x;
		if (items.size() > card) {
			Random r = new Random();
			for (int i = 0; i < card; i++) {
				x = r.nextInt(items.size());
				removeView(items.get(x));
				items.remove(x);
			}
			marginCheck();

			return;
		}
		lostAllCards();
	}

	public void lostAllCards() {
		while (items.size() != 0) {
			removeView(items.get(0));
			items.remove(0);
		}
		marginCheck();
	}

}
