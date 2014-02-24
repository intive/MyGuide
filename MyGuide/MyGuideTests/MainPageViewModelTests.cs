using Microsoft.VisualStudio.TestPlatform.UnitTestFramework;
using Moq;
using System;

namespace MyGuideTests
{
    [TestClass]
    public class MainPageViewModelTests
    {
        [TestMethod]
        public void DummyTest()
        {
            Assert.Inconclusive("Very dummy test...");

            /* Short example of using Moq mocking framework and MSTests
            *
            * Let's assume that we have one interface IFoo
            * and one VM (FooViewModel). VM will use
            * methods int Get() & void Set(int) from IFoo in its method Foo().
            * In example we will mock IFoo and use it to test method Foo.
            *
            * int testingNumber = 100;
            *
            * // Mock is class that contains created Mockup and allow operations on it.
            * // MockBehavior set how Mock reacts when unset method is called.
            * // Strict means that Mock will throw exception,
            * // Loose means that Mock will create unset method "ad hoc".
            *
            * var fooMock = new Mock<IFoo>(MockBehavior.Strict);
            *
            * // Now we are using Mock to create fake methods and make them behave like we want.
            * // foo => foo.get() points to method that we want to fake.
            *
            * fooMock.Setup(foo => foo.Get()).Returns(testingNumber);
            *
            * // It.IsAny<int>() means that Set will accept any int value.
            * // We have variety of options how to set acceptable values.
            *
            * fooMock.Setup(foo => foo.Set(It.IsAny<int>()));
            *
            * // Now we can create our VM and inject IFoo to perform the test.
            *
            * FooViewModel vm = new FooViewModel(fooMock.Object());
            * vm.Foo();
            *
            * // Now we can determinate if test is succesfull or fail.
            * // We can use Moq method Verify and/or Assert class
            *
            * mock.Verify(foo => foo.get(), Times.Once);
            *
            * Links:
            * Moq quickstart: https://github.com/Moq/moq4/wiki/Quickstart
            * Moq nice videotutorial: http://www.youtube.com/watch?v=UiOC1jsQI1o
            * MSTests walkthrough: http://msdn.microsoft.com/en-us/library/ms182532.aspx
            */
        }
    }
}