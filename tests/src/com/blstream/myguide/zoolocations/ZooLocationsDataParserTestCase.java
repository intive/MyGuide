
package com.blstream.myguide.zoolocations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.test.AndroidTestCase;

import com.blstream.myguide.R;

/** Class containing tests for ZooLocationsDataParser. */
public class ZooLocationsDataParserTestCase extends AndroidTestCase {

	private static final String ENCODING = "UTF-8";

	/** Amount of animals in xml file from userstory */
	private static final int ANIMALS = 55;
	/** Amount of ways in xml file from userstory */
	private static final int WAYS = 82;
	/** Amount of junctions in xml file from userstory */
	private static final int JUNCTIONS = 20;

	/** Checks if tag <animals> is well parsed. */
	public void testParsingAnimals() throws IOException, XmlPullParserException {
		// given
		String xmlText = "<root><animals>"
				+ "<animal lat=\"51.1052072\" lon=\"17.0754498\" id=\"1\">"
				+ "<name><en>Giraffe</en><pl>Żyrafa</pl></name>"
				+ "<description_adult>"
				+ "<image>img/giraffe_adult.jpg</image>"
				+ "<en>Description</en>"
				+ "<pl>Opis</pl>"
				+ "</description_adult>"
				+ "<description_child>"
				+ "<image>img/giraffe_child.jpg</image>"
				+ "<en>Description child</en>"
				+ "<pl>Opis dziecko</pl>"
				+ "</description_child>"
				+ "</animal>"
				+ "</animals></root>";
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		InputStream is = null;
		ZooLocationsData data = null;

		// when
		is = new ByteArrayInputStream(xmlText.getBytes(ENCODING));
		data = parser.parse(is);
		is.close();

		// then
		assertEquals(1, data.getAnimals().size());
		assertEquals(0, data.getWays().size());
		assertEquals(0, data.getJunctions().size());
		assertEquals(1, data.getAnimals().get(0).getId());
		assertEquals("Giraffe", data.getAnimals().get(0).getName(Language.EN));
		assertEquals("Żyrafa", data.getAnimals().get(0).getName(Language.PL));
		assertEquals("img/giraffe_adult.jpg", data.getAnimals().get(0).getDescriptionAdult()
				.getImageName());
		assertEquals("Description",
				data.getAnimals().get(0).getDescriptionAdult().getText(Language.EN));
		assertEquals("Opis",
				data.getAnimals().get(0).getDescriptionAdult().getText(Language.PL));
		assertEquals("img/giraffe_child.jpg", data.getAnimals().get(0).getDescriptionChild()
				.getImageName());
		assertEquals("Description child",
				data.getAnimals().get(0).getDescriptionChild().getText(Language.EN));
		assertEquals("Opis dziecko",
				data.getAnimals().get(0).getDescriptionChild().getText(Language.PL));
		assertEquals(51.1052072, data.getAnimals().get(0).getNode().getLatitude());
		assertEquals(17.0754498, data.getAnimals().get(0).getNode().getLongitude());
	}

	/** Checks if tag <ways> is well parsed. */
	public void testParsingWays() throws IOException, XmlPullParserException {
		// given
		String xmlText = "<root><ways>"
				+ "<way id=\"32997558\">"
				+ "<node lat=\"51.1054430\" lon=\"17.0773945\" />"
				+ "<node lat=\"51.1054595\" lon=\"17.0774086\" />"
				+ "</way>"
				+ "<way id=\"35948032\">"
				+ "<node lat=\"51.1057838\" lon=\"17.0751055\" />"
				+ "<node lat=\"51.1051115\" lon=\"17.0745007\" />"
				+ "<node lat=\"51.1048329\" lon=\"17.0742639\" />"
				+ "</way>"
				+ "</ways></root>";
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		InputStream is = null;
		ZooLocationsData data = null;

		// when
		is = new ByteArrayInputStream(xmlText.getBytes(ENCODING));
		data = parser.parse(is);
		is.close();

		// then
		assertEquals(0, data.getAnimals().size());
		assertEquals(2, data.getWays().size());
		assertEquals(0, data.getJunctions().size());
		assertEquals(32997558, data.getWays().get(0).getId());
		assertEquals(51.1054430, data.getWays().get(0).getNodes().get(0).getLatitude());
		assertEquals(17.0773945, data.getWays().get(0).getNodes().get(0).getLongitude());
		assertEquals(51.1054595, data.getWays().get(0).getNodes().get(1).getLatitude());
		assertEquals(17.0774086, data.getWays().get(0).getNodes().get(1).getLongitude());

		assertEquals(35948032, data.getWays().get(1).getId());
		assertEquals(51.1057838, data.getWays().get(1).getNodes().get(0).getLatitude());
		assertEquals(17.0751055, data.getWays().get(1).getNodes().get(0).getLongitude());
		assertEquals(51.1051115, data.getWays().get(1).getNodes().get(1).getLatitude());
		assertEquals(17.0745007, data.getWays().get(1).getNodes().get(1).getLongitude());
		assertEquals(51.1048329, data.getWays().get(1).getNodes().get(2).getLatitude());
		assertEquals(17.0742639, data.getWays().get(1).getNodes().get(2).getLongitude());
	}

