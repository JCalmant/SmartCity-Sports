using GalaSoft.MvvmLight;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Views;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI.Core;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

enum Action { AJOUTER, MODIFIER, SUPPRIMER }

namespace BackEndSmartCity.Service
{
    public class CommonViewModel : ViewModelBase
    {

        private INavigationService _navigation;

        public ICommand Retour => new RelayCommand(() => GoBack());
        public ICommand GoBackHome => new RelayCommand(() => AccueilApp());
        public ICommand AddSport => new RelayCommand(() => NewSport());
        public ICommand AddComplexe => new RelayCommand(() => NewComplexe());
        public ICommand Stats => new RelayCommand(() => GetStats());
        public ICommand Quitter => new RelayCommand(() => LeaveApp());

        public CommonViewModel(INavigationService _navigation)
        {
            this._navigation = _navigation;
        }

        private void LeaveApp()
        {
            Application.Current.Exit();
        }
        private void AccueilApp()
        {
            RetourPagePrec.GetList().Add("HomePage");
            _navigation.NavigateTo("HomePage");
        }
        private void NewSport()
        {
            RetourPagePrec.GetList().Add("SportPage");
            _navigation.NavigateTo("SportPage");
        }
        private void NewComplexe()
        {
            RetourPagePrec.GetList().Add("ComplexePage");
            _navigation.NavigateTo("ComplexePage");
        }
        private void GetStats()
        {
            RetourPagePrec.GetList().Add("StatsPage");
            _navigation.NavigateTo("StatsPage");
        }
        private void GoBack()
        {
            _navigation.NavigateTo(RetourPagePrec.GetPrec());
        }
    }
}