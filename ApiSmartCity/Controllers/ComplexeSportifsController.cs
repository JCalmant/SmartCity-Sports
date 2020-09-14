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
    public class ComplexeSportifsController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public ComplexeSportifsController(SportappsmartcitydbContext context, UserManager<Utilisateur> uMgr):base(uMgr)
        {
            _context = context;
        }

        // GET: api/ComplexeSportifs
        [HttpGet]
        public async Task<IEnumerable<ComplexeSportifDTO>> GetComplexeSportifs()
        {
            var complexes = new HashSet<ComplexeSportifDTO>();
            foreach(var complexe in _context.ComplexeSportifs){
                complexes.Add(new ComplexeSportifDTO{
                    ComplexeSportifId = complexe.ComplexeSportifId,
                    CoorX = complexe.CoorX,
                    CoorY = complexe.CoorY,
                    Libellé = complexe.Libellé,
                    Adresse = complexe.Adresse,
                    SiteWeb = complexe.SiteWeb,                    
                    Disponibilites = await GetDisponibilitéDTO(complexe)
                });
            }
            return complexes;
        }

        // GET: api/ComplexeSportifs/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetComplexeSportif([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var complexeSportif = await _context.ComplexeSportifs.SingleOrDefaultAsync(m => m.ComplexeSportifId == id);

            if (complexeSportif == null)
            {
                return NotFound();
            }

            return Ok(complexeSportif);
        }

        // PUT: api/ComplexeSportifs/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutComplexeSportif([FromRoute] int id, [FromBody] ComplexeSportif complexeSportif)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != complexeSportif.ComplexeSportifId)
            {
                return BadRequest();
            }

            _context.Entry(complexeSportif).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ComplexeSportifExists(id))
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

        // POST: api/ComplexeSportifs
        [HttpPost]
        public async Task<IActionResult> PostComplexeSportif([FromBody] ComplexeSportif complexeSportif)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            await _context.ComplexeSportifs.AddAsync(complexeSportif);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetComplexeSportif", new { id = complexeSportif.ComplexeSportifId }, complexeSportif);
        }

        // DELETE: api/ComplexeSportifs/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteComplexeSportif([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var complexeSportif = await _context.ComplexeSportifs.SingleOrDefaultAsync(m => m.ComplexeSportifId == id);
            if (complexeSportif == null)
            {
                return NotFound();
            }

            _context.ComplexeSportifs.Remove(complexeSportif);
            await _context.SaveChangesAsync();

            return Ok(complexeSportif);
        }

        private bool ComplexeSportifExists(int id)
        {
            return _context.ComplexeSportifs.Any(e => e.ComplexeSportifId == id);
        }

        private async Task<ICollection<DisponibilitéDTO>> GetDisponibilitéDTO(ComplexeSportif complexe){
            var disponibilites = new HashSet<DisponibilitéDTO>();
            foreach(var dispo in _context.Disponibilites){
                if(dispo.ComplexeSportifId!=null && dispo.ComplexeSportifId.Equals(complexe.ComplexeSportifId)){
                    var dispoDTO = new DisponibilitéDTO();
                    var sport = await _context.Sports.FindAsync(dispo.SportId);
                    dispoDTO.LibelléSport= sport.Libellé;
                    disponibilites.Add(dispoDTO);
                }
            }
            return disponibilites;
        }
    }
}