	/** Checks if tag <junctions> is well parsed. */
	public void testParsingJunctions() throws IOException, XmlPullParserException {
		// given
		String xmlText = "<root><ways>"
				+ "<way id=\"32997558\">"
				+ "<node lat=\"51.1054430\" lon=\"17.0773945\" />"
				+ "<node lat=\"51.1054595\" lon=\"17.0774086\" />"
				+ "</way>"
				+ "<way id=\"35948032\">"
				+ "<node lat=\"51.1057838\" lon=\"17.0751055\" />"
				+ "<node lat=\"51.1051115\" lon=\"17.0745007\" />"
				+ "</way>"
				+ "<way id=\"35948033\">"
				+ "<node lat=\"51.1054430\" lon=\"17.0773945\" />"
				+ "<node lat=\"51.1054595\" lon=\"17.0774086\" />"
				+ "</way>"
				+ "</ways>"

				+ "<junctions>"
				+ "<junction lat=\"51.1051115\" lon=\"17.0745007\">"
				+ "<way id=\"32997558\" />"
				+ "<way id=\"35948032\" />"
				+ "</junction>"
				+ "<junction lat=\"51.1069836\" lon=\"17.0729754\">"
				+ "<way id=\"32997558\" />"
				+ "<way id=\"35948033\" />"
				+ "<way id=\"35948032\" />"
				+ "</junction>"
				+ "</junctions></root>";

		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		InputStream is = null;
		ZooLocationsData data = null;

		// when
		is = new ByteArrayInputStream(xmlText.getBytes(ENCODING));
		data = parser.parse(is);
		is.close();

		// then
		assertEquals(0, data.getAnimals().size());
		assertEquals(3, data.getWays().size());
		assertEquals(2, data.getJunctions().size());
		assertEquals(51.1051115, data.getJunctions().get(0).getNode().getLatitude());
		assertEquals(17.0745007, data.getJunctions().get(0).getNode().getLongitude());
		assertEquals(32997558, data.getJunctions().get(0).getWays().get(0).getId());
		assertEquals(35948032, data.getJunctions().get(0).getWays().get(1).getId());

		assertEquals(51.1069836, data.getJunctions().get(1).getNode().getLatitude());
		assertEquals(17.0729754, data.getJunctions().get(1).getNode().getLongitude());
		assertEquals(32997558, data.getJunctions().get(1).getWays().get(0).getId());
		assertEquals(35948033, data.getJunctions().get(1).getWays().get(1).getId());
		assertEquals(35948032, data.getJunctions().get(1).getWays().get(2).getId());
	}

	/**
	 * Checks if xml from userstory is well parsed (check if amount of animals,
	 * ways and junctions are good).
	 */
	public void testParsingXmlFromUserStory() throws IOException, XmlPullParserException {
		// given
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		InputStream is = null;
		ZooLocationsData data = null;

		// when
		is = this.getContext().getResources().openRawResource(R.raw.data);
		data = parser.parse(is);
		is.close();

		// then
		assertEquals(ANIMALS, data.getAnimals().size());
		assertEquals(WAYS, data.getWays().size());
		assertEquals(JUNCTIONS, data.getJunctions().size());
	}

