package game;

import net.Sender;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import animation.Attack;

import com.mylikenews.nextoneandroid.R;

public class Monster extends RelativeLayout implements Target {

	Field field;
	int defauldamage;
	int defaulvital;
	int attackable, maxattackable;
	int maxvital, defaulmaxvital;
	int id;
	Effect whendeath;
	Effect whenturn;
	Context context;
	ViewBinder damage, vital;
	String resource;
	RelativeLayout.LayoutParams params;
	boolean uped = false;

	public Monster(Context context, Card card, Field field, int index) {
		super(context);
		deFault(context, field, index);
		setDamageVital(card.attack(), card.vital());
		this.resource = card.resource();
		setBackgroundDefault();

	}

	public void setBackgroundDefault() {
		setBackgroundResource(Method.resId(resource));
	}

	private void setDamageVital(int attack, int vital) {
		defauldamage = attack;
		damage = new ViewBinder(context, attack, this);
		damage.setBackgroundResource(R.drawable.attack);
		RelativeLayout.LayoutParams damageparams = damage.getParams();
		damageparams.addRule(RelativeLayout.ALIGN_BOTTOM);
		damageparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		damageparams.width = Method.dpToPx(30);
		damageparams.height = Method.dpToPx(42);
		damageparams.setMargins(Method.dpToPx(7), 0, 0, 0);
		this.damage.setGravity(Gravity.CENTER);

		maxvital = vital;
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
		id = Integer.parseInt(cardinfo[0]);
		setDamageVital(Integer.parseInt(cardinfo[1]),
				Integer.parseInt(cardinfo[2]));
		this.resource = cardinfo[3];
		setBackgroundDefault();
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
		setBackgroundDefault();
	}

	@SuppressLint("NewApi")
	private void deFault(Context context, Field field, int index) {

		setY(-10);
		setY(10);

		this.context = context;
		this.field = field;
		this.id = index;
		attackable = 0;
		maxattackable = 1; // 기본 공격 가능 횟수 = 1

		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(90);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.height = field.scrollHeight();
		setLayoutParams(params);

		setOnAttackClickListener();
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

	@SuppressLint("NewApi")
	private void attackLisenter(View v) {
		if (damage.Int() == 0 || attackable == 0)
			return;
		Method.alert("공격할 대상을 선택해 주세요.");
		field.othersDown();
		Sender.S("11 " +id); // 선택한 것 알려주기
		setAttackBackground();
		v.setY(-10);
		this.uped = true;

		field.attacker = (Target) v;
		field.player.enemy.field.targetSelect();
	}

	@Override
	public void setAttackBackground() {
		this.setBackgroundResource(Method.resId(resource+"attack"));
	}

	@Override
	public String toString() {
		String info;
		info = id + "," + damage.Int() + "," + vital.Int() + "," + resource;
		return info;
	}

	@Override
	public void attacked(int damage) {

		vital.add(-damage);
		if (vital.Int() < 1) {
			field.remove(this);
		}
		if (isAttacked()) {
			vital.setTextColor(Color.RED);
			return;
		}
		vital.setTextColor(Color.WHITE);
	}

	private boolean isAttacked() {
		return vital.Int() < maxvital;
	}

	public void newTurn() {
		this.attackable = maxattackable;
		setOnAttackClickListener();
		attackCheck();
	}

	@SuppressLint("NewApi")
	@Override
	public void attack(Target target, boolean isChecked) {
		if (!isChecked) {
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
				setY(10);
			}
		}else{
			setBackgroundDefault();
		}
		Attack.AttackEffect(this, target, isChecked);

		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = index();
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();
		Sender.S("9 " + enemyindex + "," + playerindex);
	}

	public int index() {
		return id;
	}

	@Override
	public int damage() {
		return damage.Int();
	}

	public void endTurn() {
		setOnClickListener(null);
		attackdisAble();
	}

	@Override
	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	public Monster cloneForAnimate() {
		Monster monster = new Monster(context, this.toString(), field, id);
		if (isAttacked()) {
			monster.vital.setTextColor(Color.RED);
		}
		return monster;
	}

	@Override
	public boolean isHero() {
		return false;
	}

	@Override
	public int getMarginY() { // 블럭들의 마진을 계산해서 넘겨줌.. 지저분하지만...
		if (field.player.me == 1) {
			return field.player.hand.getHeight()
					+ field.player.hero.getHeight();
		} else {
			return field.player.hand.getHeight()
					+ field.player.hero.getHeight() * 2;
		}
	}

	@Override
	public int getTopY() {
		if (field.player.me == 1) {
			return field.getHeight() + field.player.hero.getHeight();
		} else {
			return field.getHeight();
		}
	}

}
