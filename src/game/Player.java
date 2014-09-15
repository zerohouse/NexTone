package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.Sender;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import animation.Helper;

import com.mylikenews.nextoneandroid.DekList;

import components.ImageButton;
import dek.CardList;

public class Player {

	String dekstring, herostring;
	ArrayList<Card> dek;
	Context context;
	public Hand hand;
	Random random;
	Dummy dummy;
	public Field field;
	boolean first, done, gameend;
	public int me;
	Button change;
	ImageButton usecard, heroabillity;
	public ImageButton endturn;
	int spellpower;
	int idforMonster = 0;

	public Hero hero;

	public Player enemy;
	Game game;

	OnClickListener attacked;

	public Player(Context context, String dekstring, String herostring, int me,
			Game game, boolean first) {
		this.me = me;
		this.context = context;
		this.dekstring = dekstring;
		this.game = game;
		this.herostring = herostring;
		this.first = first;
		init();
		makeDek();

	}

	private void init() {

		// 초기화
		random = new Random();
		hand = new Hand(context);
		field = new Field(context, this);

		dummy = new Dummy();
		dek = new ArrayList<Card>();
		done = false;
		spellpower = 0;

		// 영웅 설정
		hero = new Hero(context, this, herostring);

		// 버튼 선언부
		usecard = new ImageButton(context, Method.resId("card"),
				Method.resId("cardpressed"), "     카드내기");
		usecard.getParams().addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		usecard.setId(1);

		usecard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				useCard(v);

			}
		});

		endturn = new ImageButton(context, Method.resId("done"),
				Method.resId("donepressed"), "     턴넘기기");
		endturn.getParams().addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		endturn.getParams().addRule(RelativeLayout.BELOW, usecard.getId());

		endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endTurn();
			}
		});

		OnClickListener downy = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Card.select != null) {
					Card.select.down();
					Helper.hideInfo();
				}

			}
		};

		field.setOnClickListener(downy);
		hero.setOnClickListener(downy);

	}

	public void endTurnReset() {
		endturn.setText("     턴넘기기");
		endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endTurn();
			}
		});
	}

	public void addHero() {
		field.add(hero, me);
	}

	private void useCard(View v) {
		ArrayList<Card> selected = hand.selectedCards();
		if (field.size() + monsterSum(selected) > 7) {
			Method.alert("하수인은 7명까지 소환 가능합니다.");
			return;
		}
		int manacost = costSum(selected);
		if (manacost > hero.mana.mana()) {
			Method.alert("마나가 부족합니다.");
			return;
		}

		for (Card card : selected) {
			card.use(this, false);
		}
		cardStateUpdate();
	}

	private int costSum(ArrayList<Card> selected) {
		int cost = 0;
		for (Card card : selected) {
			cost += card.cost();
		}
		return cost;
	}

	private int monsterSum(ArrayList<Card> selected) {
		int monster = 0;
		for (Card card : selected) {
			monster += card.monster();
		}
		return monster;
	}

	@SuppressLint("NewApi")
	void endTurn() {
		hero.removeView(usecard);
		hero.removeView(endturn);

		field.endTurn();
		hero.endTurn();
		sendHeroState();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sender.S("4&"); // 4 = 턴넘기기
	}

	public void endTurnByNet() {
		field.endTurn();
		hero.endTurn();
	}

	private void makeDek() {

		if (!first) {
			idforMonster = 100;
			Static.index = 300;
		}

		if (Card.defaultcards == null) {
			Card.defaultcards = new ArrayList<String>(Arrays.asList(context
					.getResources().getStringArray(CardList.heroType(0))));
		}
		if (Card.herocards == null) {
			Card.herocards = new ArrayList<String>(Arrays.asList(context
					.getResources().getStringArray(
							CardList.heroType(hero.getHeroType()))));
		} else {
			Card.enemycards = new ArrayList<String>(Arrays.asList(context
					.getResources().getStringArray(
							CardList.heroType(hero.getHeroType()))));
		}

		String[] deksplit = dekstring.split(",");
		String[] cardhowmany;
		String eachcard;
		Card card;
		int howmany;

		for (String each : deksplit) {
			cardhowmany = each.split("x");
			int cardid = Integer.parseInt(cardhowmany[0]);
			eachcard = getCardStringById(cardid);
			howmany = Integer.parseInt(cardhowmany[1]);

			for (int i = 0; i < howmany; i++) {
				card = new Card(context, eachcard, idforMonster, cardid);
				idforMonster++;
				dek.add(card);
			}
		}

	}

	public String getCardStringById(int id) {
		switch (id) {
		case -1:
			return "박쥐;;bat;1;0;0;1;1";
		case -2:
			return "토템;;totem;1;0;0;1;1";
		case -3:
			return "치유토템;내 턴이 끝날때 내 캐릭터들의 체력을 1 회복합니다.;totemheal;1;0;0;0;2";
		case -4:
			return "주문토템;주문력+1;totemspell;1;0;0;0;2";
		case -5:
			return "수호토템;방어;totemshield;1;0#1;0;0;2";
		case -6:
			return "마나+;마나를 1 획득합니다.;manaplus;0;0%0#1;0;0;0";

		}
		if (id > 999) {
			int resid = id % 1000;
			if (me == 1)
				return Card.herocards.get(resid);
			return Card.enemycards.get(resid);
		}
		return Card.defaultcards.get(id);
	}

	public void firstSetting(int size) {

		dekToHand(size);

		change = new Button(context);
		change.setText("선택한 카드 바꾸기");
		RelativeLayout.LayoutParams changeparams = Method.getParams();
		change.setLayoutParams(changeparams);

		change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSelectedCard(v);
				game.container().removeView(v);
			}
		});
		game.container().addView(change);
		Method.alert("바꿀카드를 선택해 주세요.");
	}

	public void ChangeToStartTurn() {
		change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSelectedCard(v);
				game.container().removeView(v);
				game.initView();
				newTurn();
				Sender.S("6&");
			}
		});
	}

	private void dekToHand(int size) {
		int rannum;
		Card rancard;
		int added = 0;
		while (added < size) {
			rannum = random.nextInt(dek.size());

			rancard = dek.get(rannum);
			if (!hand.contains(rancard)) {
				hand.add(rancard);
				dek.remove(rancard);
				added++;
			}
		}
	}

	private void changeSelectedCard(View v) {
		int removed = hand.removeAndReturnToDek(dek);
		dekToHand(removed);
		dekToDummy();
		Sender.S("3&" + dekstring + ";" + herostring);
		done = true;
		Card.stateChange = false;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!first) {
			Sender.S("5&"); // IAMDONE (second)
			hand.add(new Card(context, getCardStringById(-6), -1, -6));
			//hand.add(new Card(context, "", -1, -6));
			
			//성장돼지;매턴이 끝날때마다 +1/+1을 얻습니다.;pig;1;0%950#1=1;8;7;7
		}
	}

	public String dekToDummy() {
		String ranorder = dummy.shffleAdd(dek, random);
		return ranorder;
	}

	public void first() {
		first = true;
	}

	public void second() {
		first = false;
	}

	public View hand() {
		return hand.ScrollView();
	}

	public View field() {
		return field.scroll;
	}

	public void setEnemy(Player enemy) {
		this.enemy = enemy;
	}

	@SuppressLint("NewApi")
	public void newTurn() {
		Method.alert("나의 턴");
		Sender.S("10&");
		newCard();

		hero.newTurn(); // 히어로 뉴턴에서 에러
		hero.addView(usecard);
		hero.addView(endturn);

		field.newTurn();
		enemy.field.endTurn();
		sendHeroState();

	}

	public void sendHeroState() {
		Sender.S("7&1@" + hero.toString());
	}

	public void newCard() {
		if (dummy.isEmpty()) {
			hero.emptyDummy();
			Method.alert("덱에 카드가 없습니다");
			cardStateUpdate();
			return;
		}
		Card newcard = dummy.pop();
		if (hand.size() < 10) {
			hand.add(newcard);
			cardStateUpdate();
			return;
		}

		Method.alert("카드가 너무 많습니다.\n 1장 사라집니다.");
	}

	public void cardStateUpdate() {
		hero.dummysize.setText(" Cards: " + hand.size() + "/" + dummy.size());
		sendHeroState();
	}

	public boolean done() {
		return done;
	}

	public void setDekString(String dekstring) {
		this.dekstring = dekstring;
	}

	public void listenerHelper() {
		field.listenerHelper();
		hero.listenerNull();
	}

	public void attackCheck() {
		field.attackCheck();
		hero.attackCheck();
	}

	public String heroString() {
		return hero.toString();
	}

	public int me() {
		return me;
	}

	public void setListner() {
		field.setListener();
		hero.setListener();
	}

	public void attackReady() {
		field.attackReady();
		hero.hero.attackReady();
	}

	public Context context() {
		return context;
	}

	public void gameEnd(int type) {

		AlertDialog.Builder areyousure = new AlertDialog.Builder(context);

		if (gameend)
			return;

		gameend = true;

		switch (type) {
		case 0:
			areyousure.setTitle("게임에서 승리하였습니다.");
			Sender.S("100&");
			break;
		case 1:
			areyousure.setTitle("게임에서 패배하였습니다.");
			break;
		case 2:
			areyousure.setTitle("상대가 게임을 포기했습니다.");
			break;
		case 3:
			areyousure.setTitle("상대가 게임에서 떠났습니다.");
			break;
		}

		// Set up the buttons
		areyousure.setPositiveButton("이전 화면으로",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Activity) context).finish();
					}
				});
		areyousure.setNegativeButton("덱 구성하기",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Activity) context).finish();
						Intent intent = new Intent(context, DekList.class);
						context.startActivity(intent);
					}
				});

		areyousure.show();

	}

	public void spellpowerAdd(int i) {
		spellpower += i;
	}

	public int getSpellpower() {
		return spellpower;
	}

	public boolean getFirst() {
		return first;
	}

	public void heal(int amount, boolean sended, Target from, String resource) {
		field.heal(amount, sended, from, resource);
		hero.hero().heal(amount, sended, from, resource);
	}
}
