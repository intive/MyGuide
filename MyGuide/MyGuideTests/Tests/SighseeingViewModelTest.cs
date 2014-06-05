using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using Moq;
using MyGuide.DataServices.Interfaces;
using MyGuide.Services.Interfaces;
using MyGuide.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Navigation;
using Caliburn.Micro;
using MyGuideTests.Mocks;

namespace MyGuideTests.Tests
{
    [TestClass]
    public class SighseeingViewModelTest
    {
        [TestMethod]
        public void UserPositionTest()
        {
            var navService = new Mock<INavigationService>(MockBehavior.Strict);
            var messageDialogService = new Mock<IMessageDialogService>(MockBehavior.Strict);
            var dataService = new Mock<IDataService>(MockBehavior.Strict);
            var optionService = new Mock<IOptionsService>(MockBehavior.Strict);
            optionService.SetupGet(x => x.ConfigData.userLayerVisibility).Returns(true);

            var SightPViewModel = new SightseeingPageViewModel(navService.Object, messageDialogService.Object, dataService.Object, optionService.Object,new FakeCompassService());
            SightPViewModel.OnNavigatedTo(NavigationMode.New,true);
            
            Assert.IsFalse(false);
        }
    }
}
