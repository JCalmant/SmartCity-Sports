using GalaSoft.MvvmLight.Ioc;
using GalaSoft.MvvmLight.Views;
using Microsoft.Practices.ServiceLocation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BackEndSmartCity.ViewModel
{
    public class ViewModelLocator
    {
        public ViewModelLocator()
        {
            ServiceLocator.SetLocatorProvider(() => SimpleIoc.Default);

            SimpleIoc.Default.Register<MainPageViewModel>();
            SimpleIoc.Default.Register<HomePageViewModel>();
            SimpleIoc.Default.Register<SportPageViewModel>();
            SimpleIoc.Default.Register<ComplexePageViewModel>();
            SimpleIoc.Default.Register<StatsPageViewModel>();

            NavigationService navigationPages = new NavigationService();
            SimpleIoc.Default.Register<INavigationService>(() => navigationPages);

            navigationPages.Configure("MainPage", typeof(MainPage));
            navigationPages.Configure("HomePage", typeof(HomePage));
            navigationPages.Configure("SportPage", typeof(SportPage));
            navigationPages.Configure("ComplexePage", typeof(ComplexePage));
            navigationPages.Configure("StatsPage", typeof(StatsPage));

        }
        public MainPageViewModel Main
        {
            get
            {
                return ServiceLocator.Current.GetInstance<MainPageViewModel>();
            }
        }
        public HomePageViewModel Home
        {
            get
            {
                return ServiceLocator.Current.GetInstance<HomePageViewModel>();
            }
        }
        public SportPageViewModel Sport
        {
            get
            {
                return ServiceLocator.Current.GetInstance<SportPageViewModel>();
            }
        }
        public ComplexePageViewModel Complexe
        {
            get
            {
                return ServiceLocator.Current.GetInstance<ComplexePageViewModel>();
            }
        }
        public StatsPageViewModel Stats
        {
            get
            {
                return ServiceLocator.Current.GetInstance<StatsPageViewModel>();
            }
        }
    }
}
