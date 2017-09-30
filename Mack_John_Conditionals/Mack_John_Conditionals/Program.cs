using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_Conditionals
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/7/2017
             Scalable Data Infrastructions
             Section 01
             Conditionals Assignment
             */



            /*
             Example:  Stuff Your Face
             To get in the heavyweight division of the Strawberry Festival's pie eating contest you must weight 250lbs or more.

             Determine whether an entrant qualifies based on his weight.
                Given:
                    Compeitor's weight.
                Result:
                    "The competitor qualifies for the heavyweight division."  Or "The competitor needs to gain some weight!"
             */

            //Welcome user and ask them for their weight
            Console.WriteLine("Hello!  To get into the heavyweight division of the Strawberry Festival's pie eating contest, you must weigh 250lbs or more.");
            Console.WriteLine("Let's See if you qualify!  Please enter your weight now:");

            //Declare weight variable and define it with the user's input
            string userWeightInput = Console.ReadLine();

            //Declare a null variable to convert user's input into an integer
            int userWeight;

            //Validate user's input to make sure it is a valid integer and is greater than 0
            while (!int.TryParse(userWeightInput, out userWeight))
            {
                //Tell the user what went wrong and re-prompt
                Console.WriteLine("\r\nSorry!  Looks like you're a bit confused.  Please enter your weight, using whole numbers:");

                //Re-capture the user's input
                userWeightInput = Console.ReadLine();
            }

            //Check input to see if they qualify
            if (userWeight < 250)
            {
                //If the user doesn't qualify, give them the bad news
                Console.WriteLine("\r\nToo bad.  Looks like you need to gain some weight!");
            }

            else
            {
                //If the user weighs 250lbs or more, let them know they qualify
                Console.WriteLine("\r\nCongratulations!  You qualify for the heavyweight division!\r\n");
            }


            /*
             Problem #1:  Temperature Converter
             Convert a temperature to either degrees Celsius or degrees Farenheit depending on what the
             user has entered.  First ask the user for a number temperature, for example 85.  Then ask the
             user if that degree is in degrees Celcius or Fahrenheit.  Tell the user to input a "C" or "F".  If the
             temperuature is Celcius the calculator should convert to Fahrenheit.  If the temperature is given
             in Fahrenheit, then the calculator should convert to Celsius.

                User Input:
                    Temperature Number
                    Temperature Type (A String holding an "F" or a "C")
                Result
                    "The temperature is X degrees Celcius."  Or "The temperature is X degrees
                    Fahrenheit."
             */

            //Welcome user and ask them to enter a temperature
            Console.WriteLine("\r\nHello!  Let's convert some temperatures!  Please enter the temperuature you would like to convert now:");

            //Declare input variable and define it with the user's input
            string temperatureInput = Console.ReadLine();

            //Declare a new variable to convert the user's input to an number
            double temperature;

            //Validate user's input and re-prompt if necessary
            while (!double.TryParse(temperatureInput, out temperature))
            {
                //Tell the user what went wrong
                Console.WriteLine("\r\nSorry.  Please enter whole numbers, only.  Do not include letters or decimals.\r\nPlease enter the tempurature you would like to conver now:");

                //Re-capture the user's input
                temperatureInput = Console.ReadLine();
            }

            //Thank the user for their input and move on to the next step
            Console.WriteLine("\r\nThanks!  Now, is this tempurature in Fahrenheit or Celcius?\r\nEnter 'F' for Fahrenheit\r\nEnter 'C' for Celcius");

            //Declare a new char variable and use it to store the user's input
            string farenheitOrCelcius = Console.ReadLine().ToLower();

            //Test to make sure the user entered either 'F' or 'C'
            while (!((farenheitOrCelcius == "f" || farenheitOrCelcius == "c")))
            {
                Console.WriteLine("\r\nSorry!  Please choose from one of the following.\r\nEnter'F' for Fahrenheit\r\nEnter 'C' for Celcius:");
                farenheitOrCelcius = Console.ReadLine();
            }

            //Declare and define a variable holding the equation to convert from Fahrenheit to Celcius
            double farenheitConvertCelcius = (temperature - 32) / 1.8;

            //Declare and define a variable holding the equation to convert from Celcius to Fahrenheit
            double celciusConvertFarenheit = (temperature * 9) / 5 + 32;

            //If the user entered 'F', execute this block of code
            if (farenheitOrCelcius == "f")
            {
                //Display the user's conversion
                Console.WriteLine("\r\nThe temperature is {0} degrees Celcius.\r\n", farenheitConvertCelcius);
            }

            //If the user entered 'C', execute this block of code
            else if (farenheitOrCelcius == "c")
            {
                //Display the user's conversion
                Console.WriteLine("The tempurature is {0} degrees Fahrenheit.\r\n", celciusConvertFarenheit);
            }

            /*
             Data Sets to Test:
                (Note that data sets are not the only numbers that should work with your code, but you
                need to be sure to test these plus 1 of your own for the test value section.)
                    32F is 0C
                    100C is 212F
                    50 c, Does a lowercase c make a difference?  - Yes.  Only upper case F and upper case C will be allowed.  User is reprompted for all other cases and letters.
                    
                    Test One Of Your Own: 76F is 24.444C
             */



            /*
             Problem #2:  Last Chance for Gas
             A driver has to determine if they can make it across the desert with their current fuel.
             They are about to pass the last gas station for the next 200 miles and they need to determine
             whether they should stop now for gas or not.

                User Input:
                    How many gallons does your car tank hold?
                    How full is your gas tank? (in %)
                    How many miles per gallon does your car go?
                Result to Print Out:
                    "Yes, you can drive X more miles and you can make it without stopping for gas!"
                    or "You only have X miles you can drive, better get gas now while you can!"
             */

            //Welcome user and prompt for size of gas tank
            Console.WriteLine("\r\nWelcome, traveler!  You've been on the road for a while now.\r\nYou're about to arrive at the last gas station for the next 200 miles.");
            Console.WriteLine("Let's make sure you've got enough gas to keep going.\r\nIf not, we better stop to fill up!");
            Console.WriteLine("\r\nFirst, how big (in gallons) is your gas tank?");

            //Declare input variable and capture user's response
            string tankSizeInput = Console.ReadLine();

            //Declare a null variable to convert the user's response to a number
            int tankSize;

            //Validate user's input and convert to a number.  Reprompt if user did not enter a whole number
            while (!int.TryParse(tankSizeInput, out tankSize))
            {
                //Tell the user what went wrong and re-prompt
                Console.WriteLine("\r\nUh oh!  I don't understand your answer.\r\nPlease enter the size of your gas tank in gallons.\r\nEnter a whole number:");

                //Recapture the user's input
                tankSizeInput = Console.ReadLine();
            }

            //Thank the user for their input and ask for the next piece of information
            Console.Write("\r\nGreat!  Your tank holds {0} gallons of fuel.  Now, how full is your tank?\r\nPlease enter a percentage.\r\nPercent full = %", tankSize);

            //Declare a variable and capture user's input
            string percentFullInput = Console.ReadLine();

            //Declare a null variable to convert the user's reponse to a number
            int percentFull;

            //Validate user's input and convert to a number.  Reprompt if user did not enter a whole number
            while (!int.TryParse(percentFullInput, out percentFull))
            {
                //Tell the user what went wrong and re-prompt
                Console.Write("\r\nYikes!  This answer doesn't work.  Please try again.\r\nEnter a percentage indicating how full your gas tank is.\r\nPercent full = %");

                //Recapture the user's input
                percentFullInput = Console.ReadLine();
            }

            //Thank the user for ther input and move on to the next step
            Console.WriteLine("\r\nOk, your gas tank is {0}% full.  Now, how many miles per gallon (MPG) does your car get?", percentFull);

            //Declare a variable and capture the user's input
            string milesPerGallonInput = Console.ReadLine();

            //Declare a null variable to convert the user's response to a number
            int milesPerGallon;

            //Validate user's input and convert to a number.  Reprompt if user did not enter a whole number
            while (!int.TryParse(milesPerGallonInput, out milesPerGallon))
            {
                //Tell the user what went wrong and reprompt
                Console.WriteLine("Oh, no!  This is not a valid entry.  Please try again.\r\nPlease enter your car's average miles per gallon (MPG.)");

                //Recapture the user's input
                milesPerGallonInput = Console.ReadLine();
            }

            //Convert percentFull to a percentage decimal for use in calculations
            decimal percentFullDecimal = percentFull / 100m;

            //Calculate how many gallons user has in their tank
            decimal howManyGallons = tankSize * percentFullDecimal;

            //Calculate how far the user can drive
            decimal howFar = howManyGallons * milesPerGallon;

            //Tell the user whether or not they should stop
            //If the user can drive less than 200 miles, they need to stop.
            if (howFar < 200)
            {
                //Tell the user how many miles they can drive and that they need to stop for gas
                Console.WriteLine("\r\nBummer!  You can only drive {0} more miles.  Better stop for gas!\r\n", howFar);
            }

            //Otherwise, the user can continue without stopping
            else
            {
                //Tell the user how many miles they can drive and that they can keep going
                Console.WriteLine("\r\nGreat news!  You can drive another {0} and you don't need to stop for gas!\r\n", howFar);
            }

            /*
              Data Sets to Test:
                    Gallons -20, Gas Tank = 50% full, MPG- 25
                        Result: "Yes, you can drive 250 more miles and you can make it without
                        stopping for gas!"
                    Gallons -12, Gas Tank = 60% full, MPG- 20
                        Result: "You only have 144 miles you can drive, better get gas now while you can!"
                    
            Test One Of Your Own: Gallons -14, Gas Tank = 15%, MPG = 4
                        Result: "You only have 8.4 miles you can drive, better get gas now while you can!"
             */



            /*
             Problem #3:  Grade Letter Calculator
             A student earns a number grade at the conclusion of a course at Full Sail.
             Determine the appropriate letter grade for that number using conditional statements.
                Assume grades are whole numbers that never go below 0 or above 100.
                There should be only one print out to the console.
                Use Full Sail's grade scale:
                    A = 90-100
                    B = 80-89
                    C = 73-79
                    D = 70-72
                    F = 0-69
                User Input
                    Course Grade (in %)
                Result To Print Out:
                    "You have a X%, which means you have earned a(n) X in the class!"
             */

            //Welcome the user and explain what we will be doing
            Console.WriteLine("\r\nWelcome!  So, you'd like to know what your letter grade in Scalable Data Infrastructures will be, eh?");
            Console.WriteLine("Well, if you were lucky enough to have an instructor like Dan, you should have anything to worry about!");
            Console.Write("Just in case though, go ahead an enter your grade percentage and we'll let you know what your leter grade is.  Enter your grade now.\r\nGrade Percentage = %");

            //Capture user's inital input and assign it to a string variable
            string percentGradeInput = Console.ReadLine();

            //Declare a null variable that will be used to store the user's input once it has been successfully been converted to an int datatype
            int percentGrade;

            //Validate user's input to make sure it can be converted to an int datatype and is no lower than 0 and no higher than 100.  If not, reprompt
            while (!int.TryParse(percentGradeInput, out percentGrade) || percentGrade < 0 || percentGrade > 100)
            {
                //Tell the user what went wrong
                Console.Write("\r\nOh, man!  That's not a valid entry.  Let's try again.  Enter your grade now.\r\nGrade Percentage = %");

                //Recaputre user input
                percentGradeInput = Console.ReadLine();
            }

            //If percentGrade is between 90 and 100, tell the user they are getting an A
            if (percentGrade >= 90 && percentGrade <= 100)
            {
                //Tell the user they are getting an A
                Console.WriteLine("\r\nWow, look at that!  You have a {0}%, which means you're getting an A in Scalable Data Infrastructures!\r\n", percentGrade);
            }

            //If percentGrade is between 80 and 89, tell the user they are getting a B
            else if (percentGrade >=80 && percentGrade <= 100)
            {
                //Tell the user they are getting a B
                Console.WriteLine("\r\nHey, not bad!  You have a {0}%, which means you're getting a B in Scalable Data Infrustructures!\r\n", percentGrade);
            }

            //If percentGrade is between 73 and 79, tell the user they are getting a C
            else if (percentGrade >= 73 && percentGrade <= 79)
            {
                //Tell the user they are getting a C
                Console.WriteLine("\r\nGood job!  You have a {0}%, which means you're getting a C in Scalable Data Infrustructures!\r\n", percentGrade);
            }

            //If percentGrade is between 70 and 72, tell the user they are getting a D
            else if(percentGrade >= 70 && percentGrade <= 72)
            {
                //Tell the user they are getting a D
                Console.WriteLine("\r\nBetter study harder!  You have a {0}, which means you're getting a D in Scalable Data Infrustructures!\r\n", percentGrade);
            }

            //If percentGrade is between 0 and 69, tell the user they are getting an F
            else
            {
                Console.WriteLine("Oh, man...you have a {0}%, which means you're getting an F in Scalable Data Infrastructures...\r\n", percentGrade);
            }

            /*
                  Data Sets to Test:
                   Grade - 92%
                       Result- "You have a 92%, which means you have earned a(n) A in the class!"
                   Grade - 80%
                       Result - "You have a 80%, which means you have earned a(n) B in the class!"
                   Grade - 67%
                       Result - "You have a 67%, which means you have earned a(n)F in the class!"
                   What happens when you type in 120%?
                       
                   Is this a valid response?  Can you re-prompt the user?
                   No, this is not a valid response and the user is reprompted

                   Test one of your own
                   Grade - 71%
                        Result - "You have a 71%, which means you have earned a(n) D in the class!"
            */



            /*
             Problem #4: Discount Double Check
             You are going to purchase (2) items from an online store.
             If you spend $100 or more, you will get a 10% discount on your total purchase.
             If you spend between $50 and $100, you will get a 5% discount on your total purchase
             If you spend less than $50, you will get no discount.
                User input:
                Cost of First Item (in $)
                Cost of Second Item (in $)
            Result to print out:
                "Your total purchase is $X." Or
                "Your total purchase is $X, which includes your X% discount."
           */

            //Welcome user and explain what we'll be doing
            Console.WriteLine("\r\nHey, there!  You've just checked out on RainForest.com and we're going to see if you qualify for any discounts with your purchase.");

            //Prompt user to enter cost of first item
            Console.Write("\r\nPlease enter the dollar amount for the first item you purchased.\r\n$");

            //Capture user's input and store in a string variable
            string firstItemInput = Console.ReadLine();

            //Declare decimal variable to convert user input
            decimal firstItem;

            //Validate user's input.  If valid, convert to decimal.  If not, reprompt.
            while (!decimal.TryParse(firstItemInput, out firstItem) || firstItem < 0)
            {
                //Tell the user what went wrong
                Console.Write("\r\nHmmmm.  That doesn't seem to be a valid entry.  Please enter the price of your first item in Dollars and Cents.\r\n$");

                //Recapture user input
                firstItemInput = Console.ReadLine();
            }

            //Prompt user to enter cost of second item
            Console.Write("\r\nGreat!  Now, how much did your second item cost?\r\n$");

            //Capture user's input and store in a string variable
            string secondItemInput = Console.ReadLine();

            //Declare decimal variable to convert user input
            decimal secondItem;

            //Validate user's input.  If valid, convert to decimal.  If not, reprompt.
            while (!decimal.TryParse(secondItemInput, out secondItem) || secondItem < 0)
            {
                //Tell the user what went wrong
                Console.Write("\r\nHmmmm.  That doesn't seem to be a valid entry.  Please enter the price of your second item in Dollars and Cents.\r\n$");

                //Recapture user input
                secondItemInput = Console.ReadLine();
            }

            //Calculate total purchase amount and store in a new variable
            decimal totalPurchase = firstItem + secondItem;

            //If totalPurchase is $100 or more, apply 10% discount and let the user know
            if (totalPurchase >= 100)
            {
                //Calculate total purchase price, including discount
                totalPurchase = totalPurchase * .9m;
                Math.Round(totalPurchase, 2);

                //Tell the user their final price
                Console.WriteLine("\r\nYour total purchase is ${0}, which includes your 10% discount.", totalPurchase.ToString("0.00"));
            }

            //If totalPurchase is between $50 and $100, apply 5% discount and let the user know
            else if (totalPurchase >= 50)
            {
                //Calculate total purchase price, including discount
                totalPurchase = totalPurchase * .95m;
                Math.Round(totalPurchase, 2);

                //Tell the user their final price
                Console.WriteLine("\r\nYour total purchase is ${0}, which includes your 5% discount.", totalPurchase.ToString("0.00"));
            }

            //If totalPurchase is less than $50, let the user know what their final purchase price is
            else
            {
                Console.WriteLine("Your total purchase price is ${0}.", totalPurchase.ToString("0.00"));
            }

           /*
            Data Sets to Test:
                First Item Cost - $45.50, Second Item Cost - $75.00, Total - $108.45
                    Results - Your total purchase is $108.45, which includes your 10% discount."
                First Item Cost - $30.00, Second Item Cost - $25.00, total, $52.25
                    Results - "Your total purchase is $52.2, which includes your 5% discount
                First Item Cost - $5.75, Second Item Cost - $12.50, Total - $18.25
                    Results - Your total purchase is $18.25."
                
            Test One of Your Own
                First Item Cost - $37.82, Secone Item Cost - $84.23, Total - $122.05
             */

        }
    }
}
