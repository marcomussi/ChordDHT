package Request;

public class SearchRequest extends Request {

	private static final long serialVersionUID = -2665293296102796166L;
	private Long id;
	private Object obj;
	/* we can send the search request thought the nodes and the node
	 * that have the information can cast and fulfill the generic Object */
	
	public SearchRequest(Long id) {
		this.id=id;
		this.obj=null;
	}
	
	public Long getId() {
		return id;
	}

	// maybe not necessary, the constructor can be enouth
	public void setId(Long id) {
		this.id=id;
	}
	
	public void setObject(Object o) {
		this.obj=o;
	}
	
	public Object getObject() {
		return this.obj;
	}
	
}
