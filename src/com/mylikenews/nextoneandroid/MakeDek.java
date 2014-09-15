package com.mylikenews.nextoneandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
	ScrollView defaultwrap;
	ScrollView herowrap;
	boolean defaults = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_dek);

		TextView dekinfo = (TextView) findViewById(R.id.dekinfo);
		Intent intent = getIntent();
		Data data = (Data) intent.getSerializableExtra("selected");

		LinearLayout cardslayout = (LinearLayout) findViewById(R.id.defaultcards);
		LinearLayout heros = (LinearLayout) findViewById(R.id.herocards);
		LinearLayout selected = (LinearLayout) findViewById(R.id.selectedcards);
		defaultwrap = (ScrollView) findViewById(R.id.defaultscroll);
		herowrap = (ScrollView) findViewById(R.id.heroscroll);

		defaultcards = new CardList(MakeDek.this, cardslayout, 0);

		herocards = new CardList(MakeDek.this, heros, data.getHeroType());

		buttonSetting();

		selectedcards = new SelectedCardList(MakeDek.this, selected, dekinfo,
				data);

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
				selectedcards.add((CardinDek) v);

			}
		};
		defaultcards.setListenerAll(addSelected);
		herocards.setListenerAll(addSelected);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		defaultcards.setHeight();
		herocards.setHeight();
		if (defaults)
			defaultShow();
		else
			heroShow();
	}

	private void buttonSetting() {
		Button defaultcard = (Button) findViewById(R.id.defaults);
		Button herocard = (Button) findViewById(R.id.heros);
		defaultcard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				defaultShow();
			}

		});

		herocard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				heroShow();
			}

		});
	}

	private void defaultShow() {
		defaultwrap.setVisibility(View.VISIBLE);
		herowrap.setVisibility(View.GONE);
		defaults = true;
	}

	private void heroShow() {
		defaultwrap.setVisibility(View.GONE);
		herowrap.setVisibility(View.VISIBLE);
		defaults = false;
	}
}
