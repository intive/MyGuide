using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Services.Interfaces;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class DebugOptionsPageViewModel : ViewModelBase
    {
        public DebugOptionsPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService, IOptionsService optionService)
            : base(navigationService, messageDialogService, dataService, optionService)
        {
        }

        #region Properties
        private int externalObjectRadius;

        private int internalObjectRadius;

        private string langFallback;

        public Configuration ConfData { get; set; }

        public int ExternalObjectRadius
        {
            get
            {
                return externalObjectRadius;
            }
            set
            {
                externalObjectRadius = value;
                NotifyOfPropertyChange(() => ExternalObjectRadius);
            }
        }

        public int InternalObjectRadius
        {
            get
            {
                return internalObjectRadius;
            }
            set
            {
                internalObjectRadius = value;
                NotifyOfPropertyChange(() => InternalObjectRadius);
            }
        }

        public string LangFallback
        {
            get
            {
                return langFallback;
            }
            set
            {
                langFallback = value;
                NotifyOfPropertyChange(() => LangFallback);
            }
        }

        #endregion Properties

        public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            ConfData = _optionService.ConfigData;
            ExternalObjectRadius = ConfData.externalObjectRadius;
            InternalObjectRadius = ConfData.internalObjectRadius;
            LangFallback = ConfData.langFallback;
        }

        public async void SaveOpt()
        {
            ConfData.externalObjectRadius = ExternalObjectRadius;
            ConfData.internalObjectRadius = InternalObjectRadius;
            ConfData.langFallback = LangFallback;
            await _optionService.SaveOptions();
            _navigation.GoBack();
        }
    }
}