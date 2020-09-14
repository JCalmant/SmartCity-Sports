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
    public class HoraireBusController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public HoraireBusController(SportappsmartcitydbContext context, UserManager<Utilisateur> uMgr):base(uMgr)
        {
            _context = context;
        }

        // GET: api/HoraireBus
        [AllowAnonymous]
        [HttpGet]
        public IEnumerable<HoraireBus> GetHorairesBus()
        {
            return _context.HorairesBus;
        }

        // GET: api/HoraireBus/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetHoraireBus([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var horaireBus = await _context.HorairesBus.SingleOrDefaultAsync(m => m.HoraireBusId == id);

            if (horaireBus == null)
            {
                return NotFound();
            }

            return Ok(horaireBus);
        }

        // PUT: api/HoraireBus/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutHoraireBus([FromRoute] int id, [FromBody] HoraireBus horaireBus)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != horaireBus.HoraireBusId)
            {
                return BadRequest();
            }

            _context.Entry(horaireBus).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!HoraireBusExists(id))
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

        // POST: api/HoraireBus
        [HttpPost]
        public async Task<IActionResult> PostHoraireBus([FromBody] HoraireBus horaireBus)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            _context.HorairesBus.Add(horaireBus);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetHoraireBus", new { id = horaireBus.HoraireBusId }, horaireBus);
        }

        // DELETE: api/HoraireBus/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteHoraireBus([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var horaireBus = await _context.HorairesBus.SingleOrDefaultAsync(m => m.HoraireBusId == id);
            if (horaireBus == null)
            {
                return NotFound();
            }

            _context.HorairesBus.Remove(horaireBus);
            await _context.SaveChangesAsync();

            return Ok(horaireBus);
        }

        private bool HoraireBusExists(int id)
        {
            return _context.HorairesBus.Any(e => e.HoraireBusId == id);
        }
    }
}