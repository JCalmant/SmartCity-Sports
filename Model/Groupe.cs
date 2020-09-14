using System;
using System.Collections.Generic;

namespace Model
{
    public class Groupe
    {
        public Groupe(){
            AttributionGroupes = new HashSet<AttributionGroupe>();
        }
        public int GroupeId{get;set;}
        public string NomGroupe{get;set;}
        public DateTime? Creation {get;set;}
        
        public virtual ICollection<AttributionGroupe> AttributionGroupes{get;set;}
    }
}