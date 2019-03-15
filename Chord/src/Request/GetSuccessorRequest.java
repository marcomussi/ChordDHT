package Request;

public class GetSuccessorRequest extends Request {
	
	private static final long serialVersionUID = -1569873232632944010L;
	private Long id;
	
	public GetSuccessorRequest(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
