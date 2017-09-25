using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_CustomClass
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
             Custom Classes
             */



            /*
             Classes and Objects - The Hero

             Create a class to track the hit points of a character in a game.  It should contain at
             least the following:

             Member Variables
                -   Highest hit points the character can have.  The character cannot have more
                    hit points than this.
                -   Low number of hit points.  You cannot have less than 0 hit points.
                -   Current hit points.  This will be the current health of the character.

             Methods
                -   At least one method that allows you to change the hit points/health of the
                    character.
                    This could be a single method to increase or decrease hit points or it could be
                    two methods, one to increase it and one to decrease it.  But, the method(s)
                    should make sure that the new hit points number is not greater than the
                    highest hit points available to the character and not lower than 0.

             Instantiation
                -   When instantiated, the new object should accept values for at least the high
                    number (total) of hit points/health as well as the character's current hit
                    points.  In other words, the new object will be created with the highest hit
                    points and the current hit points.  But, it must also include 0 as the lowest
                    number of hit points.

             The main method should instaniate the new object first with hard-coded values.
             You should then have a loop that runs at least five times.  Each time the loop runs, it
             asks the user if the hit points have moved up or down.  Then it should ask the user
             by what number the hit points should change.  Thus, you need to figure out how the
             object's current hit points would be increased or decreased while remaining within
             the high and low numbers of hit points.  But, it should not allow the hit points to
             exceed the highest number of hit points or be less than 0.
             */



            //Instantiate and object of the HitPoints class to be used throughout the Main Method
            HitPoints playerCharacter = new HitPoints(0, 0, 0, "No Name", "No Class");

            //Welcome the user and tell them what we'll be doing
            Console.WriteLine("\r\nGreetings, adventurer!  A world of excitement and adventure awaits you, but first we have to create your character.  Are you ready?\r\ntype \"Yes\" or \"No\".");

            //Capture user response and response
            string ready = Console.ReadLine();
            ready = ready.ToLower();

            //Validate user input and respond appropriately
            while (!(ready == "yes") && !(ready == "no"))
            {
                //Tell the user what's wrong and reprompt
                Console.WriteLine("\r\nOops!  Please enter either \"Yes\" or \"No\".  Let's try again.\r\nAre you ready?");

                //Recapture user input
                ready = Console.ReadLine();
                ready = ready.ToLower();
            }

            //If the user says "yes", move foward
            if(ready == "yes")
            {
                //Prompt for character name
                Console.WriteLine("\r\nExcelent!  Before we move forward, what would you like to name your character?");
            }

            //Tell the user we're going anyway
            else if(ready == "no")
            {
                //Prompt user for character name
                Console.WriteLine("\r\nWell, that's too bad.  You're stuck now!\r\nBefore we move forward, what would you like to name your character?");
            }

            //Capture user input for character name
            playerCharacter.SetCharName(Console.ReadLine());

            //Declare a double variable used to determine if user entered letters or numbers for charName
            double charNameInvalid;

            //Validate user input and reprompt if invalid
            while (double.TryParse(playerCharacter.GetCharName(), out charNameInvalid) || string.IsNullOrWhiteSpace(playerCharacter.GetCharName()))
            {
                //If user entered numbers, tell them what's wrong and reprompt
                if (double.TryParse(playerCharacter.GetCharName(), out charNameInvalid))
                {
                    Console.WriteLine("\r\nOops!  Please enter only letters for your character's name.  Let's try again.\r\nWhat would you like to name your character?");
                }

                //If user left blank, tell them what's wrong and reprompt
                else if (string.IsNullOrWhiteSpace(playerCharacter.GetCharName()))
                {
                    Console.WriteLine("\r\nOops!  Please don't leave this blank.  Let's try again.\r\nWhat would you like to name your character?");
                }

                //Recapture user input
                playerCharacter.SetCharName(Console.ReadLine());
            }

            //Report character build progess back to user
            Console.WriteLine("\r\nCharacter Name: {0}", playerCharacter.GetCharName());

            //Tell the user what's next
            Console.WriteLine("\r\nWelcome, {0}!  Now, things are going to get a bit more interesting.  We're going to choose your character class.  Your class will determine your starting and maximum HP.  Ready?", playerCharacter.GetCharName());
            Console.WriteLine("\r\nFirst, we'll list out all of your options:");
            Console.WriteLine("FIGHTER:  Maximum HP = 500     Starting HP = 250");
            Console.WriteLine("MAGE:     Maximum HP = 250     Starting HP = 125");
            Console.WriteLine("HEALER:   Maximum HP = 120     Starting HP = 60");

            //Prompt user for their choice
            Console.WriteLine("Now, please type in your choice.  Enter \"Fighter,\" \"Mage,\" or \"Healer\":");

            //Capture user response
            playerCharacter.SetCharClass(Console.ReadLine());

            //Validate user input.  If invalid, reprompt
            while(!(playerCharacter.GetCharClass() == "fighter") && !(playerCharacter.GetCharClass() == "mage") && !(playerCharacter.GetCharClass() == "healer"))
            {
                //Tell the user what's wrong and reprompt
                Console.WriteLine("\r\nOops!  That isn't a valid option.  Let's try again.\r\nEnter \"Fighter,\" \"Mage,\" or \"Healer\":");

                //Recapture user input
                playerCharacter.SetCharClass(Console.ReadLine());
            }

            //Build character based on class selection
            playerCharacter.BuildCharacter(playerCharacter.GetCharClass());

            //Report character build progress back to user
            Console.WriteLine("\r\nCharacter Name: {0}\r\nCharacter Class: {1}\r\nMaximum HP: {2}\r\nStarting HP: {3}", playerCharacter.GetCharName(), playerCharacter.GetCharClass(), playerCharacter.GetMaxHitPoints(), playerCharacter.GetCurrentHitPoints());

            //Tell the user what's next
            Console.WriteLine("\r\nNow we're ready to fight!  Each turn, you'll be allowed to choose between one of two actions: \"ATTACK\" or \"HEAL\".");
            Console.WriteLine("Each time you fight, you will loose 10 HP.\r\nEach time you heal, you will gain 20 HP.");
            Console.WriteLine("Be careful, though.  You only have 5 healing potions!");
            
            //Player is allowed 5 healing potions
            int healingPotions = 5;

            //Loop through actions that will either increase or decrease player's HP
            while((playerCharacter.GetCurrentHitPoints() > playerCharacter.GetMinHitPoints()))
            {
                //Ask the user what they would like to do next
                Console.WriteLine("\r\nNow, would you like to heal or attack?");

                string healOrAttack = Console.ReadLine();
                healOrAttack.ToLower();

                //Validate user input and reprompt if invalid
                while(!(healOrAttack == "heal") && !(healOrAttack == "attack"))
                {
                    //Tell the user what's wrong
                    Console.WriteLine("\r\nOops!  That's not a valid choice.  Please enter \"Heal\" or \"Attack\".");

                    //Recapture user input
                    healOrAttack = Console.ReadLine();
                    healOrAttack.ToLower();
                }

                //If user selected heal and user has no potions remaining, reprompt
                while(healOrAttack == "heal" && healingPotions <= 0)
                {
                    //Tell the user what's wrong and reprompt
                    Console.WriteLine("\r\nOops!  You don't have any potions left.  All you can do now is attack and hope for the best.");

                    //Recapture user input
                    healOrAttack = Console.ReadLine();
                    healOrAttack.ToLower();
                }
                
                //If user selected heal and user has potions remaining, add 20 HP and deduct one potion
                if(healOrAttack == "heal" && healingPotions > 0)
                {
                    playerCharacter.SetCurrentHitPoints(playerCharacter.GetCurrentHitPoints() + 20);
                    healingPotions--;
                }

                //If user selected attack, deduct 10 HP
                else if(healOrAttack == "attack")
                {
                    playerCharacter.SetCurrentHitPoints(playerCharacter.GetCurrentHitPoints() - 10);
                }
                
                //If HP is max, it cannot increase any further
                if(playerCharacter.GetCurrentHitPoints() >= playerCharacter.GetMaxHitPoints())
                {
                    playerCharacter.SetCurrentHitPoints(playerCharacter.GetMaxHitPoints());

                    //Report character's HP to the user
                    Console.WriteLine("\r\n{0} has {1} HP and {2} healing potions remaining", playerCharacter.GetCharName(), playerCharacter.GetCurrentHitPoints(), healingPotions);
                }

                //If player's HP reaches 0, the character has died
                else if(playerCharacter.GetCurrentHitPoints() <= playerCharacter.GetMinHitPoints())
                {
                    //Report character death to the user
                    Console.WriteLine("\r\nOh, no!  {0} has died and the game is over.", playerCharacter.GetCharName());
                    break;
                }

                //If player's HP is between Max and Min HP, continue
                else
                {
                    //Report player's HP to the user
                    Console.WriteLine("\r\n{0} has {1} HP and {2} healing potions remaining.", playerCharacter.GetCharName(), playerCharacter.GetCurrentHitPoints(), healingPotions);
                }


            }

        }
    }
}
