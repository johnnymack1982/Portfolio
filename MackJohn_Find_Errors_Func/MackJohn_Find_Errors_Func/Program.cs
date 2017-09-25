using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MackJohn_Find_Errors_Func
{
    class Program
    {
        static void Main(string[] args)
        {

            //Name: John Mack
            //Date: 8/13/2017
            //Section: 01
            //Find The Errors In Functions

            //In this program we will be asking for 2 prices for the user
            //We will ask for the sales tax rate
            //Create a function that will return the price + sales tax
            //Create a function that will add the 2 prices with sales tax together for the total cost

            Console.WriteLine("Hello and welcome to our purchase calculator!\r\nWe will be asking you for 2 item prices and the sales tax rate.\r\n");

            Console.WriteLine("What is the cost of your first item?");

            string cost1String = Console.ReadLine();

            decimal cost1;

            //Corrected order from (cost1, out cost1String) to (cost1String, out cost1)
            while (!decimal.TryParse(cost1String, out cost1))
            {
                Console.WriteLine("Please only type in numbers!\r\nWhat is the cost of your first item?");

                cost1String = Console.ReadLine();

            }


            Console.WriteLine("What is the cost of your second item?");

            string cost2String = Console.ReadLine();

            decimal cost2;

            while (!decimal.TryParse(cost2String, out cost2))
            {
                Console.WriteLine("Please only type in numbers!\r\nWhat is the cost of your second item?");

                //Corrected from Console.WriteLine to Console.ReadLine
                cost2String = Console.ReadLine();

            }


            Console.WriteLine("What is the sales tax rate %?");

            string salestaxString = Console.ReadLine();

            //Corrected datatype from int to decimal
            decimal salesTax;

            while (!decimal.TryParse(salestaxString, out salesTax))
            {
                Console.WriteLine("Please only type in numbers!\r\nWhat is the sales tax rate in %?");

                salestaxString = Console.ReadLine();

            }

            Console.WriteLine("I have all the information I need.\r\nYour first item costs {0}.\r\nYour second item costs {1} and the sales tax is {2}%.", cost1, cost2, salesTax);

            //Corrected second argument from salestaxString to salesTax
            decimal cost1WithTax = AddSalesTax(cost1, salesTax);

            //Added argument salesTax to correspond with parameter 'tax' in the called method
            decimal cost2WithTax = AddSalesTax(cost2, salesTax);

            //corrected arguments to (cost1WithTax, cost2WithTax)
            decimal grandTotal = TotalCosts(cost1WithTax, cost2WithTax);

            //This line was causing a run-time error
            //Corrected place holders to a 0 based index, rather than a 1 based index
            Console.WriteLine("\r\nWith tax your first item costs {0}.\r\nYour second item costs {1}.", cost1WithTax.ToString("C"), cost2WithTax.ToString("C"));

            Console.WriteLine("\r\nWhich makes the total for your bill {0}", grandTotal.ToString("C"));



        }

        //Removed parameter 'decimal otherPrice'
        public static decimal AddSalesTax(decimal price, decimal tax)
        {

            decimal totalWithTax = price + price * (tax / 100);

            //Corrected from 'return totalWithTax, price;' to 'return totalWithTax;
            return totalWithTax;

        }

        //Corrected return type from void to decimal
        public static decimal TotalCosts(decimal cost1, decimal cost2)
        {
            //Corrected variables to correspond with the cost1 and cost2 parameters in the method declaration
            decimal total = cost1 + cost2;

            //Added missing ';' to end of line
            return total;

        }
    }
}
