
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

	private Settings mSettings;

	public Settings parseSettings(InputStream strXML) throws ParserConfigurationException,
			SAXException, IOException {

		mSettings = new Settings();
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		final Document doc = dBuilder.parse(strXML);

		doc.getDocumentElement().normalize();
		final Element rootElement = doc.getDocumentElement();
		final NodeList nodes = rootElement.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				mSettings.put(node.getNodeName(), node.getTextContent());
			}
		}
		return mSettings;
	}

}
