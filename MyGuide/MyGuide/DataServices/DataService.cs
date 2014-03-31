using Caliburn.Micro;
using MyGuide.DataServices;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Threading.Tasks;

namespace MyGuide.DataServices
{
    public class DataService : IDataService
    {
        private ILog _log;

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
            _log = LogManager.GetLog(typeof(DataService));
            XmlParser<Root> xmlPars = new XmlParser<Root>();
            try
            {
                Datas = await xmlPars.DeserializeXml("Data/data.xml");

                if (Datas.AnimalsList.Items.Count != 0
                    && Datas.JunctionsList.Items.Count != 0
                    && Datas.WaysList.Items.Count != 0)
                {
                    _log.Info("Loaded data.xml: {0}", CollectionsSizes());
                }
                else
                {
                    throw new LackOfDataException("Lack of Data");
                }
            }
            catch (Exception ex)
            {
                if (ex is FileNotFoundException)
                {
                    throw new LackOfDataException("There is not data file", ex);
                }
                if (ex is InvalidOperationException)
                {
                    throw new LackOfDataException("There are errors in data file", ex);
                }

                throw new LackOfDataException("Unknow exception", ex);
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