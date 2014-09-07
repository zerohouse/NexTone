package game;

import net.Sender;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import animation.Attack;
import animation.Heal;

import com.mylikenews.nextoneandroid.R;
import components.ViewBinder;

public class HeroCharacter extends RelativeLayout implements Target {

	Hero hero;
	int attackable, maxvital;
	ViewBinder vital, damage, defense;
	String resource;
	RelativeLayout.LayoutParams params;
	Weapon weapon = null;
	Context context;

	public HeroCharacter(Context context, Hero hero, String resource) {
		super(context);
		this.hero = hero;
		this.context = context;
		this.resource = resource;

		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(140);
		params.setMargins(Method.dpToPx(70), 0, 0, 0);
		setLayoutParams(params);

		setBackgroundResource(Method.resId(resource)); // 히어로 선택 부분 수정해야함.

		defense = new ViewBinder(context, 0, this, false);
		defense.setTextSize(22);

		RelativeLayout.LayoutParams defenseparam = defense.getParams();
		defenseparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		defenseparam.rightMargin = Method.dpToPx(17);
		defenseparam.topMargin = Method.dpToPx(30);

		damage = new ViewBinder(context, 0, this);
		damage.setBackgroundResource(R.drawable.attack);
		damage.setGravity(Gravity.CENTER);

		RelativeLayout.LayoutParams damageparams = damage.getParams();
		damageparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		damageparams.width = Method.dpToPx(40);
		damageparams.height = Method.dpToPx(55);

		maxvital = 30;

		vital = new ViewBinder(context, 30, this);
		vital.setBackgroundResource(R.drawable.vital);
		vital.setGravity(Gravity.CENTER);

		RelativeLayout.LayoutParams vitalparams = this.vital.getParams();
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		vitalparams.width = Method.dpToPx(45);
		vitalparams.height = Method.dpToPx(58);

		attackable = 1;
	}

	public void attackCheck() {
		if (hero.player.me != 1)
			return;
		if (attackable > 0 && damage.Int() > 0) {
			attackAble();
			return;
		}
		attackdisAble();
	}

	private void attackdisAble() {
		setBackgroundResource(Method.resId(resource));
	}

	private void attackAble() {
		setBackgroundResource(Method.resId(resource + "attackable"));
	}

	public void setAttackBackground() {
		setBackgroundResource(Method.resId(resource + "attack"));
	}

	@Override
	@SuppressLint("NewApi")
	public float getX(boolean hero) { // 히어로는 커서 엑스좌표를 100으로 넘겨주면되고
										// 일반 영웅은 아님
		if (hero)
			return Method.dpToPx(70);
		return Method.dpToPx(100);
	}

	@Override
	public int damage() {
		return damage.Int();
	}

	@Override
	public void attacked(int damage) {
		defense.add(-damage);

		vital.add(defenseCheck());
		defeatCheck();
		vitalCheck();

	}

	private int defenseCheck() {
		int remaindamage;
		if (this.defense.Int() < 1) {
			remaindamage = defense.Int();
			this.defense.setInt(0);
			this.defense.setText("");
			return remaindamage;
		}
		return 0;
	}

	@Override
	public void vitalCheck() {
		if (isAttacked()) {
			vital.setTextColor(Color.RED);
			return;
		}
		vital.setTextColor(Color.WHITE);
	}

	public boolean isAttacked() {
		return vital.Int() < maxvital;
	}

