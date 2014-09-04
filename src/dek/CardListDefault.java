package dek;

import java.util.TreeSet;

import com.mylikenews.nextoneandroid.R;

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class CardListDefault {

	Context context;
	LinearLayout layout;
	TreeSet<CardinDek> items;

	public CardListDefault(Context context, LinearLayout layout) {
		String[] resource = context.getResources().getStringArray(
				R.array.defaultcards);

		items = new TreeSet<CardinDek>();
		int i = 0;
		for (String s : resource) {
			items.add(new CardinDek(context, s, i));
			i++;
		}

		this.layout = layout;
		this.context = context;
		update();
	}

	public void update() {
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

}
