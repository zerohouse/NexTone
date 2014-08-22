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
	String dekstring;
	boolean first;

	public NetGame(Context context, LinearLayout container) {
		this.context = context;
		this.container = container;
		dekstring = "1x2 2x2 3x26";
	}

	public void Start(boolean first) {
		container.removeAllViews();
		player1 = new Player(context, dekstring);
		player1.setSender(sender);
		if (first) {
			addPlayerView(3);
			player1.first();
			player2.second();
		} else {
			addPlayerView(4);
			player1.second();
			player2.first();
		}

		this.first = first;
	}

	private void addPlayerView(int size) {
		player1.firstSetting(size);
		container.addView(player1.hand());
		container.addView(player1.field());
	}

	String response;
	Sender sender;

	@Override
	protected Void doInBackground(Void... params) {
		try {
			Socket socket = new Socket("192.168.0.11", 13333);
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

	private void Do(String response) {
		String[] tmp = response.split(" ");
		try {
			int type = Integer.parseInt(tmp[0]);
			switch (type) {
			case 0:
				if (Integer.parseInt(tmp[1]) == Value.FIRST.get())
					Start(true);
				else if (Integer.parseInt(tmp[1]) == Value.SECOND.get())
					Start(false);
				break;
			case 3:
				player2 = new Player(context, tmp[1]);
				break;
			case 4:
				if (player1.done()) {
					player1.newTurn();
				} else {
					player1.ChangeToStartTurn();
				}
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

	public void sendMessage(String string) {
		try {
			sender.sendMessage(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void alert(String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
