using System;

namespace Model
{
    public class Disponibilite
    {
        public int DisponibiliteId{get;set;}
        public int? ComplexeSportifId{get;set;}
        public virtual ComplexeSportif ComplexeSportif{get;set;}
        public int SportId{get;set;}
        public virtual Sport Sport{get;set;}
        public String UtilisateurId{get;set;}
        public virtual Utilisateur Utilisateur{get;set;}
    }
}