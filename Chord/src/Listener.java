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
		while (true) {
			Socket connection = null;
			try {
				connection = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// new thread to manage the single request arrived
			new Thread(new ManageRequest(node,connection)).start();
		}
	}
}
