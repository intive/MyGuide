using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using Moq;
using MyGuide.DataServices;
using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuideTests.Tests
{
    [TestClass]
    public class OptionServiceTest
    {
        [ClassCleanup]
        public static void ClassTestCleanup()
        {
            CleanupMethod();
        }

        public static void CleanupMethod()
        {
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                if (myIsolatedStorage.FileExists("Options/config.xml"))
                {
                    myIsolatedStorage.DeleteFile("Options/config.xml");
                    myIsolatedStorage.DeleteDirectory("Options");
                }
            }
        }

        [TestCleanup]
        public void PerTestCleanup()
        {
            CleanupMethod();
        }

        [TestMethod]
        public async Task InitializeWithFileInISOptionTest()
        {
            string xmlString =
@"<configuration>
            <!-- Fallback to language if string is not available -->
            <lang_fallback>en</lang_fallback>
            <!-- Radius within which the information that user got close to an object appears -->
            <internal_object_radius>100</internal_object_radius>
            <!-- Radius outside which the information that user got close to an object disappears -->
            <external_object_radius>200</external_object_radius>
</configuration>";
            using (IsolatedStorageFile myIsolatedStorage = IsolatedStorageFile.GetUserStoreForApplication())
            {
                myIsolatedStorage.CreateDirectory("Options");
                using (IsolatedStorageFileStream stream = myIsolatedStorage.OpenFile("Options/config.xml", FileMode.Create))
                {
                    using (StreamWriter sw = new StreamWriter(stream))
                    {
                        await sw.WriteAsync(xmlString);
                    }
                }
            }

            OptionsService ds = new OptionsService();
            await ds.Initialize();
            Debug.WriteLine(ds.ConfigData.externalObjectRadius);
            Assert.AreEqual("en", ds.ConfigData.langFallback);
            Assert.AreEqual(100, ds.ConfigData.internalObjectRadius);
            Assert.AreEqual(200, ds.ConfigData.externalObjectRadius);
        }

        [TestMethod]
        public async Task InitializeWithoutFileInISOptionTest()
        {
            OptionsService ds = new OptionsService();
            await ds.Initialize();
            Debug.WriteLine(ds.ConfigData.externalObjectRadius);
            Assert.AreEqual("pl", ds.ConfigData.langFallback);
            Assert.AreEqual(1, ds.ConfigData.internalObjectRadius);
            Assert.AreEqual(2, ds.ConfigData.externalObjectRadius);
        }

        [TestMethod]
        public async Task SaveOptionTest()
        {
            OptionsService ds = new OptionsService();
            await ds.Initialize();
            ds.ConfigData = new Configuration { langFallback = "us", externalObjectRadius = 20, internalObjectRadius = 10 };
            await ds.SaveOptions();

            OptionsService dsSecond = new OptionsService();
            await dsSecond.Initialize();

            Assert.AreEqual(ds.ConfigData.langFallback, dsSecond.ConfigData.langFallback);
            Assert.AreEqual(ds.ConfigData.internalObjectRadius, dsSecond.ConfigData.internalObjectRadius);
            Assert.AreEqual(ds.ConfigData.externalObjectRadius, dsSecond.ConfigData.externalObjectRadius);
        }

        [TestMethod]
        public async Task WritePropertiesToStringTest()
        {
            OptionsService ds = new OptionsService();
            await ds.Initialize();

            //If we will add some other parameters in Configuration class
            //we should change this tes too...
            string expectedString = "\nName: externalObjectRadius, Value: 2\nName: internalObjectRadius, Value: 1\nName: langFallback, Value: pl";
            string resultString = ds.WritePropertiesToString();
            Assert.AreEqual(expectedString, resultString);
        }
    }
}