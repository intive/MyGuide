using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Models.MapModels;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.DataServices
{
    public class VisitedAnimalService : IAnimalService
    {
        private ILog _log = LogManager.GetLog(typeof(VisitedAnimalService));
        IDataService _dataService;
        IOptionsService _optionService;
        public IObservableCollection<AnimalPushpin> AnimalsPushpins { get; set; }
        public VisitedAnimalService(IDataService ds, IOptionsService os)
        {
            _dataService = ds;
            _optionService = os;
        }

        public async Task Initialize()
        {
            XmlParser<AnimalsList> xmlPars = new XmlParser<AnimalsList>();
            AnimalsList animalsList = new AnimalsList();
            AnimalsPushpins = new BindableCollection<AnimalPushpin>();
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (myIsolatedStorage.FileExists("Data/visitedAnimals.xml"))
                {
                    try
                    {
                        using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Data/visitedAnimals.xml", FileMode.Open))
                        {
                            string xmlString;
                            using (StreamReader sr = new StreamReader(stream))
                            {
                                xmlString = await sr.ReadToEndAsync();
                            }
                            animalsList = await xmlPars.DeserializeXml(xmlString);
                            if (animalsList.Items.Count > 0)
                            {
                                _log.Info("Loaded visitedAnimals.xml. Count: {0}", animalsList.Items.Count);
                            }
                            else
                            {
                                throw new LackOfXmlFileException("Lack of Data");
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        if (ex is FileNotFoundException)
                        {
                            throw new LackOfXmlFileException("There is no data file", ex);
                        }
                        if (ex is InvalidOperationException)
                        {
                            throw new LackOfXmlFileException("There are errors in data file", ex);
                        }

                        throw new LackOfXmlFileException("Unknow exception", ex);
                    }
                }
            }

            foreach(Animal a in animalsList.Items)
            {
                AnimalsPushpins.Add(new AnimalPushpin() { Coordinate = new GeoCoordinate(a.Latitude, a.Longitude), Name = a.Name });
            }

        }

        public IObservableCollection<AnimalPushpin> SearchForAnimals(GeoCoordinate userPosition)
        {
            foreach (Animal a in _dataService.Datas.AnimalsList.Items)
            {
                GeoCoordinate animalPosition = new GeoCoordinate(a.Latitude, a.Longitude);
                double dist = userPosition.GetDistanceTo(animalPosition);
                if (dist <= _optionService.ConfigData.internalObjectRadius && !isAnimalPushpinExist(a.Name))
                {
                    AnimalsPushpins.Add(new AnimalPushpin() { Coordinate = animalPosition, Description = "description", Name = a.Name });
                }
            }
            return AnimalsPushpins;
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

        public async Task SaveList()
        {
            XmlParser<AnimalsList> xmlPars = new XmlParser<AnimalsList>();
            AnimalsList animalsList = new AnimalsList();

            foreach (AnimalPushpin ap in AnimalsPushpins)
            {
                animalsList.Items.Add(new Animal() { Latitude = ap.Coordinate.Latitude, Longitude= ap.Coordinate.Longitude, Name = ap.Name });
            }

            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Data/visitedAnimals.xml", FileMode.Create))
                {
                    using (StreamWriter sw = new StreamWriter(stream))
                    {
                        await sw.WriteAsync(await xmlPars.SerializeXml(animalsList));
                    }
                }
            }
        }
    }
}
