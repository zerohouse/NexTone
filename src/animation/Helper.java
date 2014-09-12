package animation;

import game.Method;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class Helper {

	static RelativeLayout helper;
	static RelativeLayout.LayoutParams params, costparams, vitalparams,
			attackparams, descriptionparams, nameparams;
	static TextView cost, vital, attack, description, name;
	static ImageView character;
	static Handler handler = new Handler();
	static Runnable stop = new Runnable() {
		@Override
		public void run() {
			hideInfo();
		}
	};

	public static void setHelper(Context context) {
		helper = new RelativeLayout(context);
		params = Method.getParams();
		helper.setLayoutParams(params);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.width = Method.dpToPx(140);
		params.height = Method.dpToPx(180);

		RelativeLayout layout = new RelativeLayout(context);

		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(param);
		Attack.container.addView(layout);
		layout.addView(helper);
		HideAndShow animate = new HideAndShow(layout);
		animate.animateForHelper();

		helper.setVisibility(View.INVISIBLE);

		character = new ImageView(context);
		RelativeLayout.LayoutParams charparams = Method.getParams();
		character.setLayoutParams(charparams);
		charparams.addRule(RelativeLayout.CENTER_IN_PARENT);

		costparams = Method.getParams();
		vitalparams = Method.getParams();
		attackparams = Method.getParams();
		descriptionparams = Method.getParams();
		nameparams = Method.getParams();

		int horizontalmargin = Method.dpToPx(8);
		int verticalmargin = Method.dpToPx(5);

		cost = new TextView(context);
		cost.setTextAppearance(context, R.style.myText);
		cost.setLayoutParams(costparams);
		costparams.rightMargin = horizontalmargin;
		costparams.topMargin = verticalmargin;
		costparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		vital = new TextView(context);
		vital.setTextAppearance(context, R.style.myText);
		vital.setLayoutParams(vitalparams);

		vitalparams.rightMargin = horizontalmargin;
		vitalparams.bottomMargin = verticalmargin;
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		attack = new TextView(context);
		attack.setTextAppearance(context, R.style.myText);
		attack.setLayoutParams(attackparams);

		attackparams.leftMargin = horizontalmargin;
		attackparams.bottomMargin = verticalmargin;
		attackparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		description = new TextView(context);
		description.setTextAppearance(context, R.style.myBtnText);
		description.setBackgroundColor(Color.argb(200, 200, 200, 200));
		description.setLayoutParams(descriptionparams);
		descriptionparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		descriptionparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		descriptionparams.bottomMargin = Method.dpToPx(30);
		description.setId(1);

		name = new TextView(context);
		name.setTextAppearance(context, R.style.myText);
		name.setLayoutParams(nameparams);
		name.setBackgroundColor(Color.BLACK);
		nameparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		nameparams.addRule(RelativeLayout.ABOVE, description.getId());

		helper.addView(character);
		helper.addView(name);
		helper.addView(description);
		helper.addView(cost);
		helper.addView(vital);
		helper.addView(attack);
		helper.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
			}
		});

	}

	public static void showInfo(Ani card) {
		character.setBackgroundResource(Method.resId(card.getResource()));
		helper.setVisibility(View.VISIBLE);
		helper.setBackgroundResource(Method.resId(card.getResource() + "c"));
		name.setText(card.getName());
		description.setText(card.getDescription());
		description.setGravity(Gravity.CENTER);
		cost.setText(card.getCost() + "");
		vital.setText(card.getVital() + "");
		attack.setText(card.getAttack() + "");

		handler.removeCallbacks(stop);
		handler.postDelayed(stop, 3000);
	}

	public static void hideInfo() {
		helper.setVisibility(View.INVISIBLE);
	}
}
