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
