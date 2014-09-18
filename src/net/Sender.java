package net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.bluetooth.BluetoothSocket;

public class Sender extends Thread {

	OutputStream out;
	DataOutputStream dataout;
	boolean bluetooth = false;

	public Sender(Socket sock) {
		try {
			out = sock.getOutputStream();
			dataout = new DataOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Sender(BluetoothSocket socket) {
		try {
			out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bluetooth = true;
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
	public void run(){

	}
	

	public void S(String send) {
		if (bluetooth) {
			try {
				byte[] buffer = send.getBytes();
				out.write(buffer);
			} catch (IOException e) {
			}
		} else {
			try {
				dataout.writeUTF(send);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
