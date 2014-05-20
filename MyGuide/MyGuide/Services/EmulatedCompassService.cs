using System;
using Microsoft.Devices.Sensors;
using System.Threading;
using MyGuide.Services.Interfaces;

namespace MyGuide.Services
{
    public class EmulatedCompassService : ICompassService
    {
        Timer simulatorTimer;
        TimeSpan _timeBetweenUpdates;
        Accelerometer _accelerometer;
        private const double MagneticDeclination = 4;

        public event EventHandler<Microsoft.Devices.Sensors.SensorReadingEventArgs<ICompassReading>> CurrentValueChanged;

        void OnCurrentValueChanged(object sender, SensorReadingEventArgs<ICompassReading> e)
        {
            var handler = CurrentValueChanged;
            if (handler != null)
            {
                CurrentValueChanged(this, e);
            }
        }

        public void Start()
        {
            _accelerometer = new Accelerometer();
            _accelerometer.Start();
            simulatorTimer = new Timer(SimmulateValueChange);
            simulatorTimer.Change((long)_timeBetweenUpdates.TotalMilliseconds, (long)_timeBetweenUpdates.TotalMilliseconds);

        }

        public void Stop()
        {
            _accelerometer.Stop();
        }

        private void SimmulateValueChange(object context)
        {
            try
            {
                var fakeCompassAngle = GetCompassFromAccelerometer();
                var fakeCompassReading = new CompassReading()
                {
                    MagneticNorthHeading = fakeCompassAngle,
                    TrueNorthHeading = (fakeCompassAngle + MagneticDeclination) % 360,
                    Timestamp = DateTime.Now,
                    AccuracyHeading = 0.0
                };
                if (fakeCompassReading.MagneticNorthHeading > -1)
                {
                    OnCurrentValueChanged(this, new SensorReadingEventArgs<ICompassReading>() { SensorReading = fakeCompassReading });
                }
            }
            catch
            {
            }
        }

        private double GetCompassFromAccelerometer()
        {
            var reading = _accelerometer.CurrentValue;
            var x = reading.Acceleration.X;
            var y = reading.Acceleration.Z;
            var angle = Math.Acos(-y / Math.Sqrt(Math.Pow(x, 2) + Math.Pow(y, 2)));
            if (x > 0)
            {
                return (angle / (2 * Math.PI)) * 360;
            }
            else
            {
                return 360 - (angle / (2 * Math.PI)) * 360;
            }
        }

        public event EventHandler<CalibrationEventArgs> Calibrate;

        void OnCalibrate(object sender, CalibrationEventArgs e)
        {
            var handler = Calibrate;
            if (handler != null)
            {
                Calibrate(this, e);
            }
        }

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
    }
}
