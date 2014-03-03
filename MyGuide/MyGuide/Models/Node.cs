using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace MyGuide.Models
{
    public class Node
    {
        [XmlAttribute("lat")]
        public double Latitude { get; set; }
        [XmlAttribute("lon")]
        public double Longitude { get; set; }
    }
}
