using Microsoft.Devices.Sensors;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services.Interfaces
{
    public interface ICompassReading : ISensorReading
    {
        double MagneticNorthHeading { get; set; }
        double TrueNorthHeading { get; set; }
        double AccuracyHeading { get; set; }
        
    }
}
