using Caliburn.Micro;
using Microsoft.Phone.Controls.Maps.Platform;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Services.Interfaces;
using System;
using System.Device.Location;
using System.Windows;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using Windows.Devices.Geolocation;
using Windows.Devices.Sensors;

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

        public void ShowAnimals()
        {
            UserPositionLocation = new GeoCoordinate(51.104942, 17.073820);
        }

        public void ShowMap()
        {
            UserPositionLocation = new GeoCoordinate(51.104942, 17.073520);
        }

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

        #region UserMarker

        public override void OnNavigatedFrom(NavigationMode navigationMode)
        {
            geolocator.PositionChanged -= geolocator_PositionChanged;
            compass.ReadingChanged -= compass_ReadingChanged;
        }

        public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            UserPositionLocation = new GeoCoordinate(51.104642, 17.073520);

            geolocator = new Geolocator();
            geolocator.MovementThreshold = 1;
            geolocator.DesiredAccuracy = PositionAccuracy.High;

            geolocator.PositionChanged += geolocator_PositionChanged;

            compass = Compass.GetDefault();

            uint minReportInterval = compass.MinimumReportInterval;
            uint reportInterval = minReportInterval > 16 ? minReportInterval : 16;
            compass.ReportInterval = reportInterval;

            compass.ReadingChanged += compass_ReadingChanged;
        }

        private void compass_ReadingChanged(object sender, CompassReadingChangedEventArgs e)
        {
            HeadingAngle = e.Reading.HeadingMagneticNorth;
        }

        private void geolocator_PositionChanged(Geolocator sender, PositionChangedEventArgs args)
        {
            UserPositionLocation = new GeoCoordinate(args.Position.Coordinate.Latitude, args.Position.Coordinate.Longitude); ;
        }

        #endregion UserMarker
    }
}