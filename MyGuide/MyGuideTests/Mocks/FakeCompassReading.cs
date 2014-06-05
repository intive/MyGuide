using Microsoft.Devices.Sensors;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuideTests.Mocks
{
    public class FakeCompassReading : ICompassReading
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

        public double AccuracyHeading
        {
            get;
            set;
        }

        public DateTimeOffset Timestamp
        {
            get;
            set;
        }
    }
}
