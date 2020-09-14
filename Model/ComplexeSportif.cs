using System;
using System.Collections.Generic;

namespace Model
{
    public class ComplexeSportif
    {
        public ComplexeSportif(){
            Disponibilites = new HashSet<Disponibilite>();
        }
        public int ComplexeSportifId{get;set;}
        public String Libellé{get;set;}
        public Boolean? AParkingDispo{get;set;}
        public Boolean? EstOuvert{get;set;}
        public String Adresse{get;set;}
        public String SiteWeb{get; set;}
        public Double CoorX{get;set;}
        public Double CoorY{get;set;}      
        public int? HoraireBusId{get;set;}
        public virtual HoraireBus HoraireBus{get;set;}


        public virtual ICollection<Disponibilite> Disponibilites {get;set;}
    }
}