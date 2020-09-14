using BackEndSmartCity.Model;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.DataAccess
{
    class ComplexeDataAccess
    {
        private FaiseurDeRequete _requete;

        public ComplexeDataAccess()
        {
            _requete = new FaiseurDeRequete(new Uri("https://sportappsmartcity.azurewebsites.net/api/ComplexeSportifs"));
        }

        //TODO : adapter le code suivant la gestion de complexe

        //GET (recup)
        public async Task<IEnumerable<Complexe>> Get()
        {
            var complexes = await _requete.Get();
            IEnumerable<Complexe> listeDesComplexes = complexes.Children().Select(
                complexe => new Complexe()
                {
                    Id = complexe["complexeSportifId"].Value<int>(),
                    Libellé = complexe["libellé"].Value<string>(),
                    Adresse = complexe["adresse"].Value<string>(),
                    SiteWeb = complexe["siteWeb"].Value<string>(),
                    CoordonnéeX = complexe["coorX"].Value<double>(),
                    CoordonnéeY = complexe["coorY"].Value<double>(),

                    Disponibilités = complexe["disponibilites"].Children().Select(disponibilité => new Disponibilité()
                    {
                        LibelléSport = disponibilité["libelléSport"].Value<string>(),
                        ComplexeSportif = disponibilité["complexeSportif"].Value<string>(),
                        Username = disponibilité["username"].Value<string>()
                    })
                }
                );
            return listeDesComplexes;
        }

        //POST (ajoute)
        public async Task Post(Complexe complexe)
        {
            var complexeAjouté = new JObject
            {
                { "libellé", complexe.Libellé },
                { "adresse", complexe.Adresse },
                { "siteWeb", complexe.SiteWeb },
                { "coorX", complexe.CoordonnéeX },
                { "coorY", complexe.CoordonnéeY }
            };
            await _requete.Post(complexeAjouté);
        }


        //PUT (modifie)
        public async Task Put(Complexe complexe)
        {
            var complexeMAJ = new JObject
            {
                { "ComplexeSportifId", complexe.Id },
                { "libellé", complexe.Libellé },
                { "adresse", complexe.Adresse },
                { "siteWeb", complexe.SiteWeb },
                { "coorX", complexe.CoordonnéeX },
                { "coorY", complexe.CoordonnéeY }
            };
            _requete.Put(complexeMAJ, complexe.Id);
        }

        //DELETE
        public async Task Delete(string complexeASupprimer)
        {
            var complexe = await ComplexeAvecId(complexeASupprimer);
            _requete.Delete(complexe.Id);
        }

        private async Task<Complexe> ComplexeAvecId(string complexeRecherché)
        {
            var complexes = await Get();
            var complexeID = complexes.First(complexe => complexe.Libellé.Equals(complexeRecherché));
            return complexeID;
        }

    }
}
