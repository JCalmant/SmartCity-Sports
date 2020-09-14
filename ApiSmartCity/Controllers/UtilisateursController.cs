using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Model;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using ApiSmartCity.DTO;

namespace ApiSmartCity.Controllers
{    
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [Route("api/[controller]")]
    public class UtilisateursController : BaseController
    {
        private readonly SportappsmartcitydbContext _context;

        public UtilisateursController(SportappsmartcitydbContext context, UserManager<Utilisateur> uMgr):base(uMgr)
        {
            _context = context;
        }

        // GET: api/Utilisateurs
        [HttpGet]
        public async Task<IEnumerable<UserProfilDTO>> GetUtilisateurs()
        {
            var utilisateurs = new HashSet<UserProfilDTO>();
            foreach(var utilisateur in _context.Utilisateurs){
                utilisateurs.Add(new UserProfilDTO{
                    Username = utilisateur.UserName,
                    DateNaissance = utilisateur.DateNaissance,
                    Photo = utilisateur.Photo,
                    Sexe = utilisateur.Sexe,
                    Disponibilites = await GetDisponibilitéDTO(utilisateur),
                    Amis = await GetAmitiéDTO(utilisateur)
                });
            }
            return utilisateurs;
        }
        // GET: api/Utilisateurs/Frenchoooo
        [HttpGet("{username}")]
        public async Task<IActionResult> GetUtilisateurByUsername([FromRoute] string username)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            var utilisateur = await _context.Utilisateurs.SingleOrDefaultAsync(m => m.UserName.Equals(username));
                        
            var user = new UserProfilDTO{
                Username = username,
                Id=utilisateur.Id,
                Sexe=utilisateur.Sexe                
            };
            if(utilisateur.DateNaissance!=null)user.DateNaissance=utilisateur.DateNaissance.Value;
            if(utilisateur.Photo!=null)user.Photo=utilisateur.Photo;
            if(utilisateur.AboutMe!=null)user.AboutMe=utilisateur.AboutMe;
            if(utilisateur.Profession!=null)user.Profession  = utilisateur.Profession;

            user.Disponibilites = await GetDisponibilitéDTO(utilisateur);            
            user.Amis = await GetAmitiéDTO(utilisateur);

            if (utilisateur == null)
            {
                return NotFound();
            }

            return Ok(user);
        }

        // PUT: api/Utilisateurs/5
        [HttpPut("{username}")]
        public async Task<IActionResult> PutUtilisateur([FromRoute] string username, [FromBody] Utilisateur utilisateur)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (username != utilisateur.UserName)
            {
                return BadRequest();
            }
            var user = await _context.Utilisateurs.SingleOrDefaultAsync(m => m.UserName.Equals(username));
            user = Modif(user,utilisateur);
            _context.Entry(user).State = EntityState.Modified;
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UtilisateurExists(username))
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

        // POST: api/Utilisateurs
        [HttpPost]
        public async Task<IActionResult> PostUtilisateur([FromBody] Utilisateur utilisateur)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            await _context.Utilisateurs.AddAsync(utilisateur);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetUtilisateur", new { id = utilisateur.Id }, utilisateur);
        }

        // DELETE: api/Utilisateurs/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUtilisateur([FromRoute] string id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var utilisateur = await _context.Utilisateurs.SingleOrDefaultAsync(m => m.Id.Equals(id));
            if (utilisateur == null)
            {
                return NotFound();
            }

            _context.Utilisateurs.Remove(utilisateur);
            await _context.SaveChangesAsync();

            return Ok(utilisateur);
        }

        private bool UtilisateurExists(string id)
        {
            return _context.Utilisateurs.Any(e => e.Id.Equals(id));
        }

        private Utilisateur Modif(Utilisateur aModif, Utilisateur avecModif){
            var retour = aModif;
            if(avecModif.AboutMe!=null)retour.AboutMe=avecModif.AboutMe;
            if(avecModif.UserName!=null)retour.UserName=avecModif.UserName;
            if(avecModif.Email!=null)retour.Email=avecModif.Email;
            if(avecModif.Profession!=null)retour.Profession=avecModif.Profession;
            if(avecModif.Photo!=null)retour.Photo=avecModif.Photo;
            retour.Sexe=avecModif.Sexe;
            if(avecModif.DateNaissance!=null)retour.DateNaissance=avecModif.DateNaissance;
            foreach(var dispo in avecModif.Disponibilites){
                retour.Disponibilites.Add(dispo);
            }
            return retour;
        }

        private async Task<ICollection<DisponibilitéDTO>> GetDisponibilitéDTO(Utilisateur utilisateur){
            var disponibilites = new HashSet<DisponibilitéDTO>();
            foreach(var dispo in _context.Disponibilites){
                if(dispo.UtilisateurId!=null && dispo.UtilisateurId.Equals(utilisateur.Id)){
                    var dispoDTO = new DisponibilitéDTO();
                    if(dispo.ComplexeSportifId!=null)
                    {
                        var comp =await _context.ComplexeSportifs.FindAsync(dispo.ComplexeSportifId);
                        dispoDTO.ComplexeSportif=comp.Libellé;
                    }
                    var sport = await _context.Sports.FindAsync(dispo.SportId);
                    dispoDTO.LibelléSport= sport.Libellé;
                    disponibilites.Add(dispoDTO);
                }
            }
            return disponibilites;
        }

        private async Task<ICollection<AmitiéDTO>> GetAmitiéDTO(Utilisateur utilisateur){
            var amitiés = new HashSet<AmitiéDTO>();
            foreach(var amitié in _context.Amitiés){
                if(amitié.AjouteurId.Equals(utilisateur.Id)||amitié.AjoutéId.Equals(utilisateur.Id)){
                    var ami = new AmitiéDTO{ Accepté = amitié.EstAccepté};
                    var user =await _context.Utilisateurs.FindAsync(amitié.AjoutéId);
                    ami.AmiAjouté=user.UserName;
                    user = await _context.Utilisateurs.FindAsync(amitié.AjouteurId);
                    ami.AmiAjouteur= user.UserName;
                    amitiés.Add(ami);
                }
            }
            return amitiés;
        }
    }
}