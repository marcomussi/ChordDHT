import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

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
			if (input != null) {
				BufferedReader bufReader = 
						new BufferedReader(new InputStreamReader(input));
				line = null;
				try {
					line = bufReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					req = null;
				}
			}
			req = line;
			String response = process(req);
			if (response != null) {
				output = socket.getOutputStream();
				output.write(response.getBytes());
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String process(String request) {
		return null;
	}
}