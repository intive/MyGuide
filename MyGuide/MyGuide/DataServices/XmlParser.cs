using System.IO;
using System.Threading.Tasks;
using System.Xml.Serialization;


namespace MyGuide.DataServices
{
    public class XmlParser<T> : IXmlParser<T>
    {

        
        public Task<T> DeserializeXml(string dataPath)
        {
            return Task.Run(() =>
            {
                XmlSerializer xmlDeserializer = new XmlSerializer(typeof(T));
                object obj;
                using (TextReader reader = new StreamReader(dataPath))
                {
                    obj = xmlDeserializer.Deserialize(reader);
                    
                }

                return (T)obj;
                
            });
        }

        public string SerializeXml(T objectToSerialize)
        {
            XmlSerializer xmlSerializer = new XmlSerializer(objectToSerialize.GetType());
            StringWriter stringWriter = new StringWriter();
            xmlSerializer.Serialize(stringWriter, objectToSerialize);
            return stringWriter.ToString();
           
        }
    }

}
