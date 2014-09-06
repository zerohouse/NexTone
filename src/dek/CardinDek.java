package dek;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class CardinDek extends LinearLayout implements Comparable<CardinDek> {
	int cost, attack, vital, id;
	String name, description, resource;

	Context context;
	TextView nameview, descriptionview;

	int size = 1;

	@SuppressLint("DefaultLocale")
	public CardinDek(Context context, String string, int id) {
		super(context);
		// 카드 스트링 양식 (구분자 : ;)
		// 카드이름;카드설명;코스트;데미지;바이탈;이미지리소스파일명;특수능력타입(0=empty)

		String[] cardresource = string.split(";");
		setGravity(Gravity.CENTER);
		this.context = context;
		name = cardresource[0];
		description = cardresource[1];

		cost = Integer.parseInt(cardresource[2]);
		attack = Integer.parseInt(cardresource[3]);
		vital = Integer.parseInt(cardresource[4]);

		resource = cardresource[5];
		this.id = id;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
		setOrientation(LinearLayout.VERTICAL);
		nameview = new TextView(context);
		nameview.setTextAppearance(context, R.style.myText);

		setSize(size);
		descriptionview = new TextView(context);

		String info = String.format("마나:%d, 공격력:%d, 체력:%d\n%s", cost, attack,
				vital, this.description);
		descriptionview.setText(info);

		addView(nameview);
		addView(descriptionview);

	}

	public String toStirng() {
		return name + ";" + description + ";" + cost + ";" + attack + ";"
				+ vital + ";" + resource + ";" + 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CardinDek other = (CardinDek) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public CardinDek clone() {
		CardinDek result = new CardinDek(context, toStirng(), id);
		return result;
	}

	public int getCost() {
		return cost;
	}

	public int getAttack() {
		return attack;
	}

	public int getVital() {
		return vital;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getResource() {
		return resource;
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		String namesize = "[" + cost + "] " + this.name;

		if (this.size == 2) {
			namesize += " x 2";
		}
		nameview.setText(namesize);
	}

	@Override
	public int compareTo(CardinDek another) {

		if (cost > another.getCost())
			return 1;
		else if (cost == another.getCost()) {
			if (id > another.getId())
				return 1;
			else if (id == another.getId())
				return 0;
			else
				return -1;
		} else
			return -1;
	}

}