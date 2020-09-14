using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ApiSmartCity.DTO;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Model;

namespace ApiSmartCity.Controllers
{
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [Route("api/[controller]")]
    public class AmitieController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public AmitieController(UserManager<Utilisateur> uMgr,SportappsmartcitydbContext context):base(uMgr)
        {
            _context = context;
        }

        // PUT: api/Amitié/5
        [HttpPut]
        public async Task<IActionResult> PutAmitié([FromBody] AmitiéDTO amitiéDTO)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var amitié = await RechercheAmitié(amitiéDTO);
            amitié.EstAccepté = amitiéDTO.Accepté;

            _context.Entry(amitié).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!AmitiéExists(amitié.AmitiéId))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Amitié
        [HttpPost]
        public async Task<IActionResult> PostAmitié([FromBody] Amitié amitié)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.Amitiés.Add(amitié);
            await _context.SaveChangesAsync();

            return Ok();
        }

        // DELETE: api/Amitié/5
        [HttpDelete]
        public async Task<IActionResult> DeleteAmitié([FromBody] AmitiéDTO amitiéDTO)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            
            var amitié = await RechercheAmitié(amitiéDTO);
            if (amitié == null)
            {
                return NotFound();
            }

            _context.Amitiés.Remove(amitié);
            await _context.SaveChangesAsync();

            return Ok(amitié);
        }

        private bool AmitiéExists(int id)
        {
            return _context.Amitiés.Any(e => e.AmitiéId == id);
        }

        private async Task<Amitié> RechercheAmitié(AmitiéDTO amitiéDTO){      
            var userAjouté = await _context.Utilisateurs.SingleOrDefaultAsync(m => m.UserName.Equals(amitiéDTO.AmiAjouté));
            var userAjouteur = await _context.Utilisateurs.SingleOrDefaultAsync(m => m.UserName.Equals(amitiéDTO.AmiAjouteur));
            return  await _context.Amitiés.SingleOrDefaultAsync(m => m.AjouteurId.Equals(userAjouteur.Id) && m.AjoutéId.Equals(userAjouté.Id));
        }
    }
}