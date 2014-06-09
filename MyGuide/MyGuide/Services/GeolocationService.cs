using Microsoft.Devices.Sensors;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;

namespace MyGuide.Services
{
    public class GeolocationService : IGeolocationService
    {
        Geolocator _geolocator; 
        public GeolocationService()
        {   
        }

        public event EventHandler<IGeolocationReading> PositionChanged;

        private void _geolocator_PositionChanged(Geolocator sender, PositionChangedEventArgs args)
        {
            var handler = PositionChanged;
            if (handler != null)
            {
                PositionChanged(this, new GeolocationReading() { Position = new GeoCoordinate(args.Position.Coordinate.Latitude, args.Position.Coordinate.Longitude) });
            }
        }
        
        public void StartGeolocationTracker()
        {
            _geolocator = new Geolocator();
            _geolocator.MovementThreshold = 1;
            _geolocator.DesiredAccuracy = PositionAccuracy.High;
            Task.Run(() =>
            {
                _geolocator.PositionChanged += _geolocator_PositionChanged;
            });
        }

        public void StopGeolocationTracker()
        {
            Task.Run(() =>
            {
                _geolocator.PositionChanged -= _geolocator_PositionChanged;
                _geolocator = null;
            });
        }

    }
}
