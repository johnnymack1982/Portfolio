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

/*
 
    icons8.com (n.d). icons8-user-account-64.png [Vector Icon Image]. Retrieved from https://icons8.com/icon/set/business%20contact/all

    icons8.com (n.d). icons8-company-96.png [Vector Icon Image. Retrieved from https://icons8.com/icon/set/personal%20contact/all

*/

namespace MackJohn_Assignment2
{
    partial class FormContactDetails : Form
    {
        // Custom event for when a new contact is added
        private EventHandler ContactAdded;

        // Custom event for when a contact is updated
        private EventHandler ContactUpdated;

        // Reference the currently open Contact List form for use throughout this form
        FormContactList formContactList = Application.OpenForms[0] as FormContactList;

        // Tracks if First Name input is valid
        bool firstNameValid = false;

        // Tracks if Last Name input is valid
        bool lastNameValid = false;

        // Tracks if Phone input is valid
        bool phoneValid = false;

        // Tracks if Email input is valid
        bool emailValid = false;

        // Tracks if user has selected a contact type (Personal or Business)
        bool typeSelected = false;

        // Stores current contact being worked with
        Contact newContact = new Contact();

        // Public property to make current contact accesible to other forms
        public Contact NewContact
        {
            get
            {
                return newContact;
            }
        }

        // Initialize this form
        public FormContactDetails()
        {
            InitializeComponent();

            // Make sure the Add buton is disabled by default (until valid input has been entered)
            addBtn.Enabled = false;
        }

        // Runs when text has changed in the First Name input field
        private void firstNameInput_TextChanged(object sender, EventArgs e)
        {
            // If the user enteres only alphabetical characters, this will run
            if (firstNameInput.Text.All(Char.IsLetter) && String.IsNullOrWhiteSpace(firstNameInput.Text) == false)
            {
                // Set the valid input indicator for First Name to true
                firstNameValid = true;

                // Clear the error indicator for First Name input
                firstNameValidator.Clear();

                // Turn the First Name label green to indicate valid input
                labelFirstName.ForeColor = Color.Green;
            }

            // If the user enters an invalid character, this will run
            else if (firstNameInput.Text.All(Char.IsLetter) == false)
            {
                // Set the valid input indicator for First Name to false
                firstNameValid = false;

                // Show the error indicator for invalid input on First Name input
                firstNameValidator.SetError(firstNameInput, "Only letters are allowed.");

                // Change the First Name label to red to indicate invalid input
                labelFirstName.ForeColor = Color.Red;
            }

            // If the First Name input is blank, this will run
            else
            {
                // Set the valid input indicator for First Name to false
                firstNameValid = false;

                // Change the First Name label to black
                labelFirstName.ForeColor = Color.Black;

                // Clear the error indicator for First Name
                firstNameValidator.Clear();
            }

            // Call custom method to validate user input
            ValidateInput();
        }

        // This will run when the text in the Last Name input field has changed
        private void lastNameInput_TextChanged(object sender, EventArgs e)
        {
            // If the user has not entered invalid characters, this will run
            if (lastNameInput.Text.All(Char.IsLetter) && String.IsNullOrWhiteSpace(lastNameInput.Text) == false)
            {
                // Set the valid input indicator for Last Name to true
                lastNameValid = true;

                // Clear the error indicator for Last Name
                lastNameValidator.Clear();

                // Change the label for Last Name to green to indicate valid input
                labelLastName.ForeColor = Color.Green;
            }

            // If the user has entered invalid characters, this will run
            else if (lastNameInput.Text.All(Char.IsLetter) == false)
            {
                // Set the valid input indicator for Last Name to false
                lastNameValid = false;

                // Show the error indicator for invalid input on the Last Name input field
                lastNameValidator.SetError(lastNameInput, "Only letters are allowed.");

                // Change the Last Name label red to indicate invalid input
                labelLastName.ForeColor = Color.Red;
            }

            // If the Last Name input field is blank, this will run
            else
            {
                // Set the valid input indicator for Last Name to false
                lastNameValid = false;

                // Change the Last Name label to black
                labelLastName.ForeColor = Color.Black;

                // Clear the error indicator for Last Name
                lastNameValidator.Clear();
            }

            // Call custom method to validate user input
            ValidateInput();
        }

