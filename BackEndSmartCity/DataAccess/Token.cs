using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IdentityModel.Tokens.Jwt;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.DataAccess
{
    public class Token
    {
        public static string TokenBrut { get; set; }
        public static async Task<Boolean> VerifUser(String userName, String password)
        {
            if(userName==null || password == null)
            {
                return false;
            }

            var wc = new HttpClient();

            var coordonnéeUtilisateur = new JObject
            {
                { "UserName", userName },
                { "Password", password }
            };
            
            var httpContent = new StringContent(coordonnéeUtilisateur.ToString());
            httpContent.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            var test = await wc.PostAsync(new Uri("https://sportappsmartcity.azurewebsites.net/api/jwt"), httpContent);

            if (test.Content.Headers.ContentLength==0) 
                return false; //Quand le contenu de la requete est vide -> mauvais userName

            var tokenBrutEtExpiration = await test.Content.ReadAsStringAsync(); 

            if (tokenBrutEtExpiration.Equals("Invalid credentials"))
                return false; //Contenu de la requete quand userName est ok mais pas le password

            JwtSecurityToken tokenDécrypté=Token.DécryptageToken(tokenBrutEtExpiration);

            if (tokenDécrypté.Claims.ToList().Exists(claim => claim.Type.Equals("Role")))
            {
                return tokenDécrypté.Claims.First(claim => claim.Type.Equals("Role")).Value.Equals("Admin");
            }
            return false;
        }

        public static JwtSecurityToken DécryptageToken(string tokenBrutEtExpiration)
        {
            var jsonToken = JObject.Parse(tokenBrutEtExpiration);
            TokenBrut = jsonToken["access_token"].Value<String>();
            var handler = new JwtSecurityTokenHandler();
            return handler.ReadJwtToken(TokenBrut);
        }
    }
}
