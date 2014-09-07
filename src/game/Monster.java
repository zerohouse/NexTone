package game;

import net.Sender;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import animation.Attack;

import com.mylikenews.nextoneandroid.R;

import components.ViewBinder;
import effects.monster.aura.AuraEffect;
import effects.monster.excute.ExcuteEffect;

public class Monster extends RelativeLayout implements Target {

	Field field;
	int defauldamage;
	int defaulvital;
	int attackable, maxattackable;
	int maxvital, defaulmaxvital;
	int id;
	int type;
	String effects;
	boolean defenseMonster, shield = false; // 방패/보호막
	ImageView charimage, foreimage;

	Context context;
	ViewBinder damage, vital;
	String resource;
	RelativeLayout.LayoutParams params;
	boolean uped = false;

	public Monster(Context context, Card card, Field field, String effects,
			boolean sended) {
		super(context);
		this.effects = effects;
		deFault(context, field, card.index());
		setDamageVital(card.attack(), card.vital());
		this.resource = card.resource();
		setBackgroundDefault();
		setEffects();

		if (!sended) {
			Sender.S("8 " + field.player.me + "@" + toString());

		}
	}

	public void setBackgroundDefault() {
		charimage.setBackgroundResource(Method.resId(resource));
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

	public Monster(Context context, String info, Field field, int index,
			boolean sended) {
		super(context);
		String[] cardinfo = info.split(",");
		deFault(context, field, Integer.parseInt(cardinfo[0]));

		setDamageVital(Integer.parseInt(cardinfo[1]),
				Integer.parseInt(cardinfo[2]));
		this.resource = cardinfo[3];
		setBackgroundDefault();
		this.effects = cardinfo[4];
		Log.i("effects", effects);

		setEffects();

		if (!sended) {
			Sender.S("8 " + field.player.me + "@" + toString());

		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Monster other = (Monster) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private void setEffects() {
		if (!effects.contains("#")) {
			type = Integer.parseInt(effects);
			return;
		}
		String[] effect = effects.split("#");
		type = Integer.parseInt(effect[0]);
		if (effect.length < 2)
			return;
		for (int i = 1; i < effect.length; i++) {
			setEffect(Integer.parseInt(effect[i]));
		}
	}

	private void setEffect(int effect) {
		switch (effect) {
		case 1:
			setBackgroundResource(R.drawable.vital);
			field.setDefenseMonster();
			defenseMonster = true;
			break;
		case 2:
			foreimage = new ImageView(context);
			foreimage.setBackgroundResource(R.drawable.shield);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			foreimage.setLayoutParams(lay);
			addView(foreimage);
			shield = true;
			break;
		case 3:
			break;
		}
	}

	public void attackCheck() {
		if (attackable > 0 && damage.Int() > 0) {
			attackAble();
			return;
		}
		attackdisAble();
	}

	public void attackAble() {
		charimage.setBackgroundResource(Method.resId(resource + "attackable"));
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

		charimage = new ImageView(context);
		RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		charimage.setLayoutParams(lay);
		addView(charimage);

		attackReady();
	}

	public void attackReady() { // 어택리스너에서 어택리스너를 올리고
		OnClickListener attakable = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attackReadyClicked(v);
			}

		};

		setOnClickListener(attakable);
	}

	// 어택 리스너 통합 해야함.

	@SuppressLint("NewApi")
	public void attackReadyClicked(View v) { //
		if (damage.Int() == 0 || attackable == 0)
			return;
		Method.alert("공격할 대상을 선택해 주세요.");
		field.attackCheckUpedMonster();
		Sender.S("11 " + id); // 선택한 것 알려주기
		setAttackBackground();
		v.setY(-10);
		this.uped = true;
		Static.attacker = (Target) v;

		Listeners.setAttacked();
		field.player.enemy.field.setListener();
		field.player.enemy.hero.setListener();

		field.player.endturn.setText("    취소");
		field.player.endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Static.Cancel(field.player, true);
			}

		});
	}

	@Override
	public void setAttackBackground() {
		charimage.setBackgroundResource(Method.resId(resource + "attack"));
	}

	@Override
	public String toString() {
		return id + "," + damage.Int() + "," + vital.Int() + "," + resource
				+ "," + effects;
	}

	@Override
	public void setByString(String setString) {
		String[] tmp = setString.split(",");
		int id = Integer.parseInt(tmp[0]);
		int damage = Integer.parseInt(tmp[1]);
		int vital = Integer.parseInt(tmp[2]);
		String resource = tmp[3];
		this.effects = tmp[4];
		if (this.id != id) {
			Method.alert("아이디가 다릅니다.");
			return;
		}
		this.damage.setInt(damage);
		this.vital.setInt(vital);
		vitalCheck();
		this.resource = resource;
	}

	@Override
	public void attacked(int damage) {
		if (shield && damage > 0) {
			offShield();
			return;
		}

		vital.add(-damage);
		removeCheck();
		vitalCheck();
	}

	private void offShield() {
		removeView(foreimage);
		shield = false;
	}

	private void removeCheck() {
		if (vital.Int() < 1) {
			if (death != null)
				death.run();
			if (state != null)
				state.effectEnd();

			if (defenseMonster)
				field.dieDefenseMonster();
			field.remove(this);
		}
	}

	@Override
	public void vitalCheck() {
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
		attackReady();
		attackCheck();
	}

	@SuppressLint("NewApi")
	@Override
	public void attack(Target target, boolean isChecked) {
		if (!target.attackedable()) {
			return;
		}
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
		} else {
			setBackgroundDefault();
		}

		Static.attacker = null;

		setY(10);
		attackCheck();
		Attack.AttackEffect(this, target, isChecked);

		target.attacked(damage.Int());
		this.attacked(target.damage());
		int playerindex = index();
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();

		Static.Cancel(field.player, false);
		if (!isChecked)
			Sender.S("9 " + enemyindex + "," + playerindex);
	}

	public int index() {
		return id;
	}

	@Override
	public int damage() {
		return damage.Int();
	}

	ExcuteEffect endTurn, death = null;

	public void setEndTurnEffect(ExcuteEffect effect) {
		this.endTurn = effect;
	}

	public void setDeathEffect(ExcuteEffect effect) {
		this.death = effect;
	}

	AuraEffect state = null;

	public void setAuraEffect(AuraEffect effect) {
		this.state = effect;
		effect.effectStart();
	}

	public void endTurn() {
		setOnClickListener(null);
		attackdisAble();
		if (endTurn != null) {
			endTurn.run();
		}
	}

	@Override
	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	public Monster cloneForAnimate() {
		Monster monster = new Monster(context, this.toString(), field, id, true);
		if (defenseMonster)
			field.dieDefenseMonster();
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

	@Override
	public void heal(int amount, boolean sended) {
		if (!sended)
			Sender.S("16 " + field.player.me + "," + id + "," + amount);

		if (shield && amount < 0) {
			offShield();
			return;
		}
		
		vital.add(amount);
		if (vital.Int() > maxvital) {
			vital.setInt(maxvital);
		}
		removeCheck();
		vitalCheck();
	}

	@Override
	public Player player() {
		return field.player;
	}

	@SuppressLint("NewApi")
	@Override
	public float getX(boolean isHero) {
		if (isHero)
			return getX() - Method.dpToPx(30);
		return getX();
	}

	public Field field() {
		return field;
	}

	@Override
	public boolean attackedable() {
		if (field.defenseMonsterInField() && defenseMonster == false) {
			Method.alert("방어 하수인부터 공격해야 합니다.");
			return false;
		}
		return true;
	}

	public boolean shield() {
		return defenseMonster;
	}

}
