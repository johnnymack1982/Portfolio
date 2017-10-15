using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DVP1.CE1
{
    class Backwards
    {

        public Backwards()
        {

            /*
            Name:  John Daniel Mack
            Date:  1710
            Course:  Project & Portfolio 1
            Synposis:  This class will collect a sentence from the user and ensure that the sentence contains no fewer than six (6) words.  Once user input has been validated
            it will be passed into a method that will reverse the order of all characters in the string and report the results back to the user.
            */

            Console.Clear();
            Console.WriteLine("Coding Challenge 2:  BACKWARDS");

            Console.WriteLine("\r\nHello!  Please enter a sentence containing no less than six (6) words:");
            string userSentence = Console.ReadLine();

            //Count number of words in sentence
            char[] seperator = new char[] { ' ' };
            int numberOfWords = userSentence.Split(seperator, StringSplitOptions.RemoveEmptyEntries).Length;

            //Validate that the user entered no fewer than six (6) words
            while (numberOfWords < 6)
            {
                Console.Clear();
                Console.WriteLine("Coding Challenge 2:  BACKWARDS");
                Console.WriteLine("\r\nOops!  There were only {0} words in that sentence.", numberOfWords);
                Console.WriteLine("Please enter a sentence containing no less than six (6) words:");
                userSentence = Console.ReadLine();

                //Count number of words in sentence
                numberOfWords = userSentence.Split(seperator, StringSplitOptions.RemoveEmptyEntries).Length;

            }

            Console.Clear();
            Console.WriteLine("Coding Challenge 2:  BACKWARDS");
            Console.WriteLine("\r\nGreat!  The sentence you entered is {0} words long.  Here it is again, in case you forgot what it was:", numberOfWords);
            Console.WriteLine("\"{0}\"", userSentence);

            Console.WriteLine("\r\nNext, we'll show you what that sentence would look like if you typed it backwards.  Ready?\r\n\r\nPress any key to continue...");
            Console.ReadKey();

            string reversedSentence = ReverseSentence(userSentence);

            Console.Clear();
            Console.WriteLine("Coding Challenge 2:  BACKWARDS");
            Console.WriteLine("\r\nYour reversed sentence looks like this:");
            Console.WriteLine("\"{0}\"", reversedSentence);
            Console.WriteLine("\r\nPress any key to continue...");
            Console.ReadKey();

            new Menu();

        }



        public string ReverseSentence(string _userSentence)
        {

            char[] sentenceCharacters = _userSentence.ToCharArray();
            Array.Reverse(sentenceCharacters);
            string reversedSentence = new string(sentenceCharacters);
            return reversedSentence;

        }

    }
}
