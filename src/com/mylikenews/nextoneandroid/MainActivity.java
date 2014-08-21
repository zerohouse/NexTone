package com.mylikenews.nextoneandroid;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
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

	Sender sender;
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
			try {
				sender.sendMessage(edittext.getText().toString());
				text.setText(edittext.getText().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	OnClickListener buttonConnectOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Connect connect = new Connect();
			connect.execute();
			text.setText("서버에 연결");

		}
	};

	public class Connect extends AsyncTask<Void, Integer, Void> {

		String response;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Socket socket = new Socket("192.168.0.17", 13333);
				sender = new Sender(socket);
				InputStream in = socket.getInputStream();
				DataInputStream datain = new DataInputStream(in);
				
				response = "";
				while (!response.equals("end")) {
					publishProgress(1);
					response = datain.readUTF();
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onProgressUpdate(Integer... values) {
			if (values[0] == 1)
				text.setText(response);

		}

		@Override
		protected void onPostExecute(Void result) {
			text.setText("끝남");
		}
	}

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
