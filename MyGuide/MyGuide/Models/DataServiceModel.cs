using MyGuide.DataServices;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Threading.Tasks;

namespace MyGuide.Models
{
    public class DataServiceModel : IDataServiceModel
    {
        Root datas;
 
            
        public async Task Initialize()
        {
            XmlParser<Root> xmlPars = new XmlParser<Root>();
            try
            {
                datas = await xmlPars.DeserializeXml("Data/data.xml");

                if (datas.AnimalsList.Items.Count != 0
                    && datas.JunctionsList.Items.Count != 0
                    && datas.WaysList.Items.Count != 0)
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
             
                if(ex is FileNotFoundException)         
                {
                    throw new LackOfDataException("There is not data file", ex);
                }
                if(ex is InvalidOperationException)
                {       
                    throw new LackOfDataException("There are errors in data file",ex);           
                }
                
                throw new LackOfDataException("Unknow exception",ex);
            
            }
        }

        
        public Root getData()
        {
            return datas;
        }

        public void setData(Root datas)
        {
            this.datas = datas;
        }

        


        public int AnimalsSize() { return datas.AnimalsList.Items.Count; }
        public int WaysSize() { return datas.WaysList.Items.Count; }
        public int JunctionsSize() { return datas.JunctionsList.Items.Count; }

        public string CollectionsSizes()
        {
            return String.Format("Animals: {0}\nWays: {1}\nJunctions: {2}", AnimalsSize(), WaysSize(), JunctionsSize());
        }

        public Node GetAnimalPosition(string name)
        {
            Animal animal = datas.AnimalsList.Items.Find(x => x.Name.Equals(name));
            if (animal == null)
            {
                return null;
            }
            return new Node { Latitude = animal.Latitude, Longitude = animal.Longitude };
        }

        public List<Node> GetWayListOfNodes(double id)
        {
            Way way = datas.WaysList.Items.Find(x => x.Id == id);
            if (way == null)
            {
                return null;
            }
            return way.Nodes;
        }


    }

}
