using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.DataServices
{
    interface IXmlParser<T>
    {
        Task<T> DeserializeXml(string dataPath);
        string SerializeXml(T objectToSerialize);
    }
}
