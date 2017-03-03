package Main;
import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public abstract class Job implements Serializable{
	public enum Status {
	     CREATED, QUEUED, PROCESSING, FINISHED, FAILED
	}
	
	private Status status = Status.CREATED;
	private UUID id = UUID.randomUUID();
	private Object response = null;
	
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	public Status getStatus(){
		return status;
	}
	public void setStatus(Status status){
		this.status = status;
	}
	
	public abstract Object doWork();
	public UUID getId() {
		return id;
	}
}
