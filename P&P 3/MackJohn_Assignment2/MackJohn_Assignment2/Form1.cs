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

/*
 
    icons8.com (n.d). icons8-user-account-64.png [Vector Icon Image]. Retrieved from https://icons8.com/icon/set/business%20contact/all

    icons8.com (n.d). icons8-company-96.png [Vector Icon Image. Retrieved from https://icons8.com/icon/set/personal%20contact/all

*/

namespace MackJohn_Assignment2
{
    partial class FormContactList : Form
    {
        // Custom event for when a user chooses to edit a selected contact
        private EventHandler EditContactRequest;

        // Tracks the currently selected contact
        Contact currentContact = new Contact();

        // Public property to access currently selected contact in other forms
        public Contact CurrentContact
        {
            get
            {
                return currentContact;
            }
        }

        // Initialize this form
        public FormContactList()
        {
            InitializeComponent();
        }

        // Add contact menu strip item click (or shortcut keu)
        private void addToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Instantiate new Contact Details form
            FormContactDetails formContactDetails = new FormContactDetails();

            // Show the newly instantiated Contact Details form
            formContactDetails.ShowDialog();
        }

        // Large view menu strip item click (or shortcut keu)
        private void largeToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // If the Large option is not already selected, this will run
            if (largeToolStripMenuItem.Checked == false)
            {
                // Switch listview to large icon mode
                contactsListView.View = View.LargeIcon;

                // Check the Large view menu item
                largeToolStripMenuItem.Checked = true;

                // Uncheck the Small view menu item
                smallToolStripMenuItem.Checked = false;
            }
        }

        // Small view menu strip item click (or shortcut key)
        private void smallToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // If the Small option is not already selected, this will run
            if (smallToolStripMenuItem.Checked == false)
            {
                // Switch the listview to list mode
                contactsListView.View = View.List;
                
                // Check the Small view menu item
                smallToolStripMenuItem.Checked = true;

                // Uncheck the Large view menu item
                largeToolStripMenuItem.Checked = false;
            }
        }

        // Contact double clicked in listview
        private void contactsListView_DoubleClick(object sender, EventArgs e)
        {
            // Call custom method to edit the selected contact
            Edit();
        }

        // Edit menu strip item click (or shortcut key)
        private void editToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Call custom method to edit selected contact
            Edit();
        }

        // Delete menu strip item click (or shortcut key)
        private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Call custom method to delete selected contact
            DeleteContact();
        }

        // Save menu strip item click (or shortcut key)
        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Clear file name in save file dialog to prevent confusion
            saveFileDialog1.FileName = null;

            // Show the save file dialog and continue of the user clicks OK
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Call custom method to save current contact list to file
                SaveFile();
            }
        }

        // Load menu strip item click (or shortcut key)
        private void loadToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Display the open file dialog and continue if the user clicks OK
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Call custom method to load contact list from file
                LoadFile();
            }
        }

        // Exit menu strip item (or shortcut key)
        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Exit the application
            Application.Exit();
        }

        // Custom method to edit currently selected contact
        private void Edit()
        {
            // Continue only if a contact is selected
            if (contactsListView.SelectedItems.Count != 0)
            {
                // Set the current contact from the Contact item stored in the currently selected listview item
                currentContact = contactsListView.SelectedItems[0].Tag as Contact;

                // Instantiate a new Contact Details form
                FormContactDetails formEditContacts = new FormContactDetails();

                // Subscribe to event handler for when a user chooses to edit a contact
                EditContactRequest += formEditContacts.HandleEditContactRequest;

                // Call event handler for when a user chooses to edit a contact into action
                EditContactRequest(this, new EventArgs());

                // Show the newly instantiated Contact Details form
                formEditContacts.ShowDialog();
            }
        }

        // Custom method to delete the currently selected contact
        private void DeleteContact()
        {
            // Continue only if a contact is selected
            if (contactsListView.SelectedItems.Count != 0)
            {
                // Remove the selected contact from the listview
                contactsListView.SelectedItems[0].Remove();
            }
        }

        // Custom event to save current contact list to file
        public void SaveFile()
        {
            // Instantiate new XML writer settings object
            XmlWriterSettings settings = new XmlWriterSettings();

            // Set the conformance level for the save stream to Document
            settings.ConformanceLevel = ConformanceLevel.Document;

            // Set the save stream to indent child elements
            settings.Indent = true;

            // Open a new save stream and close when finished
            using (XmlWriter saveStream = XmlWriter.Create(saveFileDialog1.FileName))
            {
                // Write the first line (and valid file indicator) of the file
                saveStream.WriteStartElement("Contact_List");

                // Save each contact in the listview to file
                foreach (ListViewItem contact in contactsListView.Items)
                {
                    // Set the current contact object to the contact object stored in the current listview item
                    currentContact = contact.Tag as Contact;

                    // Write start element for the current contact
                    saveStream.WriteStartElement("Contact");

                    // Write the current contact's first name
                    saveStream.WriteElementString("First_Name", currentContact.FirstName);

                    // Write the current contact's last name
                    saveStream.WriteElementString("Last_Name", currentContact.LastName);

                    // Write the current contact's phone number
                    saveStream.WriteElementString("Phone", currentContact.Phone);

                    // Write the current contact's email address
                    saveStream.WriteElementString("E_Mail", currentContact.EMail);

                    // Write the current contact's image index
                    saveStream.WriteElementString("Image_Index", currentContact.ImageIndex.ToString());

                    // Write end element for the current contact
                    saveStream.WriteEndElement();
                }

                // Write end element for the file
                saveStream.WriteEndElement();
            }
        }

        // Custom method to load contact list from file
        public void LoadFile()
        {
            // Instantiate new XML reader settings object
            XmlReaderSettings settings = new XmlReaderSettings();

            // Set conformance level for the load stream to Document
            settings.ConformanceLevel = ConformanceLevel.Document;

            // Set the load stream to ignore comments
            settings.IgnoreComments = true;

            // Set the load stream to ignore whitespace
            settings.IgnoreWhitespace = true;

            // Open a new load stream and closed when finished
            using (XmlReader loadStream = XmlReader.Create(openFileDialog1.FileName))
            {
                // Tell the load stream to skip to relevant content
                loadStream.MoveToContent();

                // Check for valid file indicator. If it's not there, this will run
                if (loadStream.Name != "Contact_List")
                {
                    // Set error window caption
                    string caption = "Invalid File";

                    // Set error window message
                    string message = "Please load a valid XML file";

                    // Create ok button for error window
                    MessageBoxButtons okBtn = MessageBoxButtons.OK;

                    // Display the error window
                    MessageBox.Show(message, caption, okBtn);

                    // Escape from current method
                    return;
                }

                // This will continue until the read stream reaches the end of the file
                while (loadStream.Read())
                {
                    // Instantiate new listview item to store the contact currently being read
                    ListViewItem newContact = new ListViewItem();

                    // If the current line is new contact indicator, this will run
                    if (loadStream.Name == "Contact" && loadStream.IsStartElement())
                    {
                        // Reset current contact object
                        currentContact = new Contact();

                        // Reset new contact listview item
                        newContact = new ListViewItem();
                    }

                    // If current line is a contact's first name, this will run
                    if (loadStream.Name == "First_Name" && loadStream.IsStartElement())
                    {
                        // Set the current contact's name from read information
                        currentContact.FirstName = loadStream.ReadString();
                    }

                    // If the current line is a contact's last name, this will run
                    if (loadStream.Name == "Last_Name" && loadStream.IsStartElement())
                    {
                        // Set the current contact's last name from read information
                        currentContact.LastName = loadStream.ReadString();
                    }

                    // If the current line is a contact's phone number, this will run
                    if (loadStream.Name == "Phone" && loadStream.IsStartElement())
                    {
                        // Set the current contact's phone number from read data
                        currentContact.Phone = loadStream.ReadString();
                    }

                    // If the current line is a contact's email address, this will run
                    if (loadStream.Name == "E_Mail" && loadStream.IsStartElement())
                    {
                        // Set the current contact's email address from read data
                        currentContact.EMail = loadStream.ReadString();
                    }

                    // If the current line is a contact's image index, this will run
                    if (loadStream.Name == "Image_Index" && loadStream.IsStartElement())
                    {
                        // Declare variable to store string value of current image index
                        string imageIndexString = loadStream.ReadString();

                        // Declare variable to store int value of current image index
                        int imageIndex = -1;

                        // Parse read string value into int
                        int.TryParse(imageIndexString, out imageIndex);

                        // Set current contact's image index from extracted data
                        currentContact.ImageIndex = imageIndex;

                        // Set new contact listview item's display text from current contact object's ToString method
                        newContact.Text = currentContact.ToString();

                        // Set new contact listview item's image inded from current contact object's image index
                        newContact.ImageIndex = currentContact.ImageIndex;

                        // Save current contact object inside of new contact listview item
                        newContact.Tag = currentContact;

                        // Add new contact listview item to listview
                        contactsListView.Items.Add(newContact);

                        // Sort the listview in alphabetical order
                        contactsListView.Sort();

                        // Reset new contact listview item
                        newContact = new ListViewItem();

                        // Reset current contact listview item
                        currentContact = new Contact();
                    }
                }
            }
        }

        // Custom event handler for when a new contact is added
        public void HandleContactAdded(object sender, EventArgs e)
        {
            // Save the sending form in a local variable
            FormContactDetails extractForm = sender as FormContactDetails;

            // Extract the new contact from the sending form
            currentContact = extractForm.NewContact;

            // Instantiate a new listview item to be added to the listview
            ListViewItem newContact = new ListViewItem();

            // Set the display text for the new listview item from the current contact object's ToString method
            newContact.Text = currentContact.ToString();

            // Set the image index for the new listview item from the current contact object's image index
            newContact.ImageIndex = currentContact.ImageIndex;

            // Save the current contact object inside of the new listview item
            newContact.Tag = currentContact;

            // Add the new listview item to the listview
            contactsListView.Items.Add(newContact);

            // Reset the current contact object
            currentContact = new Contact();

            // Reset the listview item
            newContact = new ListViewItem();
        }

        // Custom event handler for when a selected contact is updated
        public void HandleContactUpdated(object sender, EventArgs e)
        {
            // Save the sending form in a local variable
            FormContactDetails extractForm = sender as FormContactDetails;

            // Extract the upated contact from the sending form
            currentContact = extractForm.NewContact;

            // Update the currently selected listview item's display text from the updated contact
            contactsListView.SelectedItems[0].Text = currentContact.ToString();

            // Update the currently selected listview item's image index from the updated contact
            contactsListView.SelectedItems[0].ImageIndex = currentContact.ImageIndex;

            // Store the updated contact object in the currently selected listview item
            contactsListView.SelectedItems[0].Tag = currentContact;

            // Reset the current contact object
            currentContact = new Contact();

            // Sort the listview in alphabetical order
            contactsListView.Sort();
        }
    }
}
