using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_RestaurantCalc
{
    class Program
    {
        static void Main(string[] args)
        {
            /*
             John Mack
             Section 01
             8/3/2017
             Tip Calculator
             */

            //First, greet the user and ask them for their name
            Console.WriteLine("Hello, there!  Thank you for using \"Tip Calculator.\"  Before we begin, would you mind telling me your name?");
            string userName = Console.ReadLine();

            //Thank the user for their input and read their name back to them.
            Console.WriteLine("\r\nAwesome.  Thanks, {0}!", userName);

            //Explain the next step and collect dollar amount for the first meal
            Console.WriteLine("\r\nNext, we're going to ask you to enter the dollar amount for each of the meals you and your guests ordered today.\n\rSo how much was your first meal?");
            Console.Write("$");
            string firstMealInput = Console.ReadLine();

            //Convert the user's input from string to decimal for use in math later on
            decimal firstMealPrice = decimal.Parse(firstMealInput);

            //Collect the dollar amount for the second meal
            Console.WriteLine("\r\nGreat!  Now, how much was your second meal?");
            Console.Write("$");
            string secondMealInput = Console.ReadLine();

            //Convert the user's input from string to decimal for use in math later on
            decimal secondMealPrice = decimal.Parse(secondMealInput);

            //Collect the dollar amount for the third meal
            Console.WriteLine("\r\nAnd finally, how much did you pay for the third meal?");
            Console.Write("$");
            string thirdMealInput = Console.ReadLine();

            //Convert the user's input from string to decimal for use in math later on
            decimal thirdMealPrice = decimal.Parse(thirdMealInput);

            //Explain the next step and collect the tip amount
            Console.WriteLine("\r\nNow, please enter a tip percentage based on your overall satisfaction with the service you received today.");
            Console.Write("%");
            string tipPercentInput = Console.ReadLine();

            //Convert user's input from string to decimal for use in math later on
            decimal tipPercent = decimal.Parse(tipPercentInput);

            //Convert tipPercent from a whole number to a decimal for percentage calculations
            tipPercent = tipPercent / 100;

            //Thank the user for their input
            Console.WriteLine("\r\nThanks, {0}!  Your total is shown below.  Please come again!", userName);

            //Calculate subtotal of the three meals
            decimal mealSubTotal = firstMealPrice + secondMealPrice + thirdMealPrice;

            //Display the subtotal for the user
            Console.WriteLine("\r\nYour subtotal for three meals is $" + mealSubTotal.ToString("0.00"));

            //Calculate total price of the tip
            decimal tipSubTotal = mealSubTotal * tipPercent;

            //Display the tip total for the user
            Console.WriteLine("Your total tip is ${0}", tipSubTotal.ToString("0.00"));

            //Calculate the total bill, including tip
            decimal finalTotal = mealSubTotal + tipSubTotal;

            //Display the final total for the user
            Console.WriteLine("Your total, including tip, is ${0}", finalTotal.ToString("0.00"));

            //Calculate the total split three ways
            decimal splitTheBill = finalTotal /= 3;

            //Display the bill split three ways for the user
            Console.WriteLine("If you want to split the bill between all three guests, you should each pay ${0}", splitTheBill.ToString("0.00"));

            /*
             Test #1
                Check #1 - 10.00
                Check #2 - 15.00
                Check #3 - 25.00
                Tip % - 20
                Sub-Total Without Tip - 50.00
                Total Tip - 10.00
                Grand total with tip - 60.00
                Cost per person - 20.00

            Test #2
                Check #1 - 20.25
                Check #2 - 17.75
                Check #3 - 23.90
                Tip % - 10
                Sub-Total Without Tip - 61.90
                Total Tip - 6.19
                Grand total with tip 68.09
                Cost per person - 22.69666 or rounded 22.70
             */
        }
    }
}
