using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using System;
using System.Diagnostics;
using System.IO;
using System.IO.IsolatedStorage;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Serialization;

namespace MyGuide.DataServices
{
    public class XmlParser<T> : IXmlParser<T>
    {
        public Task<T> DeserializeXml(string xmlDocumentString)
        {
            return Task.Run(() =>
            {
                XmlSerializer xmlDeserializer = new XmlSerializer(typeof(T));

                using (TextReader reader = new StringReader(xmlDocumentString))
                {
                    return (T)xmlDeserializer.Deserialize(reader);
                }
            });
        }

        public Task<string> SerializeXml(T objectToSerialize)
        {
            return Task.Run(() =>
                {
                    XmlSerializer xmlSerializer = new XmlSerializer(objectToSerialize.GetType());

                    StringWriter stringWriter = new StringWriter();

                    xmlSerializer.Serialize(stringWriter, objectToSerialize);

                    return stringWriter.ToString();
                });
        }
    }
}