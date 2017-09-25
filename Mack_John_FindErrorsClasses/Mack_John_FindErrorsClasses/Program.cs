using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_FindErrorsClasses
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/18/2017
             Scalable Data Infrastructions
             Section 01
             Find The Errors: Classes
             */

                //Corrected MovieTheater to Theater
                //Corected variable name from AmcCineplex20 to amcCineplex20
                //Added missing m suffix to last argument
                Theater amcCineplex20 = new Theater("AMC Cineplex 20", 20, 10.00m);

                //Corrected MovieTheater to Theater
                //Corrected variable name from RegalCenema to regalCinema
                //Added missing m suffix to last argument
                //Corrected second argument from string to integer
                Theater regalCinema = new Theater("Regal Cinema", 15, 8.00m);

                //Corrected variable name from AmcCineplex20 to amcCineplex20
                //Corrected variable name from RegalCinema to amcCineplex20
                Console.WriteLine("\r\nLet's go see a movie at {0}!\r\nThey have {1} movies to choose from and the average ticket price is {2}.", amcCineplex20.GetName(), amcCineplex20.GetNumScreens(), amcCineplex20.GetTicketPrice().ToString("C"));

                //Corrected variable name from RegalCinema to regalCinema
                //Reversed order of method call from regalCinema.GetNumScreen(), regalCinema.GetTicketPrice().ToString("C") to regalCinema.GetTicketPrice().ToString("C"), regalCinema.GetNumScreens()
                Console.WriteLine("\r\nWhat about {0} instead?\r\nTheir average ticket price is only {1}.\r\nThe only drawback is that they only have {2} screens.", regalCinema.GetName(), regalCinema.GetTicketPrice().ToString("C"), regalCinema.GetNumScreens());


                //Corrected variable name from RegalCinema to regalCinema
                decimal totalRegal = regalCinema.TotalTicketCost(7);

                //Corrected variable name from RegalCinema to regalCinema
                //Corrected regalCinema.GetTicketPrice().ToString("C") to regalCinema.TotalTicketCost(4).ToString("C") 
                Console.WriteLine("\r\nIf all 4 of us go to {0}, then that would bring the total cost to {1}.", regalCinema.GetName(), regalCinema.TotalTicketCost(4).ToString("C"));

                //Corrected variable name from AmcCineplex20 to amcCineplex20
                Console.WriteLine("\r\nWait, I forgot I have a coupon for $3.00 off a movie at the {0}!", amcCineplex20.GetName());

                //Corrected variable name from AmcCineplex20 to amcCineplex20
                //This line is incorrect and unecessary, as it can be called directly from the Console.WriteLine call below
                //amcCineplex20.GetTicketPrice(amcCineplex20.GetTicketPrice() - 3.00m);

                //Corrected variable name from AmcCineplex20 to amcCineplex20
                //Corrected method call from amcCineplex20.GetTicketPrice().ToString("C") to (amcCineplex20.GetTicketPrice() - 3.00m).ToString("C"))
                Console.WriteLine("\r\nThat would make a ticket there cost only {0}.", (amcCineplex20.GetTicketPrice() - 3.00m).ToString("C"));

                //Set price per ticket for amcCineplex30 to reflect new total after $3 coupon
                amcCineplex20.SetTicketPrice(amcCineplex20.GetTicketPrice() - 3.00m);

                //Corected variable name from amcCineplex20 to amcCineplex20
                decimal totalAMC = amcCineplex20.TotalTicketCost(4);

                //Corected variable name from RegalCinema to regalCinema
                //Corrected second method call from regalCinema.GetName to amcCineplex20.GetName
                Console.WriteLine("\r\nWith your coupon, all 4 of us can go for only {0}!\r\nLet's go to {1}", totalAMC.ToString("C"), amcCineplex20.GetName());

            }
    }
}
