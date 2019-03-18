package Request;

import java.net.InetSocketAddress;

public class NotifyRequest extends Request {
	
	private static final long serialVersionUID = 6895534325605549901L;
	private InetSocketAddress address;
	
	public NotifyRequest(InetSocketAddress address) {
		this.address = address;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}
		
}
