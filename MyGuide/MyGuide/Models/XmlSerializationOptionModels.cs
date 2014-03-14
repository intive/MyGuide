using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;


namespace MyGuide.Models
{

    [XmlRoot("configuration")]
    public class Configuration
    {
        [XmlElement("lang_fallback")]
        public string langFallback;
        [XmlElement("internal_object_radius")]
        public int internalObjectRadius;
        [XmlElement("external_object_radius")]
        public int externalObjectRadius;
    }

 

}
