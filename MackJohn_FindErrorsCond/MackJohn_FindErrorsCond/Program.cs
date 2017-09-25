using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MackJohn_FindErrorsCond
{
    class Program
    {
        static void Main(string[] args)
        {

            //  NAME:  John Mack
            //  DATE:  8/10/2017
            // Scalable Data Infrastructures
            //  Section 01
            //  Find and fix the errors

            String myName = "John Doe";

            //Corrected 'my Job' to 'myJob'
            //Added missing "
            String myJob = "\"Cat Wrangler\"";

            //Changed variable type from double to decimal
            //Added m suffix to variables
            //Added missing ; to myRatePerCat
            decimal myRatePerCat = 7.50m;
            decimal totalPay = 0m;

            int numberOfCats = 40;

            //Corrected 'boolean' to 'Boolean'
            Boolean employed = true; //Make sure you use this variable

            //Corrected string concatonation
            Console.WriteLine("Hello!  My name is {0}.", myName);
            Console.WriteLine("I'm a " + myJob + ".");

            //Added missing space to string concatonation
            Console.WriteLine("My current assignment has me wrangling " + numberOfCats + " cats.");
            Console.WriteLine("So, let's get to work!");

            while (numberOfCats > 0) //Do Not Change This line
            {

                //Corrected logical operator from '=' to '=='
                if (employed == true)
                {
                    //Changed operator from '-=' to '+='
                    totalPay += myRatePerCat;

                    //Corrected 'System.out.println' to 'Console.WriteLine'
                    Console.WriteLine("I've wrangled another cat and I have made $" + totalPay + " so far.  \r\nOnly " + numberOfCats + " left!");

                }
                else
                {

                    //Corrected 'System.out.println' to 'Console.WriteLine'
                    Console.WriteLine("I've been fired!  Someone else will have to wrangle the rest!");

                    //Changed from 'continue' to 'break'
                    break;

                };

                numberOfCats--;

                if (numberOfCats == 5)
                {

                    //Corrected '==' to '='
                    //Cahnged boolean value from true to false
                    employed = false;

                }

            }

        }
    }
}
    
