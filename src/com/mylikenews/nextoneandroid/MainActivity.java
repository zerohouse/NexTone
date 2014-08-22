package com.mylikenews.nextoneandroid;


import Net.Connect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	Connect connect;
	EditText edittext;
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button buttonConnect = (Button) findViewById(R.id.button2);
		buttonConnect.setOnClickListener(buttonConnectOnClickListener);

		Button buttonSend = (Button) findViewById(R.id.button1);
		buttonSend.setOnClickListener(buttonSendOnClickListener);

		edittext = (EditText) findViewById(R.id.editText1);
		text = (TextView) findViewById(R.id.textView1);
		showMessage("으엥?");

	}

	OnClickListener buttonSendOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			connect.sendMessage(edittext.getText().toString());
			text.setText(edittext.getText().toString());

		}
	};

	OnClickListener buttonConnectOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			connect = new Connect(text);
			connect.execute();
			text.setText("서버에 연결");

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void showMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}

}
