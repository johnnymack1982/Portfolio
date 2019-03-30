using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using System.Xml;
using System.Xml.Linq;

namespace MackJohn_Assignment1
{
    public partial class FormShoppingList : Form
    {
        // Instantiate new list of items to be stored in the HAVE list
        List<ShoppingItem> haveList = new List<ShoppingItem>();

        // Instantiate new list of items to be stored in the NEED list
        List<ShoppingItem> needList = new List<ShoppingItem>();

        // Instantiate new item to track the current item to be handled
        ShoppingItem currentItem = new ShoppingItem();

        // Initialize the main form
        public FormShoppingList()
        {
            InitializeComponent();
        }

        // Add New button click
        private void addNewBtn_Click(object sender, EventArgs e)
        {
            // Instantiate new New Item form
            FormNewItem formNewItem = new FormNewItem();

            // Show the user input dialog
            formNewItem.ShowDialog();
        }

        // Move to Have button click
        private void moveToHaveBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to move currently selected item from NEED to HAVE
            MoveToHave();
        }

        // Move to Need button click
        private void movetoNeedBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to move currently selected item from HAVE to NEED
            MoveToNeed();
        }

        // Remove Selected button click
        private void removeSelectedBtn_Click(object sender, EventArgs e)
        {
            // Call custom method to remove selected item from the appropriate list
            RemoveSelected();
        }

        // Event handler for when the slected item changes in the HAVE list
        private void haveListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            // Declare variable to track the index of the currently selected item
            int currentIndex = haveListBox.SelectedIndex;

            // Make sure no items are selected in the NEED list
            needListBox.SelectedItem = null;

            // Make sure the item the user clicked on stays selected
            haveListBox.SelectedIndex = currentIndex;
        }

        // Event handler for when the selected item changes in the NEED list
        private void needListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            // Declare variable to track the index of the currently selected item
            int currentIndex = needListBox.SelectedIndex;

            // Make sure no items are selected in the HAVE list
            haveListBox.SelectedItem = null;

            // Make sure the item the user clicked on stays selected
            needListBox.SelectedIndex = currentIndex;
        }

        // Exit menu strip item click (or shortcut key)
        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Exit the application
            Application.Exit();
        }

        // Save menu strip item click (or shortcut key)
        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Call custom method to save current lists to XML file
            SaveToFile();
        }

        // Custom method to move currently selected item from NEED to HAVE
        private void MoveToHave()
        {
            // Declare varibale to track index of currently selected item
            int currentIndex = -1;

            // If an item in the NEED box is selected, this will run
            if (needListBox.SelectedItems.Count != 0)
            {
                // Set the current index to the index of the selected item
                currentIndex = needListBox.SelectedIndex;

                // Set the priority for the current item to the default for items in the HAVE list
                needList[currentIndex].Priority = 4;

                // Add the selected item to the internal HAVE list
                haveList.Add(needList[currentIndex]);

                // Add the slected item to the HAVE list that the user sees
                haveListBox.Items.Add(needList[currentIndex]);

                // Remove the selected item from the internal NEED list
                needList.RemoveAt(currentIndex);

                // Remove the selected item from the NEED list that the user sees
                needListBox.Items.RemoveAt(currentIndex);
            }
        }

        // Custom method to move an item from HAVE to NEED
        private void MoveToNeed()
        {
            // Declare variable to track the index of the selected item
            int currentIndex = -1;

            // If an item in the HAVE list is selected, this will run
            if (haveListBox.SelectedItems.Count != 0)
            {
                // Set the current index to the index of the selected item
                currentIndex = haveListBox.SelectedIndex;

                // Set the priority for the current item to the default priority for items in the NEED list
                if (haveList[currentIndex].Priority == 4)
                {
                    haveList[currentIndex].Priority = 2;
                }

                // Add the selected item to the internal NEED list
                needList.Add(haveList[currentIndex]);

                // Add the selected item to the NEED list that the user sees
                needListBox.Items.Add(haveList[currentIndex]);

                // Remove the selected item from the HAVE list
                haveList.RemoveAt(currentIndex);

                // Remove the selected item from the HAVE list that the user sees
                haveListBox.Items.RemoveAt(currentIndex);
            }
        }

        // Custom method to remove the selected item
        private void RemoveSelected()
        {
            // Declare variable to track index of selected item
            int currentIndex = -1;

            // If an item in the HAVE list is selected, this will run
            if (haveListBox.SelectedItems.Count != 0)
            {
                // Set the current index to the index of the selected item
                currentIndex = haveListBox.SelectedIndex;

                // Remove the selected item from the internal HAVE list
                haveList.RemoveAt(currentIndex);

                // Remove the selected item from the HAVE list that the user sees
                haveListBox.Items.RemoveAt(currentIndex);
            }

            // If an item in the NEED list is selected, this will run
            else if(needListBox.SelectedItems.Count != 0)
            {
                // Set the current index to the index of the selected item
                currentIndex = needListBox.SelectedIndex;

                // Remove the selected item from the internal NEED list
                needList.RemoveAt(currentIndex);

                // Remove the selected item from the NEED list that the user sees
                needListBox.Items.RemoveAt(currentIndex);
            }
        }

        // Custom method to save current lists to XML file
        private void SaveToFile()
        {
            // Make sure the file name in the Save dialog is empty to prevent confusion
            saveFileDialog1.FileName = null;

            // Show the Save File dialog and proceed if the user clicks OK
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Open save stream, write each item to file, and close the stream when finished
                using (StreamWriter saveStream = new StreamWriter(saveFileDialog1.FileName))
                {
                    // Write beginning line of new file
                    saveStream.WriteLine("Shopping List");

                    // Write a blank line
                    saveStream.WriteLine();

                    // Write beginning line for the HAVE list
                    saveStream.WriteLine("Items I HAVE:");

                    // Declare variable to track current item number in list
                    int itemNumber = 1;

                    // Write each item in the HAVE list to file
                    foreach (ShoppingItem item in haveList)
                    {
                        saveStream.WriteLine(itemNumber.ToString() + ") " + item.ToString());

                        itemNumber++;
                    }

                    // Write a blank line
                    saveStream.WriteLine();

                    // Write beginning line for NEED list
                    saveStream.WriteLine("Items I NEED:");

                    // Reset item count for next list
                    itemNumber = 1;

                    // Write each item in the NEED list to file
                    foreach (ShoppingItem item in needList)
                    {
                        saveStream.WriteLine(itemNumber.ToString() + ") " + item.ToString());

                        itemNumber++;
                    }
                }
            }
        }

        // Custom event handler for when a new item is added from user input
        public void HandleNewItemAdded(object sender, EventArgs e)
        {
            // Declare variable to store the sending form for the purpose of extracting data
            FormNewItem extractForm = sender as FormNewItem;

            // Instantiate a new ShoppingItem object
            currentItem = new ShoppingItem();

            // Set the current item from user input on the New Item form
            currentItem = extractForm.NewItem;

            // If the new item belongs in the HAVE list, put it there
            if (currentItem.HaveOrNeed == 1)
            {
                // Add the item to the internal HAVE list
                haveList.Add(currentItem);

                // Add the item to the HAVE list that the user sees
                haveListBox.Items.Add(currentItem);
            }

            // If the new item belongs in the NEED list, put it there
            else if(currentItem.HaveOrNeed == 2)
            {
                // Add the item to the internal NEED list
                needList.Add(currentItem);

                // Add the item to the NEED list that the user sees
                needListBox.Items.Add(currentItem);
            }
        }
    }
}
