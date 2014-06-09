using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace MyGuide.Models.MapModels
{
    public class AnimalPushpin
    {
        public GeoCoordinate Coordinate {get; set;}
        public string Name { get; set; }
        public string Description { get; set; } 
    }
    [XmlRoot("animals_list")]
    public class AnimalsList
    {
        
        public AnimalsList()
        {
            Items = new List<Animal>();
        }

        [XmlElement("animal")]
        public List<Animal> Items { get; set; }
    }
}
