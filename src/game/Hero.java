package game;

import net.Sender;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.R;
import components.ViewBinder;

public class Hero extends RelativeLayout implements Target {

	Player player;
	Effect effect;
	int emptyDummy;
	ViewBinder vital, defense, damage, dummysize;
	RelativeLayout hero;
	ManaStone mana;
	String name;
	Context context;
	int attackable;
	RelativeLayout.LayoutParams params;
	boolean getWeapon;
	int maxvital;
	String resource;
	
	RelativeLayout.LayoutParams dummyparam;

	Hero(Context context, Player player, String res) {
		super(context);
		resource = res;
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.height = Method.dpToPx(110);
		setBackgroundResource(Method.resId(resource+"back"));
		setLayoutParams(params);

		this.context = context;
		this.player = player;
		emptyDummy = 0;

		getWeapon = false;

		mana = new ManaStone(context, this);
		mana.setMana(0);
		RelativeLayout.LayoutParams manaparam = mana.getParams();

		manaparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		manaparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mana.setMaxmana(0);

		hero = new RelativeLayout(context);
		RelativeLayout.LayoutParams heroparams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		heroparams.width = Method.dpToPx(140);
		heroparams.setMargins(Method.dpToPx(70), 0, 0, 0);
		hero.setLayoutParams(heroparams);
		hero.setBackgroundResource(Method.resId(resource)); // 히어로 선택 부분 수정해야함.
		addView(hero);

		damage = new ViewBinder(context, 0, hero);
		damage.setBackgroundResource(R.drawable.attack);
		damage.setGravity(Gravity.CENTER);

		defense = new ViewBinder(context, 0, hero, false);
		defense.setTextSize(22);
		RelativeLayout.LayoutParams defenseparam = defense.getParams();
		defenseparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		defenseparam.rightMargin = Method.dpToPx(17);
		defenseparam.topMargin = Method.dpToPx(30);

		RelativeLayout.LayoutParams damageparams = damage.getParams();
		damageparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		damageparams.width = Method.dpToPx(40);
		damageparams.height = Method.dpToPx(55);

		maxvital = 30;
		vital = new ViewBinder(context, 30, hero);
		vital.setBackgroundResource(R.drawable.vital);
		vital.setGravity(Gravity.CENTER);

		RelativeLayout.LayoutParams vitalparams = this.vital.getParams();
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		vitalparams.width = Method.dpToPx(45);
		vitalparams.height = Method.dpToPx(58);

		dummysize = new ViewBinder(context, player.dummy.size(), this, true);
		dummyparam = dummysize.getParams();
		dummysize.setTextSize(15);

		attackable = 0;
	}

	public void newTurn() {
		if (mana.maxmana() < 10)
			mana.maxmanaAdd(1);
		mana.setMana(mana.maxmana());
		dummysize.setInt(player.dummy.size());
		dummysize.setText(" Cards: " + player.dummy.size());
		setOnAttackClickListener();
	}

	private void setOnAttackClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (attackable <= 0 || damage.Int() == 0)
					return;
				Method.alert("공격할 대상을 선택해 주세요.");
				player.field.attacker = (Target) v;
				player.field.player.enemy.field.targetSelect();
			}
		};

		setOnClickListener(attakable);
	}

	
	public void endTurn() {
		System.out.println();
		setOnClickListener(null);
	}

	public void setByString(String set) {
		String[] setsplit = set.split(",");
		mana.setMana(Integer.parseInt(setsplit[0]));
		mana.setMaxmana(Integer.parseInt(setsplit[1]));
		damage.setInt(Integer.parseInt(setsplit[3]));
		vital.setInt(Integer.parseInt(setsplit[4]));
		dummysize.setText(" Cards: " + Integer.parseInt(setsplit[5]));

		if (Integer.parseInt(setsplit[2]) != 0) { 
			defense.setInt(Integer.parseInt(setsplit[2]));
			return;
		}
		defense.setText("");
	}

	@Override
	public String toString() {
		String heroState;
		heroState = mana.mana() + "," + mana.maxmana() + "," + defense.Int()
				+ "," + damage.Int() + "," + vital.Int() + ","
				+ dummysize.Int();
		return heroState;
	}

	@Override
	public void attacked(int damage) {
		vital.add(-damage);
		defeatCheck();
		if (vital.Int() < maxvital) {
			vital.setTextColor(Color.RED);
			return;
		}
		vital.setTextColor(Color.WHITE);

	}

	@Override
	public void attack(Target target, boolean isChecked) {
		if (!isChecked) {
			if (damage.Int() == 0) {
				Method.alert("공격할 수 없습니다.");
				return;
			}
			if (attackable == 0) {
				Method.alert("공격할 수 없습니다.");
				return;
			}
		}
		attackable--;
		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = 0;
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();
		Sender.S("9 " + enemyindex + "," + playerindex);
	}

	private void defeatCheck() {
		if (vital.Int() < 1) {
			player.defeat();
		}
	}

	@Override
	public int damage() {
		return damage.Int();
	}

	public void emptyDummy() {
		emptyDummy++;
		vital.add(-emptyDummy);
		defeatCheck();
	}

	public int index() {
		return -1;
	}

	@Override
	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	@Override
	public Target cloneForAnimate() {
		Hero hero = new Hero(context, player, resource);
		hero.setByString(this.toString());
		return hero;
	}

	@Override
	public boolean isHero() {
		return true;
	}

	@Override
	public int getMarginY() {
		if (player.me == 1)
			return player.hand.getHeight();
		else {
			return player.field.getHeight() * 2 + this.getHeight()
					+ player.hand.getHeight();
		}
	}

	@Override
	public int getTopY() {
		if (player.me == 1)
			return player.field.getHeight() * 2 + this.getHeight();
		else {
			return 0;
		}
	}

	public void deSelect() {
		hero.setBackgroundResource(Method.resId(resource));
	}
	
	@Override
	public void setAttackBackground() {
		this.setBackgroundResource(Method.resId(resource+"attack"));
	}
	
	@Override
	public float getX(){
		return hero.getX()+Method.dpToPx(30);
	}

	
}