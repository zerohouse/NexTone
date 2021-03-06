package game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import animation.Attack;
import animation.Heal;
import animation.Helper;

import com.mylikenews.nextoneandroid.R;

import components.ViewBinder;
import effects.monster.aura.AuraEffect;
import effects.monster.excute.ExcuteEffect;

public class Monster extends RelativeLayout implements Target {

	public static Monster tmpmonster = null;
	
	Field field;
	final int defaultattack, defaultvital;
	int attackable, maxattackable;
	int maxvital, defaulmaxvital;
	int id;
	int type;
	int spellpower;
	int position = -1;
	String effects;
	boolean defenseMonster, shield, wakeup = false, stunned = false,
			hide = false, killer = false, die = false; // 방패/보호막/빙결
	ImageView charimage, shieldimage = null, stunimage = null,
			doubleimage = null, hideimage = null, killerimage = null,
			atable = null, attackready = null;
	Card card;

	Heal healeffect = null;

	OnClickListener showHelper;
	Context context;
	ViewBinder damage, vital;
	String resource;
	RelativeLayout.LayoutParams params;

	public Monster(Context context, Card card, Field field) {
		super(context);
		this.card = card;
		this.effects = card.getMonstereffects();
		this.resource = card.resource();
		deFault(context, field, card.getMonsterindex());
		defaultattack = card.attack();
		defaultvital = card.vital();
		spellpower = 0;
		setDamageVital(card.attack(), card.vital());
		setBackgroundDefault();
		setHelperShow();
		setEffects();
	}

	public void setBackgroundDefault() {
		if (attackready != null)
			attackready.setVisibility(View.INVISIBLE);
		if (atable != null)
			atable.setVisibility(View.INVISIBLE);
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

	public static final int DEFENSE_MONSTER = 1;
	public static final int HAS_SHIELD = 2;
	public static final int ATTACK_READY = 3;
	public static final int SPELLPOWER = 4;
	public static final int DOUBLE = 5;
	public static final int KILLER = 6;
	public static final int HIDE = 7;

	private void setEffect(int effect) {
		switch (effect) {
		case DEFENSE_MONSTER:
			setBackgroundResource(R.drawable.vital);
			defenseMonster = true;
			break;

		case HAS_SHIELD:
			setShield();
			break;

		case ATTACK_READY:
			if (field.player.me() == 1)
				newTurn();
			break;

		case SPELLPOWER:
			spellpower--;
			break;

		case DOUBLE:
			setDouble();
			break;

		case KILLER:
			setKiller();
			break;

		case HIDE:
			setHide();
			break;
		}
	}

	public void attackCheck() {
		if (attackable > 0 && damage.Int() > 0) {
			attackAble();
			return;
		}
		setBackgroundDefault();
	}

	public void attackAble() {
		if (atable == null) {
			atable = new ImageView(context);
			atable.setBackgroundResource(R.drawable.attackable);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			atable.setLayoutParams(lay);
			addView(atable, 0);
		}
		atable.setVisibility(View.VISIBLE);
		if (attackready != null)
			attackready.setVisibility(View.INVISIBLE);

	}

	@SuppressLint("NewApi")
	private void deFault(Context context, Field field, int index) {
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
		params.height = field.getHeight();
		setLayoutParams(params);

		charimage = new ImageView(context);
		RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		charimage.setLayoutParams(lay);
		charimage.setBackgroundResource(Method.resId(resource));
		addView(charimage);

		showHelper = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Helper.showInfo(card.getAni());
			}
		};

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
		Game.sender.S("11&" + id); // 선택한 것 알려주기
		setAttackBackground();

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
		if (attackready == null) {
			attackready = new ImageView(context);
			attackready.setBackgroundResource(R.drawable.attackready);
			if (player().me == 1)
				addView(attackready, 1);
			else
				addView(attackready, 0);
		}
		field.attackCheck();
		attackready.setVisibility(View.VISIBLE);

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
	public void attacked(Target target) {
		if (shield && target.damage() > 0) {
			unShield();
			return;
		}
		vital.add(-target.damage());
		removeCheck();
		vitalCheck();
		if (target.isHero())
			return;
		Monster t = (Monster) target;
		if (t.isKiller()) {
			die();
			if (death != null)
				death.run();
		}
	}

	private void removeCheck() {
		if (vital.Int() < 1) {
			die();
			if (death != null)
				death.run();

		}
	}

	@Override
	public void die() {
		if (die)
			return;
		die = true;
		field.remove(this);
		if (state != null)
			state.effectEnd();
	}

	@Override
	public void vitalCheck() {
		if (vital.Int() > defaultvital) {
			vital.setTextColor(Color.GREEN);
			return;
		}
		if (isAttacked()) {
			vital.setTextColor(Color.RED);
			return;
		}
		vital.setTextColor(Color.WHITE);
	}

	@Override
	public void damageCheck() {
		if (damage.Int() > defaultattack) {
			damage.setTextColor(Color.GREEN);
			return;
		}
		damage.setTextColor(Color.WHITE);
	}

	private boolean isAttacked() {
		return vital.Int() < maxvital;
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
		if (isHide())
			unHide();

		Static.attacker = null;

		attackCheck();
		Attack.AttackEffect(this, target, isChecked);

		target.attacked(this);
		this.attacked(target);
		int playerindex = index();
		// 상대입장에서 봐야 되니까 뒤집어짐.
		int enemyindex = target.index();

		Static.Cancel(field.player, false);

		if (!isChecked)
			Game.sender.S("9&" + enemyindex + "," + playerindex);

	}

