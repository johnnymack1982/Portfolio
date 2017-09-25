using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_StringObjects
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/14/2017
             Scalable Data Infrastructures
             Section 01
             String Objects
             */



            /*
             Problem #1: Email Address Checker
             Create a method that will accept a String as an argument and then check to see if the String
             follows the pattern of an email address, such as aaa@bbb.ccc.

             You must check the following:
             1.  Only one @ symbol in the whole string
             2.  No spaces
             3.  At least one dot after the @ symbol
             4.  There is no need to check for .com, .edu, etc.
             5.  The code should work for any string entered.

             User Input (Prompt In Main Method, Use As Srguments):
                String of an email address

             Return Value From Custom Function To Main Method:
                "valid" or "invalid"

             Result to print to the console in the Main Method:
                "The email address of X is a valid email address." or "The email address of X is
                not a valid email address."
             */

            //Welcome the user and tell them what we're doing
            Console.WriteLine("\r\nHello!  Thanks for downloading 'Families' by Pyng Media.\r\nTo get started, please enter your e-mail address:");

            //Capture user input and store in a string variable
            string userEmail = Console.ReadLine();

            //Validate that the user didn't leave their response blank
            while (string.IsNullOrWhiteSpace(userEmail))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  Please don't enter a blank response.\r\nPlease enter your e-mail address:");

                //Recapture user input
                userEmail = Console.ReadLine();
            }

            //Send user input to ValidateEmail method to validate
            ValidateEmail(userEmail);

            //Declare a new method to validate user input

            //Store returned value from ValidateEmail method
            bool valid = ValidateEmail(userEmail);

            //If email address is valid, let the user know
            if (valid == true)
            {
                Console.WriteLine("\r\nThe email address of {0} is a valid email address.\r\n", userEmail);
            }

            //If email address is invalid, let the user know
            else
            {
                Console.WriteLine("\r\nThe email address of {0} is not a valid email address.\r\n", userEmail);
            }

            /*
             Data Sets To Test
                Email = test@fullsail.com
                    Results = "The email address of test@fullsail.com is a valid email address."
                Email = test@full@sail.com
                    Results = "The email address of test@full@sail.com is not a valid email address."
                Email = test@full sail.com
                    Results = "The email address of test@full sail.com is not a valid email address."

                Now try an email of your own
                Email = jdmack@nc.rr.com
             */



            /*
             Problem #2: Separator Swap Out
                Create a method that will accept three strings.
                    1.  First will be a list of items separated by a given string
                    2.  Second will be the separator used
                    3.  Third will be a new separator

             The function will then perform the task of replacing the first separator with the second and
             returning the result.  Thus, a list such as "a,b,c,d" with the first separator being "," and the
             second separator being "/" would become "a/b/c/d".

             User Input (Prompt In Main Method, Use As Arguments):
                String of a list of items separated by a given string
                String of the separator used
                String of the new separator
            Return Value From Custom Function To Main Method
                The new String with the swapped out separator
            Result to print to the console in the Main Method:
                "The orginal string of X with the new separator is X."
             */

            //Welcome the user and explain what we'll be doing
            Console.WriteLine("\r\nHello!  We're going to replace the \"separators\" in a string with new \"separators\".  Are you ready?\r\nFirst go ahead and enter a string of words or characters and separate them with something like a comma or a semi-colon.");
            Console.WriteLine("(You don't have to use these.  They're just examples.  You can choose any \"separators\" you want to use.)");

            //Capture user input
            string userString = Console.ReadLine();

            //Validate user response.  If valid, send response to ChangeSeparator method.  If invalid, reprompt
            while (string.IsNullOrWhiteSpace(userString))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  Please don't leave this blank.\r\nPlease enter your string (with \"separators\" now:");

                //Recapture user input
                userString = Console.ReadLine();
            }

            //Prompt user for the first separator
            Console.WriteLine("\r\nNow, please enter the \"separator\" you used in your string:");

            //Capture user's input
            string firstSeparator = Console.ReadLine();

            //Validate that the entered separator is located in the user's string
            while (!userString.Contains(firstSeparator) || string.IsNullOrWhiteSpace(firstSeparator))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That character doesn't seem to be in your string.  Please enter the \"separator\" you used in your string:");

                //Recapture user's input
                firstSeparator = Console.ReadLine();
            }

            //Promput user for the new separator
            Console.WriteLine("\r\nNow, please enter the new \"separator\" you would like to replace \"{0}\" with:", firstSeparator);

            //Capture user's input
            string secondSeparator = Console.ReadLine();

            //Validate that the user didn't leave blank
            while (string.IsNullOrWhiteSpace(secondSeparator))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  Please don't leave this blank.\r\nPlease enter the new \"separator\":");

                //Recapture user's input
                secondSeparator = Console.ReadLine();
            }

            //Declare ChangeSeparator method that will be used to change the separator to the user's input for secondSeparator

            //Call the ChangeSeparator method with user input as arguments
            ChangeSeparator(userString, firstSeparator, secondSeparator);

            //Store returned value from ChangeSeparator method
            string newString = ChangeSeparator(userString, firstSeparator, secondSeparator);

            //Display results for user
            Console.WriteLine("\r\nThe original string of {0}, with the new separator is {1}.\r\n", userString, newString);

            /*Data Sets To Test
                List = "1,2,3,4,5" original Separator = ",", newSeparator = "-"
                    Results = "The original string of 1,2,3,4,5 with the new separator is 1-2-3-
                    4-5".
                List = "red: blue: green: pink", originalSeparator = ":" newSeparator = ","
                    Results = "The original string red: blue: green: pink with the new
                    separator is red, blue, gren, pink."

                Now try one of your own.
                List = "Johnny; Ashleigh; Brelynn; Molly; Bubbles; Charlee; Snickers, originalSeparator = ";", newSeparator = "+"
                    Results = "The original string "Johnny; Ashleigh; Brelynn; Molly; Bubbles; Charlee; Snickers", with the new separator is
                    "Johnny+ Ashleigh+ Brelynn+ Molly+ Bubbles+ Charlee+ Snickers.""
            */

        }



        //Declare a new method to validate user input from Problem #1
        public static bool ValidateEmail(string userEmail)
        {

            //Create a variable to determine if input is valid
            bool valid;

            //Checks if input contains first "@"
            bool contains1At;
            if (userEmail.Contains("@"))
            {
                contains1At = true;
            }

            else
            {
                contains1At = false;
            }


            //Checks if input contains any spaces
            bool containsSpace;
            if (userEmail.Contains(" "))
            {
                containsSpace = true;
            }

            else
            {
                containsSpace = false;
            }


            //Stores location of first "@"
            int locationOfAt = userEmail.IndexOf("@");


            //Checks for a second "@" after first "@"
            bool contains2At;
            if (userEmail.IndexOf("@", locationOfAt + 1) > locationOfAt)
            {
                contains2At = true;
            }

            else
            {
                contains2At = false;
            }


            //Checks to make sure there is at least one "." after the "@"
            bool dotAfterAt;
            int locationOfDot = userEmail.IndexOf(".", locationOfAt + 1);
            if (locationOfDot > locationOfAt)
            {
                dotAfterAt = true;
            }

            else
            {
                dotAfterAt = false;
            }



            if (contains1At == true && containsSpace == false && contains2At == false && dotAfterAt == true)
            {
                valid = true;
            }

            else
            {
                valid = false;
            }

            return valid;

            }



        //This method will accept the user's input from Problem #2 and swap the first separator with the second separator
        public static string ChangeSeparator(string userString, string firstSeparator, string secondSeparator)
        {

            //Replace first separator with second separator
            userString = userString.Replace(firstSeparator, secondSeparator);

            //Return updated string value to Main Method
            return userString;

        }

        }
}
