package com.mylikenews.nextoneandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import dek.Data;
import dek.EachDek;
import dek.Sql;

public class SelectDek extends Activity {

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
					startActivity(intent);
				}
			};
			if (each.getData().getSum() > 15) { // 사용 가능한 덱
				each.setOnClickListener(listener);
				able++;
			} else {
				
				each.setTitleColor(Color.RED);
			}

		}

		if (able == 0) {
			TextView text = new TextView(SelectDek.this);
			text.setGravity(Gravity.CENTER);

			text.setTextAppearance(this, R.style.myText);
			text.setText("\n\n사용가능한 덱이 없습니다.\n\n덱 만들러 가기");
			layout.addView(text);

			OnClickListener selecthero = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent newdek = new Intent(SelectDek.this, DekList.class);
					startActivity(newdek);
				}
			};
			text.setOnClickListener(selecthero);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dek_list);
		layout = (LinearLayout) findViewById(R.id.deklist);
		sql = new Sql(this);
		intent = new Intent(this, GameActivity.class);
	}
}