	public int index() {
		return id;
	}

	@Override
	public int damage() {
		return damage.Int();
	}

	ExcuteEffect newTurn = null, endTurn = null, death = null;

	public void setNewTurnEffect(ExcuteEffect effect) {
		this.newTurn = effect;
	}

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
		if (wakeup) {
			removeView(stunimage);
		}
		setHelperShow();
		setBackgroundDefault();
		if (endTurn != null) {
			endTurn.run();
		}
	}

	public void newTurn() {
		if (!isStunned())
			this.attackable = maxattackable;
		else
			wakeUp(false);

		attackReady();
		attackCheck();
		if (newTurn != null) {
			newTurn.run();
		}
	}

	@Override
	public void setStun(boolean sended, Target from, String resource) {
		if (isStunned())
			return;

		if (!sended)
			Game.sender.S("165&" + field.player.me + "#" + id + ","
					+ from.PlayerInfo() + "#" + from.index() + "," + resource);

		if (healeffect == null)
			healeffect = new Heal();
		healeffect.HealEffect(from, this, sended, resource);

		if (stunimage == null) {
			stunimage = new ImageView(context);
			stunimage.setBackgroundResource(R.drawable.stun);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			stunimage.setLayoutParams(lay);
		}
		addView(stunimage);
		stunned = true;
	}

	@Override
	public void wakeUp(boolean sended) {
		if (!isStunned())
			return;
		if (!sended)
			Game.sender.S("166&" + field.player.me + "#" + id);
		stunned = false;
		wakeup = true;
	}

	public boolean isStunned() {
		return stunned;
	}

	public boolean isDouble() {
		return maxattackable == 2;
	}

	public void unDouble() {
		removeView(doubleimage);
		maxattackable = 1;
	}

	public void setDouble() {
		if (doubleimage == null) {
			doubleimage = new ImageView(context);
			doubleimage.setBackgroundResource(R.drawable.doubleattack);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			doubleimage.setLayoutParams(lay);
		}
		addView(doubleimage);
		maxattackable = 2;
	}

	public void setShield() {
		if (shieldimage == null) {
			shieldimage = new ImageView(context);
			shieldimage.setBackgroundResource(R.drawable.shield);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			shieldimage.setLayoutParams(lay);
		}
		addView(shieldimage);
		shield = true;
	}

	public boolean isShield() {
		return shield;
	}

	public void unShield() {
		removeView(shieldimage);
		shield = false;
	}

	public void setHide() {
		if (hideimage == null) {
			hideimage = new ImageView(context);
			hideimage.setBackgroundResource(R.drawable.hide);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			hideimage.setLayoutParams(lay);
		}
		addView(hideimage);
		hide = true;
	}

	public boolean isHide() {
		return hide;
	}

	public void unHide() {
		removeView(hideimage);
		hide = false;
	}

	public void setKiller() {
		if (killerimage == null) {
			killerimage = new ImageView(context);
			killerimage.setBackgroundResource(R.drawable.killer);
			RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			killerimage.setLayoutParams(lay);
		}
		addView(killerimage);
		killer = true;
	}

	public boolean isKiller() {
		return killer;
	}

	public void unKiller() {
		removeView(killerimage);
		killer = false;
	}

	public void setHelperShow() {
		setOnClickListener(showHelper);
	}

	@Override
	public RelativeLayout.LayoutParams getParams() {
		return params;
	}

	public Monster cloneForAnimate() {
		Monster monster = new Monster(context, card, field);
		if (isShield())
			monster.unShield();
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
			return field.getHeight() + field.player.hero.getHeight()
					- Method.dpToPx(35);
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
	public void abilityUp(String amount, boolean sended, Target from,
			String resource) {

		if (healeffect == null)
			healeffect = new Heal();
		healeffect.HealEffect(from, this, sended, resource);

		String[] tmp = amount.split("=");

		int attackamount = Integer.parseInt(tmp[0]);
		int vitalamount = Integer.parseInt(tmp[1]);

		damage.add(attackamount);
		vital.add(vitalamount);
		maxvital += vitalamount;

		if (vital.Int() > maxvital) {
			vital.setInt(maxvital);
		}
		removeCheck();
		vitalCheck();
		damageCheck();

		if (!sended)
			Game.sender.S("160&" + field.player.me + "#" + id + "," + amount
					+ "," + from.PlayerInfo() + "#" + from.index() + ","
					+ resource);

	}

	@Override
	public void heal(int amount, boolean sended, Target from, String resource) {
		if (!sended)
			Game.sender.S("16&" + field.player.me + "#" + id + "," + amount
					+ "," + from.PlayerInfo() + "#" + from.index() + ","
					+ resource);

		if (isShield() && amount < 0) {
			unShield();
			return;
		}

		if (healeffect == null)
			healeffect = new Heal();
		healeffect.HealEffect(from, this, sended, resource);

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

	public boolean isDefenseMonster() {
		return defenseMonster;
	}

	@Override
	public int PlayerInfo() {
		return field.player.me();
	}

	public Card card() {
		return card;
	}

	public int getSpellpower() {
		return spellpower;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void sendInfo() {
		Game.sender.S("8&" + field.player.me + "@" + id + "@"
				+ card.index() + "@" + position);
	}

}
