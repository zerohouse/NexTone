package Game;

import java.io.IOException;
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
	Sender sender;
	Button usecard, endturn, change;

	public Player(Context context, String dekstring) {
		this.context = context;
		this.dekstring = dekstring;
		init();
		makeDek();
	}

	private void init() {
		random = new Random();
		hand = new Hand(context);
		field = new Field(context);
		dummy = new Dummy();
		dek = new ArrayList<Card>();
		usecard = new Button(context);
		usecard.setText("useCard");
		done = false;
		usecard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				useCard();
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

	private void useCard() {
		// TODO Auto-generated method stub

	}

	private void endTurn() {
		hand.removeView(usecard);
		hand.removeView(endturn);
		sendMessage(Value.YOURTURN.get() + " ");
	}

	private void makeDek() {
		String[] defaultcards = context.getResources().getStringArray(
				R.array.cards);

		String[] deksplit = dekstring.split(" ");
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
						defense, i, Card.ONCHANGE);
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
		while (added < size) {
			rannum = random.nextInt(dek.size() - 1);
			rancard = dek.get(rannum);
			if (!hand.contains(rancard)) {
				hand.addCard(rancard);
				added++;
			}
		}
	}

	private void changeSelectedCard(View v) {
		int removed = hand.removeSelectedCard();
		dekToHand(removed);
		hand.removeView(v);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		dekToDummy();
		sendMessage(Value.DEKSTRING.get() + " " + dekstring);
		done = true;
		if (!first)
			sendMessage(Value.YOURTURN.get() + " "); // START!
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

	public void dekToDummy() {
		ArrayList<Card> clonedek = cloneDek();
		removeCardinHand(clonedek);
		dummy.shffleAdd(clonedek, random);
		alert(dummy.size());
	}

	private void removeCardinHand(ArrayList<Card> clonedek) {
		for (Card card : hand.items) {
			clonedek.remove(card.index);
		}
	}

	private ArrayList<Card> cloneDek() {
		ArrayList<Card> result = new ArrayList<Card>();
		for (Card card : dek) {
			result.add(card);
		}
		return result;
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

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public void sendMessage(String string) {
		try {
			sender.sendMessage(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newTurn() {
		hand.addView(usecard);
		hand.addView(endturn);

	}

	public boolean done() {
		return done;
	}
}
