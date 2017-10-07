using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DVP1.CE1
{
    class TempConvert
    {

        public TempConvert()
        {

            /*
            Name:  John Daniel Mack
            Date:  1710
            Course:  Project & Portfolio 1
            Synposis:  This class will a temperature in farenheit from the user and display the user's input back to them.  It will then convert the user's input into celcius and report
            the results.  Next, the user will input a new temperature in celcius.  The user's input will be reported back to them.  Then, the user's input will be converted to farenheit
            and the results will be reported back to the user.
            */

            Console.Clear();
            Console.WriteLine("Challenge 4:  TEMP CONVERT");
            Console.WriteLine("\r\nHello!  Let's convert some temperatures.\r\nFirst, you'll enter a temperature in FARENHEIT and we will convert it to CELCIUS.");
            Console.Write("Please enter a temperature in FARENHEIT:  ");
            string farenheitInput = Console.ReadLine();

            double farenheit;

            while (!double.TryParse(farenheitInput, out farenheit))
            {

                Console.Clear();
                Console.Write("Oops!  Please try again.\r\nEnter a temperature in FARENHEIT:  ");
                farenheitInput = Console.ReadLine();

            }

            Console.Clear();
            Console.WriteLine("Great!  The temperature you entered is {0} FARENHEIT.", farenheit);
            Console.WriteLine("Now, we'll go ahead and convert that temperature to CELCIUS.  Ready?");
            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            double celcius = Math.Round(FarenheitToCelcius(farenheit), 2);

            Console.Clear();
            Console.WriteLine("The temperature you entered is {0} FARENHEIT.", farenheit);
            Console.WriteLine("Your converted temperature is {0} CELCIUS.", celcius);
            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            Console.Clear();
            Console.WriteLine("Next, we'll ask you for a temperature in CELCIUS and convert it to FARENHEIT.");
            Console.Write("Please enter a temperature in CELCIUS:  ");
            string celciusInput = Console.ReadLine();

            while (!double.TryParse(celciusInput, out celcius))
            {

                Console.Clear();
                Console.Write("Oops!  Please try again.\r\nEnter a temperature in CELCIUS:  ");
                celciusInput = Console.ReadLine();

            }

            Console.Clear();
            Console.WriteLine("Greate!  The temperature you entered is {0} CELCIUS.", celcius);
            Console.WriteLine("Now, we'll go ahead and covert that tempurature to FARENHEIT.  Ready?");
            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            farenheit = Math.Round(CelciusToFarenheit(celcius), 2);

            Console.Clear();
            Console.WriteLine("The temperature you entered is {0} CELCIUS.", celcius);
            Console.WriteLine("Your converted temperature is {0} FARENHEIT.", farenheit);
            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            new Menu();

        }



        public double FarenheitToCelcius(double _farenheit)
        {

            double celcius = (_farenheit - 32) * 5 / 9;
            return celcius;

        }



        public double CelciusToFarenheit(double _celcius)
        {

            double farenheit = (_celcius * 1.8) + 32;
            return farenheit;

        }

    }
}
