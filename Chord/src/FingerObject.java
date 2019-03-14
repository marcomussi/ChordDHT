import java.net.InetSocketAddress;

public class FingerObject {

	private InetSocketAddress addr;
	private Long upperBound;
	
	public FingerObject(InetSocketAddress a, Long up) {
		this.addr = a;
		this.upperBound = up;
	}
	
	public FingerObject() {
		this.addr = null;
		this.upperBound = null;
	}

	public InetSocketAddress getAddress() {
		return addr;
	}

	public Long getIntervalUpperbound() {
		return upperBound;
	}

	public void setAddress(InetSocketAddress addr) {
		this.addr = addr;
	}
	
}
