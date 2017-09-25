using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_Methods
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/13/2017
             Scalable Data Infrastructures
             Section o1
             Methods
             */



            /*
             Problem #1: Painting a Wall
             Calculate how many gallons it would take to paint a wall.  This will
                User Input (Ask & Validate In Main Method, Use As Arguments):
                    Width of wall (in feet)
                    Height of wall (in feet)
                    Number of coats of paint
                    Surface Area one gallon of paint will cover (In square feet)
                Return Value From Custom Function
                    Number of gallons of paint needed
                Results To Print To The Console In The Main Method:
                    "For X coats on that wall, you will need X gallons of paint."
             */

            //Welcome the user and tell them what we'll be doing.  Prompt user for width.
            Console.WriteLine("\r\nHello!  We're going to paint an accent wall in our bedroom, but first we need to know how much paint we'll need for that wall.  Ready?\r\nHow wide is our accent wall in feet?");

            //Capture user input and store in a string variable
            string wallWidthInput = Console.ReadLine();

            //Declare a new variable to convert user input to a number
            double wallWidth;

            //Validate user input.  If invalid, reprompt.  If valid, convert to double
            while (!double.TryParse(wallWidthInput, out wallWidth))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That's not a valid entry.  Please enter as a number.\r\nHow wide is our accent wall in feet?");

                //Recapture user input
                wallWidthInput = Console.ReadLine();
            }

            //Promput user for height
            Console.WriteLine("\r\nGreat!  Your wall is {0} feed wide.  Now, what is the height of the wall?", wallWidth);

            //Capture user input and store in a string variable
            string wallHeightInput = Console.ReadLine();

            //Declare a new variable to convert user input to a number
            double wallHeight;

            //Validate user input.  If invalid, reprompt.  If valid, convert to double
            while (!double.TryParse(wallHeightInput, out wallHeight))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That's not a valid entry.  Please enter as a number.\r\nWhat is the height of the wall?");

                //Recapture user input
                wallHeightInput = Console.ReadLine();
            }

            //Prompt user for number of coats
            Console.WriteLine("\r\nGreat!  So, your wall is {0} feet wide and {1} feet high.  Now, how many coats of paint are you going to use?", wallWidth, wallHeight);

            //Capture user input and store in a string variable
            string numberOfCoatsInput = Console.ReadLine();

            //Declare a new variable to convert user input to a number
            int numberOfCoats;

            //Validate user input.  If invalid, reprompt.  If valid, convert to int
            while (!int.TryParse(numberOfCoatsInput, out numberOfCoats))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That's not a valid entry.  Please enter as a whole number.\r\nHow many coats of paint are you going to use?");

                //Recapture user input
                numberOfCoatsInput = Console.ReadLine();
            }

            //Prompt user for surface area one gallong of paint will cover, in square feet
            Console.WriteLine("\r\nOk, so your wall is {0} feet wide and {1} feet high, and you're planning on painting a total of {2} coats.\r\nNow, every brand of paint is different, so how many square feet will one gallon of your paint cover?", wallWidth, wallHeight, numberOfCoats);

            //Capture user input and store in a string variable
            string surfaceAreaInput = Console.ReadLine();

            //Declare a new variable to convert user input to a number
            double surfaceArea;

            //Validate user input.  If invalid, reprompt.  If valid, convert to double
            while (!double.TryParse(surfaceAreaInput, out surfaceArea))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That's not a valid entry.  Please enter as a number.\r\nHow many square feet will one gallon of your paint cover?");

                //Recapture user input
                surfaceAreaInput = Console.ReadLine();
            }

            //Declare a new method to calculate number of gallons needed

            //Declare a new variable to store returned value from NumberOfGallons method
            double gallonsNeeded = NumberOfGallons(wallWidth, wallHeight, numberOfCoats, surfaceArea);

            //Display results for user
            Console.WriteLine("\r\nFor {0} coats on that wall, you will need {1} gallons of paint.\r\n", numberOfCoats, gallonsNeeded);

            /*
             Data Sets To Test
                Width = 8, Height = 10, Coats = 2, Surface Area = 300 square feet
                    Results = "For 2 coats on that wall, you will need 0.53 gallons of paint."
                Width = 30, Height = 12.5, Coats = 3, Surface Area = 350 square feet
                    Results = "For 3 coats on that wall, you will need 3.21 gallons of paint."

             Test one of your own:
                Width = 20, Height = 90, Coats = 3, Surface Area = 120
                    Results = "For 3 coats on that wall, you will need 45 gallons of paint."

             */



            /*
             Problem #2: Stung!
             It takes 9 bee stings per pound to kill an animal.  Calculate how many bee stings are needed to
             kill an animal in a function
                User Input (Ask & Validate In Main Method, Use As Arguments):
                    Animal's weight (in pounds)
                Return Value From Custom Function
                    Number of Bee Stings
                Result To Print To The Console In The Main Method:
                    "It takes X bee stings to kill this animal."
             */

            //Welcome the user and let them know what we'll be doing
            Console.WriteLine("\r\nHello!  Worried about bees, huh?  Let's try to make you feel a bit better...\r\nIt takes 9 bee stings per pound to kill an animal.  Tell me how much your animal weight, and I'll tell you how many bee stings it would take to kill them.\r\nHow many pounds does your animal weight?");


            //Capture user input and store in a string variable
            string animalWeightInput = Console.ReadLine();

            //Declare a new variable to convert user input to a double
            double animalWeight;

            //Validate user input.  If invalid, reprompt.  If valid, convert to a number
            while (!double.TryParse(animalWeightInput, out animalWeight))
            {
                //Tell the user what's wrong
                Console.WriteLine("\r\nOops!  That's not a valid entry.  Please enter as a number.\r\nHow many pounds does your animal weight?");

                //Recapture user input
                animalWeightInput = Console.ReadLine();
            }

            //Declare a new method to calculate how many bee stings it will take to kill this animal

            //Declare a new variable to store returned value from NumberOfStings method
            double numberOfStings = NumberOfStings(animalWeight);

            //Report results to the user
            Console.WriteLine("\r\nIt takes {0} bee stings to kill this animal.\r\n", numberOfStings);

            /*
             Data Sets To Test
                Amnimal's Weight = 10
                    Results = "It takes 90 bee stings to kill this animal."
                Animal's Weight = 160
                    Results = "It Takes 1440 bee stings to kill this animal."
                Animal's Weight = Twenty
                    Results = Reprompt for number value.

                Test one of your own:
                Animal's Weight = 45.6
                    Results = "It takes 411 bee stings to kill this animal."
             */



            /*
             Problem #3: Reverse It
             1.  In your main method, declare and define an array of at least 5 string elements.
                For this one problem, the array should be hard-coded and not from user
                prompted values.
            2.  Create a function that receives this array of strings.
            3.  Using a new array created in the custom method, create a loop to cycle through the
                array backwards and save the items in the reverse order to the new array.
                    a.  Remember this loop must work for any array of any size!
                    b.  You must reserve the array using a loop and NOT just hard coding it!
                    c.  You may NOT use the Array.Reverse() method in this assignment.
            4.  Return this new reversed array to the main method.
            5.  Output both the original and reversed array in the main method.
                a.  You may use a loop to output the arrays or you may convert them to a string and
                output that.

            Parameter(s) for function:
                Array of string elements
            Retrun Value From Custom Function:
                Array with string elements in reverse order.
            Result to Print to the console:
                "Your original array was [X, X, X...] and now it is reversed as [X, X, X...]"
             */

            //Declare the array to be reversed
            string[] initialArray = new string[7] { "Johnny", "Ashleigh", "Brelynn", "Molly", "Bubbles", "Charlee", "Snickers" };

            //Declare a fucntion to receive the elements of the array

            //Pass the array to the ReverseArray method
            ReverseArray(initialArray);

            //Declare new array to catch returned values from ReverseArray method
            string[] reversedArray = ReverseArray(initialArray);

            //Display results
            Console.Write("\r\nYour original array was ");

            foreach (string element in initialArray)
            {
                Console.Write("\"" + element + "\",");
            }

            Console.Write("\r\nand now it is reversed as ");

            foreach (string element in reversedArray)
            {
                Console.Write("\"" + element + "\",");
            }

            Console.WriteLine();

            /*
             Data Sets To Test
                Initial Array = ["apple", "pear", "peach", "coconut", "kiwi"]
                    Results = Your original array was ["apple", 'pear, 'peach', 'coconut",
                    "kiwi"] and now it is reversed as ["kiwi", "coconut", "peach", "pear",
                    "apple"]
                Initial array = ["red", "orange", "yellow", "green", "blue", "indigo", "violet"]
                    Results = Your original array was ["red", "orange", "yellow", "green",
                    "blue", "indego", "giolet"] and now it is reversed as ["violet", "indigo",
                    "blue", "green", "yellow", "orange", "red"]

                Now that you have tested these 2 arrays, create one of your own to test and turn in.
                Initial Array = ["Johnny", "Ashleigh", "Brelynn", "Molly", "Bubbles", "Charlee", "Snickers"
                    Results = Your original array was ["Johnny, "Ashleigh", "Brelynn", "Molly", "Bubbles", "Charlee", "Snicker",
                    and now it is reversed as "Snickers", "Charlee", "Bubbles", "Molly", "Brelynn", "Ashleigh", "Johnny"
             */
        }



        //Declare method to calculate number of gallons of paint needed for Problem#1
        //Use parameters to store values for width, height, number of coats, and surface area of one gallon of paint
        public static double NumberOfGallons(double width, double height, int coats, double area)
        {
            //Declare a variable to store calculated results
            double gallonsNeeded = Math.Round((width * height * coats / area), 2);

            //Return value of gallonsNeeded to main method
            return gallonsNeeded;
        }



        //Declare method to calculate number of bee stings needed for Problem#2
        //Use a parameter to store value for animalWeight
        public static double NumberOfStings(double weight)
        {
            //Calculate how many of stings it will take to kill this animal
            double numberOfStings = Math.Ceiling(weight * 9);

            //Return calculated value to main method
            return numberOfStings;
        }



        //Declare a method to reverse the array in Problem#3
        public static string[] ReverseArray(string[] initial)
        {

            //Declare a new variable to store the index number to use for reversing the array
            int newIndex = 0;

            //Declare a new array to store reversed array and return to main method
            string[] reversedArray = new string[7];

            //This loop calculates the index of the last item in the initial array
            //It will then assign the last item to the first index in the new array and work backwards until the entire array has been reversed
            for (int initialIndex = initial.Length - 1; initialIndex >= 0; initialIndex--, newIndex++)
            {
                reversedArray[newIndex] = initial[initialIndex];
            }

            //Returns the new array to the main method
            return reversedArray;

        }

    }
}
