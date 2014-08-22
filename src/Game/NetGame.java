package Game;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import Net.Sender;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RelativeLayout;

public class NetGame extends AsyncTask<Void, Integer, Void>  {

	Player player1, player2;
	Context context;
	RelativeLayout container;
	String dekstring;

	public NetGame(Context context, RelativeLayout container) {
		this.context = context;
		this.container = container;
		dekstring = "1x2 2x2 3x26";
	}
	
	public void Start(){
		player1 = new Player(context, container, dekstring);
		player1.firstSetting(5);
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
		int type = Integer.parseInt(tmp[0]);
		switch(type){
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
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
	

}

