package com.mylikenews.nextoneandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
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
			if (each.getData().getSum() > 15)
				each.setOnClickListener(listener);
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
