package com.mylikenews.nextoneandroid;

import game.Method;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Method.setContext(this);
		
		TextView gamestart = (TextView) findViewById(R.id.gamestart);
		TextView makedek = (TextView) findViewById(R.id.selectdek);
		
		OnClickListener startgame = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,SelectDek.class);
				startActivity(intent);
			}
		};
		gamestart.setOnClickListener(startgame);
		
		OnClickListener dekmake = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,DekList.class);
				startActivity(intent);
			}
		};
		makedek.setOnClickListener(dekmake);
		
	}
}
