using Microsoft.EntityFrameworkCore;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Threading.Tasks;
using Model;

namespace test
{
    [TestClass]
    public class UnitTest1
    {
        SportappsmartcitydbContext _context;
        [TestInitialize]
        public void TestInit()
        {
            DbContextOptionsBuilder builder=new DbContextOptionsBuilder();
            DbContextOptions options = builder.UseSqlServer(@"Data Source=sportappsmartcity.database.windows.net;Initial Catalog=sportAppSmartCityDB;User Id=francoisjulien;Password=francois&julien1").Options;
            _context = new SportappsmartcitydbContext(options);
            _context.Database.EnsureDeleted();
            _context.Database.EnsureCreated();
            _context.SaveChanges();
        }
        [TestMethod]
        public async Task TestMethod(){

        }
    }
}
