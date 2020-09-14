using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.DataAccess
{
    class FaiseurDeRequete
    {
        private HttpClient _client;
        private Uri _uri;

        public FaiseurDeRequete(Uri uri)
        {
            _client = new HttpClient();
            _client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", Token.TokenBrut);
            _uri = uri;
        }

        public async Task<JArray> Get()
        {
            var donnéeReçue = await _client.GetStringAsync(_uri);
            return JArray.Parse(donnéeReçue);
        }

        public async Task Post(JObject httpContent)
        {
            var content = new StringContent(httpContent.ToString());
            content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            await _client.PostAsync(_uri, content);
        }

        public async Task Put(JObject httpContent, int id)
        {
            var uriPut = UriId(id);
            var content = new StringContent(httpContent.ToString());
            content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            await _client.PutAsync(uriPut, content);
        }

        public async Task Delete(int id)
        {
            var uriDelete = UriId(id);
            await _client.DeleteAsync(uriDelete);
        }

        public async Task DeleteWithBody(JObject httpContent)
        {
            HttpRequestMessage request = new HttpRequestMessage
            {
                Content = new StringContent(httpContent.ToString(), Encoding.UTF8, "application/json"),
                Method = HttpMethod.Delete,
                RequestUri = _uri
            };
            await _client.SendAsync(request);
        }

        private Uri UriId(int id)
        {
            var uriPut = new StringBuilder(_uri.AbsoluteUri);
            uriPut.Append("/");
            uriPut.Append(id);
            return new Uri(uriPut.ToString());
        }
    }
}
