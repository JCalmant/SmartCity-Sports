using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Model;
using ApiSmartCity.DTO;
using System.Text;

namespace ApiSmartCity.Controllers
{    
    [Route("api/[controller]")]
    public class AccountController : BaseController
    {
        public AccountController(UserManager<Utilisateur> userManager):base(userManager)
        {
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody]NewUserDTO dto)
        {
            
                var newUser=new Utilisateur{
                        UserName=dto.UserName,
                        Email =dto.Email
                };
                IdentityResult result = await Manager.CreateAsync(newUser, dto.Password);
                StringBuilder uri = new StringBuilder("https://sportappsmartcity.azurewebsites.net/api/Utilisateurs/")
                    .Append("/")
                    .Append(dto.UserName);
                return (result.Succeeded)?Created(new Uri(uri.ToString()),newUser):(IActionResult)BadRequest();
        }
    }
}