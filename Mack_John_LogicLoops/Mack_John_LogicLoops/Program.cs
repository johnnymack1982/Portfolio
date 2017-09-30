using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_LogicLoops
{
    class Program
    {
        static void Main(string[] args)
        {
            /*
             John Mack
             8/8/2017
             Scalable Data Infrustructures
             Section 01
             Logic & Loops
             */



            /*
             Problem #1 - Logical Operators: Tire Pressure 1
             To meet to maintenance standards a car's front two tires should have the same pressure and
             the back two tires should have the same pressure.  However, the front tires and the rear tires
             can have different pressure than each other, so it is not necessary for all four tires' pressure to
             be the same!
             Create a single conditional that would determine if the tires of a given car are up to spec.  Make
             sure to use the array value inside of the conditional.
                User inputs:
                    Prompt for the pressure in each of the (4) tires one at a time.
                        Store the converted number values inside of an array that you create.
                Result to print out:
                    "The tires pass spec!" Or "Get your tires checked out!"
             */

            //Declare array to store front tire pressure values for use in conditional loop
            int[] frontTirePressure = new int[2];

            //Declare array to store back tire pressure values for use in conditional loop
            int[] backTirePressure = new int[2];

            //Welcome the user and explain what we're going to do
            Console.WriteLine("Welcome!  To meet maintenance standards for your car, the two front tires should have the same pressure and\r\nthe back two tires should have the same pressure.");
            Console.WriteLine("However, the front tires and the rear tires can have different pressure than each other.  Let's check the pressure on each tire to see if you're ok!");

            //Prompt user for Front Left tire pressure
            Console.WriteLine("\r\nPlease enter the tire pressure for your FRONT LEFT tire:");

            //Capture user input for Front Left tire pressure
            string frontLeftTireInput = Console.ReadLine();

            //Validate user input for frontLeftInput and reprompt if invalid.  Convert and store in frontPressure array if valid.
            while (!int.TryParse(frontLeftTireInput, out frontTirePressure[0]))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That entry wasn't invalid.  Let's try again.\r\nPlease enter the tire pressure for your FRONT LEFT tire:");

                //Recapture user input
                frontLeftTireInput = Console.ReadLine();
            }

            //Prompt user for Front Right tire pressure
            Console.WriteLine("\r\nPlease enter the tire pressure for your FRONT RIGHT tire:");

            //Capture user input for Front Right tire pressure
            string frontRightTireInput = Console.ReadLine();

            //Validate user input for frontRightInput and reprompt if invalid.  Convert and store in frontPressure array if valid.
            while (!int.TryParse(frontRightTireInput, out frontTirePressure[1]))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That entry wasn't valid.  Let's try again.\r\nPlease enter the tire pressure for your FRONT RIGHT tire:");

                //Recapture user input
                frontRightTireInput = Console.ReadLine();
            }

            //Prompt user for Back Left tire pressure
            Console.WriteLine("\r\nPlease enter the tire pressure for your BACK LEFT tire:");

            //Capture user input for Back Left tire pressure
            string backLeftTireInput = Console.ReadLine();

            //Validate user input for backLeftInput and reprompt if invalid.  Convert and store in backPressure array if valid.
            while (!int.TryParse(backLeftTireInput, out backTirePressure[0]))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That entry wasn't valid.  Let's try again.\r\nPlease enter the tire pressure for your BACK LEFT tire:");

                //Recapture user input
                backLeftTireInput = Console.ReadLine();
            }

            //Prompt user for Back Right tire pressure
            Console.WriteLine("\r\nPlease enter the tire pressure for your BACK RIGHT tire:");

            //Capture user input for Back Right tire pressure
            string backRightTireInput = Console.ReadLine();

            //Validate user input for backRightInput and reprompt if invalid.  Convert and store in backPressure array if valid.
            while (!int.TryParse(backRightTireInput, out backTirePressure[1]))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That entry wasn't valid.  Let's try again.\r\nPlease enter the tire pressure for your BACK RIGHT tire:");

                //Recapture user input
                backRightTireInput = Console.ReadLine();
            }

            //Run check to see if car is up to spec.  If so, let the user know
            if (frontTirePressure[0] == frontTirePressure[1] && backTirePressure[0] == backTirePressure[1])
            {
                Console.WriteLine("\r\nThe tires pass spec!\r\n");
            }

            //Otherwise, let the user know they need to get their tires checked
            else
            {
                Console.WriteLine("\r\nGet your tires checked out!\r\n");
            }

            /*
             Data Sets To Test:
                Front Left = 32, Front Right = 32, Back Left = 30, Back Right = 30 - Tires OK
                Front Left = 36, Front Right = 32, Back Left = 25, Back Right = 25 - Check Tires
                
            Test one of your own and write the results in your comment section:
                Front Left = 50, Front Right = 51, Back Left = 49, Back Right = 48 - Check Tires
             */



            /*
             Problem #2 - Logical Operators: Movie Ticket Price
             The local movie theater in town has a normal ticket price of $12.00.
             If you are a senior (55 and older) or you are under 10, you get the discounted price of $7.00.
             In addition, if a customer is seeing a movie between 14 and 17 aka (2pm-5pm, you get the discounted price of $7.00.
             Create a single conditional block that would determine which of the two prices the customer is
             eligible for.  Make sure you are using logical operators!
                User Inputs:
                    Age of the customer
                    Time of Movie (Assume whole numbers here and use military time.)
                Result to print out:
                    "The ticket price is $X."
             */

            //Welcome user and explain what we'll be doing.  Prompt for age.
            Console.WriteLine("Hello!  Going to the movies, huh?  Let's see how much your tickets will be!  Please enter your age:");

            //Capture user input and store in a string variable
            string userAgeInput = Console.ReadLine();

            //Declare a new variable to convert user input to number
            int userAge;

            //Validate user response.  Reprompt if invalid.  Convert to decimal if valid
            while (!int.TryParse(userAgeInput, out userAge))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nThat doesn't seem right.  Please enter your age:");

                //Recapture user input
                userAgeInput = Console.ReadLine();
            }

            //Prompt for movie time
            Console.WriteLine("\r\nGreat!  And what time is your movie at?  (Please use MILITARY TIME.  For example, 2pm should be entered as 14.)");

            //Capture user input and store in a string variable
            string movieTimeInput = Console.ReadLine();

            //Declare a new variable to convert user input to decimal
            int movieTime;

            //Validate user response.  Reprompt if invalid.  Convert to decimal if valid
            while(!int.TryParse(movieTimeInput, out movieTime))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nThat doesn't seem right.  Please enter the movie time in MILITARY TIME:");

                //Recapture user input
                movieTimeInput = Console.ReadLine();
            }

            //Declare ticket price variable and set to inital value of $12.00
            decimal ticketPrice = 12.00m;

            //Decide if user gets discounted price.
            //If user is 55 or older OR under 10, they get the discounted price
            //If the movie time is between 2pm and 7pm, they get the discounted price
            if ((userAge >=55 || userAge < 10) || (movieTime >=14 && movieTime <= 17))
            {
                ticketPrice = 7.00m;
            }

            //Print the result for the user
            Console.WriteLine("\r\nThe ticket price is ${0}.\r\n", ticketPrice);


            /*
             Data sets to test:
                Age = 57, Time = 20, Ticket Price = $7.00
                Age = 9, Time = 20, Ticket Price = $7.00
                Age = 38, Time = 20, Ticket Price = $12.00
                Age = 25, Time = 16, Ticket Price = $7.00
                
                Test one of your own and write the results in your comment section:
                Age = 34, Time = 7, Ticket Price = $12.00
             */



            /*
             Problem #3 - For Loop:  Add Up the Odds or Evens
             Create an array of 6 or more integers.  Then, you will prompt the user if they would like to see
             the sum of the even numbers or odd numbers from the array.
             For this question you do NOT have to ask the user for the values inside of the array, these can
             be hardcoded.
             Use a FOR loop to cycle through the array and find the correct sum that the user wants.
             Hint - If you need help finding if it is odd or even, review the Modulus operator.
                User Inputs:
                    Prompt the user to either see the sum of the odd or even numbers.
                Results to print out:
                    "The odd numbers add up to X" or "The even numbers add up to X"
             */

            //Declare and define the array
            int[] numberArray = new int[6] { 34, 35, 36, 37, 38, 39, };

            //Welcome user and explain what we're going to do.
            Console.WriteLine("\r\nHello!  I'm going to give you a list of numbers and I'd like you to tell me whether your would like to see the sum of all of the ODD or EVEN numbers in this list.");
            Console.WriteLine("Ready?  The numbers are: {0}, {1}, {2}, {3}, {4}, {5}", numberArray[0], numberArray[1], numberArray[2], numberArray[3], numberArray[4], numberArray[5]);
            Console.WriteLine("Now, would you like to see the sum of the ODD or EVEN numbers in this list?\r\nPlease enter the word ODD or EVEN:");

            //Capture the user's input and store in a new string variable
            string oddOrEven = Console.ReadLine().ToLower();

            //Validate user's input.  If invalid, reprompt
            while (oddOrEven != "odd" && oddOrEven != "even")
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That's not a valid entry.  Let's try again.\r\nWould you like to see the sum of the ODD or EVEN numbers?");

                //Recapture user input
                oddOrEven = Console.ReadLine();
            }

            //Declare a variable used to output user's request
            int numberSum = 0;

            //Cycle through array and add up odd or even numbers, depending on user's choice
            foreach (int element in numberArray)
            {
                //If user selected ODD, add all odd numbers and store sum in numberSum
                if (oddOrEven == "odd" && element %2 == 1)
                {
                    numberSum = numberSum + element;
                }

                //If user selected EVEN, add all even numbers and store sum in numberSum
                else if (oddOrEven == "even" && element %2 == 0)
                {
                    numberSum = numberSum + element;
                }
            }

            //If user selected ODD, display sum of odd numbers
            if (oddOrEven == "ODD" || oddOrEven == "odd" || oddOrEven == "Odd")
            {
                Console.WriteLine("\r\nThe odd numbers add up to {0}.\r\n", numberSum);
            }

            //If user selected EVEN, display sum of even numbers
            else if (oddOrEven == "EVEN" || oddOrEven == "even" || oddOrEven == "Even")
            {
                Console.WriteLine("\r\nThe even numbers add up to {0}.\r\n", numberSum);
            }

            /*
             Data sets to test:
                Array: {1, 2, 3, 4, 5, 6, 7}, Sum of Evens = 12, Sum of Odds = 16
                Array: {12, 13, 14, 15, 16, 17}, Sum of Evens = 42, Sum of Odds = 45
                
                My Array: {34, 35, 36, 37, 38, 39}, Sum of Evens =108 , Sum of Odds = 111
             */



            /*
             Problem #4 - While Loop: Charge It!
             While loops are an excellent choice when you need code to repeat an unknown number of
             times.
             In this problem you are going to keep buying items until you reach your maximum credit limit.
             You will ask the user for their credit limit first.
             Then create a while loop that will start deducting each purchase from the credit limit.  As long
             as the credit limit is above zero, the while loop should continue looping.  When the user goes
             over the credit limit, stop the loop and tell them how much they went over by.
                User inputs:
                    Max Credit Limit
                    Inside of the loop, keep asking the user how much their purchase is.
                Results to print out
                    For each "purchase" (hint: every time it loops) the loop should tell the user how
                    much money they have left that they could spend.  "With your current purchase
                    of $X, you can still spend $X."
             */

            //Welcome the user and tell them what we'll be doing
            Console.WriteLine("Hello!  So, you like to shop?  Let's keep track of your spending to make sure you don't go over your credit limit.");

            //Prompt user for their credit limit
            Console.WriteLine("First, how much is your credit limit?");

            //Capture user's input
            string creditLimitInput = Console.ReadLine();

            //Declare a variable to convert user's input to a decimal
            decimal creditLimit = 0.00m;

            //Validate user's input and reprompt if invalid.  If valid, convert to decimal
            while (!decimal.TryParse (creditLimitInput, out creditLimit))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That wasn't a valid entry.\r\nPleas enter your credit limit:");

                //Recapture user's input
                creditLimitInput = Console.ReadLine();
            }

            //Tell the user what we're doing next
            Console.WriteLine("\r\nNow, please enter your purchase amounts one purchase at a time.\r\nYou will be updated with your remaining credit balance after each purchase.");

            //Declare a variable to convert user's input to a decimal once they have entered it
            decimal purchaseAmount = 0.00m;

            //Calculate remaining credit balance after each purchase
            while (creditLimit > 0)
            {
                //Prompt user for purchase amount
                Console.WriteLine("\r\nPlease enter a purchase amount:");

                //Capture user's input
                string purchaseAmountInput = Console.ReadLine();

                //Validate user's input and repropmt if invalid.  If valid, convert to decimal
                while (!decimal.TryParse(purchaseAmountInput, out purchaseAmount))
                {
                    //Tell the user what's wrong
                    Console.WriteLine("O\r\nops!  That wasn't a valid entry.\r\n  Please enter a purchase amount:");

                    //Recapture user's input
                    purchaseAmountInput = Console.ReadLine();
                }

                //Deduct purchase amount from credit limit
                creditLimit = creditLimit - purchaseAmount;

                //Display updated credit limit
                Console.WriteLine("\r\nWith your current purchase of ${0}, you can still spend ${1}", purchaseAmount.ToString("0.00"), creditLimit.ToString("0.00"));
            }

            if (creditLimit == 0)
            {
                //Tell user they have reached their credit limit
                Console.WriteLine("\r\nWith your current purchase of ${0}, you have reached your credit limit and cannot spend any more.", purchaseAmount.ToString("0.00"));
            }

            else if (creditLimit < 0)
            {
                //Tell the user they have exceeded their credit limit
                Console.WriteLine("\r\nWith your current purchase of ${0}, you have exceeded your credit limit by ${1}", purchaseAmount.ToString("0.00"), creditLimit.ToString("0.00"));
            }

            /*
             Data sets to test:
                Credit Limit = 20.00
                    Purchase 1 = 5.00 - You can still spend $15.00
                    Purchase 2 = 12.00 - You can still spend $3.00
                    Purchase 3 = 7.00 - You have exceeded your limit by $4.00
                
            Test one of your own and write the results in your comment section.
                Credit Limit = 500
                    Purchase 1 = 100
                    Purchase 2 = 50
                    Purchase 3 = 100
                    Purchase 4 = 25
                    Purchase 5 = 50
                    Purchase 6 = 75
                    Purchase 7 = 100 - You have reached your credit limit

             */
        }
    }
}
