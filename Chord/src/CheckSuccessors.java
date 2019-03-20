import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import Request.CheckStatusRequest;
import Request.GetSuccListRequest;

public class CheckSuccessors extends Thread {

	private Node node;
	private Socket socket;
	private OutputStream output;
	private InputStream input;
	private ObjectOutputStream objectOutputStream;
	private GetSuccListRequest getSuccListReq;
	private CheckStatusRequest checkStatusRequest;
	
	public CheckSuccessors(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started CheckSuccessor process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 2000;
		while(true) {
			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				socket = new Socket(node.getSuccessorAddress().getAddress(),node.getSuccessorAddress().getPort());
				socket.setSoTimeout(1000);
				output = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(output);
				checkStatusRequest = new CheckStatusRequest();
				objectOutputStream.writeObject(checkStatusRequest);
				input = socket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(input);
				objectInputStream.readObject();
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				InetSocketAddress newSuccessor = node.getSuccList().get(1);
				node.getFingerTable().get(0).setAddress(newSuccessor);
				continue;
			} catch (IOException e) {	
			e.printStackTrace();
			} catch (ClassNotFoundException e) {	
				e.printStackTrace();
			}
		}
	}
	
	
	
}
