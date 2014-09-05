package com.mylikenews.nextoneandroid;

import game.Method;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectHero extends Activity {
	int id;
	Intent intent;

	public class HeroText extends TextView{
		
		String resource;

		HeroText(Context context, String resource) {
			super(context);
			this.resource = resource;
			setGravity(Gravity.CENTER);
			setBackgroundResource(Method.resId(resource));
			setText(resource);
			setTextAppearance(SelectHero.this, R.style.myText);
			 
		}

		public String resource() {
			return resource;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_hero);
		
		ArrayList<HeroText> heros = new ArrayList<HeroText>();
		heros.add(new HeroText(this, "heroblue"));
		heros.add(new HeroText(this, "herocaptain"));
		

		OnClickListener listener;
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.selecthero);
		for (HeroText s: heros) {
			layout.addView(s);
			intent = new Intent(this, SelectHeroAbility.class);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					HeroText selected = (HeroText) v;
					intent.putExtra("resource", selected.resource() );
					finish();
					startActivity(intent);
				}
			};
			s.setOnClickListener(listener);
		}


	}
}
