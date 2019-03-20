import java.io.IOException;

public class FixFingers extends Thread {
	
	private Node node;

	public FixFingers(Node n) throws IOException {
		this.node = n;
	}
	
	@Override
	public void run() {
		System.out.println("Started fix fingers process\n" 
				+ node.getNodeAddress() + "\n");
		int sleepTimeMillis = 500;
		while(true) {
			try {
				for (int next = 0; next<32; next++) {
					Thread.sleep(sleepTimeMillis);
					FingerObject currentFingerEntry = node.getFingerTable().get(next);
					Long currentId = (node.getNodeUpperBound() + (long) (Math.pow(2, next))) % (long) Math.pow(2, 32);
					currentFingerEntry.setAddress(node.findSuccessor(currentId));
				}
			} catch (InterruptedException e) {
				//e.printStackTrace();
				
			};
		}
	}
}