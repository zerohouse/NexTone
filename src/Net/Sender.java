package Net;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class Sender {

	Socket socket;
	OutputStream out;
	DataOutputStream dataout;
	
	public Sender(Socket socket){
		this.socket = socket;
		try {
			out = socket.getOutputStream();
			dataout = new DataOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String send) throws IOException{
		dataout.writeUTF(send);
	}

	
}
