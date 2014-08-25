package Game;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class Hero extends ViewWrap implements Target {

	Player player;
	int emptyDummy;
	ViewBinder vital, defense, damage, mana, maxmana, dummysize;
	String name;
	Context context;
	int attackable;

	Hero(Context context, Player player) {
		super(context);
		this.context = context;
		this.player = player;
		emptyDummy = 0;
		maxmana = new ViewBinder(context, 0, this);
		defense = new ViewBinder(context, 0, this);
		damage = new ViewBinder(context, 0, this);
		mana = new ViewBinder(context, 0, this);
		vital = new ViewBinder(context, 30, this);
		dummysize = new ViewBinder(context, player.dummy.size(), this);
		attackable = 0;
	}

	public void newTurn() {
		if (maxmana.Int < 10)
			maxmana.add(1);
		mana.setInt(maxmana.Int());
		dummysize.setInt(player.dummy.size());
		setOnAttackClickListener();
	}
	
	private void setOnAttackClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert("공격할 대상을 선택해 주세요.");
				player.field.attacker = (Target) v;
				player.field.player.enemy.field.targetSelect();
			}
		};

		setBackgroundColor(Color.YELLOW);
		setOnClickListener(attakable);
	}
	
	public void endTurn() {
		setOnClickListener(null);
	}

	public void setByString(String set) {
		String[] setsplit = set.split(",");
		mana.setInt(Integer.parseInt(setsplit[0]));
		maxmana.setInt(Integer.parseInt(setsplit[1]));
		defense.setInt(Integer.parseInt(setsplit[2]));
		damage.setInt(Integer.parseInt(setsplit[3]));
		vital.setInt(Integer.parseInt(setsplit[4]));
		dummysize.setInt(Integer.parseInt(setsplit[5]));
	}

	public String getString() {
		String heroState;
		heroState = mana.Int() + "," + maxmana.Int() + "," + defense.Int()
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
			alert("공격할 수 없습니다.");
			return;
		}
		if (attackable == 0) {
			alert("공격할 수 없습니다.");
			return;
		}
		attackable--;
		setBackgroundColor(Color.GRAY);
		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = 0;
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();
		player.sender.S("9 " + enemyindex + "," + playerindex);
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
