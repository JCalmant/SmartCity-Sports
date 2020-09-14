using BackEndSmartCity.Service;
using System.Diagnostics;
using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Views;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.Devices.Input;
using Windows.UI.Core;
using Windows.UI.Xaml.Input;
using BackEndSmartCity.DataAccess;

namespace BackEndSmartCity.ViewModel
{
    public class MainPageViewModel : ViewModelBase
    {
        private INavigationService _navigation;
        private bool _progress;
        private string _accesRefusé;

        public ICommand Connexion => new RelayCommand(() => VerifConnexion());
        public string UserName{get;set;}
        public string Password { get; set;}
        public string AccesRefusé
        {
            get =>_accesRefusé;
            set
            {
                _accesRefusé = value;
                RaisePropertyChanged("AccesRefusé");
            }
        }

        public bool Progress
        {
            get => _progress;
            set
            {
                _progress = value;
                RaisePropertyChanged("Progress");
            }
        }

        public MainPageViewModel(INavigationService _navigation)
        {
            this._navigation = _navigation;          
        }

        private async void VerifConnexion()
        {
            Progress = true;
            if (!await Token.VerifUser(UserName, Password))
            {
                AccesRefusé = "Identifiants administrateur invalides";
            }
            else
            {
                RetourPagePrec.GetList().Add("HomePage");
                _navigation.NavigateTo("HomePage");
            }
            Progress = false;
        }
    }
}
