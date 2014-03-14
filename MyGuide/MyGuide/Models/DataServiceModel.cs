using MyGuide.DataServices;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace MyGuide.Models
{
    public class DataServiceModel : IDataServiceModel
    {
        public Root Datas;
 
            
        public async Task Initialize()
        {
            XmlParser<Root> xmlPars = new XmlParser<Root>();
            try
            {
                Datas = await xmlPars.DeserializeXml("Data/data.xml");

                if (Datas.AnimalsList.Items.Count != 0
                    && Datas.JunctionsList.Items.Count != 0
                    && Datas.WaysList.Items.Count != 0)
                {
                    Debug.WriteLine(this.ToString());
                    Debug.WriteLine(CollectionsSizes());
                    
                }
                else
                {
                    throw new LackOfDataException("Lack of Data");
                }
            }
            catch(Exception ex)
            {
                if(!(ex is FileNotFoundException || ex is InvalidOperationException))
                {
                    throw new LackOfDataException("Unknow exception",ex);
                }
                else
                {
                    if(ex is FileNotFoundException)
                    {
                        throw new LackOfDataException("There is not data file", ex);
                    }
                    if(ex is InvalidOperationException)
                    {
                        throw new LackOfDataException("There are errors in data file",ex);
                    }
                }
            }
        }

        


        


        public int AnimalsSize() { return Datas.AnimalsList.Items.Count; }
        public int WaysSize() { return Datas.WaysList.Items.Count; }
        public int JunctionsSize() { return Datas.JunctionsList.Items.Count; }

        public string CollectionsSizes()
        {
            return String.Format("Animals: {0}\nWays: {1}\nJunctions: {2}", AnimalsSize(), WaysSize(), JunctionsSize());
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


    }

    public class LackOfDataException : Exception
    {
        public LackOfDataException() : base() { }
        public LackOfDataException(string message) : base(message) { }
        public LackOfDataException(string message, System.Exception inner) : base(message, inner) { }
    }
}
