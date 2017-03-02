import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
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

public class ModelFromSomeRes {
	static Set<DepModel> modelsFromDB = new HashSet<DepModel>(); // Коллекция
																	// моделей
																	// из БД
	static Set<DepModel> modelsFromXML = new HashSet<DepModel>();// Коллекция
																	// моделей
																	// из XML
	public static Savepoint savePointOne;
	final static Logger logger = Logger.getLogger(ModelFromSomeRes.class);

	public static void readModelFromDB() {
		try {
			savePointOne=ConnectionDB.con.setSavepoint();
			logger.info("create DB savepoint");
			Statement stm = ConnectionDB.con.createStatement();
			
			String sql = "SELECT DepCode, DepJob, Description FROM DepTable";
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				DepModel myModel = new DepModel(rs.getString(1), rs.getString(2), rs.getString(3));
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

	public static boolean readModelFromXML(File xmlFile) {
		Set<String> tempSet = new HashSet<String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("model");

			for (int count = 0; count < nList.getLength(); count++) {

				Node nNode = nList.item(count);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					DepModel model = new DepModel(
							screenProtector(eElement.getElementsByTagName("DepCode").item(0).getTextContent()),
							screenProtector(eElement.getElementsByTagName("DepJob").item(0).getTextContent()),
							screenProtector(eElement.getElementsByTagName("Description").item(0).getTextContent()));
					String keyString =eElement.getElementsByTagName("DepCode").item(0).getTextContent()+
							eElement.getElementsByTagName("DepJob").item(0).getTextContent();
					if (validationLengthSetFromXML(model)) {
						tempSet.add(keyString);
						System.out.println(keyString);
						modelsFromXML.add(model);
					}

					logger.info("record " + model + "  read from XML file");
				}
			}
			//System.out.println(tempSet.size());
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

	private static boolean validationLengthSetFromXML(DepModel model) {
		boolean check = true;
		if (model.getDepCode().length() > 20 || model.getDepJob().length() > 100
				|| model.getDescription().length() > 255 ||model.getDepCode().length() <1 
				|| model.getDepJob().length() < 1) {
			logger.warn("in XML file not valid record, check " + model);
			System.out.println("not valid XML");
			check = false;
		}
		return check;
	}
	private static String screenProtector(String defaultText){
		String protectedText=defaultText.replaceAll("\\pP ", "");
		return protectedText;
	}

	public static void writeXMLFile(File xmlFile) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("DB");
			doc.appendChild(root);

			for (DepModel depModel : modelsFromDB) {
				Element model = doc.createElement("model");
				root.appendChild(model);
				Element depCode = doc.createElement("DepCode");
				Element depJob = doc.createElement("DepJob");
				Element description = doc.createElement("Description");

				depCode.appendChild(doc.createTextNode(depModel.getDepCode()));
				depJob.appendChild(doc.createTextNode(depModel.getDepJob()));
				description.appendChild(doc.createTextNode(depModel.getDescription()));

				model.appendChild(depCode);
				model.appendChild(depJob);
				model.appendChild(description);
				logger.info("record  " + depModel + " add to XML file");

			}
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
