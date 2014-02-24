using Microsoft.Phone.Controls;
using MyGuide.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.Views
{
    public class PageViewBase : PhoneApplicationPage
    {
        private bool _isNewPageInstance = false;

        public PageViewBase()
        {
            _isNewPageInstance = true;
        }

        protected override void OnNavigatedFrom(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedFrom(e);
            ViewModelBase viewModel = _getViewModel();
            viewModel.OnNavigatedFrom(e.NavigationMode);
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            ViewModelBase viewModel = _getViewModel();
            viewModel.OnNavigatedTo(e.NavigationMode, _isNewPageInstance);
            _isNewPageInstance = false;
        }

        private ViewModelBase _getViewModel()
        {
            var viewModel = this.DataContext as ViewModelBase;
            if (viewModel != null)
            {
                return viewModel;
            }
            else
            {
                // Log that something went wrong
                throw new InvalidOperationException("Page " + this.GetType() + " does not contain proper ViewModel in DataContext");
            }
        }
    }
}