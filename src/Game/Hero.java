package Game;

import com.mylikenews.nextoneandroid.R;

import Net.Sender;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Hero extends RelativeLayout implements Target {

	Player player;
	Effect effect;
	int emptyDummy;
	ViewBinder vital, defense, damage, dummysize;
	ManaStone mana;
	String name;
	Context context;
	int attackable;
	RelativeLayout.LayoutParams params;

	Hero(Context context, Player player) {
		super(context);

		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.height = Method.dpToPx(100);
		setBackgroundResource(R.drawable.hero);
		setLayoutParams(params);

		this.context = context;
		this.player = player;
		emptyDummy = 0;

		defense = new ViewBinder(context, 0, this);
		RelativeLayout.LayoutParams defenseparam = defense.getParams();
		defenseparam.leftMargin = Method.dpToPx(50);

		damage = new ViewBinder(context, 0, this);
		RelativeLayout.LayoutParams damageparam = damage.getParams();
		damageparam.leftMargin = Method.dpToPx(20);

		mana = new ManaStone(context, this);
		mana.setMana(0);
		RelativeLayout.LayoutParams manaparam = mana.getParams();
		manaparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		manaparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mana.setMaxmana(0);

		vital = new ViewBinder(context, 30, this);
		RelativeLayout.LayoutParams vitalparam = vital.getParams();
		vitalparam.leftMargin = Method.dpToPx(100);

		dummysize = new ViewBinder(context, player.dummy.size(), this);
		RelativeLayout.LayoutParams dummyparam = dummysize.getParams();
		dummyparam.leftMargin = Method.dpToPx(100);
		dummyparam.topMargin = Method.dpToPx(30);

		attackable = 0;
	}

	public void newTurn() {
		if (mana.maxmana() < 10)
			mana.maxmanaAdd(1);
		mana.setMana(mana.maxmana());
		dummysize.setInt(player.dummy.size());
		setOnAttackClickListener();
	}

	private void setOnAttackClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Method.alert("공격할 대상을 선택해 주세요.");
				player.field.attacker = (Target) v;
				player.field.player.enemy.field.targetSelect();
			}
		};

		setOnClickListener(attakable);
	}

	public void endTurn() {
		setOnClickListener(null);
	}

	public void setByString(String set) {
		String[] setsplit = set.split(",");
		mana.setMana(Integer.parseInt(setsplit[0]));
		mana.setMaxmana(Integer.parseInt(setsplit[1]));
		defense.setInt(Integer.parseInt(setsplit[2]));
		damage.setInt(Integer.parseInt(setsplit[3]));
		vital.setInt(Integer.parseInt(setsplit[4]));
		dummysize.setInt(Integer.parseInt(setsplit[5]));
	}

	public String getString() {
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
	}

	@Override
	public void attack(Target target) {
		if (damage.Int() == 0) {
			Method.alert("공격할 수 없습니다.");
			return;
		}
		if (attackable == 0) {
			Method.alert("공격할 수 없습니다.");
			return;
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

	public void attackOrder(Target another) {
		another.attacked(damage.Int());
		this.attacked(another.damage());
	}

}
