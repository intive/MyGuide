using System.Collections.Generic;
using System.Xml.Serialization;

namespace MyGuide.Models
{
    public class Animal
    {
        [XmlAttribute("lat")]
        public double Latitude { get; set; }

        [XmlAttribute("lon")]
        public double Longitude { get; set; }

        [XmlText]
        public string Name { get; set; }
    }

    public class Animals
    {
        public Animals()
        {
            Items = new List<Animal>();
        }

        [XmlElement("animal")]
        public List<Animal> Items { get; set; }
    }

    public class Junction
    {
        public Junction()
        {
            HelperWay = new List<Way>();
        }

        [XmlElement("way")]
        public List<Way> HelperWay { get; set; }

        [XmlAttribute("lat")]
        public double Latitude { get; set; }

        [XmlAttribute("lon")]
        public double Longitude { get; set; }
    }

    public class Junctions
    {
        public Junctions()
        {
            Items = new List<Junction>();
        }

        [XmlElement("junction")]
        public List<Junction> Items { get; set; }
    }

    [XmlRoot("root")]
    public class Root
    {
        public Root()
        {
            AnimalsList = new Animals();
            WaysList = new Ways();
            JunctionsList = new Junctions();
        }

        [XmlElement("animals")]
        public Animals AnimalsList { get; set; }

        [XmlElement("junctions")]
        public Junctions JunctionsList { get; set; }

        [XmlElement("ways")]
        public Ways WaysList { get; set; }
    }

    public class Way
    {
        public Way()
        {
            Nodes = new List<Node>();
        }

        [XmlAttribute("id")]
        public long Id { get; set; }

        [XmlElement("node")]
        public List<Node> Nodes { get; set; }
    }

    public class Ways
    {
        public Ways()
        {
            Items = new List<Way>();
        }

        [XmlElement("way")]
        public List<Way> Items { get; set; }
    }
}