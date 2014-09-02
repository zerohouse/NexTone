package com.mylikenews.nextoneandroid;

import net.NetGame;
import game.Method;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import animation.HideAndShow;

public class GameActivity extends Activity {

	LinearLayout container;
	TextView status;
	RelativeLayout animate;
	String herostring, dekstring, ip;
	int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game);

		Method.context = this;
		container = (LinearLayout) findViewById(R.id.container);
		animate = (RelativeLayout) findViewById(R.id.animate);

		HideAndShow hideshow = new HideAndShow(container);
		hideshow.animate();

		Button connect = new Button(this);
		connect.setText("Game Start!");
		connect.setOnClickListener(doconnect);
		container.addView(connect);
		status = new TextView(this);
		status.setGravity(Gravity.CENTER);
		status.setText("참여자를 기다립니다.");

		
		dekstring = "1x2,2x2,3x26";
		herostring = "heroblue,1";
		ip = "10.73.43.233";
		port = 13333;
	}

	OnClickListener doconnect = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			NetGame ngame = new NetGame(GameActivity.this, container, animate,
					ip, port, dekstring, herostring);
			
			ngame.execute();
			container.removeView(v);
			container.addView(status);
		}
	};

}
