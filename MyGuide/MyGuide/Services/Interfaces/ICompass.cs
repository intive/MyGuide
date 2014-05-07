using Microsoft.Devices.Sensors;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services.Interfaces
{
    public interface ICompass
    {
        event EventHandler<CalibrationEventArgs> Calibrate;

        event EventHandler<SensorReadingEventArgs<CompassReading>> CurrentValueChanged;

        TimeSpan TimeBetweenUpdates { get; set; }

        void Start();
    }
}