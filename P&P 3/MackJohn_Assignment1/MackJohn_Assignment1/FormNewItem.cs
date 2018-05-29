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
        ShoppingItem newItem = new ShoppingItem();

        public FormNewItem()
        {
            InitializeComponent();

            ClearFields();
        }

        private void clearBtn_Click(object sender, EventArgs e)
        {
            ClearFields();
        }

        private void itemNameTextBox_TextChanged(object sender, EventArgs e)
        {
            ValidateInput();
        }

        private void pricePicker_ValueChanged(object sender, EventArgs e)
        {
            ValidateInput();
        }

        private void haveRdoBtn_CheckedChanged(object sender, EventArgs e)
        {
            HaveOrNeed();

            ValidateInput();
        }

        private void needRdoBtn_CheckedChanged(object sender, EventArgs e)
        {
            HaveOrNeed();

            ValidateInput();
        }

        private void priorityPicker_TextChanged(object sender, EventArgs e)
        {
            ValidateInput();
        }

        private void cancelBtn_Click(object sender, EventArgs e)
        {
            this.Close();
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

        private void ValidateInput()
        {
            if(itemNameTextBox.Text != null && pricePicker.Value != 0 && (haveRdoBtn.Checked == true || needRdoBtn.Checked == true) && priorityPicker.Text != null)
            {
                addBtn.Enabled = true;
            }

            else
            {
                addBtn.Enabled = false;
            }
        }

        private void HaveOrNeed()
        {
            if(haveRdoBtn.Checked == true)
            {
                priorityPicker.Enabled = false;
                priorityPicker.Text = null;
                newItem.Priority = 4;
            }

            else if(needRdoBtn.Checked == true)
            {
                priorityPicker.Enabled = true;
                priorityPicker.Text = null;
            }
        }
    }
}
