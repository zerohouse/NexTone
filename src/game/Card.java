package game;

import net.Sender;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import animation.Ani;
import animation.Helper;

import components.ViewBinder;

import effects.card.CardEffect;

public class Card extends RelativeLayout {

	static boolean stateChange = true;
	public static Card select = null;

	boolean haseffect;
	Context context;
	int monster;
	String cardinfo;
	Hand hand;
	Ani ani;

	ViewBinder cost, attack, vital;
	String resource, name, description, monstereffects, cardeffects;
	int index;
	LinearLayout.LayoutParams params;

	boolean selected;

	public Card(Context context, String eachcard, Hand hand, int index) {
		super(context);
		this.context = context;

		this.cardinfo = eachcard;
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

		String[] cardinfo = eachcard.split(";");

		name = cardinfo[0];
		description = cardinfo[1];
		resource = cardinfo[2];

		setBackgroundResource(Method.resId(resource + "c"));

		int cost = Integer.parseInt(cardinfo[5]);
		this.cost = new ViewBinder(context, cost, this);
		RelativeLayout.LayoutParams costparam = this.cost.getParams();
		costparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		costparam.setMargins(0, Method.dpToPx(1), Method.dpToPx(6), 0);

		// 효과 종류 구분해서 나눠줌. 카드이펙트가 잇는경우 @로 구분.
		if (!cardinfo[4].contains("@")) {
			monstereffects = cardinfo[4]; // 효과1,효과2,...
		} else {
			String[] tmp = cardinfo[4].split("@");
			monstereffects = tmp[0];
			cardeffects = tmp[1];
		}

		switch (Integer.parseInt(cardinfo[3])) {

		case 0:// 주문 카드 (몬스터 = 0)
			monster = 0;
			haseffect = true;
			this.ani = new Ani(resource, name, description, cost + "", " ", " ");

			break;

		default:// 몬스터 카드
			monster = Integer.parseInt(cardinfo[3]);

			int attack = Integer.parseInt(cardinfo[6]);
			int defense = Integer.parseInt(cardinfo[7]);

			this.ani = new Ani(resource, name, description, cost + "", attack
					+ "", defense + "");

			this.attack = new ViewBinder(context, attack, this);
			RelativeLayout.LayoutParams attackparam = this.attack.getParams();
			attackparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			attackparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			attackparam.setMargins(Method.dpToPx(6), 0, 0, Method.dpToPx(1));

			this.vital = new ViewBinder(context, defense, this);
			RelativeLayout.LayoutParams vitalparam = this.vital.getParams();
			vitalparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			vitalparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			vitalparam.setMargins(0, 0, Method.dpToPx(6), Method.dpToPx(1));

			break;
		}

		this.index = index;

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (stateChange) {
					toggleMultipleSelect();
					return;
				}
				toggleSingleSelect();

			}
		});

	}

	public void toggleMultipleSelect() {

		if (selected == false) {
			up();
		} else {
			down();
		}
	}

	public void toggleSingleSelect() {
		if (select == this) {
			down();
			select = null;
			Helper.hideInfo();
			return;
		}
		if (select != null)
			select.down();
		select = this;
		Helper.showInfo(ani);
		up();
	}

	public void down() {
		this.setBackgroundResource(Method.resId(resource + "c"));
		selected = false;
		params.bottomMargin = 0;
		setLayoutParams(params);
	}

	private void up() {
		this.setBackgroundResource(Method.resId(resource + "clicked"));
		params.bottomMargin = Method.dpToPx(10);
		setLayoutParams(params);
		selected = true;
	}

	public void deSelect() {
		up();
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

	public void use(Player player, boolean sended) {
		Sender.S("80&" + ani.toString());
		player.hand.remove(this);
		player.hero.mana.Add(-cost.Int(), false);
		if (monster > 0)
			addMonster(player);

		if (haseffect)
			runEffects(player, cardeffects);
		Helper.hideInfo();
	}

	private void runEffects(Player player, String effects) {
		String[] effect = effects.split("#");
		for (int i = 0; i < effect.length; i += 2) {
			CardEffect.run(player, Integer.parseInt(effect[i]),
					Integer.parseInt(effect[i + 1]));
			Log.i("effect", effects);
		}
	}

	private void addMonster(Player player) {
		Monster monster = new Monster(context, this, player.field,
				monstereffects, false);
		player.field.add(monster);
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public static void stateChange() {
		stateChange = true;
	}

	public String toString() {
		return cardinfo;
	}

	public Ani getAni() {
		return ani;
	}
}
