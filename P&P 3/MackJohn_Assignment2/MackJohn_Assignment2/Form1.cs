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
    }
}
