import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class StartPoint {
	final static Logger logger = Logger.getLogger(StartPoint.class);
	private static Scanner scan;
	@SuppressWarnings("unused")
	private static String input;

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// TODO Auto-generated method stub
		// ConnectionDB.connectionDB();
		// ModelFromSomeRes.readModelFromDB();
		// ModelFromSomeRes.writeXMLFile(createFile("myFile1.xml"));
		// ModelFromSomeRes.readModelFromXML(new File("myFile1.xml"));
		// SyncAndValidation.SyncSQLQuery();
		dialogWithUser();

	}

	private static File createFile(String name) {
		String path = name;
		File newXMLFile = new File(path);
		try {
			newXMLFile.createNewFile();
			return newXMLFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void dialogWithUser() {
		System.out.println("Welcome, please write help for info");
		scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			validationInput(input = scan.nextLine());
			ModelFromSomeRes.modelsFromDB.clear();
			ModelFromSomeRes.modelsFromXML.clear();
		}
	}

	private static String validationInput(String input) {
		String part[] = input.split(" ");
		switch (part.length) {
		case 0:
		case 1:
			if (part[0].startsWith("help")) {
				System.out.println("Command: \n exit to close programm \n sync namefile for synchronization \n "
						+ "load namefile for unloading");
				break;
			}
			if (part[0].startsWith("exit")) {
				logger.info("Close programm");
				try {
					ConnectionDB.con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			} else {
				System.out.println("please write help for information");
			}
		case 2:
			if (part[0].startsWith("sync")) {
				System.out.println("synchronization");
				logger.info("start synchronization");
				ConnectionDB.connectionDB();
				ModelFromSomeRes.readModelFromDB();
				if (ModelFromSomeRes.readModelFromXML(new File(part[1]))) {
					SyncAndValidation.SyncSQLQuery();
				}

			}
			if (part[0].startsWith("load")) {
				ConnectionDB.connectionDB();
				ModelFromSomeRes.readModelFromDB();
				ModelFromSomeRes.writeXMLFile(createFile(part[1]));
			} else {
				System.out.println("not correct command: error input, please write help");
			}
		default:
			System.out.println("please write help for info");
			break;
		}
		return null;
	}

}
