package game;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class Field extends LinearLayout {

	HorizontalScrollView scroll;
	ArrayList<Monster> items;
	Context context; 
	Player player;
	Target attacker;
	Hero hero;
	LinearLayout.LayoutParams params; 

	public Field(Context context, Player player) {
		super(context);
		params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
		
		this.context = context;
		this.player = player;
		items = new ArrayList<Monster>();

		
		scroll = new HorizontalScrollView(context);
		params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
		
		
		scroll.setLayoutParams(params);
		scroll.addView(this);
		scroll.setSmoothScrollingEnabled(true);
	} 
	

	public void remove(Monster monster) {
		items.remove(monster);
		removeView(monster);
	}

	public void add(Hero hero, int me) {
		this.hero = hero;
		if (me == 1) {
			player.game.container.addView(hero, player.game.container.getChildCount()-1);
			return;
		}
		player.game.container.addView(hero, 0);
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

	public int scrollHeight() {
		return scroll.getHeight();
	}

}
