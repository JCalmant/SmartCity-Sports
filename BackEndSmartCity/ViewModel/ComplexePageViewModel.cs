using BackEndSmartCity.DataAccess;
using BackEndSmartCity.Model;
using BackEndSmartCity.Service;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Views;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace BackEndSmartCity.ViewModel
{
    public class ComplexePageViewModel : CommonViewModel
    {
        private string _sportSélectionnéAjout,
            _sportSélectionnéRetrait,
            _sportSélectionnéModifAjout,
            _sportSélectionnéModifRetrait,
            _coordonnéeXInséré,
            _coordonnéeYInséré,
            _libelléInséré,
            _adresseInséré,
            _sitewebInséré,
            _erreur;
        private Complexe _complexeSelectionné;
        private ObservableCollection<string> _sportsDisponibles,_sportsPraticables;
        private ObservableCollection<Complexe> _complexes;
        private IEnumerable<Sport> _sports;
        private ComplexeDataAccess _complexeDataAccess;
        private SportDataAccess _sportDataAccess;
        private DisponibilitéDataAccess _disponibilitéDataAccess;

        public ICommand AjouterComplexe => new RelayCommand(() => AddComplexeDB());
        public ICommand Modifier => new RelayCommand(() => ModifierComplexe());
        public ICommand Supprimer => new RelayCommand(() => SupprimerComplexe());
        public ICommand AddSportToComplexe => new RelayCommand(() => AddSportComplexe());
        public ICommand RemoveSportFromComplexe => new RelayCommand(() => RemoveSportComplexe());
        public ICommand UpdateAddSportToComplexe => new RelayCommand(() => UpdateAddSportComplexe());
        public ICommand UpdateSportFromComplexe => new RelayCommand(() => UpdateSportComplexe());


        public ObservableCollection<string> SportsDisponibles
        {
            get => _sportsDisponibles;
            set
            {
                _sportsDisponibles = value;
                RaisePropertyChanged("SportsDisponibles");
            }
        }

        public ObservableCollection<string> SportsPraticables
        {
            get => _sportsPraticables;
            set
            {
                _sportsPraticables = value;
                RaisePropertyChanged("SportsPraticables");
            }
        }

        public string SportChoisiAjout
        {
            get => _sportSélectionnéAjout;
            set
            {
                _sportSélectionnéAjout = value;
                RaisePropertyChanged("SportChoisiAjout");
            }
        }

        public string SportChoisiModifAjout
        {
            get => _sportSélectionnéModifAjout;
            set
            {
                _sportSélectionnéModifAjout = value;
                RaisePropertyChanged("SportChoisiModifAjout");
            }
        }

        public string SportChoisiModifRetrait
        {
            get => _sportSélectionnéModifRetrait;
            set
            {
                _sportSélectionnéModifRetrait = value;
                RaisePropertyChanged("SportChoisiModifRetrait");
            }
        }

        public string SportChoisiRetrait
        {
            get => _sportSélectionnéRetrait;
            set
            {
                _sportSélectionnéRetrait = value;
                RaisePropertyChanged("SportChoisiRetrait");
            }
        }

        public ObservableCollection<Complexe> Complexes
        {
            get => _complexes;
            set
            {
                _complexes = value;
                RaisePropertyChanged("Complexes");
            }
        }

        public Complexe ComplexeChoisi
        {
            get => _complexeSelectionné;
            set
            {
                _complexeSelectionné = value;
                RaisePropertyChanged("ComplexeChoisi");
            }

        }

        public string InsertionCoordonneeX
        {
            get => _coordonnéeXInséré;
            set
            {
                _coordonnéeXInséré = value;
                RaisePropertyChanged("InsertionCooordonneeX");
            }
        }

        public string InsertionCoordonneeY
        {
            get => _coordonnéeYInséré;
            set
            {
                _coordonnéeYInséré = value;
                RaisePropertyChanged("InsertionCooordonneeY");
            }
        }

        public string InsertionLibelle
        {
            get => _libelléInséré;
            set
            {
                _libelléInséré = value;
                RaisePropertyChanged("InsertionLibelle");
            }
        }

        public string InsertionAdresse
        {
            get => _adresseInséré;
            set
            {
                _adresseInséré = value;
                RaisePropertyChanged("InsertionAdresse");
            }
        }

        public string InsertionSiteweb
        {
            get => _sitewebInséré;
            set
            {
                _sitewebInséré = value;
                RaisePropertyChanged("InsertionSiteweb");
            }
        }

        public string Erreur
        {
            get => _erreur;
            set
            {
                _erreur = value;
                RaisePropertyChanged("Erreur");
            }
        }

        public ComplexePageViewModel(INavigationService _navigation) : base(_navigation)
        {
            _sportDataAccess = new SportDataAccess();
            _complexeDataAccess = new ComplexeDataAccess();
            _disponibilitéDataAccess = new DisponibilitéDataAccess();
            SportsPraticables = new ObservableCollection<string>();
            InitializeSportAsync();
            InitializeComplexeAsync();
        }

        private void AddSportComplexe()
        {
            if(SportChoisiAjout!=null && !SportsPraticables.Contains(SportChoisiAjout))
                SportsPraticables.Add(SportChoisiAjout);
        }

        private void RemoveSportComplexe()
        {
            SportsPraticables.Remove(SportChoisiRetrait);
        }

        private void UpdateAddSportComplexe()
        {
            if(ComplexeChoisi!=null && !ComplexeChoisi.SportsPratiqués.Contains(SportChoisiModifAjout))
                ComplexeChoisi.SportsPratiqués.Add(SportChoisiModifAjout);
        }

        private void UpdateSportComplexe()
        {
            if(ComplexeChoisi!=null)ComplexeChoisi.SportsPratiqués.Remove(SportChoisiModifRetrait);
        }

        private async Task InitializeSportAsync()
        {
            _sports = (await _sportDataAccess.Get()).OrderBy(x => x.Libellé);
            SportsDisponibles = new ObservableCollection<string>();
            foreach (var sport in _sports)
            {
                SportsDisponibles.Add(sport.Libellé);
            }
            Refresh();
        }

        private async Task InitializeComplexeAsync()
        {
            Complexes = new ObservableCollection<Complexe>((await _complexeDataAccess.Get()).OrderBy(x => x.Libellé));
            Refresh();
        }

        private void AddComplexeDB()
        {
            Complexe valeurChamp = new Complexe
            {
                CoordonnéeX = DoubleValueField(InsertionCoordonneeX),
                CoordonnéeY = DoubleValueField(InsertionCoordonneeY),
                Libellé = InsertionLibelle,
                Adresse = InsertionAdresse,
                SiteWeb = InsertionSiteweb,
                SportsPratiqués = SportsPraticables
            };
            EnvoieRequete(Action.AJOUTER, valeurChamp);
        }

        private void ModifierComplexe()
        {
            EnvoieRequete(Action.MODIFIER, ComplexeChoisi);
        }

        private double DoubleValueField(string valeurChamp)
        {
            try
            {
                var valueString = valeurChamp.ToString();
                var tabValeur = valueString.Split('.');
                double entier = double.Parse(tabValeur[0]);
                double décimal = double.Parse(tabValeur[1]);
                double superieur = Math.Pow(10, tabValeur[1].Length);
                return entier + décimal / superieur;
            }
            catch
            {
                return 0;
            }
        }

        private void SupprimerComplexe()
        {
            _complexeDataAccess.Delete(ComplexeChoisi.Libellé);
            Complexes.Remove(ComplexeChoisi);
            ComplexeChoisi = null;
            Refresh();
        }

        private void EnvoieRequete(Action action, Complexe valeurChamp)
        {
            if (valeurChamp.Libellé != null && Regex.IsMatch(valeurChamp.Libellé, @"^[a-zA-Z0-9]"))
            {
                switch (action)
                {
                    case Action.AJOUTER:
                        AsyncAjout(valeurChamp);
                        Complexes.Add(valeurChamp);
                        InsertionCoordonneeX = null;
                        InsertionCoordonneeY = null;
                        InsertionLibelle = null;
                        InsertionAdresse = null;
                        InsertionSiteweb = null;
                        break;
                    case Action.MODIFIER:
                        _complexeDataAccess.Put(valeurChamp);
                        PutDispo(valeurChamp);
                        int i = 0;
                        while (!Complexes[i].Equals(ComplexeChoisi))
                        {
                            i++;
                        }
                        Complexes[i] = valeurChamp;
                        break;
                }
                SportsPraticables = new ObservableCollection<string>();
                Erreur = null;
                ComplexeChoisi = null;
                Refresh();
            }
            else
            {
                Erreur = "Valeur entrée incorrecte\n(complexe déjà présent ?)";
            }
        }

        public async Task AsyncAjout(Complexe complexe)
        {
            await _complexeDataAccess.Post(complexe);
            await _disponibilitéDataAccess.Post(complexe.SportsPratiqués, complexe.Libellé);
        }

        public void PutDispo(Complexe complexe)
        {
            var aSupprimer = new List<string>();
            var aCreer = new List<string>();
            var listTemporaireSuppression = new List<Disponibilité>();

            foreach (var dispo in ComplexeChoisi.Disponibilités)
            {
                if (!ComplexeChoisi.SportsPratiqués.Contains(dispo.LibelléSport))
                {
                    aSupprimer.Add( dispo.LibelléSport);
                    listTemporaireSuppression.Add(dispo);
                }
            }

            foreach(var dispo in listTemporaireSuppression)
            {
                ComplexeChoisi.Disponibilités = ComplexeChoisi.Disponibilités.Where(dispon => !dispon.Equals(dispo));
            }

            foreach(var sport in ComplexeChoisi.SportsPratiqués)
            {
                if (ComplexeChoisi.Disponibilités.Where(x => x.LibelléSport.Equals(sport)).Count() == 0)
                {
                    aCreer.Add(sport);
                    var dispos = ComplexeChoisi.Disponibilités.ToList();
                    dispos.Add(
                        new Disponibilité
                            {
                                ComplexeSportif=ComplexeChoisi.Libellé,
                                LibelléSport=sport
                            }
                        );
                    ComplexeChoisi.Disponibilités = dispos;
                }
            }
            _disponibilitéDataAccess.Post(aCreer,ComplexeChoisi.Libellé);
            _disponibilitéDataAccess.Delete(aSupprimer, ComplexeChoisi.Libellé);
        }

        private void Refresh()
        {
            if (Window.Current.Content is Frame rootFrame)
            {
                rootFrame.Navigate(typeof(ComplexePage));
            }
        }
    }
}