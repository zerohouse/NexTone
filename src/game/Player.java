package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.DekList;

import components.ImageButton;
import dek.CardList;
import effects.monster.excute.ExcuteEffect;

@SuppressLint("HandlerLeak")
public class Player {

	String dekstring, herostring;
	ArrayList<Card> dek;
	Context context;
	public Hand hand;
	Random random;
	Dummy dummy;
	public Field field;
	boolean first, done, gameend, myturn = false;
	public int me, turn = 0;
	Button change;
	ImageButton heroabillity;
	public ImageButton endturn;
	int idforMonster = 0;
	CountDownTimer turnTimer;
	TranslateAnimation translate;
	public int usedcardsize = 0;

	public final static int NEW_MONSTER = 0;
	public final static int DIE_MONSTER = 1;
	public final static int NEWTURN = 2;
	public final static int ENDTURN = 3;
	public final static int USECARD = 4;
	ArrayList<ArrayList<ExcuteEffect>> events = new ArrayList<ArrayList<ExcuteEffect>>();

	@SuppressLint("HandlerLeak")
	public Handler listener = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (events.get(NEW_MONSTER).size() == 0)
					return;
				for (ExcuteEffect ex : events.get(NEW_MONSTER)) {
					ex.run();
				}
				break;

			case 1:
				usedcardsize++;
				if (events.get(USECARD).size() == 0)
					return;
				for (ExcuteEffect ex : events.get(USECARD)) {
					ex.run();
				}
				break;

