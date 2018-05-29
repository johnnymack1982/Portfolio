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
            return Name + ": $" + Price.ToString();
        }
    }
}
