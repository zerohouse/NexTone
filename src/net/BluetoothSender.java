package net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import android.bluetooth.BluetoothSocket;

public class BluetoothSender extends Thread implements Sender {

	OutputStream out;
	DataOutputStream dataout;
	private Queue<String> todo = new LinkedList<String>();

	public BluetoothSender(BluetoothSocket socket) {
		try {
			out = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread send = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (todo.size() != 0) {
						String s = todo.poll();
						if (s != null) {
							try {
								byte[] buffer = s.getBytes();
								out.write(buffer);
								Thread.sleep(500);
							} catch (IOException e) {
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		send.start();
	}

	public void close() {
		try {
			out.close();
			dataout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

	}

	public void S(String send) {
		todo.add(send);
	}

}
