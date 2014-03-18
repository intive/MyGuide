using System.Threading.Tasks;

namespace MyGuide.DataServices.Interfaces
{
    public interface IXmlParser<T>
    {
        Task<T> DeserializeXml(string dataPath);

        string SerializeXml(T objectToSerialize);
    }
}