			case 2:
				break;
			}
		}
	};

	public Hero hero;
	public Player enemy;
	Game game;
	int timer;
	OnClickListener attacked;
	ImageView timerimg;

	@SuppressLint("NewApi")
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

		events.add(new ArrayList<ExcuteEffect>());
		events.add(new ArrayList<ExcuteEffect>());
		events.add(new ArrayList<ExcuteEffect>());
		events.add(new ArrayList<ExcuteEffect>());
		events.add(new ArrayList<ExcuteEffect>());
		events.add(new ArrayList<ExcuteEffect>());
		events.add(new ArrayList<ExcuteEffect>());

		if (me != 1)
			return;
		timerimg = new ImageView(context);
		timerimg.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams timerparams = Method.getParams();
		timerimg.setLayoutParams(timerparams);
		timerparams.height = Method.dpToPx(4);
		timerparams.width = Method.dpToPx(15);
		translate = new TranslateAnimation(0, Method.getWindowWidth()
				- Method.dpToPx(100), 0, 0);
		translate.setDuration(20000);
		turnTimer = new CountDownTimer(20000, 1000) {

			public void onTick(long millisUntilFinished) {
				if (timer == 10)
					Method.alert(timer + "초 남았어요!");

				timer -= 1;
			}

			public void onFinish() {
				endTurn();
			}
		};

	}

	public void addNewMonsterEffect(ExcuteEffect effect) {
		events.get(NEW_MONSTER).add(effect);
	}

	public void removeNewMonsterEffect(ExcuteEffect effect) {
		events.get(NEW_MONSTER).remove(effect);
	}

	public void addNewTurnEffect(ExcuteEffect effect) { // 한번실행
		events.get(NEWTURN).add(effect);
	}

	public void addEndTurnEffect(ExcuteEffect effect) { // 한번실행
		events.get(ENDTURN).add(effect);
	}

	private void init() {

		// 초기화
		random = new Random();
		hand = new Hand(context);
		field = new Field(context, this);

		dummy = new Dummy();
		dek = new ArrayList<Card>();
		done = false;

		// 영웅 설정
		hero = new Hero(context, this, herostring);

		// 버튼 선언부

		endturn = new ImageButton(context, Method.resId("done"),
				Method.resId("donepressed"), "     턴넘기기");
		endturn.getParams().addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endTurn();
			}
		});

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

	@SuppressLint("NewApi")
	void endTurn() {

		translate.cancel();
		turnTimer.cancel();
		timerimg.setAnimation(null);
		timerimg.setVisibility(View.INVISIBLE);
		usedcardsize = 0;

		for (Card card : hand.items) {
			card.normalBackground();
		}
		myturn = false;

		hero.removeView(endturn);
		int size = events.get(ENDTURN).size();
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				events.get(ENDTURN).get(0).run();
				events.get(ENDTURN).remove(0);
			}
		}
		field.endTurn();
		hero.endTurn();
		sendHeroState();
		Game.sender.S("4&"); // 4 = 턴넘기기
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
				card = new Card(context, eachcard, idforMonster, cardid, this);
				idforMonster++;
				dek.add(card);
			}
		}

	}

	public static final int BAT = -1;
	public static final int TOTEM = -2;
	public static final int TOTEM_HEAL = -3;
	public static final int TOTEM_SPELL = -4;
	public static final int TOTEM_SHIELD = -5;
	public static final int MANAPLUS = -6;
	public static final int LITTLE_DRAGON = -7;
	public static final int JONGJA = -8;
	public static final int PINKLE = -9;

	public String getCardStringById(int id) {
		switch (id) {
		case -1:
			return "박쥐;;bat;1;0;0;1;1";
		case -2:
			return "토템;;totem;1;0;0;1;1";
		case -3:
			return "치유토템;내 턴이 끝날때 내 캐릭터들의 체력을 1 회복합니다.;totemheal;1;0;0;0;2";
		case -4:
			return "주문토템;주문력+1;totemspell;1;0#4;0;0;2";
		case -5:
			return "수호토템;방어;totemshield;1;0#1;0;0;2";
		case -6:
			return "마나+;마나를 1 획득합니다.;manaplus;0;0%2#1;0;0;0";
		case -7:
			return "토템;;totem;1;0;1;2;1";
		case -8:
			return "토템;;totem;1;0;0;2;2";
		case -9:
			return "막내아들;;maknae;1;0;0;3;3";
		case -10:
			return "토템;;totem;1;0;0;4;4";
		case -11:
			return "문재인;방어;moonj;1;0#1;0;1;2";
		case -12:
			return "피닉제;(부활함);pinix;1;0;4;4;5";
		case -13:
			return "수호토템;방어;totemshield;1;0#1;1;2;3";
		case -14:
			return "개;;dog;1;0;0;1;1";
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
		dekToHand(size);
	}

	public void ChangeToStartTurn() {
		change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSelectedCard(v);
				game.container().removeView(v);
				game.initView();
				newTurn();
				Game.sender.S("6&");
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
		Game.sender.S("3&" + dekstring + ";" + herostring);
		done = true;
		Card.stateChange = false;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!first) {
			Game.sender.S("5&"); // IAMDONE (second)
			hand.add(new Card(context, getCardStringById(-6), -1, -6, this));

			// "쇼군;상대영웅에게 피해를 2줍니다.\n연계:다음턴이 시작할때 이 카드를 다시 얻습니다.;sabu;0;0%803#0;3;0;0",

			// hand.add(new Card(context, , -1, -6));
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

	public View field() {
		return field;
	}

	public void setEnemy(Player enemy) {
		this.enemy = enemy;
	}

	@SuppressLint("NewApi")
	public void newTurn() {

		turn++;
		if (turn == 1)
			hero.addView(timerimg);
		timerimg.startAnimation(translate);
		timer = 20;
		turnTimer.start();

		Method.alert("나의 턴 : " + turn);
		Game.sender.S("10&");
		newCard();

		hero.newTurn(); // 히어로 뉴턴에서 에러
		hero.addView(endturn);
		field.newTurn();
		myturn = true;

		usedcardsize = 0;

		int size = events.get(NEWTURN).size();
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				events.get(NEWTURN).get(0).run();
				events.get(NEWTURN).remove(0);
			}
		}
		enemy.field.endTurn();
		cardStateUpdate();

	}

	public void sendHeroState() {
		Game.sender.S("7&1@" + hero.toString());
	}

	public void newCard(int amount) {
		for (int i = 0; i < amount; i++) {
			newCard();
		}
	}

	public void newCard() {
		if (me != 1) {
			Game.sender.S("30&");
			Log.i("카드뽑기", "보냄");
			return;
		}
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
		for (Card card : hand.items) {
			if (card.cost() > mana())
				card.normalBackground();
			else
				card.greenBackground();
		}

		hero.dummysize.setText(" Cards: " + hand.size() + "/" + dummy.size());
		sendHeroState();
		hand.marginCheck();
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
			Game.sender.S("100&");
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

	public int getSpellpower() {
		return field.getSpellpower();
	}

	public boolean getFirst() {
		return first;
	}

	public void heal(int amount, boolean sended, Target from, String resource) {

		field.heal(amount, sended, from, resource);
		hero.hero().heal(amount, sended, from, resource);
	}

	public void newCard(Card card) {
		hand.add(card);
	}

	public int mana() {
		return hero.mana.mana();
	}
}
