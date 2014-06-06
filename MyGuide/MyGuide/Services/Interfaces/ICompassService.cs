using Microsoft.Devices.Sensors;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Services.Interfaces
{
    public interface ICompassService
    {
        event EventHandler<CalibrationEventArgs> Calibrate;

        event EventHandler<SensorReadingEventArgs<ICompassReading>> CurrentValueChanged;

        TimeSpan TimeBetweenUpdates { get; set; }

        void Start();

        void Stop();

        bool IsSupported { get; }
    }
}