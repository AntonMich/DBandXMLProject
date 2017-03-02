
public class DepModel{

	private String description;
	private String depCode;
	private String depJob;
	
	public DepModel(String depCode, String depJob, String description) {
		super();
		this.depCode = depCode;
		this.depJob = depJob;
		this.description = description;
	}
	
	public DepModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "DepModel [depCode=" + depCode + ", depJob=" + depJob + ", description=" + description + "]";
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((depCode == null) ? 0 : depCode.hashCode());
		result = prime * result + ((depJob == null) ? 0 : depJob.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}


	public boolean equalsKey(Object obj){
		DepModel other = (DepModel) obj;
		if (this.getDepCode().equals(other.getDepCode())&& this.getDepJob().equals(other.getDepJob())){
			return true;
		}
		return false;	
	}
	public boolean equalsFull(Object obj){
		DepModel other = (DepModel) obj;
		if (this.getDepCode().equals(other.getDepCode())
				&& this.getDepJob().equals(other.getDepJob())
				&&this.getDescription().equals(other.getDescription())){
			return true;
		}
		return false;	
	}
	
	

}
