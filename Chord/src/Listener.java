import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {
	
	private Node node;
	private ServerSocket serverSocket;
	
	public Listener(Node n) throws IOException {
		node = n;
		serverSocket = new ServerSocket(n.getNodeAddress().getPort());
	}
	
	@Override
	public void run() {
		System.out.println("Started listener process\n" 
				+ node.getNodeAddress() + "\n");
		while (true) {
			Socket connection = null;
			try {
				connection = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// New thread to manage the single request arrived
			new Thread(new ManageRequest(node,connection)).start();
		}
	}
}
