using Microsoft.Devices.Sensors;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace MyGuide.Services
{
    public class FakeCompassService : ICompassService
    {

        Timer simulatorTimer;
        TimeSpan _timeBetweenUpdates;
        double MagneticDeclination = 4.0;

        public event EventHandler<Microsoft.Devices.Sensors.CalibrationEventArgs> Calibrate;

        public event EventHandler<Microsoft.Devices.Sensors.SensorReadingEventArgs<ICompassReading>> CurrentValueChanged;

        public TimeSpan TimeBetweenUpdates
        {
            get
            {
                return _timeBetweenUpdates;
            }
            set
            {
                _timeBetweenUpdates = value;
            }
        }

        public void Start()
        {
            simulatorTimer = new Timer(SimmulateValueChange);
            simulatorTimer.Change((long)_timeBetweenUpdates.TotalMilliseconds, (long)_timeBetweenUpdates.TotalMilliseconds);

        }

        private double GetFakeAngle()
        {
            Random r = new Random();
           
            return (r.NextDouble() * 360);
        }

        private void SimmulateValueChange(object state)
        {
            var fakeCompassReading = new CompassReading()
            {
                MagneticNorthHeading = GetFakeAngle(),
                TrueNorthHeading = (GetFakeAngle() + MagneticDeclination) % 360,
                Timestamp = DateTime.Now,
                AccuracyHeading = 0.0
            };
            if (fakeCompassReading.MagneticNorthHeading > -1)
            {
                OnCurrentValueChanged(this, new SensorReadingEventArgs<ICompassReading>() { SensorReading = fakeCompassReading });
            }
        }

        void OnCurrentValueChanged(object sender, SensorReadingEventArgs<ICompassReading> e)
        {
            var handler = CurrentValueChanged;
            if (handler != null)
            {
                CurrentValueChanged(this, e);
            }
        }

        public void Stop()
        {
            simulatorTimer = null;
        }
    }
}
