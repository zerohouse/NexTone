package dek;

import game.Method;

import java.util.TreeSet;

import com.mylikenews.nextoneandroid.R;
import com.mylikenews.nextoneandroid.SelectHeroAbility;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardList {

	TextView dekinfo = null;
	Context context;
	LinearLayout layout;
	TreeSet<CardinDek> items;
	Data data;
	Sql sql;

	private int sum() {
		int sum = 0;
		for (CardinDek card : items) {
			if (card.getSize() == 2)
				sum++;
			sum++;
		}
		data.setSum(sum);
		return sum;
	}

	private void infoUpdate() {
		if (dekinfo == null)
			return;
		int[] costcounts = new int[8];
		for (CardinDek card : items) {
			switch (card.getCost()) {
			case 0:
				if (card.getSize() == 2)
					costcounts[0]++;
				costcounts[0]++;
				break;
			case 1:
				if (card.getSize() == 2)
					costcounts[1]++;
				costcounts[1]++;
				break;
			case 2:
				if (card.getSize() == 2)
					costcounts[2]++;
				costcounts[2]++;
				break;
			case 3:
				if (card.getSize() == 2)
					costcounts[3]++;
				costcounts[3]++;
				break;
			case 4:
				if (card.getSize() == 2)
					costcounts[4]++;
				costcounts[4]++;
				break;
			case 5:
				if (card.getSize() == 2)
					costcounts[5]++;
				costcounts[5]++;
				break;
			case 6:
				if (card.getSize() == 2)
					costcounts[6]++;
				costcounts[6]++;
				break;
			default:
				if (card.getSize() == 2)
					costcounts[7]++;
				costcounts[7]++;
			}
		}

		int sum = 0;
		for (int i : costcounts) {
			sum += i;
		}
		data.setSum(sum);
		String result = SelectHeroAbility.selectedHeroType(data.getHeroType()) + "("
				+ sum + "/30)" + "\n";

		result += String
				.format("[0]x%d, [1]x%d, [2]x%d, [3]x%d, [4]x%d, [5]x%d, [6]x%d, [7+]x%d",
						costcounts[0], costcounts[1], costcounts[2],
						costcounts[3], costcounts[4], costcounts[5],
						costcounts[6], costcounts[7]);
		dekinfo.setText(result);
		sqlUpdate(result);

	}

	private void sqlUpdate(String result) {
		data.setSummary(result);
		data.setDekstring(toString());
		sql.update(data);
	}

	public CardList(Context context, LinearLayout layout, TextView text,
			Data data) {

		this.layout = layout;
		this.context = context;
		this.data = data;
		sql = new Sql(context);
		String[] resource = context.getResources().getStringArray(
				R.array.defaultcards);
		items = new TreeSet<CardinDek>();
		if (!data.getDekstring().equals("")) {
			String[] dekcards = data.getDekstring().split(",");
			String[] tmp;
			int id, size;
			CardinDek newcard;

			for (String s : dekcards) {
				tmp = s.split("x");
				id = Integer.parseInt(tmp[0]);
				size = Integer.parseInt(tmp[1]);
				newcard = new CardinDek(context, resource[id], id);
				newcard.setSize(size);
				items.add(newcard);
				layout.addView(newcard);

			}
		}
		update();

		dekinfo = text;
		infoUpdate();

	}

	public void update() {
		infoUpdate();
		layout.removeAllViews();
		for (CardinDek card : items) {
			layout.addView(card);
		}

	}

	public boolean add(CardinDek cardindek) {
		if (sum() > 29) {
			Method.alert("카드는 30장까지 추가할 수 있습니다.");
			return false;
		}
		for (CardinDek card : items) {
			if (card.getId() == cardindek.getId()) {
				if (card.getSize() == 1) {
					card.setSize(2);
					update();
					return true;
				} else {
					return false;
				}
			}
		}
		CardinDek newcard = cardindek.clone();
		OnClickListener removeSelected = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				remove((CardinDek) v);
			}
		};

		newcard.setOnClickListener(removeSelected);

		items.add(newcard);
		layout.addView(newcard);
		update();
		return true;
	}

	public void setListenerAll(OnClickListener Listener) {
		for (CardinDek card : items) {
			card.setOnClickListener(Listener);
		}
	}

	public void remove(CardinDek remove) {
		if (remove.getSize() == 2) {
			remove.setSize(1);
			infoUpdate();
			return;
		}
		items.remove(remove);
		layout.removeView(remove);
		infoUpdate();
	}

	public String toString() {
		String result = "";
		for (CardinDek card : items) {
			result += "," + card.getId() + "x" + card.getSize();
		}
		if (result != "")
			result = result.substring(1);
		return result;
	}

	public String getHeroString() {
		return data.getHerostring();
	}

}
