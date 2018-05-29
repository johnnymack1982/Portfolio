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
    public partial class FormNewItem : Form
    {
        public FormNewItem()
        {
            InitializeComponent();

            ClearFields();
        }

        private void clearBtn_Click(object sender, EventArgs e)
        {
            ClearFields();
        }

        private void ClearFields()
        {
            itemNameTextBox.Text = null;
            pricePicker.Value = 0;
            haveRdoBtn.Checked = false;
            needRdoBtn.Checked = false;
            priorityPicker.Text = null;
            priorityPicker.Enabled = false;
            addBtn.Enabled = false;
        }
    }
}
