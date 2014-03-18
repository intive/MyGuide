using System.Xml.Serialization;

namespace MyGuide.Models
{
    [XmlRoot("configuration")]
    public class Configuration
    {
        [XmlElement("external_object_radius")]
        public int externalObjectRadius;

        [XmlElement("internal_object_radius")]
        public int internalObjectRadius;

        [XmlElement("lang_fallback")]
        public string langFallback;
    }
}