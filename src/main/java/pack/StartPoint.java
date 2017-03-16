package pack;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Создать java приложение, выполняющее две основные функции:
1) выгрузка содержимого таблицы БД в XML файл;
2) синхронизация содержимого таблицы БД по заданному XML файлу.
 * 
 * @author AntonM
 * @version 0.2.3
 * 
 * <p>
 * StartPoint - класс с методом main
 * </p>
 * 
 * */

public class StartPoint {
	final static Logger logger = Logger.getLogger(StartPoint.class);
	
	// Для работы с консолью 
	private static Scanner scan;
	
	// Строка со сканера
	@SuppressWarnings("unused")
	private static String input;

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		dialogWithUser();

	}

	/** Метод для создания файла (без проверки расширения)
 	*@param name принимает имя файла (относительный путь)
 	*@return файл если создание успешно 
 	*/
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
	/**
	 * Метод dialogWithUser
	 * создает обьект scan
	 * цикл - вызываем метод validationInput и считываем введенную строку
	 * 
	 * */
	private static void dialogWithUser() {
		System.out.println("Welcome, please write help for info");
		scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			validationInput(input = scan.nextLine());
		}
	}

	/**
	 * Метод validationInput
	 * @param принимает введенную в консоль строку
	 * 
	 * */
	private static String validationInput(String input) {
		//Создаем обьект connect
		ConnectionDB connect=new ConnectionDB();
		//Разбиваем введенную строку по пробелам
		String part[] = input.split(" ");
		//Проверяем длину строки
		switch (part.length) {
		case 0:
		case 1:
			// Если введен(начинается с) help - выдаем информацию о работе с консолью exit - завершаем работу программы
			if (part[0].startsWith("help")) {
				System.out.println("Command: \n exit to close programm \n sync namefile for synchronization \n "
						+ "load namefile for unloading");
				break;
			}
			if (part[0].startsWith("exit")) {
				logger.info("Close programm");
				System.exit(0);
			} else {
				System.out.println("please write help for information");
			}
			//Если введен(начинается с) sync - производим синхронизацию с файлом load - выгружаем в файл
		case 2:
			if (part[0].startsWith("sync")) {
				System.out.println("synchronization");
				logger.info("start synchronization");
				// создаем обьект mFSR
				ModelFromSomeRes mFSR=new ModelFromSomeRes();
				// вызываем метод чтения из Базы Данных
				mFSR.readModelFromDB(connect.connectionDB());
				// вызываем метод чтения их хмл и проверяем на валидность содержимое
				if (mFSR.readModelFromXML(new File(part[1]))){
					// создаем обьект sync
					SyncAndValidation sync=new SyncAndValidation();
					//вызываем метод синхронизации XML файла и БД
					sync.SyncSQLQuery(connect.connectionDB(), mFSR.getModelsFromXML(), mFSR.getModelsFromDB());
					
				}
			}
			if (part[0].startsWith("load")) {
				System.out.println("uploading");
				logger.info("start uploading");
				// создаем обьект mFSR
				ModelFromSomeRes mFSR=new ModelFromSomeRes();
				// вызываем метод чтения из Базы Данных
				mFSR.readModelFromDB(connect.connectionDB());
				// записываем полученную информацию в хмл
				mFSR.writeXMLFile(createFile(part[1]));
			} else {
			}
		default:
			System.out.println("please write help for info");
			break;
		}
		return null;
	}

}
