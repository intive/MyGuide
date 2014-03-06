using MyGuide.DataServices;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Models
{
    public class DataServiceModel : IDataServiceModel
    {
        public Root Datas;
        public DataServiceModel()
        {
            XmlParser xmlPars = new XmlParser();
            Datas = xmlPars.DeserializeXml("Data/data.xml");

            Debug.WriteLine(this.ToString());
            Debug.WriteLine(CollectionsSizes());
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
}