        // This will run when the text has changed in the Phone input field
        private void phoneInput_TextChanged(object sender, EventArgs e)
        {
            // If the user has entered the required number of digits, this will run
            if (phoneInput.MaskFull == true)
            {
                // Set the valid input indicator for the Phone input field to true
                phoneValid = true;

                // Change the Phone label to green to indicate valid input
                labelPhone.ForeColor = Color.Green;
            }

            // If the user has not entered the required number of digits, this will run
            else
            {
                // Set the valid input indicator for the Phone input field to false
                phoneValid = false;

                // Change the Phone label to black
                labelPhone.ForeColor = Color.Black;
            }

            // Call custom method to validate user input
            ValidateInput();
        }

        // This will run when the text in the Email input field has changed
        private void emailInput_TextChanged(object sender, EventArgs e)
        {
            // This string will be used to validate that the user has entered a valid email pattern
            string pattern = "^([0-9a-zA-Z]([-\\.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";

            // If the user entered a valid email address, this will run
            if (Regex.IsMatch(emailInput.Text, pattern))
            {
                // Set the valid input indicator for the Email input field to true
                emailValid = true;

                // Change the Email Address label to green to indicate valid input
                labelEMail.ForeColor = Color.Green;

                // Clear the error indicator for the Email input field
                emailValidator.Clear();
            }

            // If the Email input field is blank, this will run
            else if(String.IsNullOrWhiteSpace(emailInput.Text))
            {
                // Set the valid input indicator for the Email input field to false
                emailValid = false;

                // Change the Email Address label to black
                labelEMail.ForeColor = Color.Black;

                // Clear the error indicator for the Email input field
                emailValidator.Clear();
            }

            // If the user did not enter a valid email address, this will run
            else
            {
                // Set valid input indicator for Email input field to false
                emailValid = false;

                // Change the Email Address label to red to indicate invalid input
                labelEMail.ForeColor = Color.Red;

                // Show the error indicator for indicating invalid input on the Email input field
                emailValidator.SetError(emailInput, "Please enter a valid email: name@example.com");
            }

            // Call custom method to validate user input
            ValidateInput();
        }

        // This will run if the Business radio button is selected
        private void businessRdoBtn_Click(object sender, EventArgs e)
        {
            // Set valid input indicator for contact type selection to true
            typeSelected = true;

            // Call custom method to validate user input
            ValidateInput();

            // Call custom method to determine if user selected Business or Personal
            PersonalOrBusiness();
        }

        // This will run if the Personal radio button is selected
        private void personalRdoBtn_Click(object sender, EventArgs e)
        {
            // Set the valid input indicator for contact type selection to true
            typeSelected = true;

            // Call custom method to validate user input
            ValidateInput();

            // Call custom method to determine if user selected Business or Personal
            PersonalOrBusiness();
        }

        // Cancel button click
        private void cancelBtn_Click(object sender, EventArgs e)
        {
            // Close this form
            this.Close();
        }

        // Clear button click
        private void clearBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to clear user input fields
            ClearFields();
        }

        // Add button click
        private void addBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to populate new contact object with user input
            PopulateContact();

            // Subscribe to custom event handler for when a new contact is added
            ContactAdded += formContactList.HandleContactAdded;

            // Call custom event handler for when a new contact is added into action
            ContactAdded(this, new EventArgs());

