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
    public partial class FormContactList : Form
    {
        Contact currentContact = new Contact();

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
    }
}
