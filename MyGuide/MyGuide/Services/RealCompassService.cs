using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Devices.Sensors;

namespace MyGuide.Services
{
    public class RealCompassService : ICompassService
    {
        Compass _compass;

        public RealCompassService()
        {
            _compass = new Compass();
            
            _compass.CurrentValueChanged += new EventHandler<SensorReadingEventArgs<Microsoft.Devices.Sensors.CompassReading>>(_compass_CurrentValueChanged);
            _compass.Calibrate += new EventHandler<CalibrationEventArgs>(_compass_Calibrate);
        }

        private void _compass_Calibrate(object sender, CalibrationEventArgs e)
        {
           var handler = Calibrate;
           if (handler != null)
           {
               Calibrate(this, e);
           }
            
        }

        private void _compass_CurrentValueChanged(object sender, SensorReadingEventArgs<Microsoft.Devices.Sensors.CompassReading> e)
        {
             var handler = CurrentValueChanged;
             if (handler != null)
             {

                 var reading = new CompassReading()
                 {
                     MagneticNorthHeading = e.SensorReading.MagneticHeading,
                     TrueNorthHeading = e.SensorReading.TrueHeading,
                     Timestamp = e.SensorReading.Timestamp,
                     AccuracyHeading = e.SensorReading.HeadingAccuracy
                 };
                 CurrentValueChanged(this, new SensorReadingEventArgs<ICompassReading>() { SensorReading = reading });
             }
        }

        public event EventHandler<CalibrationEventArgs> Calibrate;

        public event EventHandler<SensorReadingEventArgs<ICompassReading>> CurrentValueChanged;

        public TimeSpan TimeBetweenUpdates
        {
            get
            {
                return _compass.TimeBetweenUpdates;
            }
            set
            {
                _compass.TimeBetweenUpdates = value;
            }
        }

        public void Start()
        {
            _compass.Start();
        }

        public void Stop()
        {

            Task.Run(() =>
            {

                try
                {
                    _compass.Stop();
                }
                finally
                {
                    _compass.CurrentValueChanged -= _compass_CurrentValueChanged;
                    _compass.Calibrate -= _compass_Calibrate;
                }
            });
        }
    }
}
