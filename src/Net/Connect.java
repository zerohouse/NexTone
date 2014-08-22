package Net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.widget.TextView;

public class Connect extends AsyncTask<Void, Integer, Void> {

	String response;
	Sender sender;
	TextView output;
	
	public Connect(TextView output){
		this.output = output;
	}

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
			output.setText(response);

	}
	
	@Override
	protected void onPostExecute(Void result) {
		output.setText("끝남");
	}

	public void sendMessage(String string) {
		try {
			sender.sendMessage(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
