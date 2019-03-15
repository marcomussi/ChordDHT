public class Stabilize extends Thread {
	
	private Node node;
	
	public Stabilize(Node n) {
		this.node = n;
	}
	
	@Override
	public void run() {
		int sleepTimeMillis = 5000;
		while(true) {
			
			try {
				Thread.sleep(sleepTimeMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
		}
	}
}