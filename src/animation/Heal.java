package animation;

import game.Method;
import game.Target;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Heal {

	RelativeLayout container;

	@SuppressLint("NewApi")
	public void HealEffect(Target one, Target another, boolean isChecked,
			String resource) { 

		int x = (int) one.getX(one.isHero());
		int toX = (int) another.getX(one.isHero());
 
		int block = 0;
		if (isChecked)
			block = Method.dpToPx(110);

		int origny = (int) one.getMarginY() + block;

		final View object;
		if (resource == null) {
			object = (View) one.cloneForAnimate();
		} else {
			RelativeLayout tmp1 = new RelativeLayout(Helper.context);
			ImageView tmp = new ImageView(Helper.context);
			RelativeLayout.LayoutParams tmpparams = Method.getParams();
			tmp.setLayoutParams(tmpparams);
			tmpparams.addRule(RelativeLayout.CENTER_IN_PARENT);
			tmpparams.height = Method.dpToPx(70);
			tmpparams.width = Method.dpToPx(70);
			tmp.setImageResource(Method.resId(resource));
			tmp1.addView(tmp);

			object = tmp1;
		}
		RelativeLayout.LayoutParams params = Method.getParams();
		object.setLayoutParams(params);
		params.height = Method.dpToPx(70);
		params.width = Method.dpToPx(70);

		params.height = one.getHeight();
		params.width = one.getWidth();

		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		if (container == null) {
			container = new RelativeLayout(Helper.context);
			RelativeLayout.LayoutParams relparams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			container.setLayoutParams(relparams);
			Attack.container.addView(container);
		}
		container.addView(object);

		params.leftMargin = x;
		params.bottomMargin = origny;

		int amountx = toX - x;

		int amounty = another.getTopY() - one.getTopY();

		TranslateAnimation translate = new TranslateAnimation(0, amountx, 0,
				amounty);
		translate.setInterpolator(new AccelerateInterpolator());

		translate.setDuration(600); // duartion in ms
		translate.setFillAfter(false);

		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.3f);
		alpha.setDuration(600);
		// animation1.setStartOffset(5000);
		alpha.setFillAfter(false);

		AnimationSet animations = new AnimationSet(false);// false mean dont
															// share
															// interpolators
		animations.addAnimation(translate);
		animations.addAnimation(alpha);

		object.startAnimation(animations);

		animations.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
 
			@Override
			public void onAnimationEnd(Animation arg0) {
					object.setVisibility(View.INVISIBLE);
			}
		});

	}
}
