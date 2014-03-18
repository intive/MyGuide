using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using System;
using Moq;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MyGuide.DataServices;
using MyGuide.Models;
using System.IO;

namespace MyGuideTests.Tests
{
    [TestClass]
    public class XmlParserTests
    {
        [TestMethod]
        public async Task CorrectXmlDataParser()
        {
            XmlParser <Root> xmlParser = new XmlParser<Root>();
            Root parsedData;
            string correctXmlData = "TestData/CorrectData.xml";
            
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

            Assert.AreEqual(animalName,parsedData.AnimalsList.Items.First().Name);
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
        
        public async Task IncorrectXmlDataParser()
        {
            XmlParser<Root> xmlParser = new XmlParser<Root>(); 
            string incorrectXmlData = "TestData/IncorrectData.xml";

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
            XmlParser <Root> xmlParser = new XmlParser <Root>();
            
            string lackXmlData = "TestData/LackData.xml";

            try
            {
                await xmlParser.DeserializeXml(lackXmlData);
                Assert.Fail();
            }
            catch (FileNotFoundException)
            {
                Assert.IsTrue(true);
            }
        }

        [TestMethod]

        public async Task IncorrectStructureXmlDataParser()
        {
            XmlParser <Root> xmlParser = new XmlParser <Root>();
            Root parsedData;
            Root correctParsedData;

            string incorrectXmlData = "TestData/IncorrectStructureData.xml";
            string correctXmlData = "TestData/CorrectData.xml";
            

            parsedData = await xmlParser.DeserializeXml(incorrectXmlData);
            correctParsedData = await xmlParser.DeserializeXml(correctXmlData);

            Assert.AreNotEqual(correctParsedData, parsedData);

        }

        
    }
}
