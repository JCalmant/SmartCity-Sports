using BackEndSmartCity.DataAccess;
using BackEndSmartCity.Model;
using BackEndSmartCity.Service;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Views;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;


namespace BackEndSmartCity.ViewModel
{
    public class SportPageViewModel : CommonViewModel
    {
        private String _sportSélectionné, _sportInséré, _sportAModifier, _erreur;
        private ObservableCollection<String> _sportsString;
        private SportDataAccess _sportDataAccess;
        private IEnumerable<Sport> _sports;

        public ICommand Ajouter => new RelayCommand(() => AjouterSport());
        public ICommand Modifier => new RelayCommand(() => ModifierSport());
        public ICommand Supprimer => new RelayCommand(() => SupprimerSport());
        public String InsertionSport
        {
            get => _sportInséré;
            set
            {
                _sportInséré = value;
                RaisePropertyChanged("InsertionSport");
            }
        }
        public String ModificationSport
        {
            get => _sportAModifier;
            set
            {
                _sportAModifier = value;
                RaisePropertyChanged("ModificationSport");
            }
        }
        public ObservableCollection<String> Sports
        {
            get => _sportsString;
            set
            {
                _sportsString = value;
                RaisePropertyChanged("Sports");
            }
        }
        public String SportChoisi
        {
            get => _sportSélectionné;
            set
            {
                _sportSélectionné = value;
                RaisePropertyChanged("SportChoisi");
            }
        }
        public String Erreur
        {
            get => _erreur;
            set
            {
                _erreur = value;
                RaisePropertyChanged("Erreur");
            }
        }

        public SportPageViewModel(INavigationService _navigation) : base(_navigation)
        {
            _sportDataAccess = new SportDataAccess();
            InitializeAsync();
        }

        private async Task InitializeAsync()
        {
            _sports = (await _sportDataAccess.Get()).OrderBy(x => x.Libellé);
            Sports = new ObservableCollection<string>();
            foreach (var sport in _sports)
            {
                Sports.Add(sport.Libellé);
            }
            Refresh();
        }

        private void ModifierSport()
        {
            EnvoieRequete(Action.MODIFIER, ModificationSport);
        }
        private void SupprimerSport()
        {
            _sportDataAccess.Delete(SportChoisi);
            Sports.Remove(SportChoisi);
            SportChoisi = null;
            Refresh();
        }
        private void AjouterSport()
        {
            EnvoieRequete(Action.AJOUTER, InsertionSport);
        }
        private void EnvoieRequete(Action action, string valeurChamp)
        {
            if (valeurChamp != null && !Sports.Contains(valeurChamp) && Regex.IsMatch(valeurChamp, @"^[a-zA-Z0-9]"))
            {
                switch (action)
                {
                    case Action.AJOUTER:
                        _sportDataAccess.Post(valeurChamp);
                        Sports.Add(valeurChamp);
                        InsertionSport = null;
                        break;
                    case Action.MODIFIER:
                        _sportDataAccess.Put(valeurChamp, SportChoisi);
                        int i = 0;
                        while (!Sports[i].Equals(SportChoisi))
                        {
                            i++;
                        }
                        Sports[i] = valeurChamp;
                        ModificationSport = null;
                        break;
                }
                Erreur = null;
                SportChoisi = null;
                Refresh();
            }
            else
            {
                Erreur = "Valeur entrée incorrecte\n(Sport déjà présent ?)";
            }
        }
        private void Refresh()
        {
            Frame rootFrame = Window.Current.Content as Frame;
            if (rootFrame != null)
            {
                rootFrame.Navigate(typeof(SportPage));
            }
        }
    }
}