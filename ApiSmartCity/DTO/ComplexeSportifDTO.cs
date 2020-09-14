using System.Collections.Generic;

namespace ApiSmartCity.DTO
{
    public class ComplexeSportifDTO
    {
        public ComplexeSportifDTO(){
            Disponibilites = new HashSet<DisponibilitéDTO>();
        }
        public ICollection<DisponibilitéDTO> Disponibilites{get;set;}
        public double CoorY{get;set;}
        public double CoorX{get;set;}
        public int ComplexeSportifId{get;set;}
        public string Adresse{get;set;}
        public string SiteWeb{get;set;}
        public string Libellé{get;set;}
    }
}