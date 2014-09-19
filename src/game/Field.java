package game;

import java.util.ArrayList;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import animation.HideAndShow;

public class Field extends RelativeLayout {

	ArrayList<Monster> items;
	Context context;
	Player player;
	public Hero hero;
	LinearLayout.LayoutParams params;
	AnimatorSet animate;
	Monster selected;
	final int windowWidth, windowHeight;
	int block;

	@SuppressLint("NewApi")
	public Field(Context context, Player player) {
		super(context);
		params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, 1f);
		setLayoutParams(params);

		this.context = context;
		this.player = player;
		items = new ArrayList<Monster>();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		windowWidth = size.x;
		windowHeight = size.y;

		HideAndShow hideshow = new HideAndShow(this);
		hideshow.animate(false);

		marginCheck();

	}

	@SuppressLint("NewApi")
	void marginCheck() {
		int size = items.size();
		if (size == 0)
			return;
		if (size == 0)
			return;
		int width = Method.dpToPx(90);
		int startposition = (windowWidth - size * width) / 2;

		int itemwidth = width * size > windowWidth ? Method.dpToPx(90)
				- (Method.dpToPx(5) * (size - 4)) : Method.dpToPx(90);
		int itemheight = width * size > windowWidth ? getHeight()
				- (Method.dpToPx(10) * (size - 4)) : getHeight();

		if (width * size > windowWidth) {
			block = (windowWidth - itemwidth) / (size - 1);

			for (int i = 0; i < items.size(); i++) {
				items.get(i).params.width = itemwidth;
				items.get(i).params.height = itemheight;
				items.get(i).params.leftMargin = i * block;

				if (i % 2 != 0)
					items.get(i).params.bottomMargin = getHeight() - itemheight;

			}
			return;
		}

		for (int i = 0; i < items.size(); i++) {
			items.get(i).params.leftMargin = startposition - (size - 1) * 5 + i
					* (width + 5);
		}
	}

	public int checkPosition(int x, Monster monster) {
		// Log.i("check", "start");
		if (!items.contains(monster)) {
			items.add(monster);
			addView(monster);
		}
		int position = computePosition(x);
		addCheck(position, monster);
		return position;
	}

	int checkerposition;

	private void addCheck(int position, Monster monster) {
		Log.i("position", position + "");

		if (items.size() == 0)
			return;
		if (items.size() > 6) {
			return;
		}
		if (checkerposition != position) {
			Log.i("position", "changed");
			removeView(monster);
			items.remove(monster);
			checkerposition = position;
		}
		if (!items.contains(monster)) {
			items.add(position, monster);
			addView(monster, position);
		}

		marginCheck();
	}

	private int computePosition(int i) {
		Log.i("compute", i + "");
		if (block == 0) {
			block = Method.dpToPx(90);
		}
		int result = i / block;
		if (result < 1) {
			result = 0;
		}
		if (result >= items.size() - 1) {
			result = items.size() - 1;
		}
		Log.i("computeresult", result + "");
		return result;
	}

	public void checkEnd(Monster monster) {
		if (items.size() == 0)
			return;
		Log.i("check", "end");
		removeView(monster);
		items.remove(monster);
		marginCheck();
		Monster.tmpmonster = null;
	}

	public void remove(Monster monster) {
		player.listener.sendEmptyMessage(1);
		items.remove(monster);
		removeView(monster);
		attackCheck();
		marginCheck();
	}

	public void add(Monster monster, int position, boolean sended) {
		if (items.size() > 6) {
			return;
		}
		if (position == -1) {
			items.add(monster);
			addView(monster);
		} else {
			Log.i("i", position + "");
			items.add(position, monster);
			addView(monster);
		} 
		marginCheck();
		attackCheck();
		player.listener.sendEmptyMessage(0);

		if (!sended) {
			Game.sender.S("8&" + player.me + "@" + monster.id + "@"
					+ monster.card.index() + "@" + position);
		}

	}

	public void addByCard(Card card, boolean sended, int i) {
		Monster monster = new Monster(context, card, this);
		add(monster, i, sended);
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
			player.game.container().addView(hero);
			return;
		}
		player.game.container().addView(hero, 0);
	}

	public void endTurn() {
		if(Monster.tmpmonster !=null)
			checkEnd(Monster.tmpmonster);
		Static.attacker = null;
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
			monster.attackCheck();
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
		int size = items.size() - 1;
		if (size == -1) {
			return;
		}
		for (int i = size; i > -1; i--) {
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
