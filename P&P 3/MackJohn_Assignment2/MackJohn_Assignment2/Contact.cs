using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MackJohn_Assignment2
{
    class Contact
    {
        string _firstName;
        string _lastName;
        string _phone;
        string _email;
        int _imageIndex;

        public string FirstName
        {
            get
            {
                return _firstName;
            }

            set
            {
                _firstName = value;
            }
        }

        public string LastName
        {
            get
            {
                return _lastName;
            }

            set
            {
                _lastName = value;
            }
        }

        public string Phone
        {
            get
            {
                return _phone;
            }

            set
            {
                _phone = value;
            }
        }

        public string EMail
        {
            get
            {
                return _email;
            }

            set
            {
                _email = value;
            }
        }

        public int ImageIndex
        {
            get
            {
                return _imageIndex;
            }

            set
            {
                _imageIndex = value;
            }
        }

        public override string ToString()
        {
            return LastName + ", " + FirstName + ": " + Phone + " - " + EMail;
        }
    }
}
