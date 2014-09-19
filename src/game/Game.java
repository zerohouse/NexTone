package game;



import net.Sender;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import animation.Ani;
import animation.Attack;
import animation.Helper;

import com.mylikenews.nextoneandroid.R;

public class Game {

	public static Sender sender;
	Player player1, player2;
	Context context;
	LinearLayout container;
	String player1dek, player1hero, player2dek, player2hero;
	boolean first;
	static boolean isgameStart = false;
	RelativeLayout animate;

	public Game(Context context, LinearLayout container,
			RelativeLayout animate, String dekstring, String herostring) {
		this.context = context;
		this.container = container;
		this.animate = animate;
		player1dek = dekstring;
		player1hero = herostring;
	}
	
	public void player1Setting(boolean first) {

		isgameStart = true;
		Attack.setAnimate(animate);
		Helper.setHelper(context);

		container.removeAllViews();

		player1 = new Player(context, player1dek, player1hero, 1, this, first);
		if (first) {
			Method.alert("게임을 시작합니다.");
			addPlayerCard(3);
			player1.first();
		} else {
			Method.alert("게임을 시작합니다.");
			addPlayerCard(4);
			player1.second();
		}
		this.first = first;

		Game.sender.S("7&1@" + player1.heroString());
	}

	public void player2Setting() {

		player2 = new Player(context, player2dek, player2hero, 2, this,
				!player1.getFirst());
		if (first) {
			player2.second();
		} else {
			player2.first();
		}

		player1.setEnemy(player2);
		player2.setEnemy(player1);

	}

	public void initView() {
		if (container.getChildCount() > 3) {
			return;
		}
		FrameLayout fieldarea = new FrameLayout(context);
		LinearLayout.LayoutParams fieldparam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
		fieldarea.setLayoutParams(fieldparam);

		ImageView fieldback = new ImageView(context);
		fieldback.setLayoutParams(fieldparam);
		fieldback.setScaleType(ScaleType.FIT_XY);
		fieldback.setBackgroundResource(R.drawable.field);

		LinearLayout innerfieldarea = new LinearLayout(context);
		fieldarea.setLayoutParams(fieldparam);
		innerfieldarea.setOrientation(LinearLayout.VERTICAL);

		container.addView(fieldarea, 0);

		fieldarea.addView(fieldback);
		fieldarea.addView(innerfieldarea);

		innerfieldarea.addView(player2.field());
		innerfieldarea.addView(player1.field());

		player2.addHero();
		player1.addHero();
		
		Helper.params.addRule(RelativeLayout.CENTER_IN_PARENT);

	}

	private void addPlayerCard(int size) {
		player1.firstSetting(size);
		animate.addView(player1.hand);
	}