	void defeatCheck() {
		if (vital.Int() < 1) {
			hero.player.defeat();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void attack(Target target, boolean isChecked) {

		if (!target.attackedable()) {
			return;
		}
		if (!isChecked) {
			if (damage.Int() == 0) {
				Method.alert("공격할 수 없습니다.");
				return;
			}
			if (attackable == 0) {
				Method.alert("이미 공격했습니다.");
				return;
			}
			attackable--;
		} else {
			setBackgroundDefault();
		}

		Static.attacker = null;

		attackCheck();
		Attack.AttackEffect(this, target, isChecked);

		if (weapon != null)
			weapon.use();

		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = index();
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();

		Static.Cancel(hero.player, false);
		if (!isChecked)
			Sender.S("9 " + enemyindex + "," + playerindex);
	}

	private void setBackgroundDefault() {
		setBackgroundResource(Method.resId(resource));
	}

	@Override
	public int index() {
		return -1;
	}

	@Override
	public LayoutParams getParams() {
		return params;
	}

	@Override
	public Target cloneForAnimate() {
		HeroCharacter clone = new HeroCharacter(context, hero, resource);
		clone.setByString(toString());
		clone.vitalCheck();
		clone.params.height = Method.dpToPx(100);
		clone.params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		return clone;
	}

	@Override
	public String toString() {
		return defense.Int() + "," + damage.Int() + "," + vital.Int();
	}

	@Override
	public boolean isHero() {
		return true;
	}

	@Override
	public int getMarginY() {
		if (hero.player.me == 1) {
			return hero.player.hand.getHeight();
		} else {
			return hero.player.field.getHeight() * 2 + this.getHeight()
					+ hero.player.hand.getHeight();
		}
	}

	@Override
	public int getTopY() {
		if (hero.player.me == 1)
			return hero.player.field.getHeight() * 2 + this.getHeight();
		else {
			return 0;
		}
	}

	public void attackReady() { // 어택리스너에서 어택리스너를 올리고
		if (hero.player.me != 1)
			return;
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attackReadyCliecked(v);
			}

		};

		setOnClickListener(attakable);
	}

	// 어택 리스너 통합 해야함.

	@SuppressLint("NewApi")
	public void attackReadyCliecked(View v) { //
		if (damage.Int() == 0 || attackable == 0)
			return;
		Method.alert("공격할 대상을 선택해 주세요.");

		Sender.S("11 -1"); // 선택한 것 알려주기
		setAttackBackground();

		Static.attacker = (Target) v;

		Listeners.setAttacked();

		hero.player.field.attackCheckUpedMonster();
		hero.player.enemy.field.setListener();
		hero.player.enemy.hero.setListener();

		hero.player.endturn.setText("    취소");
		hero.player.endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Static.Cancel(hero.player, true);
			}

		});
	}

	@Override
	public void heal(int amount, boolean sended, Target from) {
		if (!sended)
			Sender.S("16 " + hero.player.me + "#" + -1 + "," + amount + ","
					+ from.PlayerInfo() + "#" + from.index());

		Heal.HealEffect(from, this, sended);

		vital.add(amount);
		if (vital.Int() > maxvital) {
			vital.setInt(maxvital);
		}
		defeatCheck();
		vitalCheck();

	} 

	@Override
	public Player player() {
		return hero.player;
	}

	public void newTurn() {

		attackable = 1;
		if (weapon != null) {
			attackable = weapon.attackAble();
			damage.setInt(weapon.damage());
		}
		attackReady();
		attackCheck();

	}

	public void endTurn() {
		setOnClickListener(null);
		attackable = 0;
		damage.setInt(0);
		attackCheck();
	}

	public void getWepon(Weapon weapon) {
		this.weapon = weapon;
		if (weapon != null) {
			attackable = weapon.attackAble();
			damage.setInt(weapon.damage());
		}
		attackReady();
		attackCheck();
	}

	@Override
	public void setByString(String string) {
		String[] tmp = string.split(",");
		int defense = Integer.parseInt(tmp[0]);
		int damage = Integer.parseInt(tmp[1]);
		int vital = Integer.parseInt(tmp[2]);

		this.damage.setInt(damage);
		this.vital.setInt(vital);
		this.defense.setInt(defense);

		defenseCheck();

	}

	public void getDefense(int defense) {
		this.defense.add(defense);
		defenseCheck();
	}

	@Override
	public boolean attackedable() {
		if (hero.player.field.defenseMonsterInField()) {
			Method.alert("방패 하수인부터 공격해야 합니다.");
			return false;
		}
		return true;
	}

	@Override
	public int PlayerInfo() {
		return hero.player.me();
	}

}
