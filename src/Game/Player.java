package Game;

import java.util.ArrayList;
import java.util.Random;

import Net.Sender;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
	Sender sender;
	Button usecard, endturn, change;
	Hero hero;
	Player enemy;

	public Player(Context context, String dekstring, int me) {
		this.me = me;
		this.context = context;
		this.dekstring = dekstring;
		init();

		alert("시작세팅! 완료");
		makeDek();
		alert("덱세팅! 완료");

	}

	private void init() {
		random = new Random();
		hand = new Hand(context);
		field = new Field(context, this);

		dummy = new Dummy();
		dek = new ArrayList<Card>();
		usecard = new Button(context);
		usecard.setText("useCard");
		done = false;

		hero = new Hero(context, this);
		field.add(hero);
		usecard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				useCard(v);
			}

		});
		endturn = new Button(context);
		endturn.setText("EndTurn");
		endturn.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View v) {
				endTurn();
			}

		});
	}

	private void useCard(View v) {
		addSelectedMonster();
		

	}

	private void addSelectedMonster() {
		ArrayList<Card> selected = hand.selectedCards();
		int manacost = costSum(selected);
		if (manacost > hero.mana.Int()) {
			alert("마나가 부족합니다.");
			return;
		}

		hero.mana.add(-manacost);

		String monsterinfo;
		for (Card card : selected) {
			Monster monster = new Monster(context, card, field, field.size());
			field.add(monster);
			hand.remove(card);
			monsterinfo = monster.toString();
			sender.S("8 "+me+"@"+monsterinfo);
		}
	}

	private int costSum(ArrayList<Card> selected) {
		int cost = 0;
		for (Card card : selected) {
			cost += card.cost();
		}
		return cost;
	}

	private void endTurn() {
		hand.removeView(usecard);
		hand.removeView(endturn);
		field.endTurn();
		sender.S(CONS.YOURTURN.get() + " ");
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
				card = new Card(context, name, description, cost, attack,
						defense, i);
				dek.add(card);
			}
		}

	}

	public void firstSetting(int size) {

		dekToHand(size);

		change = new Button(context);
		change.setText("바꾸기");
		change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSelectedCard(v);
			}
		});
		hand.addView(change);
	}

	public void ChangeToStartTurn() {
		change.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSelectedCard(v);
				newTurn();
			}
		});
	}

	private void dekToHand(int size) {
		int rannum;
		Card rancard;
		int added = 0;
		alert("카드 넘기는중");
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
		hand.removeView(v);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dekToDummy();
		sender.S(CONS.DEKSTRING.get() + " " + dekstring);
		done = true;
		if (!first)
			sender.S(CONS.YOURTURN.get() + " "); // START!
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void defeat() {

	}

	public void alert(String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void alert(int message) {
		Toast toast = Toast.makeText(context, message + "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public String dekToDummy() {
		String ranorder = dummy.shffleAdd(dek, random);
		alert(dummy.size());
		return ranorder;
	}

	public void first() {
		first = true;
	}

	public void second() {
		first = false;
	}

	public View hand() {
		return hand;
	}

	public View field() {
		return field;
	}

	public void setEnemy(Player enemy) {
		this.enemy = enemy;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
		field.setSender(sender);
	}


	public void newTurn() {
		hand.addView(usecard);
		hand.addView(endturn);

		newCard();
		hero.newTurn();
		field.newTurn();
		sender.S("7 1@" + hero.getString());
	}

	private void newCard() {
		if (dummy.isEmpty()) {
			hero.emptyDummy();
			alert("덱에 카드가 없습니다");
			return;
		}
		Card newcard = dummy.pop();
		if (hand.size() < 10) {
			hand.add(newcard);
			return;
		}
		alert(hand.size() + "카드가 너무 많습니다.");
	}

	public boolean done() {
		return done;
	}

	public void setDekString(String dekstring) {
		this.dekstring = dekstring;
	}
}
