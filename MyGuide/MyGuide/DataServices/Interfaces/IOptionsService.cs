using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.DataServices.Interfaces
{
    public interface IOptionsService
    {
        Configuration ConfigData { get; set; }

        Task Initialize();

        Task SaveOptions();
    }
}