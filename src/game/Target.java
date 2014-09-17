package game;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public interface Target {

	public abstract int damage();

	public abstract void attacked(Target target);

	public abstract void attack(Target target, boolean isChecked);

	public abstract int index();

	public abstract float getX(boolean isHero);

	public abstract float getY();

	public abstract RelativeLayout.LayoutParams getParams();

	public abstract void setX(float x);

	public abstract void setY(float y);

	public abstract Target cloneForAnimate();

	public abstract boolean isHero();

	public abstract int getMarginY();

	public abstract void setLayoutParams(ViewGroup.LayoutParams params);

	public abstract void startAnimation(Animation animation);

	public abstract int getTopY();

	public abstract void setVisibility(int visibility);

	public abstract void setAttackBackground();

	public abstract void heal(int amount, boolean sended, Target from,
			String resource);

	public abstract void vitalCheck();

	public abstract Player player();

	public abstract void attackReady();

	public abstract void setByString(String set);

	public abstract String toString();

	public abstract void setAlpha(float alpha);

	public abstract boolean attackedable();

	public abstract int PlayerInfo();

	public abstract int getHeight();

	public abstract int getWidth();

	public abstract void abilityUp(String amount, boolean sended, Target from,
			String res);

	void damageCheck();

	public abstract void setStun(boolean b, Target target, String string);

	public abstract boolean isStunned();

	public abstract void wakeUp(boolean sended);

	public abstract void die();

}