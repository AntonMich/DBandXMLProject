package pack;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
 * ModelFromSomeRes - класс для загрузки сущности в программу 
 * </p>
 * 
 * */

public class ModelFromSomeRes {
	private Set<DepModel> modelsFromDB = new HashSet<DepModel>(); // Коллекция сущностей из БД
	private Set<DepModel> modelsFromXML = new HashSet<DepModel>();// Коллекция сущностей из XML
	
	public Set<DepModel> getModelsFromDB() {
		return modelsFromDB;
	}

	public Set<DepModel> getModelsFromXML() {
		return modelsFromXML;
	}
	final static Logger logger = Logger.getLogger(ModelFromSomeRes.class);

	/**
	 * Метод чтения сущности из БД
	 * @param con - подключение к БД
	 * */
	public void readModelFromDB(Connection con) {
		//System.err.println(modelsFromXML.size());
		try {
			Statement stm = con.createStatement();

			String sql = "SELECT DepCode, DepJob, Description FROM DepTable";
			ResultSet rs = stm.executeQuery(sql);
			//Считываем из таблицы DepTable поля DepCode, DepJob Description
			while (rs.next()) {
				DepModel myModel = new DepModel(rs.getString(1), rs.getString(2), rs.getString(3));
				// добавляем их в коллекцию
				modelsFromDB.add(myModel);
				logger.info("record " + myModel + "  read from DB");
			}

			logger.info("From DB load  " + modelsFromDB.size() + " objects");
			rs.close();
			stm.close();
		} catch (SQLException e) {
			logger.error("wrong SQL, check name " + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Метод чтения сущности из xml
	 * @param xmlFile- принимает файл XML
	 * @return true/false - если в xml не уникален составной ключ
	 * */
	public boolean readModelFromXML(File xmlFile) {
		//Коллекция для проверки уникальности
		Set<String> tempSet = new HashSet<String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			//Парсим файл
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("model");

			for (int count = 0; count < nList.getLength(); count++) {

				Node nNode = nList.item(count);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					DepModel model = new DepModel(
							// Экранируем от лишних символов
							screenProtector(eElement.getElementsByTagName("DepCode").item(0).getTextContent()),
							screenProtector(eElement.getElementsByTagName("DepJob").item(0).getTextContent()),
							screenProtector(eElement.getElementsByTagName("Description").item(0).getTextContent()));
					//Создаем строку для составного ключа
					String keyString = eElement.getElementsByTagName("DepCode").item(0).getTextContent()
							+ eElement.getElementsByTagName("DepJob").item(0).getTextContent();
					//Проверяем сущность на ограничение размера свойств
					if (validationLengthSetFromXML(model)) {
						tempSet.add(keyString);
						//System.out.println(keyString);
						modelsFromXML.add(model);
					}

					logger.info("record " + model + "  read from XML file");
				}
			}
			// System.out.println(tempSet.size());
			//Если ключ в файле хмл не уникален - размеры коллекций не будут равны
			if (tempSet.size() == modelsFromXML.size()) {
				System.out.println("in xml file find " + modelsFromXML.size() + "  records");
				logger.info("in xml file find " + modelsFromXML.size() + "  records");
				return true;
			} else {
				System.out.println("key is not unique");
				logger.error("key is not unique");
				return false;
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error configuration parser  " + e + "   !Error read XML");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("XML file is not correct  " + e + "   !Error read XML");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("XML file not found  " + e + "   !Error read XML");
		}
		return false;

	}
	
	//Переписать
	/**
	 * Метод для валидации размерности свойств
	 * @param model принимает сущность DepModel
	 * @return - true/false - если размерность не проходит требования
	 * */
	private static boolean validationLengthSetFromXML(DepModel model) {
		boolean check = true;
		if (model.getDepCode().length() > 20 || model.getDepJob().length() > 100
				|| model.getDescription().length() > 255 || model.getDepCode().length() < 1
				|| model.getDepJob().length() < 1) {
			logger.warn("in XML file not valid record, check " + model);
			System.out.println("not valid XML");
			check = false;
		}
		return check;
	}
	/**
	 * Метод для экранирования спец символов получаемых данных из XML
	 * @param defaultText свойства сущности DepModel
	 * @return protectedText - экранированная строка
	 * Экранируются знаки пунктуации (кавычки, запятые итд)
	 * */
	private static String screenProtector(String defaultText) {
		String protectedText = defaultText.replaceAll("\\pP ", "");
		return protectedText;
	}
	
	/**
	 * Метод записи сущности в xml
	 * @param xmlFile- принимает файл XML
	 * */
	public void writeXMLFile(File xmlFile) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//Создаем коревой элемент
			Element root = doc.createElement("DB");
			doc.appendChild(root);
			//Пробегаемся по коллекции сущностей из БД
			for (DepModel depModel : modelsFromDB) {
				Element model = doc.createElement("model");
				root.appendChild(model);
				Element depCode = doc.createElement("DepCode");
				Element depJob = doc.createElement("DepJob");
				Element description = doc.createElement("Description");
				//Добавляем значения
				depCode.appendChild(doc.createTextNode(depModel.getDepCode()));
				depJob.appendChild(doc.createTextNode(depModel.getDepJob()));
				description.appendChild(doc.createTextNode(depModel.getDescription()));

				model.appendChild(depCode);
				model.appendChild(depJob);
				model.appendChild(description);
				logger.info("record  " + depModel + " add to XML file");

			}
			//Записываем полученные результаты
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource dom = new DOMSource(doc);
			StreamResult streamRes = new StreamResult(xmlFile);
			transformer.transform(dom, streamRes);
			System.out.println("create XML File with  " + modelsFromDB.size() + "  records");
			logger.info("create XML File with  " + modelsFromDB.size() + "  records");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("error create xml file" + e);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error("error create xml file" + e);
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			logger.error("error create xml file" + e);
			e.printStackTrace();
		}
	}

}
