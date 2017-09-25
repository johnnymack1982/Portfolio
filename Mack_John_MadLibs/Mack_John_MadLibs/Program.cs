using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_MadLibs
{
    class Program
    {
        static void Main(string[] args)
        {
            /*
             John Mack
             Section 01
             8/3/2017
             MadLibs Style Story Creator
             */

            //Declare null string variables.  These will be filled with user input and used to fill in the story
            string jack;
            string food;
            string cow;
            string silverCoins;
            string man;
            string magicBeans;
            string supper;
            string beanStalk;
            string angryGiants;

            //Declare array of three null indices.  These will be filled with user input and used to fill in the story
            int[] numbers = new int[3];

            //Prompt user for input to fill string value 'jack'
            Console.WriteLine(" ");
            Console.WriteLine("Hello, there!  Today, we're going to have some fun with MadLibs.");
            Console.WriteLine("To begin, we'll ask you enter the FIRST thing that comes to mind when you see a few questions.");
            Console.WriteLine("Are you ready?");
            Console.WriteLine(" ");
            Console.WriteLine("Ok!  Enter a name.  DON'T THINK!  Just type in the first name that comes to mind.");
            jack = Console.ReadLine();

            //Prompt user for input to fill string value 'food'
            Console.WriteLine(" ");
            Console.WriteLine("Ok, now type in a random object and make it plural (that means more than one.)  And, GO!");
            food = Console.ReadLine();

            //Prompt user for input to fill string value 'cow'
            Console.WriteLine(" ");
            Console.WriteLine("Now, give me an animal.  Any aninmal will do, but be as creative as you can be!");
            cow = Console.ReadLine();

            //Prompt user for input to fill string value 'silverCoins'
            Console.WriteLine(" ");
            Console.WriteLine("You're doing great!  Now, just any other random item.");
            silverCoins = Console.ReadLine();

            //Prompt user for input to fill string value 'man'
            Console.WriteLine(" ");
            Console.WriteLine("And ANOTHER random item.  The more crazier the better!");
            man = Console.ReadLine();

            //Prompt user for input to fill string value 'magicBeans'
            Console.WriteLine(" ");
            Console.WriteLine("Now, this one is a little more specific, but still try to be creative.");
            Console.WriteLine("What's a random item you might find in your pocket?");
            magicBeans = Console.ReadLine();

            //Prompt user for input to fill string value 'supper'
            Console.WriteLine(" ");
            Console.WriteLine("Now, what is the one thing you couldn't live without, even for a single day?");
            supper = Console.ReadLine();

            //Prompt user for input to fill string value 'beanStalk'
            Console.WriteLine(" ");
            Console.WriteLine("You're doing great.  Almost there!  Let's get another completely random item.");
            beanStalk = Console.ReadLine();

            //Prompt user for input to fill string value 'angryGiants'
            Console.WriteLine(" ");
            Console.WriteLine("Last one!  One last random thing.  Go!");
            angryGiants = Console.ReadLine();

            //Prompt user for to fill numbers[0]
            Console.WriteLine(" ");
            Console.WriteLine("Alright, just one more thing before we're ready to go!");
            Console.WriteLine("Let's get three numbers, just off the top of your head.  What's the first one?");
            string numbers0 = Console.ReadLine();
            numbers[0] = int.Parse(numbers0);

            //Prompt user to fill numbers[1]
            Console.WriteLine(" ");
            Console.WriteLine("And another one.");
            string numbers1 = Console.ReadLine();
            numbers[1] = int.Parse(numbers1);

            //Prompt user to fill numbers[2]
            Console.WriteLine(" ");
            Console.WriteLine("Alright.  One more!");
            string numbers2 = Console.ReadLine();
            numbers[2] = int.Parse(numbers2);

            //Use collected values to fill in the story and print it to the Console
            Console.WriteLine(" ");
            Console.WriteLine("Alright!  Here we go!");
            Console.WriteLine(" ");

            Console.WriteLine("Once upon a time, there was a boy named " + jack + ".");
            Console.WriteLine("He lived on a small farm with his mother and they had recently fallen on hard times.");
            Console.WriteLine("They were nearly out of " + food + " and had no money to buy more. ");
            Console.WriteLine(jack + "'s mother made the hardest decision she had ever made in her life and decided that they would have to sell " + jack + "'s favorite " + cow + " at the market so they would have enough " + food + " to survive the winter.");

            Console.WriteLine(" ");
            Console.WriteLine(jack + " was told to take his " + cow + " to the market and sell it for no fewer than " + numbers[0] + " " + silverCoins + ".");
            Console.WriteLine("After trying all day to sell his favorite " + cow + " with no luck, " + jack + " was finally approached by a suspicous looking " + man + " who offered to take the " + cow + " off of " + jack + "'s hands in exchange for " + numbers[1] + " " + magicBeans + ".");
            Console.WriteLine("The " + man + " promised that the " + magicBeans + " would solve all of " + jack + "'s problems, so he reluctantly accepted.");

            Console.WriteLine(" ");
            Console.WriteLine("Upon returning home, " + jack + "'s mother was furious and immediately threw them out into the yard, sending " + jack + " to bed with no " + supper + ".");
            Console.WriteLine("Over night though, the impossible happned.");
            Console.WriteLine("The " + numbers[1] + " " + magicBeans + " transformed into a gigantic " + beanStalk + ", provided access to a strange new world where " + jack + " and his mother would ultimately solve all of their problems and provide them with a life of wealth and happiness.");
            Console.WriteLine("But not before facing a number of trials, incuding " + numbers[2] + " " + angryGiants + ".");

            Console.WriteLine(" ");
            Console.WriteLine("But that is a story for another time...");
            Console.WriteLine(" ");


        }
    }
}
