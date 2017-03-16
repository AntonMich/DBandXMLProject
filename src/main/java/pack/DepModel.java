package pack;
/**
 * Создать java приложение, выполняющее две основные функции:
1) выгрузка содержимого таблицы БД в XML файл;
2) синхронизация содержимого таблицы БД по заданному XML файлу.
 * 
 * @author AntonM
 * @version 0.2.3
 * 
 * <p>
 * DepModel - класс для описания сущности 
 * </p>
 * 
 * */
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

	/**
	 * Сравнивает обьекты класса DepModel по составному ключу DepCode и DepJob
	 * @param obj - обьект каст - DepModel
	 * @return true/false
	*/
	public boolean equalsKey(Object obj){
		DepModel other = (DepModel) obj;
		if (this.getDepCode().equals(other.getDepCode())&& this.getDepJob().equals(other.getDepJob())){
			return true;
		}
		return false;	
	}
	/**
	 * Сравнивает обьекты класса DepModel по всем свойствам
	 * @param obj - обьект каст - DepModel
	 * @return true/false
	*/
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
