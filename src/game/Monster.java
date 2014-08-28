package game;

import net.Sender;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.R;

public class Monster extends RelativeLayout implements Target {

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
	String resource;
	RelativeLayout.LayoutParams params;

	public Monster(Context context, Card card, Field field, int index) {
		super(context);
		deFault(context, field, index);
		setDamageVital(card.attack(), card.vital());
		this.resource = card.resource();
		setBackgroundResource(Method.resId(resource));

	}

	private void setDamageVital(int attack, int vital) {
		damage = new ViewBinder(context, attack, this);
		damage.setBackgroundResource(R.drawable.attack);
		RelativeLayout.LayoutParams damageparams = damage.getParams();
		damageparams.addRule(RelativeLayout.ALIGN_BOTTOM);
		damageparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		damageparams.width = Method.dpToPx(30);
		damageparams.height = Method.dpToPx(42);
		damageparams.setMargins(Method.dpToPx(7), 0, 0, 0);
		this.damage.setGravity(Gravity.CENTER);

		this.vital = new ViewBinder(context, vital, this);
		RelativeLayout.LayoutParams vitalparams = this.vital.getParams();
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.vital.setBackgroundResource(R.drawable.vital);
		vitalparams.width = Method.dpToPx(30);
		vitalparams.height = Method.dpToPx(42);

		vitalparams.setMargins(0, 0, Method.dpToPx(7), 0);
		this.vital.setGravity(Gravity.CENTER);
	}

	public Monster(Context context, String info, Field field, int index) {
		super(context);
		deFault(context, field, index);
		String[] cardinfo = info.split(",");
		setDamageVital(Integer.parseInt(cardinfo[0]),
				Integer.parseInt(cardinfo[1]));
		this.resource = cardinfo[2];
		setBackgroundResource(Method.resId(resource));
	}

	public void attackCheck() {
		if (attackable > 0 && damage.Int() > 0) {
			attackAble();
			return;
		}
		attackdisAble();
	}

	public void attackAble() {
		setBackgroundResource(Method.resId(resource + "attackable"));
	}

	public void attackdisAble() {
		attackable = 0;
		setBackgroundResource(Method.resId(resource));
	}

	private void deFault(Context context, Field field, int index) {
		this.context = context;
		this.field = field;
		this.index = index;
		attackable = 0;
		maxattackable = 1; // 기본 공격 가능 횟수 = 1

		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(100);
		params.height = field.scrollHeight();
		setLayoutParams(params);

		setOnFirstClickListener();
	}

	private void setOnFirstClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attackLisenter(v);
			}
		};

		setOnClickListener(attakable);
	}

	private void setOnAttackClickListener() {
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attackLisenter(v);
			}

		};

		setOnClickListener(attakable);
	}

	private void attackLisenter(View v) {
		if (damage.Int() == 0 || attackable == 0)
			return;
		Method.alert("공격할 대상을 선택해 주세요.");
		field.attacker = (Target) v;
		field.player.enemy.field.targetSelect();
	}

	@Override
	public String toString() {
		String info;
		info = damage.Int() + "," + vital.Int() + "," + resource;
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
		attackCheck();
	}

	@Override
	public void attack(Target target) {
		if (damage.Int() == 0) {
			Method.alert("공격력이 0인 하수인은 공격할 수 없습니다.");
			return;
		}
		if (attackable == 0) {
			Method.alert("선택한 대상은 이미 공격했습니다.");
			return;
		}
		attackable--;
		if (attackable == 0) {
			attackdisAble();
		}

		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = index;
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();
		Sender.S("9 " + enemyindex + "," + playerindex);
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
		attackdisAble();
	}

	public void attackOrder(Target another) {
		another.attacked(damage.Int());
		this.attacked(another.damage());
	}
}
