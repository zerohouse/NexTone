package com.mylikenews.nextoneandroid;

import game.Method;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import dek.CardList;
import dek.CardSelected;
import dek.CardinDek;
import dek.Data;
import dek.SelectedCardList;

public class MakeDek extends Activity {

	SelectedCardList selectedcards;
	CardList herocards;
	CardList defaultcards;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_dek);

		TextView dekinfo = (TextView) findViewById(R.id.dekinfo);
		Intent intent = getIntent();
		Data data = (Data) intent.getSerializableExtra("selected");
		
		
		LinearLayout cardslayout = (LinearLayout) findViewById(R.id.defaultcards);
		LinearLayout selected = (LinearLayout) findViewById(R.id.selectedcards);

		herocards = new CardList(MakeDek.this,
				cardslayout, data.getHeroType());
		
		defaultcards = new CardList(MakeDek.this,
				cardslayout, 0);
		defaultcards.show();
		
		
		buttonSetting();
		
		selectedcards = new SelectedCardList(MakeDek.this, selected, dekinfo, data);

		OnClickListener removeSelected = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedcards.remove((CardSelected) v);
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
		herocards.setListenerAll(addSelected);

	}

	private void buttonSetting() {
		Button defaultcard = (Button) findViewById(R.id.defaults);
		Button herocard = (Button) findViewById(R.id.heros);
		defaultcard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				defaultcards.show();
			}
		});
		
		herocard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				herocards.show();
			}
		});
	}
}
