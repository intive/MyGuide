using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services
{
    public class CompassReading : ICompassReading
    {
        public double MagneticNorthHeading 
        { 
            get; 
            set; 
        }

        public double TrueNorthHeading 
        { 
            get; 
            set; 
        }

        public DateTimeOffset Timestamp 
        { 
            get; 
            set; 
        }

        public double AccuracyHeading
        {
            get;
            set;
        }
    }
}
