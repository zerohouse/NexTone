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
		heros.add(new IconBinder(this, "heroblue"));
		heros.add(new IconBinder(this, "herocaptain"));
		

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
