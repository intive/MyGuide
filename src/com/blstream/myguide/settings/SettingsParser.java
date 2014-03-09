
package com.blstream.myguide.settings;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SettingsParser {

	private static Settings mSettings;

	public Settings parseSettings(InputStream strXML) throws ParserConfigurationException,
			SAXException, IOException {

		mSettings = new Settings();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;

		try {
			doc = dBuilder.parse(strXML);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		doc.getDocumentElement().normalize();
		Element rootElement = doc.getDocumentElement();
		NodeList nodes = rootElement.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				mSettings.put(node.getNodeName(), node.getTextContent());
			}
		}
		return mSettings;
	}

}
