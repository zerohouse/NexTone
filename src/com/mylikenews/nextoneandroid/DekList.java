package com.mylikenews.nextoneandroid;

/*
 * 
 * 덱 만들기 메뉴
 * 
 * */

import game.Method;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import dek.Data;
import dek.EachDek;
import dek.Sql;

public class DekList extends Activity {

	Intent intent;
	Data intentdata;
	Sql sql;
	EachDek each;
	LinearLayout layout;
	OnClickListener listener, selecthero;
	ArrayList<Data> deklist;
	static int lastid = 1;

	public static int lastId() {
		return lastid;
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetViews();
	}

	private void resetViews() {
		layout.removeAllViews();
		deklist = sql.dekList();

		if (deklist.size() != 0)
			lastid = deklist.get(deklist.size() - 1).getId();

		for (int i = 0; i < deklist.size(); i++) {
			each = new EachDek(this, deklist.get(i), true);
			layout.addView(each);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EachDek dek = (EachDek) v.getParent();
					sql.delete(dek.getData());
					resetViews();
				}
			};
			each.setRemoveListener(listener);

			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					EachDek dek = (EachDek) v;
					intent.putExtra("selected", dek.getData());
					intent.putExtra("state", "select");
					startActivity(intent);
				}
			};
			each.setOnClickListener(listener);
		}
		makeAddButton();

	}

	private void makeAddButton() {
		TextView text = new TextView(this);
		text.setHeight(Method.dpToPx(50));
		text.setGravity(Gravity.CENTER);
		text.setTextAppearance(this, R.style.myText);
		text.setText("+ 새로운 덱 추가하기");

		layout.addView(text);
		selecthero = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newdek = new Intent(DekList.this, SelectHero.class);
				startActivity(newdek);
			}
		};
		text.setOnClickListener(selecthero);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dek_list);
		layout = (LinearLayout) findViewById(R.id.deklist);
		sql = new Sql(this);

		intent = new Intent(this, MakeDek.class);
	}

}
