package com.mylikenews.nextoneandroid;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dek_list);
		LinearLayout layout = (LinearLayout) findViewById(R.id.deklist);
		sql = new Sql(this);
		
		EachDek each;

		OnClickListener listener;
		intent = new Intent(this, MakeDek.class);
		final ArrayList<Data> deklist = sql.dekList();
		for (int i = 0; i < deklist.size(); i++) {
			deklist.get(i).setID(i+1);
			each = new EachDek(this, deklist.get(i).getSummary());
			each.setId(i);
			layout.addView(each);
			listener = new View.OnClickListener() {
				@Override 
				public void onClick(View v) { 
					sql.delete(v.getId());
				}  
			};
			each.setRemoveListener(listener);
			 
			
			listener = new View.OnClickListener() {
				@Override 
				public void onClick(View v) {
					intent.putExtra("selected", deklist.get(v.getId()));
					intent.putExtra("state", "select");
					startActivity(intent);
				} 
			};   
			each.setOnClickListener(listener);
		}
		
		TextView text = new TextView(this);
		text.setGravity(Gravity.CENTER);
		text.setTextAppearance(this, R.style.myText);
		text.setText("+ 새로운 덱 추가하기");
		
		layout.addView(text);
		OnClickListener selecthero = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent newdek = new Intent(DekList.this, SelectHero.class); 
				newdek.putExtra("id", deklist.size()+1);
				startActivity(newdek);
			}  
		};   
		text.setOnClickListener(selecthero);
		
	}
}
