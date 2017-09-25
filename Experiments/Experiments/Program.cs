// DVP1
// Example Project using External Classes

// This simple program demonstrates how to 
// work with "Static" and "Instance" classes/methods.
// Take a moment to review each approach
// Review the Campus video Lectures in Project > Research: Development 
// to understand fully Classes, Constructors, and Objects. 

// Experiment: 
// Use this project to understand how to create an external class and...
// 1. Call a class method or property directly (if static)
// 2. Instantiate an object (if not static), to work with instance properties or methods.


﻿using System;
using System.Text;

namespace Experiments
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.Clear();
            // Calling a static class and method
            // Note: We're able to refer to the class directly...
            StaticMenu.MyMenu();

            // Instantiating an object (created from a class)
            // and then calling its method.
            // Note: You need to first create an object based on the class
            // That object now has all of the methods and properties of the class
            // available to you to call and work with in your program...
            InstanceMenu MyInstanceMenu = new InstanceMenu();

            // How would you call the AgeConvert class in Main?
            // Would you need to create an instance/object?
            // If not, why not?
            // 
            // Challenge: Try writing code below to call the AgeConversion method of the AgeConvert class
            // Challenge: Try writing code below to call the ReverseThis method of the Backwards class

        }
    }
}
