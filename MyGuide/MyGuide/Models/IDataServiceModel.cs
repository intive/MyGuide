using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace MyGuide.Models
{
    public interface IDataServiceModel
    {
        int AnimalsSize();
        int WaysSize();
        int JunctionsSize();
        string CollectionsSizes();
        Node GetAnimalPosition(string name);
        List<Node> GetWayListOfNodes(double id);
    }
}
