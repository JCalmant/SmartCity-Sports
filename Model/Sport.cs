using System;
using System.Collections.Generic;

namespace Model
{
    public class Sport
    {
        public Sport(){
            Disponibilites = new HashSet<Disponibilite>();
        }
        public int SportId{get;set;}
        public String Libellé{get;set;}
        public virtual ICollection<Disponibilite> Disponibilites {get;set;}
    }
}