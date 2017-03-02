// Данный класс не используется
public class DepModelKey{
	private String depCode;
	private String depJob;
	
	public DepModelKey(String depCode, String depJob) {
		super();
		this.depCode = depCode;
		this.depJob = depJob;
	}
	

	@Override
	public String toString() {
		return "DepModelKey [depCode=" + depCode + ", depJob=" + depJob + "]";
	}
	
	
	public String getDepCode() {
		return depCode;
	}
	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}
	public String getDepJob() {
		return depJob;
	}
	public void setDepJob(String depJob) {
		this.depJob = depJob;
	}

	
}
