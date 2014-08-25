package Game;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Net.Sender;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NetGame extends AsyncTask<Void, Integer, Void> {

	Player player1, player2;
	Context context;
	LinearLayout container;
	String player1dek, player2dek, player2hero;
	boolean first;

	public NetGame(Context context, LinearLayout container) {
		this.context = context;
		this.container = container;
		player1dek = "1x2,2x2,3x26";
	}

	public void Start(boolean first) {

		container.removeAllViews();
		player1 = new Player(context, player1dek, 1);
		player1.setSender(sender);
		if (first) {
			alert("게임을 시작합니다.");
			addPlayerCard(3);
			player1.first();
		} else {
			alert("게임을 시작합니다.");
			addPlayerCard(4);
			player1.second();
		}
		this.first = first;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sender.S("7 1@" + player1.hero.getString());
	}

	public void enemySetting() {

		player2 = new Player(context, player2dek, 2);
		player2.setSender(sender);
		if (first) {
			player2.second();
		} else {
			player2.first();
		}
		container.addView(player2.field());
		player1.setEnemy(player2);
		player2.setEnemy(player1);
	}

	private void addPlayerCard(int size) {
		player1.firstSetting(size);
		container.addView(player1.hand());
		container.addView(player1.field());
	}

	String response;
	Sender sender;

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
			Do(response);
	}

	private void Do(String resString) {
		String[] response = resString.split(" ");
		try {
			int type = Integer.parseInt(response[0]);
			switch (type) {
			case 0:
				if (Integer.parseInt(response[1]) == CONS.FIRST.get())
					Start(true);
				else if (Integer.parseInt(response[1]) == CONS.SECOND.get())
					Start(false);
				break;
			case 3:
				player2dek = response[1];
				enemySetting();

				break;
			case 4:
				if (player1.done()) {
					player1.newTurn();
				} else {
					player1.ChangeToStartTurn();
				}
				break;

			case 7:
				String res[] = response[1].split("@");
				if (res[0].equals("1")) {
					player2.hero.setByString(res[1]);
				} else {
					player1.hero.setByString(res[1]);
				}
				break;

			case 8:
				String[] mon = response[1].split("@");
				if (mon[0].equals("1")) {
					player2.field.add(mon[1]);
				} else {
					player1.field.add(mon[1]);
				}
				break;

			case 9:
				String[] attack = response[1].split(",");
				Target one,
				another;
				if (Integer.parseInt(attack[0]) != -1)
					one = player1.field.get(Integer.parseInt(attack[0]));
				else
					one = player1.field.hero;

				if (Integer.parseInt(attack[1]) != -1)
					another = player2.field.get(Integer.parseInt(attack[1]));
				else
					another = player2.field.hero;
				
					one.attackOrder(another);

				break;

			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		endGame();
	}

	private void endGame() {
		// TODO Auto-generated method stub

	}

	public void alert(String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
