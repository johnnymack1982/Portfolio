using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace MackJohn_Assignment1
{
    partial class FormNewItem : Form
    {
        // Event to be handled when a new item is added from user input
        private EventHandler NewItemAdded;

        // Refer to main form in a variable for use in this form
        FormShoppingList formShoppingList = Application.OpenForms[0] as FormShoppingList;

        // Declare variable to track the current item
        ShoppingItem newItem = new ShoppingItem();

        // Make the new item readable from other forms
        public ShoppingItem NewItem
        {
            get
            {
                return newItem;
            }
        }

        // Initialize thos form
        public FormNewItem()
        {
            // Initialize this form
            InitializeComponent();

            // Subscribe to custom event handler for when a new item is added from user input
            NewItemAdded += formShoppingList.HandleNewItemAdded;

            // Call custom method to clear user input fields
            ClearFields();

            // Instantiate new ShoppingItem object
            newItem = new ShoppingItem();
        }

        // Clear button click
        private void clearBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to clear user input fields
            ClearFields();
        }

        // Event handler for when the text in the Item Name text box changes
        private void itemNameTextBox_TextChanged(object sender, EventArgs e)
        {
            // Call custom method to validate user input
            ValidateInput();
        }

        // Event handler for when the value of the Price picker changes
        private void pricePicker_ValueChanged(object sender, EventArgs e)
        {
            // Call custom method to validate user input
            ValidateInput();
        }

        // Event handler for when the check state of the HAVE radio button changes
        private void haveRdoBtn_CheckedChanged(object sender, EventArgs e)
        {
            // Call custom method to check whether HAVE or NEED is selected
            HaveOrNeed();

            // Call custom method to validate user input
            ValidateInput();
        }

        // Event handler for when the check state of the NEED radio button changes
        private void needRdoBtn_CheckedChanged(object sender, EventArgs e)
        {
            // Call cuystom method to check whether HAVE or NEED is selected
            HaveOrNeed();

            // Call custom method to validate user input
            ValidateInput();
        }

        // Event handler for when the value of the Priority picker changes
        private void priorityPicker_TextChanged(object sender, EventArgs e)
        {
            // Call custom method to validate user input
            ValidateInput();
        }

        // Cancel button click
        private void cancelBtn_Click(object sender, EventArgs e)
        {
            // Close this form
            this.Close();
        }

        // Add button click
        private void addBtn_Click(object sender, EventArgs e)
        {
            // Call custom method for adding a new item from user input
            AddItem();
        }

        // Custom method to clear user input fields
        private void ClearFields()
        {
            // Clear Item Name text box
            itemNameTextBox.Text = null;

            // Set Price picker to 0
            pricePicker.Value = 0;

            // Uncheck HAVE radio button
            haveRdoBtn.Checked = false;

            // Uncheck NEED radio button
            needRdoBtn.Checked = false;

            // Clear Priority picker
            priorityPicker.Text = null;

            // Disable Priority picker
            priorityPicker.Enabled = false;

            // Disable Add button
            addBtn.Enabled = false;
        }

        // Custom method to validate user input
        private void ValidateInput()
        {
            // If all user input fields contain valid input, enable the Add button
            if (itemNameTextBox.Text != null && pricePicker.Value != 0 && (haveRdoBtn.Checked == true || needRdoBtn.Checked == true) && priorityPicker.Text != null)
            {
                // Enable Add button
                addBtn.Enabled = true;
            }

            // If one or more user input fields does not contain valid input, disable Add button
            else
            {
                // Disable Add button
                addBtn.Enabled = false;
            }
        }

        // Check whether HAVE or NEED radio button is selected
        private void HaveOrNeed()
        {
            // If the HAVE radio button is checked, this runs
            if (haveRdoBtn.Checked == true)
            {
                // Disable Priority picker
                priorityPicker.Enabled = false;

                // Clear Priority picker text
                priorityPicker.Text = null;

                // Set item's priorty level to the default for items in the HAVE list
                newItem.Priority = 4;
            }

            // If the NEED radio button is checked, this runs
            else if(needRdoBtn.Checked == true)
            {
                // Enable the Priority picker
                priorityPicker.Enabled = true;

                // Clear the Priority picker text
                priorityPicker.Text = null;
            }
        }

        // Custom method for adding new item from user input
        private void AddItem()
        {
            // Set new item's name from user input
            newItem.Name = itemNameTextBox.Text;

            // Set new item's price from user input
            newItem.Price = pricePicker.Value;

            // If the HAVE radio button is checked, this runs
            if (haveRdoBtn.Checked == true)
            {
                // Set Have or Need indicator to HAVE
                newItem.HaveOrNeed = 1;

                // Set new item's priority level to default for items in the HAVE list
                newItem.Priority = 4;
            }

            // If the NEED radio button is checked, this runs
            else if (needRdoBtn.Checked == true)
            {
                // Set Have or Need indicator to NEED
                newItem.HaveOrNeed = 2;

                // Set new item's priority from user's input
                newItem.Priority = priorityPicker.SelectedIndex;
            }

            // Call custom event handler for when a new item is added into action
            NewItemAdded(this, new EventArgs());

            // Close this form
            this.Close();
        }
    }
}
