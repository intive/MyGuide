using System.Xml.Serialization;

namespace MyGuide.Models
{
    [XmlRoot("configuration")]
    public class Configuration
    {
        [XmlElement("external_object_radius")]
        public int externalObjectRadius { get; set; }

        [XmlElement("internal_object_radius")]
        public int internalObjectRadius { get; set; }

        [XmlElement("lang_fallback")]
        public string langFallback { get; set; }

        [XmlElement("user_layer_visibility")]
        public bool userLayerVisibility { get; set; }
    }
}