            // Close this form
            this.Close();
        }

        // Apply button click
        private void applyBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to populate contact object with information from selected item on the contact list
            PopulateContact();

            // Subscribe to custom event handler for when a selected contact is updated
            ContactUpdated += formContactList.HandleContactUpdated;

            // Call custom event handler for when a selected contact is updated into action
            ContactUpdated(this, new EventArgs());

            // Close this form
            this.Close();
        }

        // Custom method to validate user input
        private void ValidateInput()
        {
            // If user entered valid input into all fields, this will run
            if (firstNameValid && lastNameValid && phoneValid && emailValid && typeSelected)
            {
                // Enable Add button
                addBtn.Enabled = true;

                // Enable Apply button
                applyBtn.Enabled = true;
            }

            // If the user has not entered valid input into all fields, this will run
            else
            {
                // Disable Add button
                addBtn.Enabled = false;

                // Disable Apply button
                applyBtn.Enabled = false;
            }
        }

        // Custom method to determine if user selected Personal or Business
        private void PersonalOrBusiness()
        {
            // If the Personal radio button is checked, this will run
            if (personalRdoBtn.Checked)
            {
                // Set the new contact's image index to reference the Personal contact icon
                newContact.ImageIndex = 1;
            }

            // If the Business radio button is checked, this will run
            else if (businessRdoBtn.Checked)
            {
                // Set the new contact's image index to reference the Business contact icon
                newContact.ImageIndex = 0;
            }
        }

        // Custom method to clear input fields
        private void ClearFields()
        {
            // Clear First Name input field
            firstNameInput.Text = null;

            // Clear Last Name input field
            lastNameInput.Text = null;

            // Clear Phone input field
            phoneInput.Text = null;

            // Clear Email input field
            emailInput.Text = null;

            // Uncheck Personal radio button
            personalRdoBtn.Checked = false;

            // Uncheck Business radio button
            businessRdoBtn.Checked = false;

            // Change First Name label to black
            labelFirstName.ForeColor = Color.Black;

            // Change Last Name label to black
            labelLastName.ForeColor = Color.Black;

            // Change Phone label to black
            labelPhone.ForeColor = Color.Black;

            // Change Email label to black
            labelEMail.ForeColor = Color.Black;

            // Clear First Name error indicator
            firstNameValidator.Clear();

            // Clear Last Name error indicator
            lastNameValidator.Clear();

            // Clear Email error indicator
            emailValidator.Clear();
        }

        // Custom method to populate contact object from user input
        private void PopulateContact()
        {
            // Set First Name from user input
            newContact.FirstName = firstNameInput.Text;

            // Set Last Name from user input
            newContact.LastName = lastNameInput.Text;

            // Set Phone from user input
            newContact.Phone = phoneInput.Text;

            // Set Email from user input
            newContact.EMail = emailInput.Text;
        }

        // Custom event handler for when the user chooses to edit a selected contact
        public void HandleEditContactRequest(object sender, EventArgs e)
        {
            // Create new contact object to store current contact
            Contact currentContact = new Contact();

            // Extract currently selected contact from the Contact List form
            currentContact = formContactList.CurrentContact;

            // Populate First Name input field from selected contact
            firstNameInput.Text = currentContact.FirstName;

            // Populate Last Name input field from selected contact
            lastNameInput.Text = currentContact.LastName;

            // Populate Phone input field from selected contact
            phoneInput.Text = currentContact.Phone;

            // Populate Email input field from selected contact
            emailInput.Text = currentContact.EMail;

            // If the selected contact's image index is 0, this will run
            if (currentContact.ImageIndex == 0)
            {
                // Check Business radio button
                businessRdoBtn.Checked = true;

                // Uncheck Personal radio button
                personalRdoBtn.Checked = false;

                // Set contact type validation indicator to true
                typeSelected = true;
            }

            // If the selected contact's image index is 1, this will run
            else if(currentContact.ImageIndex == 1)
            {
                // Check the Personal radio button
                personalRdoBtn.Checked = true;

                // Uncheck the Business radio button
                businessRdoBtn.Checked = false;

                // Set contact type validation indicator to true
                typeSelected = true;
            }


            // Call custom method to validate extracted input
            ValidateInput();

            // Hide the Add button
            addBtn.Visible = false;

            // Make the Apply button visible
            applyBtn.Visible = true;

            // Make the Apply button innactive by default (until user changes current input values)
            applyBtn.Enabled = false;
        }
    }
}
