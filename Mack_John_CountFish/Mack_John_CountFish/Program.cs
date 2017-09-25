using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_CountFish
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/10/2017
             Scalable Data Infrastructures
             Section 01
             Count the Fish
             */

            //Declare and define the array
            string[] fishTank = new string[10] { "red", "blue", "green", "yellow", "blue", "green", "blue", "blue", "red", "green" };

            //Welcome the user and tell them what we'll be doing
            Console.WriteLine("\r\nHello!  Before you is a large fish tank, filled with fish of all different colors.");
            Console.WriteLine("Let's count the fish together to see how many there are of a certain color!");

            //Present menu and prompt user to make a choice
            Console.WriteLine("Which color would you like to count?  (Please enter the NUMBER that corresponds to your color choice.)");
            Console.WriteLine("1) Red\r\n2) Blue\r\n3) Green\r\n4) Yellow");

            //Capture user's response and store in a string variable
            string colorChoiceInput = Console.ReadLine();

            //Declare a new variable that will be used to validate and convert user's response to an integer
            int colorChoice;

            //Validate user's response.  If invalid, reprompt.  If valid, convert to integer
            while (!int.TryParse(colorChoiceInput, out colorChoice) || (colorChoice < 1 || colorChoice > 4))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That doesn't seem to be a valid response.  Please enter the NUMBER that corresponds to your color choice.");
                Console.WriteLine("1) Red\r\n2) Blue\r\n3) Green\r\n4) Yellow");

                //Recapture user's input
                colorChoiceInput = Console.ReadLine();
            }

            //Declare a variable to store the total number of counted fish
            int fishCount = 0;

            //Declare a variable to display user's color choice in output
            string fishColor = "white";

            foreach (string element in fishTank)
            {
                //If user entered 1, count red fish
                if (colorChoice == 1 && element == "red")
                {
                    fishCount = fishCount + 1;
                    fishColor = "red";
                }

                //If user entered 2, count blue fish
                else if (colorChoice == 2 && element == "blue")
                {
                    fishCount = fishCount + 1;
                    fishColor = "blue";
                }

                //If user entered 3, count green fish
                else if (colorChoice == 3 && element == "green")
                {
                    fishCount = fishCount + 1;
                    fishColor = "green";
                }

                //If user entered 4, count yellow fish
                else if (colorChoice == 4 && element == "yellow")
                {
                    fishCount = fishCount + 1;
                    fishColor = "yellow";
                }
            }

            //Display output for user
            Console.WriteLine("\r\nIn the fish tank there are {0} fish of the color {1}.", fishCount, fishColor);

        }
    }
}
