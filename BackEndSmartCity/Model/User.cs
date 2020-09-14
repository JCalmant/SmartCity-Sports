using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.Model
{
    public class User
    {
        public IEnumerable<Disponibilité> Disponibilités { get; set; }
    }
}
