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
        public FormContactList()
        {
            InitializeComponent();
        }

        private void addToolStripMenuItem_Click(object sender, EventArgs e)
        {
            FormContactDetails formContactDetails = new FormContactDetails();
            formContactDetails.ShowDialog();
        }
    }
}
