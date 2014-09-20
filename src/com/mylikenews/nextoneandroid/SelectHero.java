package com.mylikenews.nextoneandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import components.IconBinder;

public class SelectHero extends Activity {
	int id;
	Intent intent;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_hero);
		setTitle("NexTone - 영웅 선택");
		ArrayList<IconBinder> heros = new ArrayList<IconBinder>();
		
		IconBinder hero;
		
		hero = new IconBinder(this, "heroheo");
		hero.setText("우주대통령\n미스타허");
		hero.setIconResource(R.drawable.heroheo);
		heros.add(hero);
		
		hero = new IconBinder(this, "herosingha");
		hero.setText("굴다리 전설\n싱하");
		hero.setIconResource(R.drawable.herosingha);
		heros.add(hero);
		
		hero = new IconBinder(this, "heroblue");
		hero.setText("파란");
		hero.setIconResource(R.drawable.heroblue);
		heros.add(hero);
		
		hero = new IconBinder(this, "herocaptain");
		hero.setText("선장");
		hero.setIconResource(R.drawable.herocaptain);
		heros.add(hero);

		

		

		OnClickListener listener;
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.selecthero);
		for (IconBinder s: heros) {
			layout.addView(s);
			intent = new Intent(this, SelectHeroAbility.class);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					IconBinder selected = (IconBinder) v;
					intent.putExtra("resource", selected.resource() );
					finish();
					startActivity(intent);
				}
			};
			s.setOnClickListener(listener);
		}


	}
}
