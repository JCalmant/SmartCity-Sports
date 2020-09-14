using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
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
    public class GroupesController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public GroupesController(SportappsmartcitydbContext context, UserManager<Utilisateur> uMgr):base(uMgr)
        {
            _context = context;
        }

        // GET: api/Groupes
        [HttpGet]
        public IEnumerable<Groupe> GetGroupes()
        {
            return _context.Groupes;
        }

        // GET: api/Groupes/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetGroupe([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var groupe = await _context.Groupes.SingleOrDefaultAsync(m => m.GroupeId == id);

            if (groupe == null)
            {
                return NotFound();
            }

            return Ok(groupe);
        }

        // PUT: api/Groupes/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutGroupe([FromRoute] int id, [FromBody] Groupe groupe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != groupe.GroupeId)
            {
                return BadRequest();
            }

            _context.Entry(groupe).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GroupeExists(id))
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

        // POST: api/Groupes
        [HttpPost]
        public async Task<IActionResult> PostGroupe([FromBody] Groupe groupe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.Groupes.Add(groupe);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetGroupe", new { id = groupe.GroupeId }, groupe);
        }

        // DELETE: api/Groupes/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteGroupe([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var groupe = await _context.Groupes.SingleOrDefaultAsync(m => m.GroupeId == id);
            if (groupe == null)
            {
                return NotFound();
            }

            _context.Groupes.Remove(groupe);
            await _context.SaveChangesAsync();

            return Ok(groupe);
        }

        private bool GroupeExists(int id)
        {
            return _context.Groupes.Any(e => e.GroupeId == id);
        }
    }
}