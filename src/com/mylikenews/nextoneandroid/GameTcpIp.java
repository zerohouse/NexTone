package com.mylikenews.nextoneandroid;

import game.Game;
import game.Method;
import net.NetGame;
import net.Sender;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import animation.HideAndShow;
import dek.Data;

public class GameTcpIp extends Activity {

	LinearLayout container;
	TextView status;
	RelativeLayout animate;
	String herostring, dekstring, ip;
	int port;
	Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 서버의 아이피와 포트를 지정한다.
		ip = "192.168.0.17";
		port = 13333;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game);

		Game.resetStart();

		Method.setContext(this);
		container = (LinearLayout) findViewById(R.id.container);
		animate = (RelativeLayout) findViewById(R.id.animate);

		HideAndShow hideshow = new HideAndShow(container);
		hideshow.animate();
		
		Intent intent = new Intent(GameTcpIp.this, SelectDek.class);
		startActivityForResult(intent, 0);
		
		status = new TextView(this);
		status.setGravity(Gravity.CENTER);
		status.setText("참여자를 기다립니다.");

	}

	// 2.0 and above
	@Override
	public void onBackPressed() {
		if (!Game.isStart()) {
			finish();
			return;
		}
		showAreYouSureDialog();
	}

	// Before 2.0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!Game.isStart()) {
			finish();
			return super.onKeyDown(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAreYouSureDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case 0: 
			if (resultCode == RESULT_OK) { 
				Data data = (Data) intent.getSerializableExtra("selected");
				dekstring = data.getDekstring();
				herostring = data.getHerostring();
				game = new Game(GameTcpIp.this, container, animate,
						dekstring, herostring);
				
				NetGame ngame = new NetGame(ip, port, game);
				ngame.execute();
				container.addView(status);
				return;
			}
			finish();
			break;
		}
	}

	public void showAreYouSureDialog() {
		AlertDialog.Builder areyousure = new AlertDialog.Builder(
				GameTcpIp.this);

		areyousure.setTitle("게임에서 나갑니다.");

		// Set up the buttons
		areyousure.setNegativeButton("포기하고 다음판 할래!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Sender.S("101&"); // 항복
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