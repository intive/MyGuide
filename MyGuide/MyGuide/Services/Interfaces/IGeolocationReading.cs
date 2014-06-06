using Microsoft.Devices.Sensors;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;

namespace MyGuide.Services.Interfaces
{
    public interface IGeolocationReading 
    {
        GeoCoordinate Position { get; set; }
    }
}
