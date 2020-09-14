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
using System.Threading.Tasks;
using System.Windows.Input;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace BackEndSmartCity.ViewModel
{
    public class StatsPageViewModel : CommonViewModel
    {
        private ObservableCollection<Complexe> _complexes;
        private ObservableCollection<Disponibilité> _disponibilités;
        private ObservableCollection<PieChart> _pieCharts;
        private ComplexeDataAccess _complexeDataAccess;
        private UserDataAccess _usersDisponibilitéDataAccess;
        private Complexe _complexeChoisi;
        private String _erreur;

        public ICommand StatsByComplexe => new RelayCommand(() => Statistique());
        public ICommand AllStats => new RelayCommand(() => InitializeAsync());
        public Complexe ComplexeChoisi
        {
            get => _complexeChoisi;
            set
            {
                _complexeChoisi = value;
                RaisePropertyChanged("ComplexeChoisi");
            }
        }

        public ObservableCollection<PieChart> PieCharts
        {
            get => _pieCharts;
            set
            {
                _pieCharts = value;
                RaisePropertyChanged("PieCharts");
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

        public ObservableCollection<Disponibilité> Disponibilités
        {
            get => _disponibilités;
            set
            {
                _disponibilités = value;
                RaisePropertyChanged("Disponibilités");
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


        public StatsPageViewModel(INavigationService _navigation) : base(_navigation)
        {
            _complexeDataAccess = new ComplexeDataAccess();
            _usersDisponibilitéDataAccess = new UserDataAccess();
            InitializeAsync();
        }

        private async Task InitializeAsync()
        {
            Complexes = new ObservableCollection<Complexe>((await _complexeDataAccess.Get()).OrderBy(x => x.Libellé));
            Disponibilités = new ObservableCollection<Disponibilité>(await _usersDisponibilitéDataAccess.GetUsersDisponibilités(Disponibilité.DISPONIBILITÉ_DIFFERENTE));
            PieCharts = new ObservableCollection<PieChart>();

            foreach (var dispo in Disponibilités)
            {

                if (PieCharts.Where(pieChart => pieChart.ValueName.Equals(dispo.ComplexeSportif)).Count() == 0)
                {
                    PieCharts.Add(new PieChart
                    {
                        ValueName = dispo.ComplexeSportif,
                        Pourcentage = 1
                    });
                }
                else
                {
                    PieCharts.First(value => value.ValueName.Equals(dispo.ComplexeSportif)).Pourcentage++;
                }
            }
            Refresh();
        }

        private async Task Statistique()
        {
            PieCharts = new ObservableCollection<PieChart>();
            Disponibilités = new ObservableCollection<Disponibilité>(await _usersDisponibilitéDataAccess.GetUsersDisponibilités(Disponibilité.TOUTE));
            foreach (var dispo in Disponibilités)
            {
                if (dispo.ComplexeSportif != null && dispo.ComplexeSportif.Equals(_complexeChoisi.Libellé))
                {
                    if (PieCharts.Where(pieChart => pieChart.ValueName.Equals(dispo.LibelléSport)).Count() == 0)
                    {
                        PieCharts.Add(new PieChart
                        {
                            ValueName = dispo.LibelléSport,
                            Pourcentage = 1
                        });
                    }
                    else
                    {
                        PieCharts.First(value => value.ValueName.Equals(dispo.LibelléSport)).Pourcentage++;
                    }
                }
            }

            Erreur = null;

            if (PieCharts.Count() == 0)
            {
                Erreur = "Aucune donnée disponible";
            }

            Refresh();
        }

        private void Refresh()
        {
            Frame rootFrame = Window.Current.Content as Frame;
            if (rootFrame != null)
            {
                rootFrame.Navigate(typeof(StatsPage));
            }
        }

        public class PieChart
        {
            public string ValueName { get; set; }
            public double Pourcentage { get; set; }
        }
    }
}