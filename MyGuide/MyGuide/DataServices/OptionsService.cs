using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using System;
using System.IO;
using System.IO.IsolatedStorage;
using System.Reflection;
using System.Threading.Tasks;

namespace MyGuide.DataServices
{
    public class OptionsService : IOptionsService
    {
        private ILog _log = LogManager.GetLog(typeof(DataService));

        public Configuration ConfigData { get; set; }

        public async Task Initialize()
        {
            XmlParser<Configuration> xmlPars = new XmlParser<Configuration>();

            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (myIsolatedStorage.FileExists("Options/config.xml"))
                {
                    try
                    {
                        using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Options/config.xml", FileMode.Open))
                        {
                            string xmlString;
                            using (StreamReader sr = new StreamReader(stream))
                            {
                                xmlString = await sr.ReadToEndAsync();
                            }
                            ConfigData = await xmlPars.DeserializeXml(xmlString);
                        }
                    }
                    catch (Exception ex)
                    {
                        if (ex is FileNotFoundException)
                        {
                            throw new LackOfXmlFileException("There is not options file", ex);
                        }
                        if (ex is InvalidOperationException)
                        {
                            throw new LackOfXmlFileException("There are errors in option file", ex);
                        }

                        throw new LackOfXmlFileException("Unknow exception", ex);
                    }
                }
                else
                {
                    try
                    {
                        string xmlString;
                        using (StreamReader sr = new StreamReader("Data/config.xml"))
                        {
                            xmlString = await sr.ReadToEndAsync();
                        }

                        ConfigData = await xmlPars.DeserializeXml(xmlString);

                        myIsolatedStorage.CreateDirectory("Options");
                        using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Options/config.xml", FileMode.Create))
                        {
                            using (StreamWriter sw = new StreamWriter(stream))
                            {
                                await sw.WriteAsync(xmlString);
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        if (ex is FileNotFoundException)
                        {
                            throw new LackOfXmlFileException("There is not options file in the package!", ex);
                        }
                        if (ex is InvalidOperationException)
                        {
                            throw new LackOfXmlFileException("There are errors in option file in the package", ex);
                        }

                        throw new LackOfXmlFileException("Unknow exception", ex);
                    }
                }
            }
            _log.Info("Loaded config.xml: {0}", WritePropertiesToString());
        }

        public async Task SaveOptions()
        {
            XmlParser<Configuration> xmlPars = new XmlParser<Configuration>();
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Options/config.xml", FileMode.Create))
                {
                    using (StreamWriter sw = new StreamWriter(stream))
                    {
                        await sw.WriteAsync(await xmlPars.SerializeXml(ConfigData));
                    }
                }
            }
        }

        public string WritePropertiesToString()
        {
            PropertyInfo[] propertyInfos;
            propertyInfos = typeof(Configuration).GetProperties();
            string tempString = string.Empty;
            Array.Sort(propertyInfos,
                    delegate(PropertyInfo propertyInfoFirst, PropertyInfo propertyInfoSecond)
                    { return propertyInfoFirst.Name.CompareTo(propertyInfoSecond.Name); });

            foreach (PropertyInfo propertyInfo in propertyInfos)
            {
                tempString += String.Format("\nName: {0}, Value: {1}", propertyInfo.Name, propertyInfo.GetValue(ConfigData));
            }
            return tempString;
        }
    }
}