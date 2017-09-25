// This simple class, demonstrates how a static class can be created.
// A static class can be called directly.
// In other words, you do not need to create an object to call any of 
// static class methods... instead you can simply refer to it directly
// e.g. StaticMenu.MyMenu();


using System;
namespace Experiments
{
    static public class StaticMenu
    {
        static public void MyMenu()
        {
            Console.WriteLine("Hello World. This message was generated from the Static Menu's MyMenu method!");
            Console.WriteLine("Notice how there isn't a constructor in this class?");
            Console.WriteLine("Do you understand why?");
            Console.WriteLine("If not, watch the campus video lecture to understand the concept better.");
            Console.WriteLine();
            Console.WriteLine("Here is my menu... ");

            // more code here.
		}
    }
}