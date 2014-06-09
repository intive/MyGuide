using MyGuide.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace MyGuide.DataServices.Interfaces
{
    public interface IDataService
    {
        Root Datas { get; set; }

        int AnimalsSize();

        string CollectionsSizes();

        Node GetAnimalPosition(string name);

        List<Node> GetWayListOfNodes(double id);

        Task Initialize();

        int JunctionsSize();

        int WaysSize();
    }
}