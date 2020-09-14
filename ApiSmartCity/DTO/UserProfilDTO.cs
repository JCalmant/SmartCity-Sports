using System;
using System.Collections.Generic;

namespace ApiSmartCity.DTO
{
    public class UserProfilDTO
    {
        public UserProfilDTO(){
            Disponibilites = new HashSet<DisponibilitéDTO>();
            Amis = new HashSet<AmitiéDTO>();
        }
        public string Id{get;set;}
        public string Username{get;set;}
        public DateTime? DateNaissance{get;set;}
        public Boolean Sexe{get;set;}
        public string Photo{get;set;}
        public string AboutMe{get;set;}
        public string Profession{get;set;}
        
        public ICollection<DisponibilitéDTO> Disponibilites{get;set; }
        public ICollection<AmitiéDTO> Amis { get; set; }
    }
}