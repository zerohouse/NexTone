package game;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.R;

import components.ViewBinder;
import effects.monster.excute.ExcuteEffect;

public class Weapon extends RelativeLayout {
	int attackable;
	ViewBinder damage, vital;
	Hero hero;
	RelativeLayout.LayoutParams params;
	Context context;
	String resource;
	int maxvital;
	ExcuteEffect deathEffect = null, attackEffect = null;
	boolean legend = false;

	Weapon(Context context, Hero hero, int damage, int vital, String resource) {
		super(context);
		this.resource = resource;
		this.context = context;
		this.hero = hero;

		setBackgroundResource(Method.resId(resource));
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.width = Method.dpToPx(70);
		params.height = Method.dpToPx(60);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = Method.dpToPx(12);
		params.leftMargin = Method.dpToPx(4);
		setLayoutParams(params);

		attackable = 1;
		this.damage = new ViewBinder(context, damage, this);
		this.damage.setBackgroundResource(R.drawable.attack);
		RelativeLayout.LayoutParams damageparams = this.damage.getParams();
		damageparams.addRule(RelativeLayout.ALIGN_BOTTOM);
		damageparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		damageparams.width = Method.dpToPx(25);
		damageparams.height = Method.dpToPx(28);
		damageparams.setMargins(Method.dpToPx(7), 0, 0, 0);
		this.damage.setTextSize(18);
		this.damage.setGravity(Gravity.CENTER);

		maxvital = vital;
		this.vital = new ViewBinder(context, vital, this);
		RelativeLayout.LayoutParams vitalparams = this.vital.getParams();
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.vital.setBackgroundResource(R.drawable.vital);
		this.vital.setTextSize(18);
		vitalparams.width = Method.dpToPx(25);
		vitalparams.height = Method.dpToPx(28);

		vitalparams.setMargins(0, 0, Method.dpToPx(7), 0);
		this.vital.setGravity(Gravity.CENTER);

	}

	public int attackAble() {
		return attackable;
	}

	public int damage() {
		return damage.Int();
	}

	public void use(Target target) {
		if (attackEffect != null) {
			attackEffect.run();
		}
		if (legend) {
			legendUse(target);
			return;
		}
		vital.add(-1);
		weaponCheck();
	}

	private void legendUse(Target target) {
		if (!target.isHero())
			damage.add(-1);
		else
			vital.add(-1);

		weaponCheck();
	}

	public void legend() {
		this.legend = true;
	}

	private void weaponCheck() {
		if (maxvital > vital.Int())
			vital.setTextColor(Color.RED);
		else
			vital.setTextColor(Color.WHITE);

		if (vital.Int() < 1 || damage.Int() < 1) {
			die();
		}
	}

	public void die() {
		if (deathEffect != null) {
			deathEffect.run();
		}
		hero.removeView(this);
		hero.hero.weapon = null;
	}

	public void setDeathEffect(ExcuteEffect excuteEffect) {
		deathEffect = excuteEffect;
	}

	public void setAttackEffect(ExcuteEffect excuteEffect) {
		attackEffect = excuteEffect;
	}

}
