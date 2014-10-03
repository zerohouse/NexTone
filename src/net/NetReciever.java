package net;

import game.Game;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import android.os.AsyncTask;

public class NetReciever extends AsyncTask<Void, Integer, Void> {

	String response;
	Game game;
	String ip;
	int port;
	private Queue<String> todo = new LinkedList<String>();

	public NetReciever(String ip, int port, Game game) {
		this.ip = ip;
		this.port = port;
		this.game = game;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			Socket socket = new Socket(ip, port);
			Sender sender = new NetSender(socket);
			sender.run();
			Game.sender = sender;

			InputStream in = socket.getInputStream();
			DataInputStream datain = new DataInputStream(in);

			response = "";

			while (!response.equals("end")) {
				response = datain.readUTF();
				todo.add(response);
				publishProgress(1);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onProgressUpdate(Integer... values) {
		if (values[0] == 1) {
			doAll();
		}
	}

	public void doAll() {
		while (true) {
			String s = todo.poll();
			if (s != null) {
				game.Do(s);
			} else {
				return;
			}
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		endGame();
	}

	private void endGame() {

	}
}
