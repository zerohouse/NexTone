package game;

import java.util.ArrayList;
import java.util.Random;

import net.Sender;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import animation.Ani;
import animation.Helper;

import com.mylikenews.nextoneandroid.R;
import components.ViewBinder;

import effects.monster.excute.ExcuteEffect;

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
	Ani ani;

	ViewBinder cost, attack, vital;
	String resource, name, description, monstereffects, cardeffect;
	int idforMonster, cardid;
	LinearLayout.LayoutParams params;

	boolean selected;

	public Card(Context context, String eachcard, int idforMonster, int cardid) {
		super(context);
		this.context = context;
		this.cardid = cardid;
		this.cardinfo = eachcard;
		this.ani = new Ani(eachcard);
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

		cardBackground();
		ImageView image = new ImageView(context);
		image.setImageResource(Method.resId(resource));
		addView(image,0);

		if (monster != 0) {
			int attack = Integer.parseInt(cardinfo[6]);
			int defense = Integer.parseInt(cardinfo[7]);

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

		this.idforMonster = idforMonster;

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

	private void cardBackground() {
		if (monster != 0) {
			setBackgroundResource(R.drawable.monstercard);
		} else {
			setBackgroundResource(R.drawable.spellcard);
		}
	}

	private void clickBackground() {
		if (monster != 0) {
			setBackgroundResource(R.drawable.monstercardclicked);
		} else {
			setBackgroundResource(R.drawable.spellcardclicked);
		}
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
		cardBackground();
		selected = false;
		params.bottomMargin = 0;
		setLayoutParams(params);
	}

	private void up() {
		clickBackground();
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
		return cardid;
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
		Sender.S("80&" + player.me + "@" + -1 + "@" + cardid); // send effect
		player.hand.remove(this); // remove card
		player.hero.mana.Add(-cost.Int(), false); // consume mana
	}

	private void runEffects(final Player player, String effects, Card card) {
		final String[] effect = effects.split("#");
		int type = Integer.parseInt(effect[0]);
		final String amount = effect[1];

		final Monster mon;

		switch (type) {
		case 0:
			removeCardFromHand(player);
			player.hero.mana.Add(Integer.parseInt(amount), false);

			break;

		case 50:
			removeCardFromHand(player);
			String res = "fireball";
			if (effect.length == 3) {
				res = effect[2];
			}
			player.enemy.field.heal(Integer.parseInt(amount)
					+ player.spellpower, false, player.hero.hero(), res);

			break;

		case 100:
			Method.alert("대상을 선택하세요.");

			OnClickListener heal = new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					int spellpower = 0;
					if (monster > 0) {
						addMonster(player);
					} else {
						spellpower += player.spellpower;
					}

					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					String res = null;
					if (effect.length == 3) {
						res = effect[2];
					}
					selected.heal(Integer.parseInt(amount) + spellpower, false,
							player.hero.hero(), res);

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

		case 200:
			if (player.field.size() == 0) {
				removeCardFromHand(player);
				addMonster(player);
				return;
			}

			Method.alert("대상을 선택하세요.");

			OnClickListener abilityup = new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (monster > 0) {
						addMonster(player);
					}

					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					String res = null;
					if (effect.length == 3) {
						res = effect[2];
					}
					selected.abilityUp(amount, false, player.hero.hero(), res);

				}

			};

			Listeners.setListener(abilityup);

			player.endturn.setText("    취소");
			player.endturn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Static.Cancel(player, true);

				}

			});

			player.field.setListener();
			break;

		case 900: // 이순신
			removeCardFromHand(player);
			mon = new Monster(context, this, player.field, false);
			player.field.add(mon);
			mon.maxattackable = 0;
			mon.setEndTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					Random r = new Random();
					Target target;
					int t = r.nextInt(player.enemy.field.size() + 1);
					if (t == player.enemy.field.size())
						target = player.enemy.hero.hero();
					else {
						target = player.enemy.field.get(t);
					}
					target.heal(Integer.parseInt(amount), false, mon, "tutle");
				}
			});

			break;

		case 901: // 파괴전차
			removeCardFromHand(player);
			mon = new Monster(context, this, player.field, false);
			player.field.add(mon);
			mon.setNewTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					Random r = new Random();
					Target target;
					int t = r.nextInt(player.enemy.field.size() + 1);
					if (t == player.enemy.field.size())
						target = player.enemy.hero.hero();
					else {
						target = player.enemy.field.get(t);
					}
					target.heal(Integer.parseInt(amount), false, mon, "pig");
				}
			});

			break;

		case 950: // 그롤
			removeCardFromHand(player);
			mon = new Monster(context, this, player.field, false);
			player.field.add(mon);

			mon.setNewTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					mon.abilityUp(amount, false, mon, "pig");
				}
			});

			mon.setEndTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					mon.abilityUp(amount, false, mon, "pig");
				}
			});

			break;

		case 910: // 논개
			removeCardFromHand(player);
			Monster tmp1 = new Monster(context, new Card(context,
					"박쥐;;bat;1;0;0;1;1", Static.index(), -1),
					player.enemy.field, false);
			Monster tmp2 = new Monster(context, new Card(context,
					"박쥐;;bat;1;0;0;1;1", Static.index(), -1),
					player.enemy.field, false);
			player.enemy.field.add(tmp1);
			player.enemy.field.add(tmp2);
			addMonster(player);
			break;
		}
	}

	private void addMonster(Player player) {
		Monster monster = new Monster(context, this, player.field, false);
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

	public String getMonstereffects() {
		return monstereffects;
	}

	public int getMonsterindex() {
		return idforMonster;
	}
}
