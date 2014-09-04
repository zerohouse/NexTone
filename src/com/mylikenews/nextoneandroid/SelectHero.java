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

public class SelectHero extends Activity {

	Intent intent;
	int id;
	Sql sql;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_hero);

		ArrayList<TextView> heros = new ArrayList<TextView>();
		TextView hero;
		sql = new Sql(this);

		OnClickListener listener;
		Intent get = getIntent();
		id = get.getExtras().getInt("id");
		LinearLayout layout = (LinearLayout) findViewById(R.id.selecthero);
		for (int i = 0; i < 9; i++) {
			hero = new TextView(this);
			hero.setGravity(Gravity.CENTER);
			hero.setText(selectedHeroName(i));
			hero.setTextAppearance(SelectHero.this, R.style.myText);
			heros.add(hero);
			layout.addView(hero);
			hero.setId(i);
			intent = new Intent(this, MakeDek.class);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Data data = new Data(id, "heroblue,"+v.getId(), "");  //히어로 리소스 부분 수정해야댐..
					sql.insert(data);
					intent.putExtra("selected", data);
					finish();
					startActivity(intent);
				}
			};
			hero.setOnClickListener(listener);
		}

	}

	public static String selectedHeroName(int i) {
		String hero = "";
		switch (i) {
		case 0:
			hero = "[주술사]";
			break;
		case 1:
			hero = "[마법사]";
			break;
		case 2:
			hero = "[사제]";
			break;
		case 3:
			hero = "[도적]";
			break;
		case 4:
			hero = "[전사]";
			break;
		case 5:
			hero = "[흑마법사]";
			break;
		case 6:
			hero = "[사냥꾼]";
			break;
		case 7:
			hero = "[성기사]";
			break;
		case 8:
			hero = "[드루이드]";
			break;
		}
		return hero;
	}
}
