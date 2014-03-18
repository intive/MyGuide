using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using Moq;
using MyGuide.DataServices;
using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuideTests.Tests
{
    [TestClass]
    public class DataServiceModelTest
    {
        
        Root correctData;
        [TestInitialize]
        public void InitTest()
        {
            correctData = new Root();
            correctData.AnimalsList.Items.Add(new Animal() { Name = "Żyrafa", Latitude = 51.11111, Longitude = 12.11111 });
            correctData.JunctionsList.Items.Add(new Junction()
            {
                Latitude = 51.11111,
                Longitude = 12.11111,
                HelperWay = new List<Way>() { new Way() { Id = 33333 }, new Way() { Id = 22222 } }
            });
            correctData.WaysList.Items.Add(
                new Way()
                {
                    Id = 11111,
                    Nodes = new List<Node>() { 
                        new Node() { Latitude = 51.11111, Longitude = 12.11111 }, 
                        new Node() { Latitude = 52.11111, Longitude = 13.11111 } }
                });
            correctData.WaysList.Items.Add(
               new Way() { Id = 55555, Nodes = new List<Node>() { new Node(), new Node() } });
        }
       
        [TestMethod]
        public void CorrectDataInitializeTest()
        {
           //TODO: Test for DataServiceModel.Initialize();
        }

        [TestMethod]
        public void SizesMethodTest()
        {
            DataServiceModel ds = new DataServiceModel();
            ds.Datas = correctData;
            string collectionsSize = "Animals: 1\nWays: 2\nJunctions: 1";
            
            Assert.AreEqual(1, ds.AnimalsSize());
            Assert.AreEqual(1, ds.JunctionsSize());
            Assert.AreEqual(2, ds.WaysSize());
            Assert.AreEqual(collectionsSize,ds.CollectionsSizes());
        }

        [TestMethod]
        public void GetAnimalPositionTest()
        {
            DataServiceModel ds = new DataServiceModel();
            ds.Datas =correctData;
            Node correctNode = new Node() { Latitude = 51.11111, Longitude = 12.11111 };
            Node testNode = ds.GetAnimalPosition("Żyrafa");

            Assert.AreEqual(correctNode.Latitude,testNode.Latitude);
            Assert.AreEqual(correctNode.Longitude,testNode.Longitude);

        }

        [TestMethod]
        public void GetWayListOfNodesTest()
        {
            DataServiceModel ds = new DataServiceModel();
            ds.Datas = correctData;
            Node correctNode = new Node() { Latitude = 51.11111, Longitude = 12.11111 };
            List<Node> nodeList = ds.GetWayListOfNodes(11111);
            
            Assert.AreEqual(correctNode.Latitude,nodeList.First().Latitude);
            Assert.AreEqual(correctNode.Longitude,nodeList.First().Longitude);
        }
        

    }
}
