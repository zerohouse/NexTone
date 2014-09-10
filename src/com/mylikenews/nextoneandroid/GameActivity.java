package com.mylikenews.nextoneandroid;

import game.Method;
import net.NetGame;
import net.Sender;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import animation.HideAndShow;
import dek.Data;

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
		
		NetGame.resetStart();

		Method.setContext(this);
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

		Data data = (Data) getIntent().getSerializableExtra("selected");
		dekstring = data.getDekstring();
		herostring = data.getHerostring();
		ip = "192.168.0.11";
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
	
	// 2.0 and above
	@Override
	public void onBackPressed() {
		if(!NetGame.isStart()){
			finish();
			return;
		}
		showAreYouSureDialog();
	}

	// Before 2.0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!NetGame.isStart()){
			finish();
			return super.onKeyDown(keyCode, event);
		}
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        showAreYouSureDialog();
	    }
	    return super.onKeyDown(keyCode, event);
	}

	public void showAreYouSureDialog() {
		AlertDialog.Builder areyousure = new AlertDialog.Builder(GameActivity.this);
		
		areyousure.setTitle("게임에서 나갑니다.");

		// Set up the buttons
		areyousure.setNegativeButton("포기하고 다음판 할래!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Sender.S("101 "); //항복
						Sender.close();
						finish();
					}
				});
		areyousure.setPositiveButton("아니 잘못 눌렀어!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		
		areyousure.show();
	}
}
