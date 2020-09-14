using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.Service
{
    public class RetourPagePrec
    {
        private static List<String> pagesPrec = new List<String>();

        public static String GetPrec()
        {
            var retour = pagesPrec[pagesPrec.Count - 2];
            pagesPrec.RemoveAt(pagesPrec.Count - 1);
            return retour;
        }

        public static List<String> GetList()
        {
            return pagesPrec;
        }

    }
}
