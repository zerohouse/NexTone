package animation;

import game.Method;
import game.Target;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class Heal {


	@SuppressLint("NewApi")
	public static void HealEffect(Target one, Target another,
			boolean isChecked) {

		int x = (int) one.getX(one.isHero());
		int toX = (int) another.getX(one.isHero());

		int block = 0;
		if (isChecked)
			block = Method.dpToPx(110);

		int origny = (int) one.getMarginY() + block;

		final Target clone = one.cloneForAnimate();
		Attack.container.addView((View) clone);

		clone.setAlpha((float) 0.5);
		clone.getParams().leftMargin = x;
		clone.getParams().bottomMargin = origny;

		int amountx = toX - x;

		int amounty = another.getTopY() - one.getTopY();

		TranslateAnimation animation = new TranslateAnimation(0, amountx, 0,
				amounty);
		animation.setInterpolator(new AccelerateInterpolator());

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

				Attack.container.removeView((View) clone);
			}
		});

	}


	@SuppressLint("NewApi")
	public static void HealedEffect(Target one, Target another) {
		int x = (int) one.getX(one.isHero());
		int toX = (int) another.getX(one.isHero());

		int origny = (int) one.getMarginY();

		final Target clone = one.cloneForAnimate();
		Attack.container.addView((View) clone);

		clone.setAlpha((float) 0.5);
		clone.getParams().leftMargin = x;
		clone.getParams().bottomMargin = origny;

		int amountx = x - toX;

		int amounty = another.getTopY() - one.getTopY();

		TranslateAnimation animation = new TranslateAnimation(0, amountx, 0,
				amounty);
		animation.setInterpolator(new AccelerateInterpolator());

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

				Attack.container.removeView((View) clone);
			}
		});
	}

}
