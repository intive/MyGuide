using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;

namespace MyGuide.Services
{
    public class GeolocationReading : IGeolocationReading
    {
        public Geoposition Position 
        { 
            get; 
            set; 
        } 
    }
}
