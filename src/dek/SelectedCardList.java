package dek;

import game.Method;

import java.util.TreeSet;

import com.mylikenews.nextoneandroid.SelectHeroAbility;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectedCardList {

	TextView dekinfo = null;
	Context context;
	LinearLayout layout;
	TreeSet<CardSelected> items;
	Data data;
	Sql sql;
	String[] defaultcards, herocards;

	private int sum() {
		int sum = 0;
		for (CardSelected card : items) {
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
		for (CardSelected card : items) {
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
		String result = data.getName() + "(" + sum + "/30)" + "\n";

		result += String.format("%s",
				SelectHeroAbility.heroType(data.getHeroType())[0]);

		/*
		 * result += String.format(
		 * "영웅능력 : %s\n0x%d, 1x%d, 2x%d, 3x%d, 4x%d, 5x%d, 6x%d, 7+x%d",
		 * SelectHeroAbility.heroType(data.getHeroType())[0], costcounts[0],
		 * costcounts[1], costcounts[2], costcounts[3], costcounts[4],
		 * costcounts[5], costcounts[6], costcounts[7]);
		 */
		dekinfo.setText(result);
		sqlUpdate(result);

	}

	private void sqlUpdate(String result) {
		data.setSummary(result);
		data.setDekstring(toString());
		sql.update(data);
	}

	public SelectedCardList(Context context, LinearLayout layout,
			TextView text, Data data) {

		this.layout = layout;
		this.context = context;
		this.data = data;
		sql = new Sql(context);
		defaultcards = context.getResources().getStringArray(
				CardList.heroType(0));
		herocards = context.getResources().getStringArray(
				CardList.heroType(data.getHeroType()));
		items = new TreeSet<CardSelected>();
		if (!data.getDekstring().equals("")) {
			String[] dekcards = data.getDekstring().split(",");
			String[] tmp;
			int id, size;
			CardSelected newcard;

			for (String s : dekcards) {
				tmp = s.split("x");
				id = Integer.parseInt(tmp[0]);
				size = Integer.parseInt(tmp[1]);
				newcard = new CardSelected(context, getCardById(id), id);
				newcard.setSize(size);
				items.add(newcard);
				layout.addView(newcard);

			}
		}
		update();

		dekinfo = text;
		infoUpdate();

	}

	private String getCardById(int id) {
		if (id > 999) {
			return herocards[(id % 1000)];
		}
		return defaultcards[id];
	}

	public void update() {
		infoUpdate();
		layout.removeAllViews();
		for (CardSelected card : items) {
			layout.addView(card);
		}

	}

	public boolean add(CardinDek cardindek) {
		if (sum() > 29) {
			Method.alert("카드는 30장까지 추가할 수 있습니다.");
			return false;
		}
		for (CardSelected card : items) {
			if (card.getId() == cardindek.getId()) {
				if (card.isLegend()) {
					Method.alert("전설카드는 한장만 넣을 수 있습니다.");
					return false;
				}
				if (card.getSize() == 1) {
					card.setSize(2);
					update();
					return true;
				} else {
					Method.alert("카드는 두장까지 넣을 수 있습니다.");
					return false;
				}
			}
		}
		CardSelected newcard = cardindek.clone();
		OnClickListener removeSelected = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				remove((CardSelected) v);
			}
		};

		newcard.setOnClickListener(removeSelected);

		items.add(newcard);
		layout.addView(newcard);
		update();
		return true;
	}

	public void setListenerAll(OnClickListener Listener) {
		for (CardSelected card : items) {
			card.setOnClickListener(Listener);
		}
	}

	public void remove(CardSelected remove) {
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
		for (CardSelected card : items) {
			result += "," + card.toString(); // card.tostring메소드를 만들어서 추가.
		}
		if (result != "")
			result = result.substring(1);
		return result;
	}

	public String getHeroString() {
		return data.getHerostring();
	}

}
