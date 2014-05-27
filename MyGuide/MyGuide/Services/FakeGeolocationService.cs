using Microsoft.Devices.Sensors;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;

namespace MyGuide.Services
{
    public class FakeGeolocationService : IGeolocationService
    {
        Timer simulatorTimer;
        TimeSpan _timeBetweenUpdates;
        public FakeGeolocationService()
        {
            simulatorTimer = new Timer(SimmulateValueChange);
            simulatorTimer.Change((long)_timeBetweenUpdates.TotalMilliseconds, (long)_timeBetweenUpdates.TotalMilliseconds);
        }

        private void SimmulateValueChange(object state)
        {
            var fakeGeolocationReading = new GeolocationReading()
            {
                Position = GetFakePosition()
            };

            _geolocator_PositionChanged(this, fakeGeolocationReading);

        }

        private GeoCoordinate GetFakePosition()
        {

            return new GeoCoordinate(51.1047078,17.0784479);
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


        public event EventHandler<IGeolocationReading> PositionChanged;

        private void _geolocator_PositionChanged(object sender, IGeolocationReading args)
        {
            var handler = PositionChanged;
            if (handler != null)
            {
                PositionChanged(this, new GeolocationReading() { Position = args.Position });
            }
        }


        public void StopGeolocationTracker()
        {
            simulatorTimer = null;
        }

    }
}
