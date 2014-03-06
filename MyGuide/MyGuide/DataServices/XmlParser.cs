using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Resources;
using System.Xml.Serialization;


namespace MyGuide.DataServices
{
    public class XmlParser
    {
        public Root DeserializeXml(string dataPath)
        {
            XmlSerializer deserializer = new XmlSerializer(typeof(Root));
            TextReader reader = new StreamReader(dataPath);
            object obj = deserializer.Deserialize(reader);
            Root xmlData = (Root)obj;
            reader.Close();
            return xmlData;
        }
    }

}
