using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using Moq;
using MyGuide.DataServices;
using MyGuide.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuideTests.Tests
{
    [TestClass]
    class DataServiceModelTest
    {
        [TestMethod]
        public void CorrectDataTest()
        {
            
            
            Root root = new Root();
            root.AnimalsList.Items.Add(new Animal());
            root.JunctionsList.Items.Add(new Junction());
            root.WaysList.Items.Add(new Way());

          

        }


    }
}
