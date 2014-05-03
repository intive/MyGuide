using Caliburn.Micro;
using MyGuide.DataServices;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.IO.IsolatedStorage;
using System.Threading.Tasks;

namespace MyGuide.DataServices
{
    public class DataService : IDataService
    {
        private ILog _log = LogManager.GetLog(typeof(DataService));

        public Root Datas { get; set; }

        public int AnimalsSize()
        {
            return Datas.AnimalsList.Items.Count;
        }

        public string CollectionsSizes()
        {
            return String.Format("\nAnimals: {0}\nWays: {1}\nJunctions: {2}", AnimalsSize(), WaysSize(), JunctionsSize());
        }

        public Node GetAnimalPosition(string name)
        {
            Animal animal = Datas.AnimalsList.Items.Find(x => x.Name.Equals(name));
            if (animal == null)
            {
                return null;
            }
            return new Node { Latitude = animal.Latitude, Longitude = animal.Longitude };
        }

        public List<Node> GetWayListOfNodes(double id)
        {
            Way way = Datas.WaysList.Items.Find(x => x.Id == id);
            if (way == null)
            {
                return null;
            }
            return way.Nodes;
        }

        public async Task Initialize()
        {
            XmlParser<Root> xmlPars = new XmlParser<Root>();

            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (myIsolatedStorage.FileExists("Data/data.xml"))
                {
                    try
                    {
                        using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Data/data.xml", FileMode.Open))
                        {
                            string xmlString;
                            using (StreamReader sr = new StreamReader(stream))
                            {
                                xmlString = await sr.ReadToEndAsync();
                            }
                            Datas = await xmlPars.DeserializeXml(xmlString);
                            if (Datas.AnimalsList.Items.Count != 0
                           && Datas.JunctionsList.Items.Count != 0
                           && Datas.WaysList.Items.Count != 0)
                            {
                                _log.Info("Loaded data.xml: {0}", CollectionsSizes());
                            }
                            else
                            {
                                throw new LackOfXmlFileException("Lack of Data");
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        if (ex is FileNotFoundException)
                        {
                            throw new LackOfXmlFileException("There is no data file", ex);
                        }
                        if (ex is InvalidOperationException)
                        {
                            throw new LackOfXmlFileException("There are errors in data file", ex);
                        }

                        throw new LackOfXmlFileException("Unknow exception", ex);
                    }
                }
                else
                {
                    try
                    {
                        string xmlString;
                        using (StreamReader sr = new StreamReader("Data/data.xml"))
                        {
                            xmlString = await sr.ReadToEndAsync();
                        }
                        Datas = await xmlPars.DeserializeXml(xmlString);

                        if (Datas.AnimalsList.Items.Count != 0
                            && Datas.JunctionsList.Items.Count != 0
                            && Datas.WaysList.Items.Count != 0)
                        {
                            _log.Info("Loaded data.xml: {0}", CollectionsSizes());
                        }
                        else
                        {
                            throw new LackOfXmlFileException("Lack of Data");
                        }

                        myIsolatedStorage.CreateDirectory("Data");
                        using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Data/data.xml", FileMode.Create))
                        {
                            using (StreamWriter sw = new StreamWriter(stream))
                            {
                                await sw.WriteAsync(xmlString);
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        if (ex is FileNotFoundException)
                        {
                            throw new LackOfXmlFileException("There is not data file", ex);
                        }
                        if (ex is InvalidOperationException)
                        {
                            throw new LackOfXmlFileException("There are errors in data file", ex);
                        }

                        throw new LackOfXmlFileException("Unknow exception", ex);
                    }
                }
            }
        }

        public int JunctionsSize()
        {
            return Datas.JunctionsList.Items.Count;
        }

        public int WaysSize()
        {
            return Datas.WaysList.Items.Count;
        }
    }
}