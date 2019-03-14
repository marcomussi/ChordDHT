import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import Request.GetSuccessorRequest;

public class ManageRequest extends Thread{

	private Socket socket;
	private Node node;

	public ManageRequest(Node n,Socket s) {
		this.node = n;
		this.socket = s;
	}
	
	@Override
	public void run() {
		String req;
		String line = null;
		InputStream input;
		OutputStream output;
		
		try {
			input = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(input);
			process(objectInputStream.readObject());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String process(Object request) {
		OutputStream output;
		ObjectOutputStream objectOutputStream;
		if(request instanceof GetSuccessorRequest)
		{
			InetSocketAddress response = node.findSuccessor(((GetSuccessorRequest) request).getId());
			try {
				output = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(output);
				objectOutputStream.writeObject(response);
			} catch (IOException e) {	
				e.printStackTrace();
			}
		}
		return null;
	}
}