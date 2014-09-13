package game;

import java.util.ArrayList;
import java.util.Random;

import net.NetGame;
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
	String[] defaultcards, herocards;
	int spellpower;

	public Hero hero;

	public Player enemy;
	NetGame game;

	OnClickListener attacked;

	public Player(Context context, String dekstring, String herostring, int me,
			NetGame game) {
		this.me = me;
		this.context = context;
		this.dekstring = dekstring;
		this.game = game;
		this.herostring = herostring;
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
		defaultcards = context.getResources().getStringArray(
				CardList.heroType(0));
		herocards = context.getResources().getStringArray(
				CardList.heroType(hero.getHeroType()));
		String[] deksplit = dekstring.split(",");
		String[] cardhowmany;
		String eachcard;
		Card card;
		int howmany;
		int id = 0;
		for (String each : deksplit) {
			cardhowmany = each.split("x");
			eachcard = getCardById(Integer.parseInt(cardhowmany[0]));
			howmany = Integer.parseInt(cardhowmany[1]);

			for (int i = 0; i < howmany; i++) {
				card = new Card(context, eachcard, hand, id);
				id++;
				dek.add(card);
			}
		}

	}

	private String getCardById(int id) {
		if (id > 999) {
			int resid = id % 1000;
			return herocards[resid];
		}
		return defaultcards[id];
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
			newCard("마나스톤;이번 턴에 마나를 1 획득합니다.;manaplus;0;0%0#1;0");
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

	private void newCard(String string) {
		hand.add(new Card(context, string, hand, 900));
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

}
