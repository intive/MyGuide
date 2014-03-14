using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MyGuide.DataServices;
using Windows.Storage;

namespace MyGuide.Models
{
    public class OptionServiceModel : IOptionServiceModel
    {
        public async Task Initialize()
        {
            XmlParser<Configuration> xmlPars = new XmlParser<Configuration>();
            var file = await Windows.Storage.StorageFile.GetFileFromApplicationUriAsync(new Uri("ms-appx:///Data/config.xml"));
            
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                myIsolatedStorage.CreateDirectory("Options");
                myIsolatedStorage.CreateFile("Options/config.xml");
                StorageFile file2 = await StorageFile.GetFileFromPathAsync("isostore:/Options/config.xml");
                using (Stream s = await file2.OpenStreamForWriteAsync())
                {
                    using (Stream stream = await file.OpenStreamForReadAsync())
                    {
                        await stream.CopyToAsync(s);
                    }
                }
            }
        }
    }
}
