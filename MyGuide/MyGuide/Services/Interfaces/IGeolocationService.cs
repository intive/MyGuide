using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Geolocation;

namespace MyGuide.Services.Interfaces
{
    public interface IGeolocationService
    {
        event EventHandler<IGeolocationReading> PositionChanged;

        void StopGeolocationTracker();

        void StartGeolocationTracker();
    }
}
