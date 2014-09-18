package game;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import animation.Ani;
import animation.Attack;
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
	String resource, name, description, monstereffects, cardeffect = "";
	int idforMonster, cardid;
	RelativeLayout.LayoutParams params;
	int dX, dY, firstX, firstY;
	Player player;
	float rotate;

	boolean selected;

	@SuppressLint("ClickableViewAccessibility")
	public Card(Context context, String eachcard, int idforMonster, int cardid,
			Player player) {
		super(context);
		this.player = player;
		this.context = context;
		this.cardid = cardid;
		this.cardinfo = eachcard;
		this.ani = new Ani(eachcard);
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(70);
		params.height = Method.dpToPx(90);
		setLayoutParams(params);

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

		normalBackground();
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

		setOnTouchListener(new OnTouchListener() {
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				final int X = (int) event.getRawX();
				final int Y = (int) event.getRawY();
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					if (stateChange)
						toggleMultipleSelect();
					else
						SingleSelect();
					setRotation(0);
					Helper.showInfo(ani);
					firstX = params.leftMargin;
					firstY = params.topMargin;
					RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					dX = X - lParams.leftMargin;
					dY = Y - lParams.topMargin;

					break;
				case MotionEvent.ACTION_UP:
					params.leftMargin = firstX;
					params.topMargin = firstY;
					setRotation(rotate);
					if ((Y - dY) < 730)
						useCard();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					params.leftMargin = firstX;
					params.topMargin = firstY;
					setRotation(rotate);
					break;
				case MotionEvent.ACTION_MOVE:
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
							.getLayoutParams();
					layoutParams.leftMargin = X - dX;
					layoutParams.topMargin = Y - dY;
					Log.i("y", "" + (Y - dY));
					view.setLayoutParams(layoutParams);
					break;
				}
				Attack.container.invalidate();
				return true;
			}

		});
	}

	public void normalBackground() {
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

	public void greenBackground() {
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
			selected = true;
			greenBackground();
		} else {
			selected = false;
			normalBackground();
		}
	}

	public void SingleSelect() {
		/*if (select == this) {
			down();*/
			select = this;
			selected = true;
/*			Helper.hideInfo();
			return;
		}
		if (select != null)
			select.down();
		select = this;
		Helper.showInfo(ani);
		up();*/
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
	
	public void useCard() {
		
		if (!player.myturn) {
			Method.alert("상대방의 차례입니다.");
			return;
		}
		if (player.field.size() + monster > 7) {
			Method.alert("하수인은 7명까지 소환 가능합니다.");
			return;
		}
		
		if (cost() > player.mana()) {
			Method.alert("마나가 부족합니다.");
			return;
		}
		
		use(false);
		player.cardStateUpdate();
	
	}
	
	public void use(boolean sended) {
		Helper.hideInfo();

		if (haseffect) {
			runEffects(cardeffect);
			return;
		}

		addMonster(player);
		removeCardFromHand(player);
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

	private void removeCardFromHand(final Player player) {
		Game.sender.S("80&" + player.me + "@" + -1 + "@" + cardid); // send
																	// effect
		if (cardeffect.contains("OVERMANA")) {
			final int overmana = Integer
					.parseInt(cardeffect.split("OVERMANA")[1]);
			player.addNewTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					player.hero.mana.Add(-overmana, false);
				}
			});
		}
		if (cardeffect.contains("LOSTCARD")) {
			int card = Integer.parseInt(cardeffect.split("LOSTCARD")[1]);
			player.hand.lostCards(card);
		}
		player.listener.sendEmptyMessage(1);
		player.hand.remove(this); // remove card
		player.hero.mana.Add(-cost.Int(), false); // consume mana
	}

	public final static int NOEFFECT = 0;
	public final static int MANA_ADD = 2;
	public final static int SPAWN = 1;
	public final static int MASSIVE_ATTACK = 50;
	public final static int SPELLPOWER = 55;

	public final static int ONTHEFIELD_HEAL = 100;

	public final static int ABILITY_UP = 200;

	public final static int STUN = 300;
	public final static int STUN_GUN = 310;
	public final static int STUN_DAMAGE_GUN = 315;
	public final static int STUN_MASSIVE = 320;
	public final static int STUN_MASSIVE_DAMAGE = 325;

	public final static int DRAW_CARD = 400;
	public final static int DRAW_CARD_50 = 401;
	public final static int DRAWCARD_ONTHEFIELD = 60;
	public final static int DRAWCARD_TURNSTART = 61;
	public final static int DRAWCARD_TRUNEND = 62;
	public final static int DRAWCARD_DIE = 63;

	public final static int GET_WEAPON = 500;
	public final static int GET_WEAPON_DEATH_EFFECT = 510;
	public final static int GET_WEAPON_LEGEND = 520;
	public final static int GET_WEAPON_HEAL = 530;
	public final static int GET_WEAPON_BUFF = 540;

	public final static int DEATH_EFFECT = 600;
	public final static int DEATH_EFFECT_HEAL = 601;
	public final static int DEATH_EFFECT_SPAWN = 602;
	public final static int DEATH_EFFECT_GET_WEAPON = 603;
	public final static int DEATH_EFFECT_DRAWCARD = 604;
	public final static int DEATH_EFFECT_GETWEAPON = 605;

	public final static int THISTURNONLY_ABILITY = 700;

	public final static int USEDCARD_DAMAGE = 800;
	public final static int USEDCARD_DAMAGEUP = 801;
	public final static int USEDCARD_SPAWN = 802;
	public final static int USEDCARD_NEXTTURNBACK = 803;
	public final static int USEDCARD_ABILITYUP = 804;

	public final static int ATTACK_TURNEND = 900;
	public final static int ATTACK_TURNSTART = 901;
	public final static int RANDOM_ATTACK = 902;
	public final static int ABILITYUP_EVERYTURN = 950;

	String res;

	public void runEffects(String effects) {
		final String[] effect = effects.split("#");
		int type = Integer.parseInt(effect[0]);
		final String amount = effect[1];
		res = "pig";
		if (effect.length > 2) {
			res = effect[2];
		}

		final Monster mon;
		final String[] tmp;
		final Field field;
		final Player effectedPlayer;
		switch (type) {

		// "쇼군;방어 보호막 돌진 공격X2;sabu;1;0#1#2#5#3;8;3;5"
		// "쇼군;공격X2;sabu;1;0#5;6;4;5"
		// "쇼군;공격X2;sabu;1;0#5;3;2;3"
		// "쇼군;공격X2;sabu;1;0#5;1;1;1"

		case NOEFFECT:
			addMonster(player);
			removeCardFromHand(player);
			break;

		case SPAWN:
			removeCardFromHand(player);
			if (monster > 0)
				addMonster(player);
			tmp = amount.split("="); // amount양식 : 필드=인덱스=몇마리
			field = Integer.parseInt(tmp[0]) == 0 ? player.field
					: player.enemy.field;
			for (int i = 0; i < Integer.parseInt(tmp[2]); i++)
				field.add(new Monster(context, new Card(context, player
						.getCardStringById(Integer.parseInt(tmp[1])), Static
						.index(), Integer.parseInt(tmp[1]), player), field, false));
			break;

		case USEDCARD_DAMAGE:
			final int damage;
			tmp = amount.split("=");
			damage = player.usedcardsize == 0 ? Integer.parseInt(tmp[0])
					: Integer.parseInt(tmp[1]);
			if (damage == 0 && monster > 0) {
				removeCardFromHand(player);
				addMonster(player);
				return;
			}
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
					selected.heal(damage + spellpower, false,
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

		case USEDCARD_DAMAGEUP:

			final String am = player.usedcardsize == 0 ? "2=0" : "4=0";
			Method.alert("대상을 선택하세요.");

			OnClickListener abilityup = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeCardFromHand(player);
					Target selected = (Target) v;
					Static.Cancel(player, false);
					String res = null;
					if (effect.length == 3) {
						res = effect[2];
					}
					selected.abilityUp(am, false, player.hero.hero(), res);

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

		case USEDCARD_SPAWN:
			removeCardFromHand(player);
			if (monster > 0)
				addMonster(player);
			if (player.usedcardsize == 0)
				return;
			player.field.add(new Monster(context, new Card(context, player
					.getCardStringById(-7), Static.index(), -7, player), player.field,
					false));
			break;

		case USEDCARD_NEXTTURNBACK:
			removeCardFromHand(player);
			player.enemy.hero.hero().heal(-2, false, player.hero.hero(), res);
			final Card back = this;
			player.addNewTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					player.newCard(back);
				}
			});
			break;

		case USEDCARD_ABILITYUP:
			removeCardFromHand(player);
			mon = addMonster(player);
			int up = player.usedcardsize * 2;
			mon.abilityUp(up + "=" + up, false, mon, res);
			break;

		case DEATH_EFFECT_GETWEAPON: // player=dam=dura
			removeCardFromHand(player);
			tmp = amount.split("=");
			effectedPlayer = Integer.parseInt(tmp[0]) == 0 ? player
					: player.enemy;

			addMonster(player).setDeathEffect(new ExcuteEffect() {
				@Override
				public void run() {
					effectedPlayer.hero.getWeapon(Integer.parseInt(tmp[1]),
							Integer.parseInt(tmp[2]), res, false, 0);
				}
			});
			break;

		case DEATH_EFFECT_SPAWN: // field=cardid=size
			removeCardFromHand(player);
			tmp = amount.split("=");
			field = Integer.parseInt(tmp[0]) == 0 ? player.field
					: player.enemy.field;

			addMonster(player).setDeathEffect(new ExcuteEffect() {
				@Override
				public void run() {
					for (int i = 0; i < Integer.parseInt(tmp[2]); i++)
						field.add(new Monster(context, new Card(context, player
								.getCardStringById(Integer.parseInt(tmp[1])),
								Static.index(), Integer.parseInt(tmp[1]), player),
								field, false));
				}
			});
			break;

		case DEATH_EFFECT_DRAWCARD: // player=amount
			removeCardFromHand(player);
			tmp = amount.split("=");
			effectedPlayer = Integer.parseInt(tmp[0]) == 0 ? player
					: player.enemy;

			addMonster(player).setDeathEffect(new ExcuteEffect() {
				@Override
				public void run() {
					effectedPlayer.newCard(Integer.parseInt(tmp[1]));
				}
			});
			break;

		case DEATH_EFFECT_HEAL:
			removeCardFromHand(player);
			tmp = amount.split("=");
			mon = addMonster(player);
			mon.setDeathEffect(new ExcuteEffect() {
				@Override
				public void run() {
					switch (Integer.parseInt(tmp[0])) {
					case 0:
						player.hero.hero().heal(Integer.parseInt(tmp[1]),
								false, player.hero.hero(), res);
						break;
					case 1:
						player.enemy.hero.hero().heal(Integer.parseInt(tmp[1]),
								false, player.hero.hero(), res);
						break;
					case 2:
						player.enemy.field.heal(Integer.parseInt(tmp[1]),
								false, player.hero.hero(), res);
						player.field.heal(Integer.parseInt(tmp[1]), false,
								player.hero.hero(), res);
						break;
					case 3:
						player.heal(Integer.parseInt(tmp[1]), false,
								player.hero.hero(), res);
						player.enemy.heal(Integer.parseInt(tmp[1]), false,
								player.hero.hero(), res);
						break;
					case 4:
						player.heal(Integer.parseInt(tmp[1]), false,
								player.hero.hero(), res);
						break;
					}
				}
			});
			break;

		case GET_WEAPON:
			tmp = amount.split("=");
			removeCardFromHand(player);
			player.hero.getWeapon(Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]), res, false, 0);
			break;

		case GET_WEAPON_BUFF:
			tmp = amount.split("=");
			removeCardFromHand(player);
			final Weapon buffw = player.hero.getWeapon(
					Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), res,
					false, 0);
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

		case STUN_MASSIVE_DAMAGE:
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

		case STUN_MASSIVE:
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

		case DRAW_CARD_50:
			removeCardFromHand(player);
			mon = addMonster(player);
			mon.setNewTurnEffect(new ExcuteEffect() {
				@Override
				public void run() {
					Random r = new Random();
					if (r.nextInt(2) == 1)
						player.newCard();
				}
			});
			break;

		case DRAWCARD_ONTHEFIELD:
			removeCardFromHand(player);
			mon = addMonster(player);
			player.newCard(Integer.parseInt(amount));
			break;

		case ONTHEFIELD_HEAL: // 전장에 나오며 공격 / 혹은 치료
			Method.alert("대상을 선택하세요.");
			OnClickListener he = new View.OnClickListener() {
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

			Listeners.setListener(he);

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

			OnClickListener abup = new View.OnClickListener() {
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

			Listeners.setListener(abup);

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

		}
	}

}
