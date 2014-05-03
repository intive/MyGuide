using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using MyGuide.DataServices;
using MyGuide.Models;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Threading.Tasks;

namespace MyGuideTests.Tests
{
    [TestClass]
    public class DataServiceTest
    {
        private Root correctData;

        [TestMethod]
        public async Task CorrectDataInitializeTest()
        {

            string xmlString;
            using (StreamReader sr = new StreamReader("TestData/CorrectData.xml"))
            {
                xmlString = await sr.ReadToEndAsync();
            }
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                myIsolatedStorage.CreateDirectory("Data");
                using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Data/data.xml", FileMode.Create))
                {
                    using (StreamWriter sw = new StreamWriter(stream))
                    {
                        await sw.WriteAsync(xmlString);
                    }
                }
            }

            DataService ds = new DataService();
            await ds.Initialize();
            Debug.WriteLine(ds.CollectionsSizes());
            Assert.AreEqual("Zebra", ds.Datas.AnimalsList.Items.First().Name);
            Assert.AreEqual(51.1048329, ds.Datas.JunctionsList.Items.First().Latitude);
            Assert.AreEqual(17.0742639, ds.Datas.JunctionsList.Items.First().Longitude);
            Assert.AreEqual(35948032, ds.Datas.WaysList.Items.First().Id);
            CleanupMethod();
        }

        public static void CleanupMethod()
        {
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (myIsolatedStorage.FileExists("Data/data.xml"))
                {
                    myIsolatedStorage.DeleteFile("Data/data.xml");
                    myIsolatedStorage.DeleteDirectory("Data");
                }
            }
        }

        [TestMethod]
        public async Task InitializeWithoutFileInISOptionTest()
        {
            DataService ds = new DataService();
            await ds.Initialize();
            Debug.WriteLine(ds.CollectionsSizes());
            Assert.AreEqual("Żyrafa", ds.Datas.AnimalsList.Items.First().Name);
            Assert.AreEqual(51.1050455, ds.Datas.JunctionsList.Items.First().Latitude);
            Assert.AreEqual(17.0720156, ds.Datas.JunctionsList.Items.First().Longitude);
            Assert.AreEqual(32997558, ds.Datas.WaysList.Items.First().Id);
            CleanupMethod();
        }
        

        [TestMethod]
        public void GetAnimalPositionTest()
        {
            DataService ds = new DataService();
            ds.Datas = correctData;
            Node correctNode = new Node() { Latitude = 51.11111, Longitude = 12.11111 };
            Node testNode = ds.GetAnimalPosition("Żyrafa");

            Assert.AreEqual(correctNode.Latitude, testNode.Latitude);
            Assert.AreEqual(correctNode.Longitude, testNode.Longitude);
        }

        [TestMethod]
        public void GetWayListOfNodesTest()
        {
            DataService ds = new DataService();
            ds.Datas = correctData;
            Node correctNode = new Node() { Latitude = 51.11111, Longitude = 12.11111 };
            List<Node> nodeList = ds.GetWayListOfNodes(11111);

            Assert.AreEqual(correctNode.Latitude, nodeList.First().Latitude);
            Assert.AreEqual(correctNode.Longitude, nodeList.First().Longitude);
        }

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
        public void SizesMethodTest()
        {
            DataService ds = new DataService();
            ds.Datas = correctData;
            string collectionsSize = "\nAnimals: 1\nWays: 2\nJunctions: 1";

            Assert.AreEqual(1, ds.AnimalsSize());
            Assert.AreEqual(1, ds.JunctionsSize());
            Assert.AreEqual(2, ds.WaysSize());
            Assert.AreEqual(collectionsSize, ds.CollectionsSizes());
        }
    }
}