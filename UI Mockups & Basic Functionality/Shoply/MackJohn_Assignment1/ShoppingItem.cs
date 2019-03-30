using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MackJohn_Assignment1
{
    class ShoppingItem
    {
        string _name;
        decimal _price;
        int _haveOrNeed;
        int _priority;

        public string Name
        {
            get
            {
                return _name;
            }

            set
            {
                _name = value;
            }
        }

        public decimal Price
        {
            get
            {
                return _price;
            }

            set
            {
                _price = value;
            }
        }

        public int HaveOrNeed
        {
            get
            {
                return _haveOrNeed;
            }

            set
            {
                _haveOrNeed = value;
            }
        }

        public int Priority
        {
            get
            {
                return _priority;
            }

            set
            {
                _priority = value;
            }
        }

        public override string ToString()
        {
            string priorityString = "";

            if(Priority == 0)
            {
                priorityString = " - URGENT";
            }

            else if(Priority == 1)
            {
                priorityString = " - Must Have";
            }

            else if (Priority == 2)
            {
                priorityString = " - Need Soon";
            }

            else if (Priority == 3)
            {
                priorityString = " - Nice to Have";
            }

            return Name + ": " + Price.ToString("C") + priorityString;
        }
    }
}
