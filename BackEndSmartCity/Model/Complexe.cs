using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.Model
{
    public class Complexe
    {
        private ObservableCollection<string> _sportsPratiqués;
        private IEnumerable<Disponibilité> _disonibilité;
        public int Id { get; set; }
        public string Libellé { get; set; }
        public string Adresse { get; set; }
        public string SiteWeb { get; set; }
        public double CoordonnéeX { get; set; }
        public double CoordonnéeY { get; set; }

        public IEnumerable<Disponibilité> Disponibilités
        {
            get=> _disonibilité;
            set
            {
                _disonibilité = value.OrderBy(x=>x.LibelléSport);
                SportsPratiqués = new ObservableCollection<string>();
                if (_disonibilité != null)
                {
                    foreach (var dispo in _disonibilité)
                        if(!SportsPratiqués.Contains(dispo.LibelléSport))SportsPratiqués.Add(dispo.LibelléSport);
                }
            }
        }

        public ObservableCollection<string> SportsPratiqués
        {
            get => _sportsPratiqués;
            set => _sportsPratiqués=value; 
        }
        public override string ToString()
        {
            return Libellé;
        }
    }
}
