package Game;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class Monster extends ViewWrap implements Target {

	Field field;
	int defauldamage;
	int defaulvital;
	int attackable, maxattackable;
	int maxvital, defaulmaxvital;
	int index;
	Effect whendeath;
	Effect whenturn;
	Context context;
	ViewBinder damage, vital;

	public Monster(Context context, Card card, Field field, int index) {
		super(context);
		deFault(context, field, index);
		this.damage = new ViewBinder(context, card.attack.Int(), this);
		this.vital = new ViewBinder(context, card.vital.Int(), this);
		
		
		
		
	}

	public Monster(Context context, String info, Field field, int index) {
		super(context);
		deFault(context, field, index);
		String[] cardinfo = info.split(",");
		this.damage = new ViewBinder(context, Integer.parseInt(cardinfo[0]),
				this);
		this.vital = new ViewBinder(context, Integer.parseInt(cardinfo[1]),
				this);
		
	}

	private void deFault(Context context, Field field, int index) {
		this.context = context;
		this.field = field;
		this.index = index;
		attackable = 0;
		maxattackable = 1;  // 기본 공격 가능 횟수 = 1
		
		setOnFirstClickListener();
	}
	
	private void setOnFirstClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert("이번턴에 낸 하수인은 공격할 수 없습니다.");
			}
		};

		setOnClickListener(attakable);
	}

	private void setOnAttackClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert("공격할 대상을 선택해 주세요.");
				field.attacker = (Target) v;
				field.player.enemy.field.targetSelect();
			}
		};

		setBackgroundColor(Color.YELLOW);
		setOnClickListener(attakable);
	}

	@Override
	public String toString() {
		String info;
		info = damage.Int() + "," + vital.Int();
		return info;
	}

	@Override
	public void attacked(int damage) {
		vital.add(-damage);
		if (vital.Int() < 1) {
			field.remove(this);
		}
	}

	public void newTurn() {
		this.attackable = maxattackable;
		setOnAttackClickListener();
	}

	@Override
	public void attack(Target target) {
		if (damage.Int() == 0) {
			alert("공격력이 0인 하수인은 공격할 수 없습니다.");
			return;
		}
		if (attackable == 0) {
			alert("선택한 대상은 이미 공격했습니다.");
			return;
		}
		attackable--;
		setBackgroundColor(Color.GRAY);
		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = index;
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();
		field.sender.S("9 " + enemyindex + "," + playerindex);
	}

	public int index() {
		return index;
	}

	@Override
	public int damage() {
		return damage.Int();
	}

	public void endTurn() {
		setOnClickListener(null);
	}

	public void attackOrder(Target another) {
		another.attacked(damage.Int());
		this.attacked(another.damage());
	}
}
