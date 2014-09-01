package game;

import components.ViewBinder;
import effects.Effect;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Card extends RelativeLayout {

	Effect effect;
	int monster;
	boolean haseffect, hasmonster;
	Context context;

	ViewBinder cost, attack, vital;
	String resource, name, description;
	int index;
	LinearLayout.LayoutParams params;

	boolean selected;

	public Card(Context context, String name, String description, int cost,
			int attack, int vital, String resource, int index) {
		super(context);
		this.context = context;

		hasmonster = true;
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(70);
		params.height = Method.dpToPx(90);
		params.leftMargin = 5;
		params.rightMargin = 5;
		setLayoutParams(params);
		params.setMargins(Method.dpToPx(4), Method.dpToPx(1), Method.dpToPx(4),
				Method.dpToPx(1));

		selected = false;

		monster = 1;
		this.resource = resource;

		setBackgroundResource(Method.resId(resource + "c"));

		this.attack = new ViewBinder(context, attack, this);
		RelativeLayout.LayoutParams attackparam = this.attack.getParams();
		attackparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		attackparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		attackparam.setMargins(Method.dpToPx(6), 0, 0, Method.dpToPx(1));

		this.cost = new ViewBinder(context, cost, this);
		RelativeLayout.LayoutParams costparam = this.cost.getParams();
		costparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		costparam.setMargins(0, Method.dpToPx(1), Method.dpToPx(6), 0);

		this.vital = new ViewBinder(context, vital, this);
		RelativeLayout.LayoutParams vitalparam = this.vital.getParams();
		vitalparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		vitalparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparam.setMargins(0, 0, Method.dpToPx(6), Method.dpToPx(1));

		this.name = name;
		this.description = description;
		this.index = index;

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSelect();
			}
		});
	}

	public void toggleSelect() {
		if (selected == false) {
			this.setBackgroundResource(Method.resId(resource + "clicked"));
			params.bottomMargin = Method.dpToPx(10);
			setLayoutParams(params);
			selected = true;
		} else {
			this.setBackgroundResource(Method.resId(resource + "c"));
			selected = false;
			params.bottomMargin = 0;
			setLayoutParams(params);
		}
	}

	public void deSelect() {
		this.setBackgroundResource(Method.resId(resource + "c"));
		selected = false;
		params.bottomMargin = 0;
		setLayoutParams(params);
	}

	public boolean selected() {
		return selected;
	}

	public int index() {
		return index;
	}

	public int cost() {
		return cost.Int();
	}

	public String resource() {
		return resource;
	}

	public int attack() {
		return attack.Int();
	}

	public int vital() {
		return vital.Int();
	}

	public int monster() {
		return monster;
	}

	public void use(Player player) {
		if (hasmonster)
			addMonster(player);
		if(haseffect)
			effect.run(0);
	}

	private void addMonster(Player player) {
		Monster monster = new Monster(context, this, player.field, index, false);
		player.field.add(monster);
		player.hand.remove(this);
		player.hero.mana.Add(-cost.Int(), false);
	}

}
