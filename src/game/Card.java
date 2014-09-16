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

	boolean haseffect, legend;
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

		legend = false;
		if (cardinfo[4].contains("LEGEND"))
			legend = true;

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
		addView(image, 0);

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
		if (legend) {
			setBackgroundResource(R.drawable.legend);
			return;
		}
		if (monster != 0) {
			setBackgroundResource(R.drawable.monstercard);
			return;
		}
		setBackgroundResource(R.drawable.spellcard);

	}

	private void clickBackground() {
		if (legend) {
			setBackgroundResource(R.drawable.legendclicked);
			return;
		}
		if (monster != 0) {
			setBackgroundResource(R.drawable.monstercardclicked);
			return;
		}
		setBackgroundResource(R.drawable.spellcardclicked);

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
			runEffects(player, cardeffect);
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

	public final static int MANA_ADD = 0;
	public final static int MASSIVE_ATTACK = 50;
	public final static int SPELLPOWER = 55;
	public final static int DRAWCARD_ONTHEFIELD = 60;
	public final static int DRAWCARD_TURNSTART = 61;
	public final static int DRAWCARD_TRUNEND = 62;
	public final static int DRAWCARD_DIE = 63;
	public final static int HEAL_ONTHEFIELD = 100;
	public final static int ABILITY_UP = 200;
	public final static int STUN = 300;
	public final static int STUN_GUN = 310;
	public final static int STUN_DAMAGE_GUN = 315;
	public final static int MASSIVE_STUN = 320;
	public final static int MASSIVE_STUN_DAMAGE = 325;
	public final static int DRAW_CARD = 400;
	public final static int GET_WEAPON = 500;
	public final static int GET_WEAPON_DEATH_EFFECT = 510;
	public final static int GET_WEAPON_LEGEND = 520;
	public final static int GET_WEAPON_HEAL = 530;
	public final static int GET_WEAPON_BUFF = 540;
	public final static int ATTACK_TURNEND = 900;
	public final static int ATTACK_TURNSTART = 901;
	public final static int RANDOM_ATTACK = 902;
	public final static int ADD_TWO_BATS = 910;
	public final static int ABILITYUP_EVERYTURN = 950;

	String res;

	private void runEffects(final Player player, String effects) {
		final String[] effect = effects.split("#");
		int type = Integer.parseInt(effect[0]);
		final String amount = effect[1];
		res = "pig";
		if (effect.length == 3) {
			res = effect[2];
		}
		final Monster mon;
		String[] tmp;
		switch (type) {

		case GET_WEAPON:
			tmp = amount.split("=");
			removeCardFromHand(player);
			player.hero.getWeapon(Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]), res, false, 0);
			break;

		case GET_WEAPON_BUFF:
			tmp = amount.split("=");
			removeCardFromHand(player);
			final Weapon buffw = player.hero.getWeapon(Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]), res, false, 0);
			final ExcuteEffect buff = new ExcuteEffect() {
				@Override
				public void run() {
					player.field.items.get(player.field.size() - 1).abilityUp(
							"1=1", false, player.hero.hero(), res);
					buffw.use(player.hero.hero());
				}
			};
			player.addNewMonsterEffect(buff);
			buffw.setDeathEffect(new ExcuteEffect() {
				
				@Override
				public void run() {
					player.removeNewMonsterEffect(buff);
				}
			});
			
			
			break;

		case GET_WEAPON_HEAL:
			tmp = amount.split("=");
			removeCardFromHand(player);
			player.hero.getWeapon(Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]), "heroability3", false, 0)
					.setDeathEffect(new ExcuteEffect() {
						@Override
						public void run() {
							player.hero.hero.heal(2, false, player.hero.hero(),
									res);
						}
					});
			break;

		case GET_WEAPON_DEATH_EFFECT:
			tmp = amount.split("=");
			removeCardFromHand(player);
			player.hero.getWeapon(Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]), "heroability3", false, 0)
					.setDeathEffect(new ExcuteEffect() {
						@Override
						public void run() {
							player.field.heal(-1, false, player.hero.hero(),
									res);
							player.enemy.field.heal(-1, false,
									player.hero.hero(), res);
						}
					});
			break;

		case GET_WEAPON_LEGEND:
			tmp = amount.split("=");
			removeCardFromHand(player);
			player.hero.getWeapon(Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]), "heroability3", false, 0)
					.legend();
			break;

		case MASSIVE_STUN_DAMAGE:
			removeCardFromHand(player);
			res = "bullet";
			if (effect.length == 3) {
				res = effect[2];
			}
			player.enemy.field.Stun(false, player.hero.hero(), res);
			player.enemy.field.heal(
					Integer.parseInt(amount) + player.getSpellpower(), false,
					player.hero.hero(), res);
			break;

		case DRAW_CARD:
			removeCardFromHand(player);
			player.newCard(Integer.parseInt(amount));
			break;

		case STUN:
			Method.alert("대상을 선택하세요.");
			OnClickListener stun = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeCardFromHand(player);
					if (monster > 0) {
						addMonster(player);
					}
					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					selected.setStun(false, player.hero.hero(), "bullet");
				}
			};

			Listeners.setListener(stun);

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

		case STUN_DAMAGE_GUN:
			Method.alert("대상을 선택하세요.");
			OnClickListener stundgun = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					selected.setStun(false, player.hero.hero(), "bullet");
					selected.heal(
							player.getSpellpower() + Integer.parseInt(amount),
							false, player.hero.hero(), "bullet");
				}
			};

			Listeners.setListener(stundgun);

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

		case STUN_GUN:
			Method.alert("대상을 선택하세요.");
			OnClickListener stungun = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					if (!selected.isStunned()) {
						selected.setStun(false, player.hero.hero(), "bullet");
					} else
						selected.heal(player.getSpellpower() - 4, false,
								player.hero.hero(), "bullet");
				}
			};

			Listeners.setListener(stungun);

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

		case MASSIVE_STUN:
			removeCardFromHand(player);
			player.enemy.field.Stun(false, player.hero.hero(), "bullet");
			break;

		case MANA_ADD: // 이번턴에만 마나를 올림
			removeCardFromHand(player);
			player.hero.mana.Add(Integer.parseInt(amount), false);
			break;

		case MASSIVE_ATTACK:// 상대 필드에 광역데미지
			removeCardFromHand(player);
			String re = "fireball";
			if (effect.length == 3) {
				re = effect[2];
			}
			player.enemy.field.heal(
					Integer.parseInt(amount) + player.getSpellpower(), false,
					player.hero.hero(), re);
			break;

		case DRAWCARD_ONTHEFIELD:
			removeCardFromHand(player);
			mon = addMonster(player);
			player.newCard(Integer.parseInt(amount));
			break;

		case HEAL_ONTHEFIELD: // 전장에 나오며 공격 / 혹은 치료
			Method.alert("대상을 선택하세요.");
			OnClickListener heal = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int spellpower = 0;
					if (monster > 0) {
						addMonster(player);
					} else {
						spellpower += player.getSpellpower();
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

		case ABILITY_UP: // 능력치 올림 amount 공격력=방어력의 형태로 정의
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

		case ATTACK_TURNEND: // 이순신
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

		case RANDOM_ATTACK:
			removeCardFromHand(player);
			Random r = new Random();
			Target target;
			int t;
			for (int i = 0; i < Integer.parseInt(amount)
					- player.getSpellpower(); i++) {
				t = r.nextInt(player.enemy.field.size() + 1);
				if (t == player.enemy.field.size())
					target = player.enemy.hero.hero();
				else {
					target = player.enemy.field.get(t);
				}
				target.heal(-1, false, player.hero.hero(), "pig");
			}
			break;

		case ATTACK_TURNSTART: // 파괴전차
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

		case ABILITYUP_EVERYTURN: // 그롤
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

		case ADD_TWO_BATS: // 논개
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

	private Monster addMonster(Player player) {
		Monster monster = new Monster(context, this, player.field, false);
		player.field.add(monster);
		return monster;
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
