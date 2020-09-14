using BackEndSmartCity.Model;
using Newtonsoft.Json.Linq;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.DataAccess
{
    class DisponibilitéDataAccess
    {
        private FaiseurDeRequete _requete;

        public DisponibilitéDataAccess()
        {
            _requete = new FaiseurDeRequete(new Uri("https://sportappsmartcity.azurewebsites.net/api/Disponibilites"));
        }

        public async Task Post(IEnumerable<string> sports, string complexeName)
        {
            foreach (var sport in sports)
            {
                var dispoAjoutée = new JObject
                {
                    { "complexeSportif", complexeName },
                    { "libelléSport", sport }
                };
                await _requete.Post(dispoAjoutée);
            }
        }

        public async Task Delete(IEnumerable<string> sports, string complexeName)
        {
            foreach (var sport in sports)
            {
                var dispoSupprimée = new JObject
                {
                    { "complexeSportif", complexeName },
                    { "libelléSport", sport }
                };
                await _requete.DeleteWithBody(dispoSupprimée);
            }
        }
    }
}
