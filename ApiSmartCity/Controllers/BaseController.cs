using System;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Model;

namespace ApiSmartCity.Controllers
{
    public abstract class BaseController : Controller
    {
        private UserManager<Utilisateur> _uMgr;
        public UserManager<Utilisateur> Manager {get=>_uMgr;}
        public BaseController(UserManager<Utilisateur> uMgr)
        {
            _uMgr=uMgr;
        }
       protected async Task<Utilisateur> GetCurrentUserAsync()
       {
            if(this.HttpContext.User==null)
                throw new Exception("L'utilisateur n'est pas identifié");
            Claim userNameClaim=this.HttpContext.User.Claims.FirstOrDefault(claim => claim.Type==ClaimTypes.NameIdentifier);
            if(userNameClaim==null)
                throw new Exception("Le token JWT semble ne pas avoir été interprété correctement");
            return await _uMgr.FindByNameAsync(userNameClaim.Value);
       }
    }
}