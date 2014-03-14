using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Resources;
using System.Xml;
using System.Xml.Serialization;
using Windows.Storage;


namespace MyGuide.DataServices
{
    public class XmlParser<T> : IXmlParser<T>
    {

        private XmlSerializer xmlSerializer;
        public async Task<T> DeserializeXml(string dataPath)
        {
            return await Task.Run(() =>
            {
                xmlSerializer = new XmlSerializer(typeof(T));
                T xmlData;
                using (TextReader reader = new StreamReader(dataPath))
                {
                    object obj = xmlSerializer.Deserialize(reader);
                    xmlData = (T)obj;
                }

                return xmlData;
            });
        }

        public string SerializeXml(T objectToSerialize)
        {
            xmlSerializer = new XmlSerializer(objectToSerialize.GetType());
            StringWriter stringWriter = new StringWriter();
            xmlSerializer.Serialize(stringWriter, objectToSerialize);
            return stringWriter.ToString();
           //TODO: add serialization to file in 'dataPath' path
        }
    }

}
