package com.mylikenews.nextoneandroid;

import game.Method;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import dek.CardList;
import dek.CardListDefault;
import dek.CardinDek;
import dek.Data;

public class MakeDek extends Activity {

	CardList selectedcards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_dek);

		LinearLayout defcards = (LinearLayout) findViewById(R.id.defaultcards);
		LinearLayout selected = (LinearLayout) findViewById(R.id.selectedcards);

		CardListDefault defaultcards = new CardListDefault(MakeDek.this,
				defcards);

		TextView dekinfo = (TextView) findViewById(R.id.dekinfo);

		Intent intent = getIntent();

		Data data = (Data) intent.getSerializableExtra("selected");

		selectedcards = new CardList(MakeDek.this, selected, dekinfo, data);

		OnClickListener removeSelected = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedcards.remove((CardinDek) v);
			}
		};
		selectedcards.setListenerAll(removeSelected);

		OnClickListener addSelected = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selectedcards.add((CardinDek) v)) {
					Method.alert("카드는 두장까지 넣을 수 있습니다.");
					return;
				}
			}
		};
		defaultcards.setListenerAll(addSelected);

	}
}