	public void Do(String resString) {
		try {
			String[] response = resString.split("&");

			int type = Integer.parseInt(response[0]);
			switch (type) {

			case 0: // 0번이 넘어오면 시작한다. (카드 바꾸기 화면 실행)
				Card.stateChange();
				if (Integer.parseInt(response[1]) == 1) {
					player1Setting(true);
					return;
				}
				player1Setting(false);
				break;

			case 3: // 3번은 상데의 덱과 히어로정보를 가지고 온다.
					// 상대의 덱을 세팅.
				String[] dekhero = response[1].split(";");
				player2dek = dekhero[0];
				player2hero = dekhero[1];
				player2Setting();
				break;
 
			case 30: // 카드뽑아라.
				player1.newCard();
				break;

			case 4: // 4번이 넘어오면 턴을 넘긴다.
				player2.endTurnByNet();
				player1.newTurn();
				break;

			case 5: // 맨처음 시작할때. 선공 플레이어가 세팅을 마쳤으면,
					// 게임을 시작하고, 아니면 카드 바꾸기 아이템에
					// 게임 시작 이벤트도 걸어준다.
				if (player1.done()) {
					Log.i("init", "시작함");
					initView();
					player1.newTurn();
					Game.sender.S("6&");
					return;
				}

				Log.i("init", "시작함");
				player1.ChangeToStartTurn();
				break;

			case 6: // 처음 화면에 필드와 히어로를 추가한다.
				initView();

			case 7: // 7번은 영웅정보를 가지고 온다.
					// (이미 생성된) 영웅 인스턴스의 정보를 바꾼다.
				String res[] = response[1].split("@");
				if (res[0].equals("1")) {
					player2.hero.setByString(res[1]);
					return;
				}
				player1.hero.setByString(res[1]);
				break;

			case 8: // 8번은 사용할 카드의 정보를 가지고 온다.
					// 플레이어의 덱에있는 카드를 index를 통해 사용.

				String[] mon = response[1].split("@");

				Card card;
				String cardstring;
				if (mon[0].equals("1")) {
					cardstring = player2.getCardStringById(Integer
							.parseInt(mon[2]));
					card = new Card(context, cardstring, 
							Integer.parseInt(mon[1]), Integer.parseInt(mon[2]), player2);
					player2.field.addByCard(card, true, Integer.parseInt(mon[3])); 
					return;
				}
				cardstring = player1
						.getCardStringById(Integer.parseInt(mon[2]));
				card = new Card(context, cardstring, Integer.parseInt(mon[1]),
						Integer.parseInt(mon[2]), player1);
				player1.field.addByCard(card, true, Integer.parseInt(mon[3]));
				break;

			case 80:
				Helper.showInfo(new Ani(getCardStringById(response[1])));
				break;

			case 9: // 9번은 공격정보를 가져온다.
					// 인덱스의 몬스터들끼리 공격을 주고 받는다.
				String[] attack = response[1].split(",");
				Target one,
				another;
				if (Integer.parseInt(attack[0]) == -1)
					one = player1.field.hero.hero();
				else
					one = player1.field.getByIndex(Integer.parseInt(attack[0]));

				if (Integer.parseInt(attack[1]) == -1)
					another = player2.field.hero.hero();
				else
					another = player2.field.getByIndex(Integer
							.parseInt(attack[1]));

				another.attack(one, true);
				break;

			case 10:
				Method.alert("상대방의 턴입니다."); // 턴알림
				break; 

			case 11:
				Target attacker;
				int resint = Integer.parseInt(response[1]);

				player2.hero.deSelect();
				player2.field.attackCheck(); 

				if (resint == -1)
					attacker = player2.hero.hero();
				else
					attacker = player2.field.getByIndex(resint);

				attacker.setAttackBackground();// 상대방의 필드에서 선택되면 attack이미지로 변경

				break;

			case 12:
				Static.Cancel(player2, false);

				break;

			case 13:
				String mana[] = response[1].split(",");
				if (Integer.parseInt(mana[0]) == 1) {
					player2.hero.mana.Add(Integer.parseInt(mana[1]), true);
					return;
				}
				player1.hero.mana.Add(Integer.parseInt(mana[1]), true);

				break;

			case 16:
				String heal[] = response[1].split(",");

				int healamount = Integer.parseInt(heal[1]);

				Target healtarget = getByIndex(heal[0]);
				Target from = getByIndex(heal[2]);

				if (heal[3].equals("null")) {
					healtarget.heal(healamount, true, from, null);
					break;
				}
				healtarget.heal(healamount, true, from, heal[3]);
				break;

			case 165:
				String stun[] = response[1].split(",");
				Target stuntarget = getByIndex(stun[0]);
				Target stunfrom = getByIndex(stun[1]);
				if (stun[2].equals("null")) {
					stuntarget.setStun(true, stunfrom, null);
					break;
				}
				stuntarget.setStun(true, stunfrom, stun[2]);
				break;

			case 166:
				Target wake = getByIndex(response[1]);
				wake.wakeUp(true);
				break;

			case 160:
				String ability[] = response[1].split(",");
				String abamount = ability[1];

				Target abtarget = getByIndex(ability[0]);
				Target abfrom = getByIndex(ability[2]);

				if (ability[3].equals("null")) {
					abtarget.abilityUp(abamount, true, abfrom, null);
					break;
				}
				abtarget.abilityUp(abamount, true, abfrom, ability[3]);
				break;

			case 14:
				String weapon[] = response[1].split(",");
				int damage = Integer.parseInt(weapon[0]);
				int vital = Integer.parseInt(weapon[1]);
				String resource = weapon[2];
				player2.hero.getWeapon(damage, vital, resource, true, 0);
				break;

			case 15:
				String defense[] = response[1].split(",");
				if (Integer.parseInt(defense[0]) == 1) {
					player2.hero.getDefense(Integer.parseInt(defense[1]), true,
							0);
					return;
				}
				player1.hero.getDefense(Integer.parseInt(defense[1]), true, 0);
				break;

			case 18:
				String dam[] = response[1].split(",");
				if (Integer.parseInt(dam[0]) == 1) {
					player2.hero.setDamage(Integer.parseInt(dam[1]), true);
					return;
				}
				player1.hero.setDamage(Integer.parseInt(dam[1]), true);
				break;

			case 100:
				player1.gameEnd(1); // 패배
				Game.sender.S("550&");
				break;

			case 101:
				player1.gameEnd(2); // 상대가 나감.
				break;

			case 103:
				player1.gameEnd(3); // 상대가 나감.
				break;

			case 550: // 게임 엔드.
				Game.sender.close();
				break;
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getCardStringById(String string) {
		String[] tmp = string.split("@");
		if (tmp[0].equals("1")) {
			return player2.getCardStringById(Integer.parseInt(tmp[2]));
		}
		return player1.getCardStringById(Integer.parseInt(tmp[2]));

	}

	private Target getByIndex(String index) {
		String[] tmp = index.split("#");
		if (Integer.parseInt(tmp[0]) == 1) {
			if (Integer.parseInt(tmp[1]) == -1)
				return player2.hero.hero();
			return player2.field.getByIndex(Integer.parseInt(tmp[1]));
		}
		if (Integer.parseInt(tmp[1]) == -1)
			return player1.hero.hero();
		return player1.field.getByIndex(Integer.parseInt(tmp[1]));
	}

	public ViewGroup container() {
		return container;
	}

	public static boolean isStart() {
		return isgameStart;
	}

	public static void resetStart() {
		isgameStart = false;
	}

	public void waitForDekSelect() {
		animate.removeAllViews();
		animate.addView(container);
		TextView text = new TextView(context);
		text.setTextAppearance(context, R.style.myText);
		text.setText("상대가 덱을 고르고 있습니다.");
		container.addView(text);
	}



}
