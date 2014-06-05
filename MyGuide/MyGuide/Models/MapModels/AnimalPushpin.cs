using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Models.MapModels
{
    public class AnimalPushpin
    {

        public GeoCoordinate Coordinate {get; set;}
        public string Name { get; set; }
        public string Description { get; set; } 
    }
}
