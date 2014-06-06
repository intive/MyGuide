using MyGuide.DataServices.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuideTests.Mocks
{
    public class DataServiceMock :IDataService
    {
        public MyGuide.Models.Root Datas
        {
            get
            {
                return new MyGuide.Models.Root();
            }
            set
            {
                ;
            }
        }

        public int AnimalsSize()
        {
            throw new NotImplementedException();
        }

        public string CollectionsSizes()
        {
            throw new NotImplementedException();
        }

        public MyGuide.Models.Node GetAnimalPosition(string name)
        {
            throw new NotImplementedException();
        }

        public List<MyGuide.Models.Node> GetWayListOfNodes(double id)
        {
            throw new NotImplementedException();
        }

        public Task Initialize()
        {
            throw new NotImplementedException();
        }

        public int JunctionsSize()
        {
            throw new NotImplementedException();
        }

        public int WaysSize()
        {
            throw new NotImplementedException();
        }
    }
}
