using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_FinalProject
{
    class Program
    {
        static void Main(string[] args)
        {

            /*
             John Mack
             8/19/2017
             Scalable Data Infrastructures
             Section 01
             Final Project
             */



            /*
             Item List Check OUt

             INSTRUCTIONS:
             1.  You will be asking the user for a comma-separated list of events that they wish to buy
                 tickets for.
                    a.  Validate this to make sure it is not left blank.
                    b.  Send this text string into the custom function TextToArray.

             2.  Create a custon function named TextToArray that will split apart the user's list text
                 string into an array of individual events.
                    a.  Make sure to remove any spaces before or after the event itself.
                    b.  Return this array of events to be purchased to the Main method.

             3.  Once you have the string array of events back in your Main Method create a 2nd custom
                 function called PromptForCosts.
                    a.  This function should accept the string array of events.
                    b.  Inside of this funtion create an array to hold the costs of these events.
                        i.  Note the length of this Array or ArrayList must depend on how
                            many events the user typed in.  AKA do NOT hard-code this.
                    c.  Loop through the events array and prompt the user for the cost of each event in
                        the array.
                            i.  Validate the user is typing in a valid response.
                            ii.  Once it is validated, store this cost inside of the cost array.
                    d.  After you get each cost, Return this array to the Main.

             4.  After catching the returned cost Array, create one last custom function called
                 SumOfCosts.
                    a.  This array should accept the cost array as a parameter.
                    b.  Inside of this function create a variable to hold the total sum of the events costs.
                    c.  Loop through each item in the cost Array and add their total to the sum.
                    d.  Return this sum to the Main method.

             5.  Use the total sum variable that is returned in a final output to the user that is in this format:
                a.  "The total cost of all of your events will be $X."
                    i.  Where X is a formatted text string that contains the cost rounded to 2 digits.

             6.  Test, test, test
                a.  Test your code using the data below and at the very least 2 more times and put
                    your Test Results in a multi-lined comment at the end of your Main Method.

             7.  When you are finised zup your whole folder and submit it on FSO.
             */

            //Welcome the user and tell them what we're going to do
            Console.WriteLine("/r/nHello!  You've got a busy weekend coming up!\r\nIt's up to you to figure out how to fit this all into your schedule, but let's see how much everything is going to cost.");

            //Prompt user for list of comma-separated events
            Console.WriteLine("\r\nFirst, please type in a list of all of the events you're going to attend, separated by commas.");

            //Capture user's input
            string eventListInput = Console.ReadLine();

            //Validate user input.  Check to see if left blank.  If so, reprompt.  Also check to make sure list is separated by commas.  If not, reprompt
            while (string.IsNullOrWhiteSpace(eventListInput) || !eventListInput.Contains(","))
            {
                //If the input is blank, tell the user and reprompt
                if (string.IsNullOrWhiteSpace(eventListInput))
                {
                    Console.WriteLine("\r\nOops!  Please don't leve this blank.\r\nPlese type in a list of all of the events you're going to attend, separated by commas.");
                    eventListInput = Console.ReadLine();
                }

                //If the input does not contain at least one comma, tell the user and reprompt
                else if (!eventListInput.Contains(","))
                {
                    Console.WriteLine("\r\nOops!  I don't see any commas in your list.\r\nPlease type in a list of all of the events you're going to attend, separated by commas.");
                    eventListInput = Console.ReadLine();
                }
            }



            //Create custom function, TextToArray

            //Send user input to TextToArray function and capture the results
            string[] eventListArray = TextToArray(eventListInput);



            //Create custom function, PromptForCosts

            //Send eventListArray to PromptForCosts and capture results
            ArrayList eventCostArray = PromptForCosts(eventListArray);



            //Create custom function, SumOfCosts

            //Send eventCostArray to SumOfCosts method and capture result
            decimal totalofEvents = SumOfCosts(eventCostArray);

            //Display results for user
            Console.WriteLine("\r\nThe cost for all of your events will be {0}.", totalofEvents.ToString("C"));

            /*
             Test Values:
                Event List - "Fiddler On The Roof, Guardians Of The Galaxy 2, Wonder Woman"
                    Fiddler On The Roof Cost - 50.00
                    Guardians Of The Galaxy 2 Cost - Twelve Dollars
                        Re-Prompted New Cost - 12.00
                    Wonder Woman Cost - 15.00
                Final Results - "The total cost of all of your events will be $77.00."

                Event List - "Jeff Dunham, Terry Crews, Ron White, Jeff Foxworthy
                    Jeff Dunham Cost - 100.50
                    Terry Crews Cost - 220.72
                    Ron White Cost - 103.65
                    Jeff Foxworthy Cost - 72.43
                Final Results - "The total cost of all of your events will be $497.30."

                Event List - "Flight to Hawaii, Hotel for 3 nights, Flight home"
                    Flight to Hawaii - 397.23
                    Hotel for 3 nights - 2043.89
                    Flight Home 507.43
                Final Results - "The total cost of all of your events will be $2,948.55."
             */

        }



        //Create a custom function called TextToArray that will accept the user's input and convert it to an array
        public static string[] TextToArray(string userList)
        {

            //Declare a new array that will hold the values of userList, once they have been split
            string[] listArray = new string[] { };

            //Split userList and store in listArray, without spaces
            listArray = userList.Split(',');

            //Remove all spaces from userList
            int currentCount = 0;
            foreach (string element in listArray)
            {
                listArray[currentCount] = element.Trim();
                currentCount++;
            }

            //Return listArray to Main Method
            return listArray;

        }



        //Create a custom function called PromptForCosts that will loop through the events array and prompt the user for the cost of each item
        public static ArrayList PromptForCosts (string[] eventList)
        {

            //Declare a new array that will store the cost of each event item
            ArrayList eventCost = new ArrayList();

            //Loop through the event list and prompt user for each item cost
            string eventCostInput;
            decimal itemEventCost;
            foreach (string element in eventList)
            {
                //Prompt user for event cost
                Console.WriteLine("\r\nHow much does the event, {0} cost?", element);
                eventCostInput = Console.ReadLine();

                //Validate user input.  If invalid, reprompt.  If valid, store in eventCost array
                while (!decimal.TryParse(eventCostInput, out itemEventCost))
                {
                    //Tell the user what's wrong and reprompt
                    Console.WriteLine("\r\nOops!  That's not a valid entry.  Please enter as a number.\r\nHow much does the event, {0} cost?", element);

                    //Recapture user inpt
                    eventCostInput = Console.ReadLine();
                }

                eventCost.Add(itemEventCost);
            }

            return eventCost;

        }



        //Create a custom function called SumOfCosts that will loop through the cost array and add up the total of costs
        public static decimal SumOfCosts(ArrayList eventCost)
        {

            //Declare variable to store total of events
            decimal eventTotal = 0.00m;

            //Loop through eventCost ArrayList and add up sum of event costs
            foreach (decimal element in eventCost)
            {

                eventTotal = eventTotal + element;

            }

            //Return eventTotal to Main Method
            return eventTotal;

        }

    }
}
