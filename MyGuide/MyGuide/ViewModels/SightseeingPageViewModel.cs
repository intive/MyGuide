using Caliburn.Micro;
using Microsoft.Devices.Sensors;
using Microsoft.Xna.Framework;
using MyGuide.DataServices.Interfaces;
using MyGuide.Services.Interfaces;
using System;
using System.Device.Location;
using System.Diagnostics;
using System.Threading;
using System.Windows.Navigation;
using Windows.Devices.Geolocation;
using Windows.Foundation;

namespace MyGuide.ViewModels
{
    public class SightseeingPageViewModel : ViewModelBase
    {
        private double _headingAngle;
        private GeoCoordinate _userPositionLocation;
        private Compass compass;
        private Geolocator geolocator;

        public SightseeingPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService, IOptionsService optionService)
            : base(navigationService, messageDialogService, dataService, optionService)
        {
        }

        public double HeadingAngle
        {
            get { return _headingAngle; }
            set { _headingAngle = value; NotifyOfPropertyChange(() => HeadingAngle); }
        }

        public GeoCoordinate UserPositionLocation
        {
            get { return _userPositionLocation; }
            set { _userPositionLocation = value; NotifyOfPropertyChange(() => UserPositionLocation); }
        }

        #region Commands
        //Uncomment all method and set some instructions when we'll implement appbar clickable
        //Remeber to add icons for appbar buttons!

        //public void ShowAnimals()
        //{
        //}

        //public void ShowMap()
        //{
        //}

        //public void ShowInformation()
        //{
        //}

        //public void ShowGastronomy()
        //{
        //}

        //public void ShowHistory()
        //{
        //}

        //public void ShowPreferences()
        //{
        //}

        #endregion Commands

        public override void OnNavigatedFrom(NavigationMode navigationMode)
        {
            compass.CurrentValueChanged -= compass_ReadingChanged;
            Thread.Sleep(100);
            if (Compass.IsSupported)
            {
                Debug.WriteLine(compass.IsDataValid);
                compass.Stop();
                Debug.WriteLine(compass.IsDataValid);
            }

            Debug.WriteLine(geolocator.LocationStatus);
            geolocator.PositionChanged -= geolocator_PositionChanged;
            Debug.WriteLine(geolocator.LocationStatus);
        }

        public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            UserPositionLocation = new GeoCoordinate(51.104642, 17.073520);

            geolocator = new Geolocator();
            geolocator.MovementThreshold = 1;
            geolocator.DesiredAccuracy = PositionAccuracy.High;
            geolocator.PositionChanged += geolocator_PositionChanged;

            if (Compass.IsSupported)
            {
                compass = new Compass();
                //It is a crucial point. TimeBetweenUpdates should be multiple of 20.
                //But I say that when it is 100ms, there is problem 9/10 with ending
                //event when event hendler is removing in OnNavigatedFrom.
                compass.TimeBetweenUpdates = TimeSpan.FromMilliseconds(400);

                compass.CurrentValueChanged += new EventHandler<SensorReadingEventArgs<CompassReading>>(compass_ReadingChanged);
                compass.Start();
            }
        }

        #region UserMarker

        private void compass_ReadingChanged(object sender, SensorReadingEventArgs<CompassReading> e)
        {
            HeadingAngle = e.SensorReading.MagneticHeading;
        }

        private void geolocator_PositionChanged(Geolocator sender, PositionChangedEventArgs args)
        {
            UserPositionLocation = new GeoCoordinate(args.Position.Coordinate.Latitude, args.Position.Coordinate.Longitude);
        }

        #endregion UserMarker
    }
}