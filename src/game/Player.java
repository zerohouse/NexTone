package game;

import java.util.ArrayList;
import java.util.Random;

import net.NetGame;
import net.Sender;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.R;

import components.ImageButton;

public class Player {

	String dekstring, herostring;
	ArrayList<Card> dek;
	Context context;
	Hand hand;
	Random random;
	Dummy dummy;
	public Field field;
	boolean first, done;
	public int me;
	Button change;
	ImageButton usecard, heroabillity;
	public ImageButton endturn;

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
			card.use(this);
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
		Sender.S("4 "); // 4 = 턴넘기기
	}

	public void endTurnByNet() {
		field.endTurn();
		hero.endTurn();
	}

	private void makeDek() {
		String[] defaultcards = context.getResources().getStringArray(
				R.array.defaultcards);

		String[] deksplit = dekstring.split(",");
		String[] cardhowmany;
		String eachcard;
		Card card;
		int howmany;
		int id = 0;
		for (String each : deksplit) {
			cardhowmany = each.split("x");
			eachcard = defaultcards[Integer.parseInt(cardhowmany[0])];
			howmany = Integer.parseInt(cardhowmany[1]);

			for (int i = 0; i < howmany; i++) {
				card = new Card(context, eachcard, id);
				id++;
				dek.add(card);
			}
		}

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
				Sender.S("6 ");
			}
		});
	}

	private void dekToHand(int size) {
		int rannum;
		Card rancard;
		int added = 0;
		while (added < size) {
			rannum = random.nextInt(dek.size() - 1);

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
		Sender.S(3 + " " + dekstring + ";" + herostring);
		done = true;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!first)
			Sender.S(5 + " "); // IAMDONE (second)
	}

	public void defeat() {

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
		Sender.S("10 ");
		newCard();

		hero.newTurn(); // 히어로 뉴턴에서 에러
		hero.addView(usecard);
		hero.addView(endturn);

		field.newTurn();
		enemy.field.endTurn();
		sendHeroState();

	}

	public void sendHeroState() {
		Sender.S("7 1@" + hero.toString());
	}

	public void newCard() {
		hero.dummysize.setInt(dummy.size());
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

	public void listenerNull() {
		field.listenerNull();
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

}
