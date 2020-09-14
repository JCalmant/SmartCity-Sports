using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;
using System.IO;

namespace Model
{
    public class Utilisateur:IdentityUser
    {
        public Utilisateur()
        {
            AmitiésAjouteur = new HashSet<Amitié>();
            AmitiésAjoutées = new HashSet<Amitié>();
            Attributions = new HashSet<AttributionGroupe>();
            Disponibilites = new HashSet<Disponibilite>();
        }
        public String UtilisateurId{get;set;}
        public DateTime? DateNaissance{get;set;}
        public String Ville{get;set;}
        public int? AnnéeEtude {get;set;}
        public String AboutMe{get;set;}
        public DateTime DateInscritpion{get;set;}
        public Boolean Sexe{get;set;}
        public String Photo {get;set;}
        public Boolean EstBanni{get;set;}
        public String Profession{get;set;}
        public virtual ICollection<AttributionGroupe> Attributions{get; set;}
        public virtual ICollection<Disponibilite> Disponibilites{get;set; }
        public virtual ICollection<Amitié> AmitiésAjoutées { get; set; }
        public virtual ICollection<Amitié> AmitiésAjouteur { get; set; }
    }
}