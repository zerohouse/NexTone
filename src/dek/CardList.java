package dek;

import java.util.TreeSet;

import com.mylikenews.nextoneandroid.R;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class CardList {

	Context context;
	LinearLayout layout;
	TreeSet<CardinDek> items;

	public CardList(Context context, LinearLayout layout, int type) {
		String[] resource = context.getResources().getStringArray(
				heroType(type));

		items = new TreeSet<CardinDek>();
		int i = 0;
		for (String s : resource) {
			items.add(new CardinDek(context, s, i));
			i++;
		}

		this.layout = layout;
		this.context = context;
	}

	public void show() {
		layout.removeAllViews();
		for (CardinDek card : items) {
			layout.addView(card);
		}
	}

	public void setListenerAll(OnClickListener Listener) {
		for (CardinDek card : items) {
			card.setOnClickListener(Listener);
		}
	}

	public TreeSet<CardinDek> getTreeSet() {
		return items;
	}

	public static int heroType(int type) {

		int result = 100;
		switch (type) {

		case 0:
			result = R.array.totem;
			return result;
		case 1:
			result = R.array.magician;
			return result;
		case 2:
			result = R.array.heal;
			return result;
		case 3:
			result = R.array.thief;
			return result;
		case 4:
			result = R.array.defense;
			return result;
		case 5:
			result = R.array.drawcard;
			return result;
		case 6:
			result = R.array.bat;
			return result;
		case 7:
			result = R.array.hunter;
			return result;
		case 8:
			result = R.array.druid;
			return result;

		case 10:
			result = R.array.defaultcards;
			return result;

		}
		return result;
	}

}
