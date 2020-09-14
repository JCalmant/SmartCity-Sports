using System;
using System.Collections.Generic;

namespace Model
{
    public class HoraireBus
    {
        public HoraireBus(){
            Complexe=new HashSet<ComplexeSportif>();
        }
        public int HoraireBusId{get;set;}
        public DateTime HeureArrivée{get;set;}
        public virtual ICollection<ComplexeSportif> Complexe{get;set;}
    }
}