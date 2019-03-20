import java.io.IOException;

public class CheckSuccessors extends Thread {

	private Node node;

	public CheckSuccessors(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started CheckSuccessors process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 500;
		while(true) {
			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
			
		}
	}
}
