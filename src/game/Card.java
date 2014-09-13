package game;

import java.util.ArrayList;

import net.Sender;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import animation.Ani;
import animation.Helper;

import components.ViewBinder;

public class Card extends RelativeLayout {

	static boolean stateChange = true;
	public static boolean use = false;
	public static Card select = null;
	public static ArrayList<String> defaultcards = null;
	public static ArrayList<String> herocards = null;
	public static ArrayList<String> enemycards = null;

	boolean haseffect;
	Context context;
	int monster;
	String cardinfo;
	Hand hand;
	Ani ani;

	ViewBinder cost, attack, vital;
	String resource, name, description, monstereffects, cardeffect;
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
		// costparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		costparam.setMargins(Method.dpToPx(6), Method.dpToPx(1), 0, 0);

		// 효과 종류 구분해서 나눠줌. 카드이펙트가 잇는경우 %로 구분.
		if (!cardinfo[4].contains("%")) {
			monstereffects = cardinfo[4]; // 효과1,효과2,...
			haseffect = false;
		} else {
			String[] tmp = cardinfo[4].split("%");
			monstereffects = tmp[0];
			cardeffect = tmp[1];
			haseffect = true;
		}

		monster = Integer.parseInt(cardinfo[3]);

		if (monster == 0)
			this.ani = new Ani(resource, name, description, cost + "", " ", " ");
		else {
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
		Helper.hideInfo();

		if (haseffect) {
			runEffects(player, cardeffect, this);
			return;
		}

		addMonster(player);
		removeCardFromHand(player);
	}

	private void removeCardFromHand(Player player) {
		Sender.S("80&" + ani.toString()); // send effect
		player.hand.remove(this); // remove card
		player.hero.mana.Add(-cost.Int(), false); // consume mana
	}

	private void runEffects(final Player player, String effects, Card card) {
		final String[] effect = effects.split("#");
		int type = Integer.parseInt(effect[0]);
		final int amount = Integer.parseInt(effect[1]);



		switch (type) {
		case 0:
			removeCardFromHand(player);
			player.hero.mana.Add(amount, false);

			break;

		case 100:

			Method.alert("대상을 선택하세요.");

			
			OnClickListener heal = new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (monster > 0)
						addMonster(player);

					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					String res = null;
					if (effect.length == 3) {
						res = effect[2];
					}
					selected.heal(amount, false, player.hero.hero(), res);

				}

			};

			Listeners.setListener(heal);

			player.endturn.setText("    취소");
			player.endturn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Static.Cancel(player, true);

				}

			});

			player.field.setListener();
			player.hero.setListener();
			player.enemy.field.setListener();
			player.enemy.hero.setListener();
			break;
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
