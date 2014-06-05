using Caliburn.Micro;
using Microsoft.Devices.Sensors;
using Microsoft.Phone.Controls.Maps;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Models.MapModels;
using MyGuide.Services;
using MyGuide.Services.Interfaces;
using System;
using System.Device.Location;
using System.Diagnostics;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class SightseeingPageViewModel : ViewModelBase
    {
        private double _headingAngle;
        private GeoCoordinate _userPositionLocation;
        private GeoCoordinate _centerMapPositionLocation;
        private ICompassService compass { get; set; }
        private IGeolocationService geolocator { get; set; }

        public SightseeingPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService, IOptionsService optionService)
            : base(navigationService, messageDialogService, dataService, optionService)
        {
            AnimalsPushpins = new BindableCollection<AnimalPushpin>();
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

        public GeoCoordinate CenterMapPositionLocation
        {
            get { return _centerMapPositionLocation; }
            set { _centerMapPositionLocation = value; NotifyOfPropertyChange(() => CenterMapPositionLocation); }
        }

        #region Commands
        //Uncomment all method and set some instructions when we'll implement appbar clickable
        //Remeber to add icons for appbar buttons!

        public void ShowAnimals()
        {
            WritePushpinList();
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

        public override void OnNavigatedFrom(NavigationMode navigationMode)
        {
            Task.Run(() =>
            {

                try
                {
                    if (Compass.IsSupported)
                    {

                        compass.Stop();
                        
                    }
                }
                finally
                {


                    compass.Calibrate -= compass_Calibrate;
                    compass.CurrentValueChanged -= compass_CurrentValueChanged;

                    //Geolocator don't want to unsubscribe when start button clicked sometimes
                    geolocator.StopGeolocationTracker();
                    geolocator.PositionChanged -= geolocator_PositionChanged;
                }
            });
           
        }

        ~SightseeingPageViewModel()
        {
        }

        public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            UserPositionLocation = new GeoCoordinate(51.104642, 17.073520);
            CenterMapPositionLocation = new GeoCoordinate(51.104642, 17.073520);
            UserLayerVisibility = _optionService.ConfigData.userLayerVisibility;

            geolocator = new GeolocationService();
            geolocator.PositionChanged += new EventHandler < IGeolocationReading > (geolocator_PositionChanged);

            if (Compass.IsSupported)
            {
                compass = new RealCompassService();

                //Crucial point
                compass.TimeBetweenUpdates = TimeSpan.FromMilliseconds(200);
                compass.Calibrate += new EventHandler<CalibrationEventArgs>(compass_Calibrate);
                compass.CurrentValueChanged += new EventHandler<SensorReadingEventArgs<ICompassReading>>(compass_CurrentValueChanged);
                
                compass.Start();
            }
            else
            {
                compass = new EmulatedCompassService();
                compass.TimeBetweenUpdates = TimeSpan.FromMilliseconds(200);
                compass.Calibrate += new EventHandler<CalibrationEventArgs>(compass_Calibrate);
                compass.CurrentValueChanged += new EventHandler<SensorReadingEventArgs<ICompassReading>>(compass_CurrentValueChanged);

                compass.Start();
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

        private void geolocator_PositionChanged(object sender, IGeolocationReading e)
        {

            UserPositionLocation = new GeoCoordinate(e.Position.Coordinate.Latitude, e.Position.Coordinate.Longitude);
            CenterMapPositionLocation = new GeoCoordinate(e.Position.Coordinate.Latitude, e.Position.Coordinate.Longitude);
            SearchForAnimals();
      
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

        #region AnimalsPushpins
        private IObservableCollection<AnimalPushpin> animalsPushpins { get; set; }
        public IObservableCollection<AnimalPushpin> AnimalsPushpins
        {
            get
            {
                return animalsPushpins;
            }
            set
            {
                animalsPushpins = value; 
                NotifyOfPropertyChange(() => AnimalsPushpins);
            }
        }

        public void SearchForAnimals()
        {
            foreach(Animal a in _dataService.Datas.AnimalsList.Items)
            {
                GeoCoordinate animalPosition = new GeoCoordinate(a.Latitude, a.Longitude);
                double dist = UserPositionLocation.GetDistanceTo(animalPosition);
                if (dist <= _optionService.ConfigData.internalObjectRadius && !isAnimalPushpinExist(a.Name))
                {
                    AnimalsPushpins.Add(new AnimalPushpin() { Coordinate = animalPosition, Description = "description", Name = a.Name });
                }
            }   
        }

        private bool isAnimalPushpinExist(string animalName)
        {
            bool result = false;
            foreach (AnimalPushpin ap in AnimalsPushpins)
            {
                if (ap.Name != null && ap.Name.Equals(animalName))
                {
                    result = true;
                }
            }
            return result;
        }

        public void WritePushpinList()
        {
            foreach (AnimalPushpin ap in AnimalsPushpins)
            {
                Debug.WriteLine(ap.Name + " " + ap.Coordinate.Latitude + " " + ap.Coordinate.Longitude);
            }
        }

       
        #endregion AnimalsPushpins
    }
}