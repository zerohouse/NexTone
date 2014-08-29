package game;

import java.util.ArrayList;
import java.util.Random;

import net.Sender;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.R;

public class Player {

	String dekstring;
	ArrayList<Card> dek;
	Context context;
	Hand hand;
	Random random;
	Dummy dummy;
	Field field;
	boolean first, done;
	int me;
	Button usecard, endturn, change, heroabillity;
	Hero hero;
	public Player enemy;
	NetGame game;

	public Player(Context context, String dekstring, int me, NetGame game) {
		this.me = me;
		this.context = context;
		this.dekstring = dekstring;
		this.game = game;
		init();

		makeDek();

	}

	private void init() {
		random = new Random();
		hand = new Hand(context);
		field = new Field(context, this);

		dummy = new Dummy();
		dek = new ArrayList<Card>();
		done = false;

		hero = new Hero(context, this, "heroblue");

		usecard = new Button(context);
		usecard.setText("useCard");
		usecard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				useCard(v);
			}
		});

		RelativeLayout.LayoutParams cardparams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		usecard.setLayoutParams(cardparams);

		endturn = new Button(context);
		endturn.setText("EndTurn");
		endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endTurn();
			}
		});

		RelativeLayout.LayoutParams endparams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		usecard.setId(1);
		endturn.setLayoutParams(endparams);
		cardparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		endparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		endparams.addRule(RelativeLayout.BELOW, usecard.getId());
	}

	public void addHero() {
		field.add(hero, me);
	}

	private void useCard(View v) {
		addSelectedMonster();
	}

	private void addSelectedMonster() {
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
 
		hero.mana.manaAdd(-manacost);

		String monsterinfo;
		for (Card card : selected) {
			Monster monster = new Monster(context, card, field, card.index());
			field.add(monster);
			hand.remove(card);
			monsterinfo = monster.toString();
			Sender.S("8 " + me + "@" + monsterinfo);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	private void endTurn() {
		hero.removeView(usecard);
		hero.removeView(endturn);
		field.endTurn();
		Sender.S(4 + " "); // 4 = 턴넘기기
		hero.endTurn();
	}

	private void makeDek() {
		String[] defaultcards = context.getResources().getStringArray(
				R.array.cards);

		String[] deksplit = dekstring.split(",");
		String[] cardhowmany, cardinfo;
		String eachcard;
		Card card;
		int attack, defense, cost;
		String resource;
		String name, description;
		int howmany;
		for (String each : deksplit) {
			cardhowmany = each.split("x");
			eachcard = defaultcards[Integer.parseInt(cardhowmany[0])];
			howmany = Integer.parseInt(cardhowmany[1]);

			for (int i = 0; i < howmany; i++) {
				cardinfo = eachcard.split(";");
				name = cardinfo[0];
				description = cardinfo[1];
				cost = Integer.parseInt(cardinfo[2]);
				attack = Integer.parseInt(cardinfo[3]);
				defense = Integer.parseInt(cardinfo[4]);
				resource = cardinfo[5];

				card = new Card(context, name, description, cost, attack,
						defense, resource, i);
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
				game.container.removeView(v);
			}
		});
		game.container.addView(change);
		Method.alert("바꿀카드를 선택해 주세요.");
	}

	public void ChangeToStartTurn() {
		change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSelectedCard(v);
				game.container.removeView(v);
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
		Sender.S(3 + " " + dekstring);
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

	public void newTurn() {
		Method.alert("나의 턴입니다.");
		Sender.S("10 ");
		newCard();

		hero.newTurn(); // 히어로 뉴턴에서 에러
		hero.addView(usecard);
		hero.addView(endturn);
		field.newTurn();
		enemy.field.endTurn();
		Sender.S("7 1@" + hero.toString());

	}

	private void newCard() {
		if (dummy.isEmpty()) {
			hero.emptyDummy();
			Method.alert("덱에 카드가 없습니다");
			return;
		}
		Card newcard = dummy.pop();
		if (hand.size() < 10) {
			hand.add(newcard);
			return;
		}
		Method.alert("카드가 너무 많습니다.\n 1장 사라집니다.");
	}

	public boolean done() {
		return done;
	}

	public void setDekString(String dekstring) {
		this.dekstring = dekstring;
	}
}
