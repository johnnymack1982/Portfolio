using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DVP1.CE1
{
    class SwapInfo
    {

        public SwapInfo()
        {

            /*
             Name:  John Daniel Mack
             Date:  1710
             Course:  Project & Portfolio 1
             Synposis:  This class will collect the user's first and last name and report the input back to the user.  It will then swap the values of each variable and report
                        the reversed values back to the user again.
             */

            string[] nameArray = new string[2];

            Console.WriteLine("Coding Challenge 1:  SWAP INFO");
            Console.Write("\r\nHello!  \r\nPlease enter your FIRST NAME:  ");

            string firstName = Console.ReadLine();



            while (String.IsNullOrWhiteSpace(firstName))
            {
                Console.Clear();
                Console.Write("Oops!  Please don't leave this blank.  \r\nPlease enter your FIRST NAME:  ");

                firstName = Console.ReadLine();
            }

            nameArray[0] = firstName;



            Console.Clear();
            Console.Write("Thanks, {0}!  Now, please enter your LAST NAME:  ", firstName);

            string lastName = Console.ReadLine();



            while (String.IsNullOrWhiteSpace(lastName))
            {
                Console.Clear();
                Console.Write("Opps!  Please don't leave this blank.  \r\nPlease enter your LAST NAME:  ");

                lastName = Console.ReadLine();
            }

            nameArray[1] = lastName;



            Console.Clear();
            Console.WriteLine("Great!  So, your full name is:  \r\n\r\nFIRST NAME - {0} \r\nLAST NAME - {1}", firstName, lastName);
            Console.WriteLine("Care to have a bit of fun?  Let's pretend your FIRST NAME is your LAST NAME and your LAST NAME is your FIRST NAME!  Ready?");
            Console.WriteLine("Don't worry, I'll do all the work for you.  \r\n\r\nPress any key to continue...");
            Console.ReadKey();

            Console.Clear();


            nameArray = SwapNames(nameArray);
            firstName = nameArray[0];
            lastName = nameArray[1];
            

            Console.WriteLine("POOF!  Now, your name looks like this!  \r\n\r\nFIRST NAME - {0} \r\nLAST NAME - {1}", firstName, lastName);

        }



        public string[] SwapNames(string[] nameReversalArray)
        {

            string[] reversedNameArray = new string[2];

            reversedNameArray[0] = nameReversalArray[1];
            reversedNameArray[1] = nameReversalArray[0];

            return reversedNameArray;

        }

    }
}
