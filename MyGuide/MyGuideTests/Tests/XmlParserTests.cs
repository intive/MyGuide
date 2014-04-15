using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using Moq;
using MyGuide.DataServices;
using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuideTests.Tests
{
    [TestClass]
    public class XmlParserTests
    {
        [TestMethod]
        public async Task CorrectXmlDataParser()
        {
            XmlParser<Root> xmlParser = new XmlParser<Root>();
            Root parsedData;
            string correctXmlDataPath = "TestData/CorrectData.xml";
            string correctXmlData = string.Empty;

            using (StreamReader sr = new StreamReader(correctXmlDataPath))
            {
                correctXmlData = await sr.ReadToEndAsync();
            }

            string animalName = "Żyrafa";
            double animalLatitude = 51.1046625;
            double animalLongitude = 17.0771680;

            long id = 32997558;
            double wayLatitude = 51.1054430;
            double wayLongitude = 17.0773945;

            double junctionLatitude = 51.1048329;
            double junctionLongitude = 17.0742639;
            long junctionWayId = 35948032;

            parsedData = await xmlParser.DeserializeXml(correctXmlData);

            Assert.AreEqual(animalName, parsedData.AnimalsList.Items.First().Name);
            Assert.AreEqual(animalLatitude, parsedData.AnimalsList.Items.Last().Latitude);
            Assert.AreEqual(animalLongitude, parsedData.AnimalsList.Items.Last().Longitude);

            Assert.AreEqual(id, parsedData.WaysList.Items.First().Id);
            Assert.AreEqual(wayLatitude, parsedData.WaysList.Items.First().Nodes.First().Latitude);
            Assert.AreEqual(wayLongitude, parsedData.WaysList.Items.First().Nodes.First().Longitude);

            Assert.AreEqual(junctionLatitude, parsedData.JunctionsList.Items.First().Latitude);
            Assert.AreEqual(junctionLongitude, parsedData.JunctionsList.Items.First().Longitude);
            Assert.AreEqual(junctionWayId, parsedData.JunctionsList.Items.First().HelperWay.First().Id);
        }

        [TestMethod]
        public async Task IncorrectStructureXmlDataParser()
        {
            XmlParser<Root> xmlParser = new XmlParser<Root>();
            Root parsedData;
            Root correctParsedData;

            string incorrectXmlDataPath = "TestData/IncorrectStructureData.xml";
            string correctXmlDataPath = "TestData/CorrectData.xml";
            string incorrectXmlData = string.Empty;
            string correctXmlData = string.Empty;


            using (StreamReader sr = new StreamReader(incorrectXmlDataPath))
            {
                incorrectXmlData = await sr.ReadToEndAsync();
            }

            using (StreamReader sr = new StreamReader(correctXmlDataPath))
            {
                correctXmlData = await sr.ReadToEndAsync();
            }

            parsedData = await xmlParser.DeserializeXml(incorrectXmlData);
            correctParsedData = await xmlParser.DeserializeXml(correctXmlData);

            Assert.AreNotEqual(correctParsedData, parsedData);
        }

        [TestMethod]
        public async Task IncorrectXmlDataParser()
        {
            XmlParser<Root> xmlParser = new XmlParser<Root>();
            string incorrectXmlDataPath = "TestData/IncorrectData.xml";
            string incorrectXmlData = string.Empty;

            using (StreamReader sr = new StreamReader(incorrectXmlDataPath))
            {
                incorrectXmlData = await sr.ReadToEndAsync();
            }

            try
            {
                await xmlParser.DeserializeXml(incorrectXmlData);
                Assert.Fail();
            }
            catch (InvalidOperationException)
            {
                Assert.IsTrue(true);
            }
        }

        [TestMethod]
        public async Task LackXmlDataParser()
        {
            XmlParser<Root> xmlParser = new XmlParser<Root>();

            string lackXmlDataPath = "TestData/LackData.xml";
            string lackXmlData = string.Empty;
           

            try
            {
                using (StreamReader sr = new StreamReader(lackXmlDataPath))
                {
                    lackXmlData = await sr.ReadToEndAsync();
                }
                await xmlParser.DeserializeXml(lackXmlData);
                Assert.Fail();
            }
            catch (FileNotFoundException)
            {
                Assert.IsTrue(true);
            }
        }
    }
}