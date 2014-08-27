package com.mylikenews.nextoneandroid;


import game.Method;
import game.NetGame;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	LinearLayout container;
	TextView status; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game);

		Method.context = this;
		container = (LinearLayout) findViewById(R.id.container);
		Button connect = new Button(this);
		connect.setText("Game Start!");
		connect.setOnClickListener(doconnect);
		container.addView(connect);
		status = new TextView(this);
		status.setText("참여자를 기다립니다.");
		
	}

	OnClickListener doconnect = new OnClickListener() {
		@Override
		public void onClick(View v) {
			NetGame ngame = new NetGame(MainActivity.this, container, "10.73.43.102", 13333);
			ngame.execute();
			container.removeView(v);
			container.addView(status);
		}
	};


}
