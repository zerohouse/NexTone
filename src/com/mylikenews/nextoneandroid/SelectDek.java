package com.mylikenews.nextoneandroid;

/*
 * 
 * 게임 진입시 덱선택하는 액티비티.
 * 
 * */


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import dek.Data;
import dek.EachDek;
import dek.Sql;

public class SelectDek extends Activity {

	Bundle extra;
	Intent intent;
	Data intentdata;
	Sql sql;
	EachDek each;
	LinearLayout layout;
	OnClickListener listener, selecthero;
	ArrayList<Data> deklist;

	@Override
	protected void onResume() {
		super.onResume();
		resetViews();

	}

	private void resetViews() {
		layout.removeAllViews();
		deklist = sql.dekList();

		int able = 0;

		for (int i = 0; i < deklist.size(); i++) {
			each = new EachDek(this, deklist.get(i), false);
			layout.addView(each);

			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EachDek dek = (EachDek) v;
					intent.putExtra("selected", dek.getData());
					intent.putExtra("state", "select");
					setResult(RESULT_OK, intent);
					finish();
				}
			};
			if (each.getData().getSum() > 29) { // 사용 가능한 덱
				each.setOnClickListener(listener);
				able++;
			} else {
				
				each.setTitleColor(Color.RED);
			}

		}

		if (able == 0) {
			showGoToMakeDekDialog();
		}

	}
	
	public void showGoToMakeDekDialog() {
		AlertDialog.Builder areyousure = new AlertDialog.Builder(SelectDek.this);
		
		areyousure.setTitle("사용가능한 덱이 없습니다.");

		// Set up the buttons
		areyousure.setPositiveButton("덱 만들러 가기",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
						Intent newdek = new Intent(SelectDek.this, DekList.class);
						startActivity(newdek);
					}
				});
		areyousure.setNegativeButton("이전 화면으로",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		areyousure.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dek_list);
		extra = new Bundle();
		intent = new Intent();
		layout = (LinearLayout) findViewById(R.id.deklist);
		sql = new Sql(this);
		intent = new Intent(this, GameTcpIp.class);
	}
}
