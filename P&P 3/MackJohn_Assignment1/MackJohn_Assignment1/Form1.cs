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

        // Load menu strip item click (or shortcut key)
        private void loadToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Call custom method to load previously saved lists from XML file
            LoadFromFile();
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
            // Show the Save File dialog and proceed if the user clicks OK
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Clear the filename from the dialog box to prevent confusion
                saveFileDialog1.FileName = null;

                // Instantiate new XML settings item
                XmlWriterSettings settings = new XmlWriterSettings();

                // Set the save stream conformance level to Document
                settings.ConformanceLevel = ConformanceLevel.Document;

                // Set the save stream to indent child elements
                settings.Indent = true;

                // Opens save stream, saves to selected file name, and closes the stream when finished
                using (XmlWriter saveStream = XmlWriter.Create(saveFileDialog1.FileName, settings))
                {
                    // Write valid file indicator
                    saveStream.WriteStartElement("Shopping_List");

                    // Write beginning line for HAVE list
                    saveStream.WriteStartElement("Have_List");

                    // For each item in the HAVE list, this will run
                    foreach (ShoppingItem item in haveList)
                    {
                        // Start a new item
                        saveStream.WriteStartElement("Item");

                        // Write the current item's name to file
                        saveStream.WriteElementString("Name", item.Name);

                        // Write the current item's price to file
                        saveStream.WriteElementString("Price", item.Price.ToString());

                        // Write the current item's HAVE or NEED indicator to file
                        saveStream.WriteElementString("Have_Or_Need", "1");

                        // Write the current item's priority level to file
                        saveStream.WriteElementString("Priority", "4");

                        // Write end element for the current item
                        saveStream.WriteEndElement();
                    }

                    // Write end element for the HAVE list
                    saveStream.WriteEndElement();

                    // Write beginning line for NEED list
                    saveStream.WriteStartElement("Need_List");

                    // For each item in the NEED list, this will run
                    foreach (ShoppingItem item in needList)
                    {
                        // Start a new item
                        saveStream.WriteStartElement("Item");

                        // Write the current item's name to file
                        saveStream.WriteElementString("Name", item.Name);

                        // Write the current item's price to file
                        saveStream.WriteElementString("Price", item.Price.ToString());

                        // Write the current item's Have or Need indicator to file
                        saveStream.WriteElementString("Have_Or_Need", "2");

                        // Write the current item's priority level to file
                        saveStream.WriteElementString("Priority", item.Priority.ToString());

                        // Write end element for the current item
                        saveStream.WriteEndElement();
                    }

                    // Write end element for the NEED list
                    saveStream.WriteEndElement();

                    // Write end element for the XML file
                    saveStream.WriteEndElement();
                }
            }
        }

        // Custom method to load saved XML file into lists
        private void LoadFromFile()
        {
            // Declare new XML reader settings item
            XmlReaderSettings settings = new XmlReaderSettings();

            // Set the load stream's conformance level to Document
            settings.ConformanceLevel = ConformanceLevel.Document;

            // Set the load stream to ignore comments
            settings.IgnoreComments = true;

            // Set the load stream to ignore white space
            settings.IgnoreWhitespace = true;

            // Show the Open File dialog and proceed if the user selects a file and clicks OK
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Open the load stream, read the selected file, and close the stream when finished
                using (XmlReader loadStream = XmlReader.Create(openFileDialog1.FileName, settings))
                {
                    // Tell the load stream to move to relevant content
                    loadStream.MoveToContent();

                    // If the selected file does not contain the valid file indicator, alert the user
                    if (loadStream.Name != "Shopping_List")
                    {
                        string message = "Please choose a valid XML file";
                        string caption = "Invalid File";

                        MessageBox.Show(message, caption, MessageBoxButtons.OK, MessageBoxIcon.Error);

                        return;
                    }

                    // Otherwise, clear the lists of all content to prepare to take incoming data
                    else
                    {
                        haveList.Clear();
                        haveListBox.Items.Clear();

                        needList.Clear();
                        needListBox.Items.Clear();
                    }

                    // This will continue until the load stream reaches the end of the XML file
                    while (loadStream.Read())
                    {
                        // If the current line is a new item indicator, set the current item to a new instance of ShoppingItem
                        if (loadStream.Name == "Item" && loadStream.IsStartElement())
                        {
                            // Instantiate new ShoppingItem object
                            currentItem = new ShoppingItem();
                        }

                        // If the current line is an item's name, set the current item's name according to the item being read
                        if (loadStream.Name == "Name" && loadStream.IsStartElement())
                        {
                            // Set the current item's name from the current line in the XML file
                            currentItem.Name = loadStream.ReadString();
                        }

                        // If the current line is an item's price, set the current item's price according to the item being read
                        if (loadStream.Name == "Price" && loadStream.IsStartElement())
                        {
                            // Declare variable to store string value of current line
                            string priceString = null;

                            // Declare variable to store decimal value of current line
                            decimal price = 0;

                            // Read string value of current line
                            priceString = loadStream.ReadString();

                            // Parse read string value into decimal
                            decimal.TryParse(priceString, out price);

                            // Set current item's price according to parsed data from current line
                            currentItem.Price = price;
                        }

                        // If the current line is an item's Have or Need indicator, set the current item's Have or Need indicator according to the item being read
                        if (loadStream.Name == "Have_Or_Need" && loadStream.IsStartElement())
                        {
                            // Declare variable to store string value of current line
                            string haveOrNeedString = null;

                            // Declare variable to store int value of current line
                            int haveOrNeed = 0;

                            // Read string value of current line
                            haveOrNeedString = loadStream.ReadString();

                            // Parse string value of current line into int
                            int.TryParse(haveOrNeedString, out haveOrNeed);

                            // Set current item's Have or Need indicator according to parsed data from the current line
                            currentItem.HaveOrNeed = haveOrNeed;
                        }

                        // If the current line is an item's priority level, set the current item's priority level according to the item being read
                        if (loadStream.Name == "Priority" && loadStream.IsStartElement())
                        {
                            // Declare variable to store string value of current line
                            string priorityString = null;

                            // Declare variable to store int value of current line
                            int priority = 0;

                            // Read string value of current line
                            priorityString = loadStream.ReadString();

                            // Parse string value of current line into int
                            int.TryParse(priorityString, out priority);

                            // Set current item's priority level according to the parsed data from the current line
                            currentItem.Priority = priority;

                            // If the item belongs in the HAVE list, put it there
                            if (currentItem.HaveOrNeed == 1)
                            {
                                // Add item to the internal HAVE list
                                haveList.Add(currentItem);

                                // Add item to the HAVE list that the user sees
                                haveListBox.Items.Add(currentItem);
                            }

                            // If the item belongs in the NEED list, put it there
                            else if (currentItem.HaveOrNeed == 2)
                            {
                                // Add the item to the internal NEED list
                                needList.Add(currentItem);

                                // Add the item to the NEED list that the user sees
                                needListBox.Items.Add(currentItem);
                            }
                        }
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
