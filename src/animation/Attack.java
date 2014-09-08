package animation;

import game.Method;
import game.Target;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class Attack {

	static RelativeLayout container;
	static Target onesave;

	@SuppressLint("NewApi")
	public static void AttackEffect(Target one, Target another,
			boolean isChecked) {

		onesave = one;
		int x = (int) one.getX(one.isHero());
		int toX = (int) another.getX(one.isHero());

		int block = 0;
		if (isChecked)
			block = Method.dpToPx(110);

		int origny = (int) one.getMarginY() + block;

		final Target clone = one.cloneForAnimate();
		container.addView((View) clone);

		one.setAlpha((float) 0.5);
		clone.getParams().leftMargin = x;
		clone.getParams().bottomMargin = origny;

		int amountx = toX - x;

		int amounty = another.getTopY() - one.getTopY();

		TranslateAnimation animation = new TranslateAnimation(0, amountx, 0,
				amounty);
		animation.setInterpolator(new AccelerateInterpolator());

		animation.setRepeatCount(1);
		animation.setRepeatMode(ValueAnimator.REVERSE);

		animation.setDuration(500); // duartion in ms
		animation.setFillAfter(false);
		clone.startAnimation(animation);

		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {

				onesave.setAlpha((float) 1);
				container.removeView((View) clone);
			}
		});

	}

	public static void setAnimate(RelativeLayout animate) {
		container = animate;
		
	}

	@SuppressLint("NewApi")
	public static void AttackedEffect(Target one, Target another) {
		onesave = one;
		int x = (int) one.getX(one.isHero());
		int toX = (int) another.getX(one.isHero());

		int origny = (int) one.getMarginY();

		final Target clone = one.cloneForAnimate();
		container.addView((View) clone);

		one.setAlpha((float) 0.5);
		clone.getParams().leftMargin = x;
		clone.getParams().bottomMargin = origny;

		int amountx = x - toX;

		int amounty = another.getTopY() - one.getTopY();

		TranslateAnimation animation = new TranslateAnimation(0, amountx, 0,
				amounty);
		animation.setInterpolator(new AccelerateInterpolator());

		animation.setRepeatCount(1);
		animation.setRepeatMode(ValueAnimator.REVERSE);

		animation.setDuration(500); // duartion in ms
		animation.setFillAfter(false);
		clone.startAnimation(animation);

		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {

				onesave.setAlpha((float) 1);
				container.removeView((View) clone);
			}
		});
	}

}
