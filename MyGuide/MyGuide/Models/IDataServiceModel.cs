using System.Collections.Generic;
using System.Threading.Tasks;


namespace MyGuide.Models
{
    public interface IDataServiceModel
    {
        Task Initialize();
        int AnimalsSize();
        int WaysSize();
        int JunctionsSize();
        string CollectionsSizes();
        Node GetAnimalPosition(string name);
        List<Node> GetWayListOfNodes(double id);
    }
}
