import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionDB {

	
	final  static Logger logger=Logger.getLogger(ConnectionDB.class);
	public static Connection con;
	

	public static void connectionDB(){
		
		try {
			Properties props = new Properties();
			FileInputStream in;
			in = new FileInputStream("property/db.properties");
			props.load(in);
			in.close();
			String driver = props.getProperty("jdbc.driver");
			if (driver != null) {
			    Class.forName(driver) ;
			}

			String url = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");

			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connect");
			logger.info("OK... connection to DB");
		} catch (FileNotFoundException e) {
			System.out.println("File with properties not found");
			logger.error("File with properties not found   "+e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File with properties not correct");
			logger.error("File with properties not correct   "+e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Can't found driver");
			logger.error("Can't found driver  "+e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Can't connection to database");
			logger.error("Can't connection to database  "+e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}
