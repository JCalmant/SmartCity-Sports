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
    public class SportDataAccess
    {
        private FaiseurDeRequete _requete;

        public SportDataAccess()
        {
            _requete = new FaiseurDeRequete(new Uri("https://sportappsmartcity.azurewebsites.net/api/Sports"));
        }

        public async Task<IEnumerable<Sport>> Get()
        {
            var sports = await _requete.Get();
            IEnumerable<Sport> listeDesSports = sports.Children().Select(sport => new Sport()
            {
                Id = sport["sportId"].Value<int>(),
                Libellé = sport["libellé"].Value<string>()
            });
            return listeDesSports;
        }

        public void Post(String sport)
        {
            var sportAjouté = new JObject
            {
                {"libellé",sport }
            };
            _requete.Post(sportAjouté);
        }
        public async Task Put(String nouveauLibellé,String ancienLibellé)
        {
            var sport = await SportAvecId(ancienLibellé);
            var sportMisAjour = new JObject
            {
                {"sportId",sport.Id },
                {"libellé",nouveauLibellé}
            };
            _requete.Put(sportMisAjour, sport.Id);
        }

        public async Task Delete(string sportASupprimer)
        {
            var sport = await SportAvecId(sportASupprimer);
            _requete.Delete(sport.Id);
        }
        private async Task<Sport> SportAvecId(string sportRecherché)
        {
            var sports = await Get();
            var sportId = sports.First(sport => sport.Libellé.Equals(sportRecherché));
            return sportId;
        }
    }
}
