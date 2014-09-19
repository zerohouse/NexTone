package net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

public class NetSender extends Thread implements Sender {

	OutputStream out;
	DataOutputStream dataout;

	public NetSender(Socket sock) {
		try {
			out = sock.getOutputStream();
			dataout = new DataOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			out.close();
			dataout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

	}

	public void S(String send) {
		Log.i("try", send);

		try {
			dataout.writeUTF(send);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