	/**
	 * Checks if parser throws XmlPullParserException with adequate message when
	 * there is a way in junctions tag but not in ways tag.
	 */
	public void testJunctionWithBadWay() throws IOException {
		// given
		String xmlText = "<root><junctions><junction lat=\"51.1054430\" lon=\"17.0773945\">"
				+ "<way id=\"123456\"/>"
				+ "</junction></junctions></root>";
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		InputStream is = null;
		XmlPullParserException exception = null;

		// when
		try {
			is = new ByteArrayInputStream(xmlText.getBytes(ENCODING));
			parser.parse(is);
		} catch (XmlPullParserException exc) {
			exception = exc;
		} finally {
			is.close();
		}

		// then
		assertNotNull(exception);
		assertTrue(exception instanceof ZooLocationsDataParser.WayNotFoundException);
	}

	public void testParsingTracks() throws IOException, XmlPullParserException {
		// given
		String xmlText = "<root><visiting_tracks>"
				+ "<track><name><pl>Trasa 1</pl><en>Track 1</en></name>"
				+ "<description><pl>Opis 1</pl><en>Description 1</en></description>"
				+ "<image>img/track1.jpg</image>"
				+ "<animals>"
				+ "<animal_id>1</animal_id><animal_id>2</animal_id>"
				+ "</animals></track>"
				+ "</visiting_tracks>"
				+ "<animals>"
				+ "<animal lat=\"51.1052072\" lon=\"17.0754498\" id=\"1\">"
				+ "<name><en>Animal1</en><pl>Zwierze1</pl></name>"
				+ "<description_adult>"
				+ "<image>img/giraffe_adult.jpg</image><en>Description</en><pl>Opis</pl>"
				+ "</description_adult>"
				+ "<description_child>"
				+ "<image>img/giraffe_child.jpg</image><en>Description child</en><pl>Opis dziecko</pl>"
				+ "</description_child>"
				+ "</animal>"
				+ "<animal lat=\"51.1052072\" lon=\"17.0754498\" id=\"2\">"
				+ "<name><en>Animal2</en><pl>Zwierze2</pl></name>"
				+ "<description_adult>"
				+ "<image>img/giraffe_adult.jpg</image><en>Description</en><pl>Opis</pl>"
				+ "</description_adult>"
				+ "<description_child>"
				+ "<image>img/giraffe_child.jpg</image><en>Description child</en><pl>Opis dziecko</pl>"
				+ "</description_child>"
				+ "</animal>"
				+ "</animals></root>";
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		InputStream is = null;
		ZooLocationsData data = null;

		// when
		is = new ByteArrayInputStream(xmlText.getBytes(ENCODING));
		data = parser.parse(is);
		is.close();

		// then
		assertEquals(2, data.getAnimals().size());
		assertEquals(0, data.getWays().size());
		assertEquals(0, data.getJunctions().size());
		assertEquals(1, data.getTracks().size());
		assertEquals(2, data.getTracks().get(0).getAnimals().size());
		assertEquals("Track 1", data.getTracks().get(0).getName(Language.EN));
		assertEquals("Trasa 1", data.getTracks().get(0).getName(Language.PL));
		assertEquals("Description 1", data.getTracks().get(0).getDescription(Language.EN));
		assertEquals("Opis 1", data.getTracks().get(0).getDescription(Language.PL));
		assertEquals("img/track1.jpg", data.getTracks().get(0).getImage());
		assertEquals(1, data.getTracks().get(0).getAnimals().get(0).getId());
		assertEquals(2, data.getTracks().get(0).getAnimals().get(1).getId());
		assertEquals("Animal1", data.getTracks().get(0).getAnimals().get(0).getName(Language.EN));
		assertEquals("Zwierze1", data.getTracks().get(0).getAnimals().get(0).getName(Language.PL));
		assertEquals("Animal2", data.getTracks().get(0).getAnimals().get(1).getName(Language.EN));
		assertEquals("Zwierze2", data.getTracks().get(0).getAnimals().get(1).getName(Language.PL));
	}

