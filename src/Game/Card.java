package Game;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Card extends LinearLayout {

	static final int ONCHANGE = 1;
	static final int ONGAME = 2;

	Effect effect;
	Context context;

	int index, cost, attack, defense;
	String name, description;
	TextView vcost, vattack, vdefense, vname, vdescription, vindex;

	int status;
	boolean selected;

	public Card(Context context, String name, String description, int cost,
			int attack, int defense, int index, int status) {
		super(context);
		this.context = context;
		this.status = status;
		selected = false;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);

		ViewGroup.LayoutParams textparams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		this.attack = attack;
		this.cost = cost;
		this.defense = defense;
		this.name = name;
		this.description = description;
		this.index = index;

		vname = initTextView(textparams, name + "");
		vdescription = initTextView(textparams, description + "");
		vcost = initTextView(textparams, cost + "");
		vdefense = initTextView(textparams, defense + "");
		vattack = initTextView(textparams, attack + "");
		vindex = initTextView(textparams, index + "");

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				useCard();
			}
		});
	}

	private TextView initTextView(ViewGroup.LayoutParams textparams,
			String value) {
		TextView text = new TextView(context);
		text.setLayoutParams(textparams);
		text.setText(value);
		addView(text);
		return text;
	}

	public void useCard() {

		switch (status) {
		case ONCHANGE:
			toggleSelect();

			break;

		case ONGAME:
			alert("ONGAME");
			break;
		}

	}

	private void toggleSelect() {
		if (selected == false) {
			this.setBackgroundColor(Color.DKGRAY);
			selected = true;

			alert("선택");
		} else {
			this.setBackgroundColor(Color.WHITE);
			selected = false;

			alert("선택안함");
		}
	}

	public void alert(String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public boolean selected(){
		return selected;
	}
}
