package net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.bluetooth.BluetoothSocket;

public class Sender {

	static OutputStream out;
	static DataOutputStream dataout;
	static boolean bluetooth = false;

	public Sender(Socket sock) {
		try {
			out = sock.getOutputStream();
			dataout = new DataOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			out.close();
			dataout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void S(String send) {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setBluetoothSocket(BluetoothSocket socket) {
		try {
			out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bluetooth = true;

	}

}
