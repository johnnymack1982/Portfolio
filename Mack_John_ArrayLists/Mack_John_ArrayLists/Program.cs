using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_ArrayLists
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/15/2017
             Scalable Data Infrastructures
             Section 01
             ArrayLists
             */



            /*
             In this assignment you will have the following objectives:

                - Add "using System.Collections" to the top of your code in order to use ArrayLists.
                - Create a new custom method
                    + Everything that follows should be placed inside this method
                    + Be sure to call the custom method from the main method
                Create the 1st ArrayList of items.
                    + These could be names of family members, sports teams, states, anything you like.
                Create a 2nd ArrayList with the same number of elements.
                    + Each element in this ArrayList should describe an element in the first
                      ArrayList.
                    + Thus, if your first ArrayList contains sports teams, the second ArrayList
                      could include the state to which that team belongs.
                    + You should have one description for each element in the first ArrayList, so
                      they will be the same length.
                - Create a loop that cycles through both lists at the same time.
                - Each time the loop runs, pull an element from the 1st ArrayList and the matching
                  element from the 2nd ArrayList and combine them into one sentence.
                    + Such as "John Adams is my father" where "John Adams" comes from the
                      1st ArrayList and "father" comes from the 2nd ArrayList.
                    + Or another example would be "The Bulls are from Chicago."  Where Bulls
                      would come from the 1st ArrayList and Chicago would come from the 2nd.
                - After you have output a combined sentence for each pair of elements, then we
                  will be making a change to both lists.
                    + Remove (2) elements from each of the 2 ArrayLists
                    + Insert (1) new element at the start of each of the 2 ArrayLists
                - Create a 2nd loop and cycle through both ArrayLists like before, creating a
                  sentence using 1 element from each of the 2 ArrayLists.
                    + Check to make sure that the matching pair of elements still match.
                        - Example: Make sure the Bulls are still from Chicago.
             */

            //Declare the MyFamily method

            //Call the MyFamily method
            MyFamily();

        }



        //Declare the MyFamily Method
        public static void MyFamily()
        {

            Console.WriteLine(" ");

            //Create the first ArrayList with the names of my family
            ArrayList myFamily = new ArrayList() { "Johnny", "Ashleigh", "Brelynn", "Molly", "Bubbles", "Charlee", "Snickers"};

            //Create the second ArrayList with the roles of my family
            ArrayList familyRoles = new ArrayList() { "Brelynn's daddy", "Brelynn's mommy", "our daughter", "a dog", "a dog", "a dog", "a cat" };

            //Declare a variable to store current index number of familyRoles for loop iteration
            int familyRolesIndex = 0;

            //Create loop to cycle through both lists at the same time and combine arrays into a sentence.
            foreach (string familyMember in myFamily)
            {

                //Display array relationship for current iteration.
                Console.WriteLine("{0} is {1}.", familyMember, familyRoles[familyRolesIndex]);

                //Increase familyRolesIndex by one to keep even with current index of familyMember
                familyRolesIndex++;
                
            }

            Console.WriteLine(" ");

            //Remove last two items from first ArrayList
            myFamily.RemoveRange(5, 2);

            //Remove last two items from second ArrayList
            familyRoles.RemoveRange(5, 2);

            //Add new item to the beginning of the first ArrayList
            myFamily.Insert(0, "Myra");

            //Add new item to the beginning of the first ArrayList
            familyRoles.Insert(0, "the new baby");

            //Reset familyRolesIndex to 0
            familyRolesIndex = 0;
            
            //Create loop to cycle through both lists at the same time and combine arrays into a sentence.
            foreach (string member in myFamily)
            {

                //Display array relationship for current iteration.
                Console.WriteLine("{0} is {1}.", member, familyRoles[familyRolesIndex]);

                //Increase familyRolesIndex by one to keep even with current index of familyMember
                familyRolesIndex++;

            }

        }
    }
}
