using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.Model
{
    public class Disponibilité
    {
        public const bool TOUTE = true;
        public const bool DISPONIBILITÉ_DIFFERENTE = false;
        public string Username { get; set; }
        public string ComplexeSportif { get; set; }
        public string LibelléSport { get; set; }
    }
}
