using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mack_John_FindErrorsClasses
{
    class Theater
    {

        //Member Variables
        string mTheaterName;
        int mNumberOfScreens;
        decimal mAverageTicketPrice;

        //Create the constructor function
        //Corrected datatype of _numberOfScreens from double to int
        public Theater(string _theaterName, int _numberOfScreens, decimal _averageTicketPrice)
        {

            //Use the incomming parameters to inialize our original member variables
            mTheaterName = _theaterName;
            mNumberOfScreens = _numberOfScreens;
            mAverageTicketPrice = _averageTicketPrice;

            //These variables are not declared or used anywhere else in the program and should be removed
            //mMovieNames = _movieNames;

        }

        //Getters
        public string GetName()
        {
            return mTheaterName;
        }

        public int GetNumScreens()
        {
            //Added missing return call at beginning of line
            return mNumberOfScreens;
        }

        //Corrected datatype from int to decimal
        public decimal GetTicketPrice()
        {
            return mAverageTicketPrice;
        }

        //Settters
        public void SetTitle(string _theaterName)
        {
            this.mTheaterName = _theaterName;
        }

        public void SetNumScreens(int _numScreens)
        {
            this.mNumberOfScreens = _numScreens;
        }

        public void SetTicketPrice(decimal _ticketPrice)
        {
            //Corrected from _ticketPrice = this.mAverageTicketPrice
            //To this.mAverageTicketPrice = _ticketPrice;
            this.mAverageTicketPrice = _ticketPrice;
        }


        //Custom function to return how much #number of tickets will be
        public decimal TotalTicketCost(int _numberOfTickets)
        {
            //Corrected _AverageTicketPrice to mAverageTicketPrice
            decimal totalCost = _numberOfTickets *  mAverageTicketPrice;

            return totalCost;

        }

    }
}
