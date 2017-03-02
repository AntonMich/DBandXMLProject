import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class SyncAndValidation {

	final static Logger logger = Logger.getLogger(SyncAndValidation.class);
	private static Set<DepModel> notInsertDepModel = new HashSet<DepModel>();
	private static Set<DepModel> updateDepModel = new HashSet<DepModel>();
	private static Set<DepModel> deleteDepModel = new HashSet<DepModel>();

	private static void createCollectionForSQL() {
		System.out.println(ModelFromSomeRes.modelsFromXML.size() + "  test");
		for (DepModel modelDB : ModelFromSomeRes.modelsFromDB) {
			boolean delete = true;

			for (DepModel modelXML : ModelFromSomeRes.modelsFromXML) {
				System.out.println("DB " + modelDB + '\n' + "XML   " + modelXML);
				if (modelDB.equalsKey(modelXML) && !(modelDB.getDescription().equals(modelXML.getDescription()))) {
					updateDepModel.add(modelXML);
					notInsertDepModel.add(modelXML);
					delete = false;
					break;
				} else if (modelDB.equalsKey(modelXML)
						&& (modelDB.getDescription().equals(modelXML.getDescription()))) {
					delete = false;
					System.out.println("FULL OK");
					notInsertDepModel.add(modelXML);
				}
			}
			if (delete) {
				deleteDepModel.add(modelDB);
			}
			ModelFromSomeRes.modelsFromXML.removeAll(notInsertDepModel);
		}

	}

	public static void printColl() {
		createCollectionForSQL();
		System.out.println("to add!");
		for (DepModel modelXML : ModelFromSomeRes.modelsFromXML) {
			System.out.println(modelXML);
		}
		System.out.println("to update!");
		for (DepModel modelUp : updateDepModel) {
			System.out.println(modelUp);
		}
		System.out.println("to delete!");
		for (DepModel modelDel : deleteDepModel) {
			System.out.println(modelDel);
		}

	}

	public static void SyncSQLQuery() {
		printColl();
		PreparedStatement pStmt;
		try (Connection conn = ConnectionDB.con) {
			boolean oldAutoCommit=conn.getAutoCommit();
	        conn.setAutoCommit(false);
			try {
				for (DepModel modelXML : ModelFromSomeRes.modelsFromXML) {
					pStmt = conn.prepareStatement("INSERT INTO DepTable(DepCode, DepJob, Description) VALUES (?,?,?)");
					pStmt.setString(1, modelXML.getDepCode());
					pStmt.setString(2, modelXML.getDepJob());
					pStmt.setString(3, modelXML.getDescription());
					pStmt.executeUpdate();
					logger.info("Insert record " + modelXML);
				}
				// System.out.println("to update!");
				for (DepModel modelUp : updateDepModel) {
					pStmt = conn.prepareStatement("UPDATE DepTable SET Description=? WHERE DepCode=? AND DepJob=?");
					pStmt.setString(1, modelUp.getDescription());
					pStmt.setString(2, modelUp.getDepCode());
					pStmt.setString(3, modelUp.getDepJob());
					pStmt.executeUpdate();
					logger.info("Update record " + modelUp);
					// System.out.println(modelUp);
				}
				for (DepModel modelDel : deleteDepModel) {
					pStmt = conn.prepareStatement("DELETE FROM DepTable WHERE DepCode=? AND DepJob=?");
					pStmt.setString(1, modelDel.getDepCode());
					pStmt.setString(2, modelDel.getDepJob());
					pStmt.executeUpdate();
					logger.info("Delete record " + modelDel);
				}
				conn.commit();
				System.out.println(
						"Добавлено записей - " + ModelFromSomeRes.modelsFromXML.size() + "  Обновлено записей  - "
								+ updateDepModel.size() + "  Удалено записей   -  " + deleteDepModel.size());
				logger.info("Commit DB");
			} catch (SQLException e) {
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
