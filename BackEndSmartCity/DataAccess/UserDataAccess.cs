using BackEndSmartCity.Model;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.DataAccess
{
    public class UserDataAccess
    {
        private FaiseurDeRequete _requete;

        public UserDataAccess()
        {
            _requete = new FaiseurDeRequete(new Uri("https://sportappsmartcity.azurewebsites.net/api/Utilisateurs"));
        }

        public async Task<IEnumerable<Disponibilité>> GetUsersDisponibilités(bool isTotalWanted)
        {
            var users = await _requete.Get();
            var listeDesDisponibilités = new List<Disponibilité>();

            IEnumerable<User> listeDesUtilisateurs = users.Children().Select(user => new User()
            {
                Disponibilités = user["disponibilites"].Children().Select(disponibilité => new Disponibilité()
                {
                    LibelléSport = disponibilité["libelléSport"].Value<string>(),
                    ComplexeSportif = disponibilité["complexeSportif"].Value<string>(),
                    Username = user["username"].Value<string>()
                })
            });

            foreach(var user in listeDesUtilisateurs)
            {
                foreach(var dispo in user.Disponibilités)
                {
                    if (isTotalWanted)
                    {
                        listeDesDisponibilités.Add(dispo);
                    }
                    else
                    {
                        if (dispo.ComplexeSportif!=null &&  listeDesDisponibilités.Where(disponibilité =>
                                                         disponibilité.LibelléSport.Equals(dispo.ComplexeSportif)
                                                         && disponibilité.Username.Equals(dispo.Username)).Count() == 0)
                            listeDesDisponibilités.Add(dispo);
                    }
                }
            }

            return listeDesDisponibilités;
        }
    }
}
