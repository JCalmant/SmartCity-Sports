using System;
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
    public class AttributionGroupesController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public AttributionGroupesController(SportappsmartcitydbContext context, UserManager<Utilisateur> uMgr):base(uMgr)
        {
            _context = context;
        }

        // GET: api/AttributionGroupes
        [HttpGet]
        public IEnumerable<AttributionGroupe> GetAttributionGroupes()
        {
            return _context.AttributionGroupes;
        }

        // GET: api/AttributionGroupes/Frenchoooo
        [HttpGet("{username}")]
        public async Task<IActionResult> GetAttributionGroupe([FromRoute] string username)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var utilisateur = await _context.Utilisateurs.SingleOrDefaultAsync(u=> u.UserName.Equals(username));

            if (utilisateur == null)
            {
                return NotFound();
            }

            var utilisateurId = utilisateur.Id;
            var allUserGroupes = new HashSet<GroupeDTO>();

            foreach(var attribution in _context.AttributionGroupes)
            {
                if(attribution.UtilisateurId.Equals(utilisateurId))
                {
                    allUserGroupes.Add(new GroupeDTO()
                                        {
                                            GroupeId=attribution.GroupeId
                                        });
                }
            }
            
            foreach(var attribution in _context.AttributionGroupes)
            {
                if(!attribution.UtilisateurId.Equals(utilisateur.Id) && allUserGroupes.Any(a => a.GroupeId == attribution.GroupeId))
                {
                    var user = await _context.Utilisateurs.FirstAsync(u=>u.Id.Equals(attribution.UtilisateurId));
                    allUserGroupes.FirstOrDefault(a => a.GroupeId == attribution.GroupeId).Membre.Add(user.UserName);                    
                }
            }

            return Ok(allUserGroupes);
        }

        // PUT: api/AttributionGroupes/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutAttributionGroupe([FromRoute] int id, [FromBody] AttributionGroupe attributionGroupe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != attributionGroupe.AttributionGroupeId)
            {
                return BadRequest();
            }

            _context.Entry(attributionGroupe).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!AttributionGroupeExists(id))
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

        // POST: api/AttributionGroupes
        [HttpPost]
        public async Task<IActionResult> PostAttributionGroupe([FromBody] GroupeDTO groupeDTO)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            await _context.Database.BeginTransactionAsync();
            try{
                var users = groupeDTO.Membre.ToArray();
                var user1 = await _context.Utilisateurs.FirstOrDefaultAsync(u => u.UserName.Equals(users[0]));

                if(groupeDTO.GroupeId==0){
                    var user2 = await _context.Utilisateurs.FirstOrDefaultAsync(u => u.UserName.Equals(users[1]));

                    await _context.Groupes.AddAsync(new Groupe());

                    await _context.SaveChangesAsync();

                    var groupeCréé = await _context.Groupes.LastAsync();

                    await _context.AttributionGroupes.AddAsync(new AttributionGroupe
                                                                {
                                                                    GroupeId=groupeCréé.GroupeId,
                                                                    UtilisateurId = user1.Id
                                                                });

                    await _context.SaveChangesAsync();

                    await _context.AttributionGroupes.AddAsync(new AttributionGroupe
                                                                {
                                                                    GroupeId=groupeCréé.GroupeId,
                                                                    UtilisateurId = user2.Id
                                                                });                
                }
                else
                {
                    await _context.AttributionGroupes.AddAsync(new AttributionGroupe
                                                                {
                                                                    GroupeId=groupeDTO.GroupeId,
                                                                    UtilisateurId = user1.Id
                                                                });
                }
                
                await _context.SaveChangesAsync();
            
                _context.Database.CommitTransaction();

                var dictionnary = new Dictionary<string,int>();
                dictionnary.Add("NumeroChat",_context.Groupes.Last().GroupeId);
                return Ok(dictionnary);
            }
            catch
            {
                return BadRequest();
            }
        }

        // DELETE: api/AttributionGroupes/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteAttributionGroupe([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var attributionGroupe = await _context.AttributionGroupes.SingleOrDefaultAsync(m => m.AttributionGroupeId == id);
            if (attributionGroupe == null)
            {
                return NotFound();
            }

            _context.AttributionGroupes.Remove(attributionGroupe);
            await _context.SaveChangesAsync();

            return Ok(attributionGroupe);
        }

        private bool AttributionGroupeExists(int id)
        {
            return _context.AttributionGroupes.Any(e => e.AttributionGroupeId == id);
        }
    }
}