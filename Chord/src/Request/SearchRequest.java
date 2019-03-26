package Request;

public class SearchRequest extends Request {

	private static final long serialVersionUID = -2665293296102796166L;
	private Long key;
	private Object obj;
	/* We can send the search request thought the nodes and the node
	 * that have the information can cast and fulfill the generic Object */
	
	public SearchRequest(Long key) {
		this.key=key;
		this.obj=null;
	}
	
	public Long getKey() {
		return key;
	}

	// Maybe not necessary, the constructor can be enouth
	public void setKey(Long key) {
		this.key=key;
	}
	
	public void setObject(Object o) {
		this.obj=o;
	}
	
	public Object getObject() {
		return this.obj;
	}
	
}