	public void testParsingTickets() throws XmlPullParserException, IOException {
		// given
		String plTicketBaseName = "bilet", enTicketBaseName = "ticket";
		// three tickets (2 individual, 1 group
		// price is used as a counter, starting from 1
		String xmlMock = "" +
				"<root>" +
				"\t<tickets_information>\n" +
				"\t\t<individual>\n" +
				"\t\t\t<ticket>\n" +
				"\t\t\t\t<description>\n" +
				"\t\t\t\t\t<pl>" + plTicketBaseName + "1</pl>\n" +
				"\t\t\t\t\t<en>" + enTicketBaseName + "1</en>\n" +
				"\t\t\t\t</description>\n" +
				"\t\t\t\t<price>1</price>\n" +
				"\t\t\t</ticket>\n" +
				"\t\t\t<ticket>\n" +
				"\t\t\t\t<description>\n" +
				"\t\t\t\t\t<pl>" + plTicketBaseName + "2</pl>\n" +
				"\t\t\t\t\t<en>" + enTicketBaseName + "2</en>\n" +
				"\t\t\t\t</description>\n" +
				"\t\t\t\t<price>2</price>\n" +
				"\t\t\t</ticket>" +
				"\t\t</individual>\n" +
				"\t\t<group>\n" +
				"\t\t\t<ticket>\n" +
				"\t\t\t\t<description>\n" +
				"\t\t\t\t\t<pl>" + plTicketBaseName + "3</pl>\n" +
				"\t\t\t\t\t<en>" + enTicketBaseName + "3</en>\n" +
				"\t\t\t\t</description>\n" +
				"\t\t\t\t<price>3</price>\n" +
				"\t\t\t</ticket>\n" +
				"\t\t</group>\n" +
				"\t\t<information>\n" +
				"\t\t\t<key1>value1</key1>\n" +
				"\t\t\t<key2>value2</key2>\n" +
				"\t\t\t<key3>value3</key3>\n" +
				"\t\t\t<key4>value4</key4>\n" +
				"\t\t</information>\n" +
				"\t</tickets_information>\n" +
				"</root>\n";
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		ZooLocationsData data = null;

		// when
		InputStream is = new ByteArrayInputStream(xmlMock.getBytes());
		data = parser.parse(is);
		is.close();

		// then
		assertNotNull(data);
		assertEquals(3, data.getTicketInformation().getTickets().size());
		int k = 1, individualTicketsCount = 0, groupTicketsCount = 0;
		for (Ticket ticket : data.getTicketInformation().getTickets()) {
			String pl = String.format("%s%d", plTicketBaseName, k);
			String en = String.format("%s%d", enTicketBaseName, k);

			assertEquals(pl, ticket.getDescription(Language.PL));
			assertEquals(en, ticket.getDescription(Language.EN));
			assertEquals(k, ticket.getPrice());
			k++;

			switch (ticket.getType()) {
				case INDIVIDUAL:
					individualTicketsCount++;
					break;

				case GROUP:
					groupTicketsCount++;
					break;

				default: break;
			}
		}
		assertEquals(2, individualTicketsCount);
		assertEquals(1, groupTicketsCount);
		assertEquals(4, data.getTicketInformation().getAllInformation().size());
	}

	public void testParsingAccessInformation() throws IOException, XmlPullParserException {
		// given
		String xmlMock = "" +
				"<root>" +
				"\t<access_information>\n" +
				"\t\t<trams>\n" +
				"\t\t\t1,2,   3, 4, 5,6\n" +
				"\t\t</trams>\n" +
				"\t\t<parkings_information>\n" +
				"\t\t\t<key1>value1</key1>\n" +
				"\t\t\t<key2>value2</key2>\n" +
				"\t\t\t<key3>value3</key3>\n" +
				"\t\t\t<key4>value4</key4>\n" +
				"\t\t</parkings_information>\n" +
				"\t</access_information>\n" +
				"</root>\n";
		ZooLocationsDataParser parser = new ZooLocationsDataParser();
		ZooLocationsData data = null;

		// when
		InputStream is = new ByteArrayInputStream(xmlMock.getBytes());
		data = parser.parse(is);
		is.close();

		// then
		assertNotNull(data);
		assertEquals("1,2,   3, 4, 5,6", data.getAccessInformation().getTrams());
		assertEquals(4, data.getAccessInformation().getAllParkingInformation().size());
	}

}
