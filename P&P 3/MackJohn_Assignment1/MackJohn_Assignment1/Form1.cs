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
    public partial class FormShoppingList : Form
    {
        List<ShoppingItem> haveList = new List<ShoppingItem>();
        List<ShoppingItem> needList = new List<ShoppingItem>();

        ShoppingItem currentItem = new ShoppingItem();

        public FormShoppingList()
        {
            InitializeComponent();
        }

        private void addNewBtn_Click(object sender, EventArgs e)
        {
            FormNewItem formNewItem = new FormNewItem();
            formNewItem.ShowDialog();
        }

        public void HandleNewItemAdded(object sender, EventArgs e)
        {
            FormNewItem extractForm = sender as FormNewItem;

            currentItem = new ShoppingItem();
            currentItem = extractForm.NewItem;

            if(currentItem.HaveOrNeed == 1)
            {
                haveList.Add(currentItem);
                haveListBox.Items.Add(currentItem);
            }

            else if(currentItem.HaveOrNeed == 2)
            {
                needList.Add(currentItem);
                needListBox.Items.Add(currentItem);
            }
        }
    }
}
