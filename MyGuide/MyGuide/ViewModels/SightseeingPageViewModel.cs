using Caliburn.Micro;
using Microsoft.Devices.Sensors;
using Microsoft.Xna.Framework;
using MyGuide.DataServices.Interfaces;
using MyGuide.Services;
using MyGuide.Services.Interfaces;
using System;
using System.Device.Location;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Threading;
using Windows.Devices.Geolocation;
using Windows.Foundation;
using Windows.UI.Core;

namespace MyGuide.ViewModels
{
    public class SightseeingPageViewModel : ViewModelBase
    {
        private double _headingAngle;
        private GeoCoordinate _userPositionLocation;
        private ICompassService _compassService { get; set; }
        private IGeolocationService _geolocationService { get; set; }

        public SightseeingPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService, IOptionsService optionService, ICompassService compassService, IGeolocationService geolocationService)
            : base(navigationService, messageDialogService, dataService, optionService)
        {
            _compassService = compassService;
            _geolocationService = geolocationService;
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
            Task.Run(() =>
            {

                try
                {
                    if (Compass.IsSupported)
                    {

                        _compassService.Stop();
                        
                    }
                }
                finally
                {


                    _compassService.Calibrate -= compass_Calibrate;
                    _compassService.CurrentValueChanged -= compass_CurrentValueChanged;

                    //_geolocationService don't want to unsubscribe when start button clicked sometimes
                    _geolocationService.StopGeolocationTracker();
                    _geolocationService.PositionChanged -= _geolocationService_PositionChanged;
                }
            });
           
        }

        public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            UserPositionLocation = new GeoCoordinate(51.104642, 17.073520);
            UserLayerVisibility = _optionService.ConfigData.userLayerVisibility;

            _geolocationService.PositionChanged += new EventHandler < IGeolocationReading > (_geolocationService_PositionChanged);

            if (_compassService.IsSupported)
            {

                //Crucial point
                _compassService.TimeBetweenUpdates = TimeSpan.FromMilliseconds(200);
                _compassService.Calibrate += new EventHandler<CalibrationEventArgs>(compass_Calibrate);
                _compassService.CurrentValueChanged += new EventHandler<SensorReadingEventArgs<ICompassReading>>(compass_CurrentValueChanged);

                _compassService.Start();
            }

        }

        

        #region UserMarker

        private void compass_CurrentValueChanged(object sender, SensorReadingEventArgs<ICompassReading> e)
        {
            HeadingAngle = e.SensorReading.MagneticNorthHeading;
            HeadingAccuracy = e.SensorReading.AccuracyHeading;
            if (HeadingAccuracy <= 10 && calibrationStackPanelVisibility)
            {
                Brush brush;

                Deployment.Current.Dispatcher.BeginInvoke(() =>
                {
                    brush = new SolidColorBrush(Colors.Green);
                    CalibrationButtonColor = brush; ;
                });
                
            }
        }

        private void _geolocationService_PositionChanged(object sender, IGeolocationReading e)
        {
            UserPositionLocation = new GeoCoordinate(e.Position.Latitude, e.Position.Longitude);
        }

        private bool userLayerVisibility;
        public bool UserLayerVisibility
        {
            get
            {
                return userLayerVisibility;
            }
            set
            {
                userLayerVisibility = value;
                NotifyOfPropertyChange(() => UserLayerVisibility);
            }
        }

        #endregion UserMarker



        #region CalibrationStackPanel

        private void compass_Calibrate(object sender, CalibrationEventArgs e)
        {
            CalibrationStackPanelVisibility = true;

        }

        private double headingAccuracy;
        public double HeadingAccuracy
        {
            get
            {
                return headingAccuracy;
            }
            set
            {
                headingAccuracy = value; NotifyOfPropertyChange(() => HeadingAccuracy);
            }
        }

        private Brush calibrationButtonColor;
        public Brush CalibrationButtonColor
        {
            get
            {
                return calibrationButtonColor;
            }
            set
            {
                calibrationButtonColor = value; NotifyOfPropertyChange(() => CalibrationButtonColor);
            }
        }

        private bool calibrationStackPanelVisibility;
        public bool CalibrationStackPanelVisibility
        {
            get
            {
                return calibrationStackPanelVisibility;
            }
            set
            {
                calibrationStackPanelVisibility = value; NotifyOfPropertyChange(() => CalibrationStackPanelVisibility);
            }
        }

        public void CalibrationButton()
        {
            CalibrationStackPanelVisibility = false;
            
        }
        #endregion CalibrationStackPanel
    }
}