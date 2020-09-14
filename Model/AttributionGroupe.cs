using System;
using System.Collections.Generic;
namespace Model
{
    public class AttributionGroupe
    {
        public AttributionGroupe(){
            Messages = new HashSet<Message>();
        }
        public int AttributionGroupeId{get;set;}
        public String UtilisateurId{get;set;}
        public virtual Utilisateur Utilisateur{get;set;}
        public int GroupeId{get;set;}
        public virtual Groupe Groupe{get;set;}
        public virtual ICollection<Message> Messages{get;set;}
    }
}