using System;
using System.ComponentModel.DataAnnotations;

namespace Model
{
    public class Amitié
    {
        [Key]
        public int AmitiéId{get;set;}
        [Required]
        public Boolean EstAccepté{get;set;}

        public String AjoutéId{get;set;}
        public virtual Utilisateur Ajouté{get;set;}

        public String AjouteurId{get;set;}
        public virtual Utilisateur Ajouteur{get;set;}
    }
}