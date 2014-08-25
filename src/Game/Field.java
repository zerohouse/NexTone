package Game;

import java.util.ArrayList;

import Net.Sender;
import android.content.Context;
import android.view.View;

public class Field extends ViewWrap {

	ArrayList<Monster> items;
	Context context;
	Player player;
	Target attacker;
	Sender sender;
	Hero hero;

	public Field(Context context, Player player) {
		super(context);
		
		this.context = context;
		this.player = player;
		items = new ArrayList<Monster>();

	}
	
	public void setSender(Sender sender){
		this.sender = sender;
	}

	public void remove(Monster monster) {
		items.remove(monster);
		removeView(monster);
	}

	public void add(Hero hero) {
		this.hero = hero;
		addView(hero);
	}
	
	public void add(Monster monster) {
		items.add(monster);
		addView(monster);
	}

	public void add(String string) {
		Monster monster = new Monster(context, string, this, items.size());
		items.add(monster);
		addView(monster);
	}

	public void newTurn() {
		for (Monster monster : items) {
			monster.newTurn();
		}
	}

	protected void targetSelect() {
		OnClickListener attacked = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.enemy.field.attacker.attack((Target) v);
			}
		};

		for (Monster monster : items) {
			monster.setOnClickListener(attacked);
		}
		hero.setOnClickListener(attacked);
	}

	public void endTurn() {
		for (Monster monster : items) {
			monster.endTurn();
		}
	}

	public Monster get(int i) {
		return items.get(i);
	}

	public int size() {
		return items.size();
	}



}
