using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MackJohn_Assignment2
{
    public partial class FormContactDetails : Form
    {
        bool firstNameValid = false;

        public FormContactDetails()
        {
            InitializeComponent();
        }

        private void firstNameInput_TextChanged(object sender, EventArgs e)
        {
            if (firstNameInput.Text.All(Char.IsLetter))
            {
                firstNameValid = true;
            }

            else
            {
                firstNameValid = false;
            }
        }
    }
}
