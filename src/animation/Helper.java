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

	static Context context;
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

	public static void setHelper(Context con) {
		context = con;
		helper = new RelativeLayout(con);
		params = Method.getParams();
		helper.setLayoutParams(params);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.width = Method.dpToPx(140);
		params.height = Method.dpToPx(180);

		RelativeLayout layout = new RelativeLayout(con);

		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(param);
		Attack.container.addView(layout);
		layout.addView(helper);

		HideAndShow animate = new HideAndShow(layout);
		animate.animateForHelper();

		helper.setVisibility(View.INVISIBLE);

		character = new ImageView(con);
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

		cost = new TextView(con);
		cost.setTextAppearance(con, R.style.myText);
		cost.setLayoutParams(costparams);
		costparams.leftMargin = horizontalmargin;
		costparams.topMargin = verticalmargin;
		// costparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		vital = new TextView(con);
		vital.setTextAppearance(con, R.style.myText);
		vital.setLayoutParams(vitalparams);

		vitalparams.rightMargin = horizontalmargin;
		vitalparams.bottomMargin = verticalmargin;
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		vitalparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		attack = new TextView(con);
		attack.setTextAppearance(con, R.style.myText);
		attack.setLayoutParams(attackparams);

		attackparams.leftMargin = horizontalmargin;
		attackparams.bottomMargin = verticalmargin;
		attackparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		description = new TextView(con);
		description.setTextAppearance(con, R.style.myBtnText);
		description.setBackgroundColor(Color.argb(200, 200, 200, 200));
		description.setLayoutParams(descriptionparams);
		descriptionparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		descriptionparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		descriptionparams.bottomMargin = Method.dpToPx(30);
		description.setId(1);

		name = new TextView(con);
		name.setTextAppearance(con, R.style.myText);
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
		if (card.hasMonster()) {
			helper.setBackgroundResource(R.drawable.monstercard);
		} else {
			helper.setBackgroundResource(R.drawable.spellcard);
		}
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
