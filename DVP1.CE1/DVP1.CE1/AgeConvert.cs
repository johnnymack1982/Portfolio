using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DVP1.CE1
{
    class AgeConvert
    {

        public AgeConvert()
        {

            /*
            Name:  John Daniel Mack
            Date:  1710
            Course:  Project & Portfolio 1
            Synposis:  This class will collect the user's name and age in years.  It will then pass the user's age in years into custom functions, calculating the user's age in
            days, hours, minutes and seconds, taking leap year into account.
            */

            Console.Clear();

            Console.WriteLine("Coding Challenge 3:  AGE CONVERT");

            Console.Write("\r\nHello!  Please enter your name:  ");
            string userName = Console.ReadLine();

            //Validate user input
            while (String.IsNullOrWhiteSpace(userName))
            {
                Console.Clear();
                Console.Write("Oops!  Please don't leave this blank.\r\nPlease enter your name:  ");
                userName = Console.ReadLine();
            }

            Console.Clear();
            Console.Write("Thanks, {0}!  Now, please enter your age in years:  ", userName);
            string userAgeInput = Console.ReadLine();

            int userAge;

            //Validate user input
            while (!int.TryParse(userAgeInput, out userAge) || userAge < 1)
            {
                Console.Clear();
                Console.Write("Oops!  Please enter a whole number greater than 0.\r\nPlease enter your age in years:  ");
                userAgeInput = Console.ReadLine();
            }

            Console.Clear();
            Console.WriteLine("Great!  You entered the following information");
            Console.WriteLine("\r\nName:  {0}\r\nAge:  {1}", userName, userAge);
            Console.WriteLine("\r\nNow, we'll show you how many days, hours, minutes and seconds you have been alive.  Ready?");
            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            Console.Clear();
            decimal daysAlive = DaysAlive(userAge);
            Console.WriteLine("{0} has been alive for {1} years.", userName, userAge);
            Console.WriteLine("{0} has been alive for {1} days.", userName, daysAlive.ToString("#,##0"));

            decimal hoursAlive = HoursAlive(daysAlive);
            Console.WriteLine("{0} has been alive for {1} hours.", userName, hoursAlive.ToString("#,##0"));

            decimal minutesAlive = MinutesAlive(hoursAlive);
            Console.WriteLine("{0} has been alive for {1} minutes.", userName, minutesAlive.ToString("#,##0"));

            decimal secondsAlive = SecondsAlive(minutesAlive);
            Console.WriteLine("{0} has been alive for {1} seconds.", userName, secondsAlive.ToString("#,##0"));

            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            new Menu();

        }



        public decimal DaysAlive(decimal _userAge)
        {

            decimal leapYearDays = Math.Round(_userAge / 4);
            decimal daysAlive = (_userAge * 365) + leapYearDays;
            return daysAlive;

        }



        public decimal HoursAlive(decimal _daysAlive)
        {

            decimal hoursAlive = _daysAlive * 24;
            return hoursAlive;

        }



        public decimal MinutesAlive(decimal _hoursAlive)
        {

            decimal minutesAlive = _hoursAlive * 60;
            return minutesAlive;

        }



        public decimal SecondsAlive(decimal _minutesAlive)
        {

            decimal secondsAlive = _minutesAlive * 60;
            return secondsAlive;

        }

    }
}
