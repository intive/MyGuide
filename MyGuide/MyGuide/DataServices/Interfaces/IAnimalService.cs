using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Caliburn.Micro;
using MyGuide.Models.MapModels;
using System.Device.Location;


namespace MyGuide.DataServices.Interfaces
{
    public interface IAnimalService
    {
        Task Initialize();
        Task SaveList();
        Task<IObservableCollection<AnimalPushpin>> SearchForAnimals(GeoCoordinate userPosition);
        Task DeleteVisitedAnimalsMark();
    }
}


