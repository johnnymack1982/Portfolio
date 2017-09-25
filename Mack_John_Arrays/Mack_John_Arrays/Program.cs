using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_Arrays
{
    class Program
    {
        static void Main(string[] args)
        {
            /*
             John Mack
             Section 01
             8/2/2017
             Arrays Assignment
             */

            //Create your own project and call it Lastname_Firstname_Arrays
            //Copy this code inside of this Main section into your Main Section
            //Work through each of the sections

            //Declare and Define The Starting Number Arrays

            //I changed the datatype from int to double for the first array in order to get the correct value to display when averaging the array
            double[] firstArray = new double[4] { 4, 20, 60, 150 };
            double[] secondArray = new double[4] { 5, 40.5, 65.4, 145.98 };

            //Find the total of each array and store it in a variable and output to console

            //This line declares and defines the variable to store the total of the first array
            double firstArrayTotal = firstArray[0] + firstArray[1] + firstArray[2] + firstArray[3];

            //This line outputs the value of firstArrayTotal to the Console
            Console.WriteLine("The total of the first array is " + firstArrayTotal + ".");

            //This line declares and defines the variable to store the total of the second array
            double secondArrayTotal = secondArray[0] + secondArray[1] + secondArray[2] + secondArray[3];

            //This line outpouts the value of secondArrayTotal to the Console
            Console.WriteLine("The total of the second array is " + secondArrayTotal + ".");


            //Calculate the average of each array and store it in a variable and output to console
            //Just a reminder to check the averages with a calculator as well, to make sure they are correct.

            //This line declares and defines the variable to store the average of the first array
            double firstArrayAverage = (firstArray[0] + firstArray[1] + firstArray[2] + firstArray[3]) / 4;

            //This line outputs the value of firstArrayAverage to the Console
            Console.WriteLine("The average of the first array is " + firstArrayAverage + ".");

            //This line declares and defines the variable to store the average of the second array
            double secondArrayAverage = (secondArray[0] + secondArray[1] + secondArray[2] + secondArray[3]) / 4;

            //This line outputs the value of the secondArrayAverage to the Console
            Console.WriteLine("The average of the second array is " + secondArrayAverage + ".");



            /*
               Create a 3rd number array.  
               The values of this array will come from the 2 given arrays.
                -You will take the first item in each of the 2 number arrays, add them together and then store this sum inside of the new array.
                -For example Add the index#0 of array 1 to index#0 of array2 and store this inside of your new array at the index#0 spot.
                -Repeat this for each index #.
                -Do not add them by hand, the computer must add them.
                -Do not use the numbers themselves, use the array elements.
                -After you have the completed new array, output this to the Console.
             */

            //This line declares and defines the new array
            double[] thirdArray = new double[4] { firstArray[0] + secondArray[0], firstArray[1] + secondArray[1], firstArray[2] + secondArray[2], firstArray[3] + secondArray[3] };

            //This line displays the values for each index in the new array to the Console
            Console.WriteLine("The elements of the third array are " + thirdArray[0] + ", " + thirdArray[1] + ", " + thirdArray[2] + ", and " + thirdArray[3] + ".");



            /*
               Given the array of strings below named MixedUp.  
               You must create a string variable that puts the items in the correct order to make a complete sentence.
                -Use each element in the array, do not re-write the strings themselves.
                -Concatenate them in the correct order to form a sentence.
                -Store this new sentence string inside of a string variable you create.
                -Output this new string variable to the console.
             */

            //Declare and Define The String Array
            string[] MixedUp = new string[] { "but the lighting of a", "Education is not", "fire.", "the filling", "of a bucket," };

            //This line declares and defines the new string variable used to put the sentence back into order
            string dontWorryIFixedIt = MixedUp[1] + " " + MixedUp[3] + " " + MixedUp[4] + " " + MixedUp[0] + " " + MixedUp[2];

            //This line ouputs the value of dontWorryIFixedIt to the Console
            Console.WriteLine(dontWorryIFixedIt);
        }
    }
}
