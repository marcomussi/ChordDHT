import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
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
	private CheckStatusRequest checkStatusRequest;
	
	public CheckSuccessors(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started CheckSuccessor process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 500;
		while(true) {
			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				// If I'm the successor of myself, it's not useful to check
				if (node.getSuccessorAddress().equals(node.getNodeAddress()))
					continue;
				socket = new Socket(node.getSuccessorAddress().getAddress(),node.getSuccessorAddress().getPort());
				socket.setSoTimeout(1000);
				output = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(output);
				checkStatusRequest = new CheckStatusRequest();
				objectOutputStream.writeObject(checkStatusRequest);
				input = socket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(input);
				objectInputStream.readObject();
			} catch (SocketTimeoutException|ConnectException e ) {
				System.out.println("A problem occurs while contacting the successor. A new successor will be assigned soon.");
				node.getSuccList().remove(0);
				// If the successor list is empty, then I'm my own successor
				if (node.getSuccList().isEmpty())
					node.getFingerTable().get(0).setAddress(node.getNodeAddress());
				else {
					InetSocketAddress newSuccessor = node.getSuccList().get(0);
					node.getFingerTable().get(0).setAddress(newSuccessor);
				}
			} catch (IOException e) {	
			e.printStackTrace();
			} catch (ClassNotFoundException e) {	
				e.printStackTrace();
			}
		}
	}
	
	
	
}
