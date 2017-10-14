using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DVP1.CE1
{
    class Menu
    {

        public Menu()
        {

            Console.Clear();
            Console.WriteLine("Challenge 5:  MENU");

            Console.WriteLine("\r\nPlease choose an item from the following menu:");

            Console.WriteLine("\r\n1) Challenge 1 - Swap Info:  Enter your first and last name and we'll reverse them for you!");
            Console.WriteLine("2) Challenge 2 - Backwards:  Enter a sentence and we'll type it again in reverse!");
            Console.WriteLine("3) Challenge 3 - Age Convert:  Enter your age in years and we'll tell you how many days, hours, minutes, and seconds you've been alive!");
            Console.WriteLine("4) Challenge 4 - Temp Convert:  Enter temperatures in farenheit and celcius and we'll convert them for you!");
            Console.WriteLine("5) EXIT PROGRAM");

            Console.Write("\r\nPlease enter your selection:  ");
            string userChoiceInput = Console.ReadLine();

            int userChoice;

            while (!int.TryParse(userChoiceInput, out userChoice))
            {

                Console.Clear();
                Console.WriteLine("Challenge 5:  MENU");

                Console.WriteLine("\r\nPlease choose an item from the following menu:");

                Console.WriteLine("\r\n1) Challenge 1 - Swap Info:  Enter your first and last name and we'll reverse them for you!");
                Console.WriteLine("2) Challenge 2 - Backwards:  Enter a sentence and we'll type it again in reverse!");
                Console.WriteLine("3) Challenge 3 - Age Convert:  Enter your age in years and we'll tell you how many days, hours, minutes, and seconds you've been alive!");
                Console.WriteLine("4) Challenge 4 - Temp Convert:  Enter temperatures in farenheit and celcius and we'll convert them for you!");
                Console.WriteLine("5) EXIT PROGRAM");

                Console.WriteLine("\r\nOops!  That wasn't a valid choice.  Please enter the number that corresponds with your choice.");
                Console.Write("\r\nPlease enter your selection:  ");
                userChoiceInput = Console.ReadLine();

            }



            if (userChoice == 1)
            {

                new SwapInfo();

            }



            else if (userChoice == 2)
            {

                new Backwards();

            }



            else if (userChoice == 3)
            {

                new AgeConvert();

            }



            else if (userChoice == 4)
            {

                new TempConvert();

            }



            else
            {

                Console.Clear();
                Console.WriteLine("EXIT PROGRAM");
                Console.WriteLine("\r\nYou have chosen to exit the program.\r\n\r\n");

                Environment.Exit(0);

            }

        }

    }
}
