using MyGuide.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace MyGuide.DataServices.Interfaces
{
    public interface IDataService
    {
        int AnimalsSize();

        string CollectionsSizes();

        Node GetAnimalPosition(string name);

        List<Node> GetWayListOfNodes(double id);

        Task Initialize();

        int JunctionsSize();

        int WaysSize();
    }
}