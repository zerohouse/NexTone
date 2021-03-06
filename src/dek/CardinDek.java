package dek;

import game.Method;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class CardinDek extends RelativeLayout implements Comparable<CardinDek> {
	int cost, attack, vital, id;
	String name, description, resource, string;
	boolean hasMonster, legend;

	ImageView background, character;
	Context context;
	TextView nameview, descriptionview;
	LinearLayout.LayoutParams params;

	int size = 1;

	@SuppressLint({ "DefaultLocale", "NewApi" })
	public CardinDek(Context context, String string, int id) {
		super(context);
		// 카드 스트링 양식 (구분자 : ;)
		// 카드이름;카드설명;코스트;데미지;바이탈;이미지리소스파일명;특수능력타입(0=empty)
		this.string = string;
		String[] cardresource = string.split(";");
		setGravity(Gravity.CENTER);
		this.context = context;
		name = cardresource[0];
		description = cardresource[1];

		hasMonster = true;
		if (Integer.parseInt(cardresource[3]) == 0)
			hasMonster = false;
		
		legend = false;
		if (cardresource[4].contains("LEGEND"))
			legend = true;

		cost = Integer.parseInt(cardresource[5]);
		attack = Integer.parseInt(cardresource[6]);
		vital = Integer.parseInt(cardresource[7]);

		resource = cardresource[2];

		this.id = id;

		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);

		nameview = new TextView(context);
		nameview.setTextAppearance(context, R.style.myText);
		nameview.setGravity(Gravity.CENTER);
		LayoutParams nameparams = Method.getParams();
		nameview.setLayoutParams(nameparams);
		nameparams.addRule(CENTER_HORIZONTAL);
		nameview.setId(100);

		setSize(size);
		descriptionview = new TextView(context);
		LayoutParams desparams = Method.getParams();
		descriptionview.setLayoutParams(desparams);
		desparams.addRule(CENTER_HORIZONTAL);
		desparams.addRule(BELOW, nameview.getId());
		String info = String.format("공격력:%d, 체력:%d\n%s", attack, vital,
				this.description);
		descriptionview.setText(info);
		descriptionview.setGravity(Gravity.CENTER);

		background = new ImageView(context);
		LayoutParams imageparams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		background.setLayoutParams(imageparams);
		background.setScaleType(ScaleType.FIT_XY);

		if(legend)
			background.setImageResource(R.drawable.legend);
		else if(hasMonster)
			background.setImageResource(R.drawable.monstercard);
		else
			background.setImageResource(R.drawable.spellcard);
		
		background.setAlpha((float) 0.5);

		character = new ImageView(context);
		LayoutParams charparams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		character.setLayoutParams(charparams);
		character.setScaleType(ScaleType.CENTER_CROP);
		character.setImageResource(Method.resId(resource));
		character.setAlpha((float) 0.5);

		addView(background);
		addView(character);

		addView(nameview);
		addView(descriptionview);
	}

	public void setHeight() {
		params.height = nameview.getHeight() + descriptionview.getHeight();
	}

	@Override
	public String toString() {
		// "토템;;totem;0;0;0;1;1";
		return string;
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
	public CardSelected clone() {
		CardSelected result = new CardSelected(context, toString(), id);
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
