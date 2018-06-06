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

namespace MackJohn_Assignment2
{
    partial class FormContactList : Form
    {
        private EventHandler EditContactRequest;

        Contact currentContact = new Contact();

        public Contact CurrentContact
        {
            get
            {
                return currentContact;
            }
        }

        public FormContactList()
        {
            InitializeComponent();
        }

        private void addToolStripMenuItem_Click(object sender, EventArgs e)
        {
            FormContactDetails formContactDetails = new FormContactDetails();
            formContactDetails.ShowDialog();
        }

        private void largeToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(largeToolStripMenuItem.Checked == false)
            {
                contactsListView.View = View.LargeIcon;
                largeToolStripMenuItem.Checked = true;
                smallToolStripMenuItem.Checked = false;
            }
        }

        private void smallToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (smallToolStripMenuItem.Checked == false)
            {
                contactsListView.View = View.List;
                smallToolStripMenuItem.Checked = true;
                largeToolStripMenuItem.Checked = false;
            }
        }

        private void contactsListView_DoubleClick(object sender, EventArgs e)
        {
            Edit();
        }

        private void editToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Edit();
        }

        private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            DeleteContact();
        }

        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            saveFileDialog1.FileName = null;

            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                SaveFile();
            }
        }

        private void loadToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                LoadFile();
            }
        }

        private void Edit()
        {
            if(contactsListView.SelectedItems.Count != 0)
            {
                currentContact = contactsListView.SelectedItems[0].Tag as Contact;

                FormContactDetails formEditContacts = new FormContactDetails();

                EditContactRequest += formEditContacts.HandleEditContactRequest;

                EditContactRequest(this, new EventArgs());

                formEditContacts.ShowDialog();
            }
        }

        private void DeleteContact()
        {
            if(contactsListView.SelectedItems.Count != 0)
            {
                contactsListView.SelectedItems[0].Remove();
            }
        }

        public void HandleContactAdded(object sender, EventArgs e)
        {
            FormContactDetails extractForm = sender as FormContactDetails;

            currentContact = extractForm.NewContact;

            ListViewItem newContact = new ListViewItem();

            newContact.Text = currentContact.ToString();
            newContact.ImageIndex = currentContact.ImageIndex;
            newContact.Tag = currentContact;

            contactsListView.Items.Add(newContact);

            currentContact = new Contact();
            newContact = new ListViewItem();
        }

        public void HandleContactUpdated(object sender, EventArgs e)
        {
            FormContactDetails extractForm = sender as FormContactDetails;

            currentContact = extractForm.NewContact;

            contactsListView.SelectedItems[0].Text = currentContact.ToString();
            contactsListView.SelectedItems[0].ImageIndex = currentContact.ImageIndex;
            contactsListView.SelectedItems[0].Tag = currentContact;

            currentContact = new Contact();

            contactsListView.Sort();
        }

        public void SaveFile()
        {
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.ConformanceLevel = ConformanceLevel.Document;
            settings.Indent = true;

            using (XmlWriter saveStream = XmlWriter.Create(saveFileDialog1.FileName))
            {
                saveStream.WriteStartElement("Contact_List");

                foreach(ListViewItem contact in contactsListView.Items)
                {
                    currentContact = contact.Tag as Contact;

                    saveStream.WriteStartElement("Contact");

                    saveStream.WriteElementString("First_Name", currentContact.FirstName);
                    saveStream.WriteElementString("Last_Name", currentContact.LastName);
                    saveStream.WriteElementString("Phone", currentContact.Phone);
                    saveStream.WriteElementString("E_Mail", currentContact.EMail);
                    saveStream.WriteElementString("Image_Index", currentContact.ImageIndex.ToString());

                    saveStream.WriteEndElement();
                }

                saveStream.WriteEndElement();
            }
        }

        public void LoadFile()
        {
            XmlReaderSettings settings = new XmlReaderSettings();
            settings.ConformanceLevel = ConformanceLevel.Document;
            settings.IgnoreComments = true;
            settings.IgnoreWhitespace = true;

            using(XmlReader loadStream = XmlReader.Create(openFileDialog1.FileName))
            {
                loadStream.MoveToContent();

                if(loadStream.Name != "Contact_List")
                {
                    string caption = "Invalid File";
                    string message = "Please load a valid XML file";
                    MessageBoxButtons okBtn = MessageBoxButtons.OK;

                    MessageBox.Show(message, caption, okBtn);

                    return;
                }

                while (loadStream.Read())
                {
                    ListViewItem newContact = new ListViewItem();

                    if(loadStream.Name == "Contact" && loadStream.IsStartElement())
                    {
                        currentContact = new Contact();
                        newContact = new ListViewItem();
                    }

                    if(loadStream.Name == "First_Name" && loadStream.IsStartElement())
                    {
                        currentContact.FirstName = loadStream.ReadString();
                    }

                    if(loadStream.Name == "Last_Name" && loadStream.IsStartElement())
                    {
                        currentContact.LastName = loadStream.ReadString();
                    }

                    if(loadStream.Name == "Phone" && loadStream.IsStartElement())
                    {
                        currentContact.Phone = loadStream.ReadString();
                    }

                    if(loadStream.Name == "E_Mail" && loadStream.IsStartElement())
                    {
                        currentContact.EMail = loadStream.ReadString();
                    }

                    if(loadStream.Name == "Image_Index" && loadStream.IsStartElement())
                    {
                        string imageIndexString = loadStream.ReadString();
                        int imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        currentContact.ImageIndex = imageIndex;

                        newContact.Text = currentContact.ToString();
                        newContact.ImageIndex = currentContact.ImageIndex;
                        newContact.Tag = currentContact;

                        contactsListView.Items.Add(newContact);
                        contactsListView.Sort();

                        newContact = new ListViewItem();
                        currentContact = new Contact();
                    }
                }
            }
        }
    }
}
