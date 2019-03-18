import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import Request.GetPredecessorRequest;
import Request.GetSuccessorRequest;

public class ManageRequest extends Thread{

	private Socket socket;
	private Node node;
	private InputStream input;
	private OutputStream output;

	public ManageRequest(Node n,Socket s) {
		this.node = n;
		this.socket = s;
	}
	
	@Override
	public void run() {
		try {
			input = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(input);
			process(objectInputStream.readObject());
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String process(Object request) {
		ObjectOutputStream objectOutputStream;
		InetSocketAddress response = null;
		if(request instanceof GetSuccessorRequest) {
			//System.out.println("Sto cercando il successore di questo ID: " + ((GetSuccessorRequest) request).getId());
			response = node.findSuccessor(((GetSuccessorRequest) request).getId());
		}
		if(request instanceof GetPredecessorRequest) {
			response = node.getPredecessorAddr();
		}
		try {
			output = socket.getOutputStream();
			objectOutputStream = new ObjectOutputStream(output);
			objectOutputStream.writeObject(response);
		} catch (IOException e) {	
			e.printStackTrace();
		}
		return null;
	}
}