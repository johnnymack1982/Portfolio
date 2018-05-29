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

        private void moveToHaveBtn_Click(object sender, EventArgs e)
        {
            MoveToHave();
        }

        private void movetoNeedBtn_Click(object sender, EventArgs e)
        {
            MoveToNeed();
        }

        private void removeSelectedBtn_Click(object sender, EventArgs e)
        {
            RemoveSelected();
        }

        private void haveListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            int currentIndex = haveListBox.SelectedIndex;

            needListBox.SelectedItem = null;

            haveListBox.SelectedIndex = currentIndex;
        }

        private void needListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            int currentIndex = needListBox.SelectedIndex;

            haveListBox.SelectedItem = null;

            needListBox.SelectedIndex = currentIndex;
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SaveToFile();
        }

        private void MoveToHave()
        {
            int currentIndex = -1;

            if (needListBox.SelectedItems.Count != 0)
            {
                currentIndex = needListBox.SelectedIndex;

                needList[currentIndex].Priority = 4;

                haveList.Add(needList[currentIndex]);
                haveListBox.Items.Add(needList[currentIndex]);

                needList.RemoveAt(currentIndex);
                needListBox.Items.RemoveAt(currentIndex);
            }
        }

        private void MoveToNeed()
        {
            int currentIndex = -1;

            if(haveListBox.SelectedItems.Count != 0)
            {
                currentIndex = haveListBox.SelectedIndex;

                if(haveList[currentIndex].Priority == 4)
                {
                    haveList[currentIndex].Priority = 2;
                }

                needList.Add(haveList[currentIndex]);
                needListBox.Items.Add(haveList[currentIndex]);

                haveList.RemoveAt(currentIndex);
                haveListBox.Items.RemoveAt(currentIndex);
            }
        }

        private void RemoveSelected()
        {
            int currentIndex = -1;

            if(haveListBox.SelectedItems.Count != 0)
            {
                currentIndex = haveListBox.SelectedIndex;

                haveList.RemoveAt(currentIndex);
                haveListBox.Items.RemoveAt(currentIndex);
            }

            else if(needListBox.SelectedItems.Count != 0)
            {
                currentIndex = needListBox.SelectedIndex;

                needList.RemoveAt(currentIndex);
                needListBox.Items.RemoveAt(currentIndex);
            }
        }

        private void SaveToFile()
        {
            if(saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                saveFileDialog1.FileName = null;

                XmlWriterSettings settings = new XmlWriterSettings();

                settings.ConformanceLevel = ConformanceLevel.Document;
                settings.Indent = true;

                using (XmlWriter saveStream = XmlWriter.Create(saveFileDialog1.FileName, settings))
                {
                    saveStream.WriteStartElement("Shopping_List");
                    saveStream.WriteStartElement("Have_List");
                    
                    foreach(ShoppingItem item in haveList)
                    {
                        saveStream.WriteStartElement("Item");

                        saveStream.WriteElementString("Name", item.Name);
                        saveStream.WriteElementString("Price", item.Price.ToString());
                        saveStream.WriteElementString("Have_Or_Need", "1");
                        saveStream.WriteElementString("Priority", "4");

                        saveStream.WriteEndElement();
                    }

                    saveStream.WriteEndElement();

                    saveStream.WriteStartElement("Need_List");

                    foreach(ShoppingItem item in needList)
                    {
                        saveStream.WriteStartElement("Item");

                        saveStream.WriteElementString("Name", item.Name);
                        saveStream.WriteElementString("Price", item.Price.ToString());
                        saveStream.WriteElementString("Have_Or_Need", "2");
                        saveStream.WriteElementString("Priority", item.Priority.ToString());

                        saveStream.WriteEndElement();
                    }

                    saveStream.WriteEndElement();
                    saveStream.WriteEndElement();
                }
            }
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
