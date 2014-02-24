using Caliburn.Micro;
using MyGuide.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide
{
    public class Bootstrapper : PhoneBootstrapper
    {
        private PhoneContainer container;

        protected override void BuildUp(object instance)
        {
            container.BuildUp(instance);
        }

        protected override void Configure()
        {
            //It's workaround with designMode problem where rootFrame is null and exception is thrown
            if (Execute.InDesignMode)
                return;

            container = new PhoneContainer();

            //It's workaround with designMode problem where rootFrame is null and exception is thrown
            if (!Execute.InDesignMode)
                container.RegisterPhoneServices(RootFrame);

            container.PerRequest<MainPageViewModel>();

            //All VM should be add to this container, e.g. container.PerRequest<AnotherViewModel>();

            AddCustomConventions();
        }

        protected override IEnumerable<object> GetAllInstances(Type service)
        {
            return container.GetAllInstances(service);
        }

        protected override object GetInstance(Type service, string key)
        {
            return container.GetInstance(service, key);
        }

        private static void AddCustomConventions()
        {
        }
    }
}