package com.mylikenews.nextoneandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import dek.Data;
import dek.Sql;

public class SelectHeroAbility extends Activity {

	Intent intent;
	Sql sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_hero);

		ArrayList<TextView> heros = new ArrayList<TextView>();
		TextView hero;
		sql = new Sql(this);

		OnClickListener listener;

		LinearLayout layout = (LinearLayout) findViewById(R.id.selecthero);
		for (int i = 0; i < 9; i++) {
			hero = new TextView(this);
			hero.setGravity(Gravity.CENTER);
			hero.setText(selectedHeroType(i));
			hero.setTextAppearance(SelectHeroAbility.this, R.style.myText);
			heros.add(hero);
			layout.addView(hero);
			hero.setId(i);
			intent = new Intent(this, MakeDek.class);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Data data = new Data(DekList.LastId() + 1, getIntent()
							.getExtras().getString("resource")
							+ ","
							+ v.getId(), ""); // 히어로 리소스 부분 수정해야댐..
					intent.putExtra("selected", data);
					sql.insert(data);
					finish();
					startActivity(intent);
				}
			};
			hero.setOnClickListener(listener);
		}

	}

	public static String selectedHeroType(int i) {
		String hero = "";
		switch (i) {
		case 0:
			hero = "[토템소환]";
			break;
		case 1:
			hero = "[피해(1)]";
			break;
		case 2:
			hero = "[치유(2)]";
			break;
		case 3:
			hero = "[무기장착(1/2)]";
			break;
		case 4:
			hero = "[방어(2)]";
			break;
		case 5:
			hero = "[생명력-2/카드뽑기]";
			break;
		case 6:
			hero = "[적영웅피해(2)]";
			break;
		case 7:
			hero = "[1/1도깨비소환]";
			break;
		case 8:
			hero = "[방어력1/공격력1]";
			break;
		}
		return hero;
	}
}
