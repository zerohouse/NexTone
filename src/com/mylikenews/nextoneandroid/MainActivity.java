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

		final TextView gamestart = (TextView) findViewById(R.id.gamestart);
		TextView makedek = (TextView) findViewById(R.id.selectdek);
		final TextView bluegame = (TextView) findViewById(R.id.bluetoothgame);
		final TextView tcpgame = (TextView) findViewById(R.id.tcpgame);

		gamestart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gamestart.setVisibility(View.GONE);
				tcpgame.setVisibility(View.VISIBLE);
				bluegame.setVisibility(View.VISIBLE);
			}
		});
		bluegame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GameBluetooth.class);
				startActivity(intent);
			}
		});
		tcpgame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						GameTcpIp.class);
				startActivity(intent);
			}
		});
		
		
		

		OnClickListener dekmake = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DekList.class);
				startActivity(intent);
			}
		};
		makedek.setOnClickListener(dekmake);

	}
}
