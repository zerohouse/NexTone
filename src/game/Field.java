package game;

import java.util.ArrayList;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import animation.HideAndShow;

public class Field extends LinearLayout {

	HorizontalScrollView scroll;
	ArrayList<Monster> items;
	Context context;
	Player player;
	public Hero hero;
	LinearLayout.LayoutParams params;
	AnimatorSet animate;
	Monster selected;

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
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 1f);

		scroll.setLayoutParams(params);
		scroll.addView(this);
		scroll.setSmoothScrollingEnabled(true);

		HideAndShow hideshow = new HideAndShow(this);
		hideshow.animate();
	}

	public void remove(Monster monster) {
		player.listener.sendEmptyMessage(1);
		items.remove(monster);
		removeView(monster);
	}

	public void add(Monster monster) {
		if (items.size() > 6) {
			return;
		}
		items.add(monster);
		addView(monster);
		player.listener.sendEmptyMessage(0);
	}

	public void addByCard(Card card, boolean sended) {
		Monster monster = new Monster(context, card, this, sended);
		add(monster);
	}

	public void newTurn() {
		for (Monster monster : items) {
			monster.newTurn();
		}
	}

	public void setListener() {
		for (Monster monster : items) {
			if (monster.isHide()) {
				monster.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Method.alert("숨었쪙!");
					}
				});
			} else
				monster.setOnClickListener(Listeners.listener);
		}
	}

	public void add(Hero hero, int me) {
		this.hero = hero;
		if (me == 1) {
			player.game.container().addView(hero,
					player.game.container().getChildCount() - 1);
			return;
		}
		player.game.container().addView(hero, 0);
	}

	public void endTurn() {
		Static.attacker = null;
		attackCheckUpedMonster();
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

	@SuppressLint("NewApi")
	public void attackCheckUpedMonster() {
		for (Monster monster : items) {
			if (monster.uped == true) {
				monster.setY(10);
				monster.uped = false;
				monster.attackCheck();
			}

		}
	}

	public void othersNotAttack() {
		for (Monster monster : items) {
			monster.setBackgroundDefault();
		}
	}

	public Target getByIndex(int id) {
		for (Target target : items) {
			if (target.index() == id) {
				return target;
			}
		}
		return null;
	}

	public void listenerHelper() {
		for (Monster monster : items) {
			monster.setHelperShow();
		}
	}

	@SuppressLint("NewApi")
	public void attackCheck() {
		for (Monster monster : items) {
			if (monster.uped == true) {
				monster.setY(10);
				monster.uped = false;
				monster.attackCheck();
			} else {
				monster.attackCheck();
			}
		}
	}

	public void attackReady() {
		for (Monster monster : items) {
			monster.attackReady();
		}
	}

	public ArrayList<Monster> monsters() {
		return items;
	}

	public boolean defenseMonsterInField() {
		int defenseMonster = 0;
		for (Monster mon : items) {
			if (mon.defenseMonster)
				defenseMonster++;
		}
		Log.i("shield", defenseMonster + "");
		if (defenseMonster > 0)
			return true;
		return false;
	}

	public void heal(int amount, boolean sended, Target from, String resource) {
		for (int i = items.size() - 1; i > -1; i--) {
			items.get(i).heal(amount, sended, from, resource);
		}
	}

	public int getSpellpower() {
		int power = 0;
		for (Monster mon : items) {
			power += mon.getSpellpower();
		}
		return power;
	}

	public void Stun(boolean sended, Target from, String resource) {
		for (Monster mon : items) {
			mon.setStun(sended, from, resource);
		}

	}

}
