using BackEndSmartCity.Service;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Views;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace BackEndSmartCity.ViewModel
{
    public class HomePageViewModel : CommonViewModel
    {
        public HomePageViewModel(INavigationService _navigation):base(_navigation)
        {
        }
    }
}