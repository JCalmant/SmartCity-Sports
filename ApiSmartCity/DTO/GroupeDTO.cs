using System.Collections.Generic;

namespace ApiSmartCity.DTO
{
    public class GroupeDTO
    {
        public GroupeDTO(){
            Membre = new HashSet<string>();
        }
        public int GroupeId{get;set;}
        public ICollection<string> Membre{get;set;}
    }
}