package pack;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
/**
 * Создать java приложение, выполняющее две основные функции:
1) выгрузка содержимого таблицы БД в XML файл;
2) синхронизация содержимого таблицы БД по заданному XML файлу.
 * 
 * @author AntonM
 * @version 0.2.3
 * 
 * <p>
 * SyncAndValidation - класс для записи в БД
 * </p>
 * <p>
 * Коллекция notInsertDepModel для значений, которые !НЕ! будут добавляться из БД
 * Коллекция updateDepModel для значений, который будут обновлены
 * Коллекция deleteDepModel для значений, которые будут удалены
 * </p>
 * 
 * */
public class SyncAndValidation {

	final static Logger logger = Logger.getLogger(SyncAndValidation.class);
	private Set<DepModel> notInsertDepModel = new HashSet<DepModel>();
	private Set<DepModel> updateDepModel = new HashSet<DepModel>();
	private Set<DepModel> deleteDepModel = new HashSet<DepModel>();
	
	/**
	 * Метод для создания коллекций на изменение, обновление и удаление данных из БД
	 * @param db Коллекция сущностей из БД 
	 * @param xml Коллекция сущностей из XML
	 * */
	private void createCollectionForSQL(Set<DepModel> xml, Set<DepModel> db) {
		//System.out.println(xml.size() + "  test");
		//Пробегаемся по коллекциям
		for (DepModel modelDB : db) {
		// триггер для коллекции на удаление
			boolean delete = true;

			for (DepModel modelXML : xml) {
				//System.out.println("DB " + modelDB + '\n' + "XML   " + modelXML);
				//Если совпал составной ключ, но не совпал description - добавляем сущность из xml
				//в коллекцию на обновление и в коллекцию на !НЕ добавление
				if (modelDB.equalsKey(modelXML) && !(modelDB.getDescription().equals(modelXML.getDescription()))) {
					updateDepModel.add(modelXML);
					notInsertDepModel.add(modelXML);
					//переключаем триггер
					delete = false;
					break;
					//Если совпал составной ключ и совпал description - добавляем сущность из xml
					//коллекцию на !НЕ добавление и переключаем триггер
				} else if (modelDB.equalsKey(modelXML)
						&& (modelDB.getDescription().equals(modelXML.getDescription()))) {
					delete = false;
					System.out.println("FULL OK");
					notInsertDepModel.add(modelXML);
				}
			}
			// Если триггер true - обавляем сущность из бд
			//коллекцию на удаление
			if (delete) {
				deleteDepModel.add(modelDB);
			}
			xml.removeAll(notInsertDepModel);
		}

	}

//	private void printColl() {
//		
//		System.out.println("to add!");
//		for (DepModel modelXML : ModelFromSomeRes.modelsFromXML) {
//			System.out.println(modelXML);
//		}
//		System.out.println("to update!");
//		for (DepModel modelUp : updateDepModel) {
//			System.out.println(modelUp);
//		}
//		System.out.println("to delete!");
//		for (DepModel modelDel : deleteDepModel) {
//			System.out.println(modelDel);
//		}
//
//	}
	/** Метод для создания коллекций на изменение, обновление и удаление данных из БД
	 * @param conn Подключение к БД
	 * @param db Коллекция сущностей из БД 
	 * @param xml Коллекция сущностей из XML
	 * */
	public void SyncSQLQuery(Connection conn, Set<DepModel> xml, Set<DepModel> db) {
		//printColl();
		//Вызываем метод createCollectionForSQL
		createCollectionForSQL(xml, db);
		PreparedStatement pStmt;
		try{
			boolean oldAutoCommit=conn.getAutoCommit();
	        conn.setAutoCommit(false);
			try {
				//По коллекции xml добавляем записи в БД
				for (DepModel modelXML : xml) {
					pStmt = conn.prepareStatement("INSERT INTO DepTable(DepCode, DepJob, Description) VALUES (?,?,?)");
					pStmt.setString(1, modelXML.getDepCode());
					pStmt.setString(2, modelXML.getDepJob());
					pStmt.setString(3, modelXML.getDescription());
					pStmt.executeUpdate();
					logger.info("Insert record " + modelXML);
				}
				// System.out.println("to update!");
				//По коллекции updateDepModel обновляем записи в БД
				for (DepModel modelUp : updateDepModel) {
					pStmt = conn.prepareStatement("UPDATE DepTable SET Description=? WHERE DepCode=? AND DepJob=?");
					pStmt.setString(1, modelUp.getDescription());
					pStmt.setString(2, modelUp.getDepCode());
					pStmt.setString(3, modelUp.getDepJob());
					pStmt.executeUpdate();
					logger.info("Update record " + modelUp);
					// System.out.println(modelUp);
				}
				//По коллекции deleteDepModel удаляем записи в БД
				for (DepModel modelDel : deleteDepModel) {
					pStmt = conn.prepareStatement("DELETE FROM DepTable WHERE DepCode=? AND DepJob=?");
					pStmt.setString(1, modelDel.getDepCode());
					pStmt.setString(2, modelDel.getDepJob());
					pStmt.executeUpdate();
					logger.info("Delete record " + modelDel);
				}
				//Если нет ошибок - коммитим
				conn.commit();
				System.out.println(
						"Добавлено записей - " + xml.size() + "  Обновлено записей  - "
								+ updateDepModel.size() + "  Удалено записей   -  " + deleteDepModel.size());
				logger.info("Commit DB");
			} catch (SQLException e) {
				//Если произошли ошибки - откатываем состояние БД
				conn.rollback();
				System.out.println("Rollback DB");
				logger.info("rollback DB");
			}
			finally{
				conn.setAutoCommit(oldAutoCommit);
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.error("ROLLBACK OR COMMIT ERROR"+e1);
		}
	}

}
