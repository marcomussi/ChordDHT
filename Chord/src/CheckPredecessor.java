import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import Request.CheckPredecessorStatusRequest;

public class CheckPredecessor extends Thread {

	private Node node;
	private Socket socket;
	private OutputStream output;
	private InputStream input;
	private ObjectOutputStream objectOutputStream;
	private CheckPredecessorStatusRequest checkPredReq;
	
	public CheckPredecessor(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started CheckPredecessor process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 2000;
		while(true) {
			System.out.println("My predecessor is " + node.getPredecessorAddr());
			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				socket = new Socket(node.getPredecessorAddr().getAddress(),node.getPredecessorAddr().getPort());
				socket.setSoTimeout(1000);
				output = socket.getOutputStream();
				objectOutputStream = new ObjectOutputStream(output);
				checkPredReq = new CheckPredecessorStatusRequest();
				objectOutputStream.writeObject(checkPredReq);
				input = socket.getInputStream();
				ObjectInputStream objectInputStream = new ObjectInputStream(input);
				InetSocketAddress result = (InetSocketAddress) objectInputStream.readObject();
				if(result == node.getPredecessorAddr())
					continue;
				else
					node.setPredecessorAddr(null);
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				node.setPredecessorAddr(null);
				continue;
			} catch (IOException e) {	
			e.printStackTrace();
			} catch (ClassNotFoundException e) {	
				e.printStackTrace();
			}
		}
	}
	
	
	
}
