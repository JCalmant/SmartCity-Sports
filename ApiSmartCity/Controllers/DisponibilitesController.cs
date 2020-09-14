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
    public class DisponibilitesController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public DisponibilitesController(SportappsmartcitydbContext context, UserManager<Utilisateur> uMgr):base(uMgr)
        {
            _context = context;
        }

        // GET: api/Disponibilites
        [HttpGet]
        public IEnumerable<Disponibilite> GetDisponibilites()
        {
            return _context.Disponibilites;
        }

        // GET: api/Disponibilites/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetDisponibilite([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var disponibilite = await _context.Disponibilites.SingleOrDefaultAsync(m => m.DisponibiliteId == id);

            if (disponibilite == null)
            {
                return NotFound();
            }

            return Ok(disponibilite);
        }

        // PUT: api/Disponibilites/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDisponibilite([FromRoute] int id, [FromBody] Disponibilite disponibilite)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != disponibilite.DisponibiliteId)
            {
                return BadRequest();
            }

            _context.Entry(disponibilite).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DisponibiliteExists(id))
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

        // POST: api/Disponibilites
        [HttpPost]
        public async Task<IActionResult> PostDisponibilite([FromBody] DisponibilitéDTO disponibilitéDTO)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            var disponibilite = await DispoBuilder(disponibilitéDTO);
            await _context.Disponibilites.AddAsync(disponibilite);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDisponibilite", new { id = disponibilite.DisponibiliteId }, disponibilite);
        }

        // DELETE: api/Disponibilites/5
        [HttpDelete]
        public async Task<IActionResult> DeleteDisponibilite([FromBody] DisponibilitéDTO disponibilitéDTO)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            
            var disponibilite = await DispoToDelete(disponibilitéDTO);
            foreach(var dispo in disponibilite)
            {
                _context.Disponibilites.Remove(dispo);
            }
            await _context.SaveChangesAsync();

            return Ok(disponibilite);
        }

        private bool DisponibiliteExists(int id)
        {
            return _context.Disponibilites.Any(e => e.DisponibiliteId == id);
        }


        private async Task<Disponibilite> DispoBuilder(DisponibilitéDTO disponibilitéDTO)
        {
            var dispo = new Disponibilite();
            if(disponibilitéDTO.Username!=null)
            {
                var user = await _context.Utilisateurs.FirstOrDefaultAsync(u => u.UserName.Equals(disponibilitéDTO.Username));
                dispo.UtilisateurId=user.Id;
            }
            if(disponibilitéDTO.ComplexeSportif!=null)
            {
                var comp= await _context.ComplexeSportifs.FirstOrDefaultAsync(c=>c.Libellé.Equals(disponibilitéDTO.ComplexeSportif));
                dispo.ComplexeSportifId=comp.ComplexeSportifId;
            }
            var sport= await _context.Sports.FirstOrDefaultAsync(s=>s.Libellé.Equals(disponibilitéDTO.LibelléSport));
            dispo.SportId =sport.SportId;
            return dispo;
        }

        private async Task<IEnumerable<Disponibilite>> DispoToDelete(DisponibilitéDTO disponibilitéDTO)
        {
            if(disponibilitéDTO.Username!=null)
            {
                var disponibilite = new HashSet<Disponibilite>();
                foreach(var dispo in _context.Disponibilites)
                {
                    if(dispo.UtilisateurId!=null)
                    {
                        var user  = await _context.Utilisateurs.FirstOrDefaultAsync(s=>s.UserName.Equals(disponibilitéDTO.Username));
                        if(dispo.UtilisateurId.Equals(user.Id))
                        {    

                            if(disponibilitéDTO.LibelléSport!=null) 
                            {     
                                var sport = await _context.Sports.FirstOrDefaultAsync(u=>u.Libellé.Equals(disponibilitéDTO.LibelléSport));
                                if(dispo.SportId.Equals(sport.SportId))                
                                    disponibilite.Add(dispo);
                            }    
                            else
                            {
                                if(dispo.ComplexeSportifId!=null)
                                {     
                                    var comp  =  await _context.ComplexeSportifs.FirstOrDefaultAsync(u=>u.Libellé.Equals(disponibilitéDTO.ComplexeSportif));
                                    if(dispo.ComplexeSportifId.Equals(comp.ComplexeSportifId))       
                                        disponibilite.Add(dispo);
                                }
                            }
                        }
                    }
                }
                return disponibilite;
            }
            else
            {
                IEnumerable<Disponibilite> disponibilite;
                disponibilite = _context.Disponibilites.Where(
                    x =>x.ComplexeSportif.Libellé.Equals(disponibilitéDTO.ComplexeSportif)
                    && x.Sport.Libellé.Equals(disponibilitéDTO.LibelléSport)
                );
                return disponibilite;
            }
        }
    }
}