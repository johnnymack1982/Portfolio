using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Text.RegularExpressions;

namespace MackJohn_Assignment2
{
    public partial class FormContactDetails : Form
    {
        bool firstNameValid = false;
        bool lastNameValid = false;
        bool phoneValid = false;
        bool emailValid = false;
        bool typeSelected = false;

        Contact newContact = new Contact();

        public FormContactDetails()
        {
            InitializeComponent();

            addBtn.Enabled = false;
        }

        private void firstNameInput_TextChanged(object sender, EventArgs e)
        {
            if (firstNameInput.Text.All(Char.IsLetter) && String.IsNullOrWhiteSpace(firstNameInput.Text) == false)
            {
                firstNameValid = true;

                firstNameValidator.Clear();

                labelFirstName.ForeColor = Color.Green;
            }

            else if (firstNameInput.Text.All(Char.IsLetter) == false)
            {
                firstNameValid = false;

                firstNameValidator.SetError(firstNameInput, "Only letters are allowed.");

                labelFirstName.ForeColor = Color.Red;
            }

            else
            {
                firstNameValid = false;

                labelFirstName.ForeColor = Color.Black;

                firstNameValidator.Clear();
            }

            ValidateInput();
        }

        private void lastNameInput_TextChanged(object sender, EventArgs e)
        {
            if(lastNameInput.Text.All(Char.IsLetter) && String.IsNullOrWhiteSpace(lastNameInput.Text) == false)
            {
                lastNameValid = true;

                lastNameValidator.Clear();

                labelLastName.ForeColor = Color.Green;
            }

            else if (lastNameInput.Text.All(Char.IsLetter) == false)
            {
                lastNameValid = false;

                lastNameValidator.SetError(lastNameInput, "Only letters are allowed.");

                labelLastName.ForeColor = Color.Red;
            }

            else
            {
                firstNameValid = false;

                labelFirstName.ForeColor = Color.Black;

                lastNameValidator.Clear();
            }
           
            ValidateInput();
        }

        private void phoneInput_TextChanged(object sender, EventArgs e)
        {
            if(phoneInput.MaskFull == true)
            {
                phoneValid = true;

                labelPhone.ForeColor = Color.Green;
            }

            else
            {
                phoneValid = false;

                labelPhone.ForeColor = Color.Black;
            }

            ValidateInput();
        }

        private void emailInput_TextChanged(object sender, EventArgs e)
        {
            string pattern = "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";

            if(Regex.IsMatch(emailInput.Text, pattern))
            {
                emailValid = true;

                labelEMail.ForeColor = Color.Green;

                emailValidator.Clear();
            }

            else if(String.IsNullOrWhiteSpace(emailInput.Text))
            {
                emailValid = false;

                labelEMail.ForeColor = Color.Black;

                emailValidator.Clear();
            }

            else
            {
                emailValid = false;

                labelEMail.ForeColor = Color.Red;

                emailValidator.SetError(emailInput, "Please enter a valid email: name@example.com");
            }

            ValidateInput();
        }

        private void businessRdoBtn_Click(object sender, EventArgs e)
        {
            typeSelected = true;

            ValidateInput();

            PersonalOrBusiness();
        }

        private void personalRdoBtn_Click(object sender, EventArgs e)
        {
            typeSelected = true;

            ValidateInput();

            PersonalOrBusiness();
        }

        private void cancelBtn_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void ValidateInput()
        {
            if (firstNameValid && lastNameValid && phoneValid && emailValid && typeSelected)
            {
                addBtn.Enabled = true;
            }

            else
            {
                addBtn.Enabled = false;
            }
        }

        private void PersonalOrBusiness()
        {
            if (personalRdoBtn.Checked)
            {
                newContact.ImageIndex = 0;
            }

            else if (businessRdoBtn.Checked)
            {
                newContact.ImageIndex = 1;
            }
        }
    }
}
