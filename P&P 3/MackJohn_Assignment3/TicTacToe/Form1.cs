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

namespace TicTacToe
{
    public partial class frmTicTacToe : Form
    {
        // NAME: John D. Mack
        // CLASS AND TERM: DVP3 Term C201806
        // PROJECT: Tic Tac Toe

        /* THINGS TO CONSIDER:
            - You must change the project name to conform to the required
              naming convention.
            - You must comment the code throughout.  Failure to do so could result
              in a lower grade.
            - All button names and other provided variables and controls must
              remain the same.  Changing these could result in a 0 on the project.
            - Selecting Blue or Red on the View menu should change the imageList
              attached to all buttons so that any current play will change the color
              of all button images.
            - Saved games should save to XML.  A game should load only from XML and
              should not crash the application if a user tries to load an incorrect 
              file.
        */

        int imageIndex = 0;
        int turnCount = 0;

        public frmTicTacToe()
        {
            InitializeComponent();
        }

        private void blueToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(blueToolStripMenuItem.Checked == false)
            {
                SwitchToBlue();
            }
        }

        private void redToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(redToolStripMenuItem.Checked == false)
            {
                SwitchToRed();
            }
        }

        private void xToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(xToolStripMenuItem.Checked == false)
            {
                imageIndex = 1;

                xToolStripMenuItem.Checked = true;
                oToolStripMenuItem.Checked = false;
            }
        }

        private void oToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(oToolStripMenuItem.Checked == false)
            {
                imageIndex = 0;

                oToolStripMenuItem.Checked = true;
                xToolStripMenuItem.Checked = false;
            }
        }

        private void saveGameToolStripMenuItem_Click(object sender, EventArgs e)
        {
            saveFileDialog1.FileName = null;

            if(saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                SaveToFile();
            }
        }

        private void loadGameToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if(openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                LoadFile();
            }
        }

        private void toolStripButton1_Click(object sender, EventArgs e)
        {
            ClearGrid();
        }

        private void r1c1button_Click(object sender, EventArgs e)
        {
            if (r1c1button.ImageIndex != 0 && r1c1button.ImageIndex != 1)
            {
                r1c1button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r1c2button_Click(object sender, EventArgs e)
        {
            if (r1c2button.ImageIndex != 0 && r1c2button.ImageIndex != 1)
            {
                r1c2button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r1c3button_Click(object sender, EventArgs e)
        {
            if (r1c3button.ImageIndex != 0 && r1c3button.ImageIndex != 1)
            {
                r1c3button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r2c1button_Click(object sender, EventArgs e)
        {
            if (r2c1button.ImageIndex != 0 && r2c2button.ImageIndex != 1)
            {
                r2c1button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r2c2button_Click(object sender, EventArgs e)
        {
            if (r2c2button.ImageIndex != 0 && r2c2button.ImageIndex != 1)
            {
                r2c2button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r2c3button_Click(object sender, EventArgs e)
        {
            if (r2c3button.ImageIndex != 0 && r2c3button.ImageIndex != 1)
            {
                r2c3button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r3c1button_Click(object sender, EventArgs e)
        {
            if (r3c1button.ImageIndex != 0 && r3c1button.ImageIndex != 1)
            {
                r3c1button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r3c2button_Click(object sender, EventArgs e)
        {
            if (r3c2button.ImageIndex != 0 && r3c2button.ImageIndex != 1)
            {
                r3c2button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void r3c3button_Click(object sender, EventArgs e)
        {
            if (r3c3button.ImageIndex != 0 && r3c3button.ImageIndex != 1)
            {
                r3c3button.ImageIndex = imageIndex;

                NextPlayer();
            }
        }

        private void NextPlayer()
        {
            if(imageIndex == 0)
            {
                imageIndex = 1;
            }

            else if(imageIndex == 1)
            {
                imageIndex = 0;
            }

            turnCount++;

            CheckTurn();

            CheckForWinner();
        }

        private void CheckTurn()
        {
            if(turnCount != 0)
            {
                selectToolStripMenuItem.Enabled = false;
            }

            else
            {
                selectToolStripMenuItem.Enabled = true;
            }
        }

        private void CheckForWinner()
        {
            string caption = "WINNER!";

            if((r1c1button.ImageIndex == 0 && r1c2button.ImageIndex == 0 && r1c3button.ImageIndex == 0) ||
               (r2c1button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r2c3button.ImageIndex == 0) ||
               (r3c1button.ImageIndex == 0 && r3c2button.ImageIndex == 0 && r3c3button.ImageIndex == 0) ||
               (r1c1button.ImageIndex == 0 && r2c1button.ImageIndex == 0 && r3c1button.ImageIndex == 0) ||
               (r1c2button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r3c2button.ImageIndex == 0) ||
               (r1c3button.ImageIndex == 0 && r2c3button.ImageIndex == 0 && r3c3button.ImageIndex == 0) ||
               (r1c1button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r3c3button.ImageIndex == 0) ||
               (r1c3button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r3c1button.ImageIndex == 0))
            {
                MessageBox.Show("Congratulations, 'O' player! You've won the game!", caption);

                DisableGrid();
            }

            else if ((r1c1button.ImageIndex == 1 && r1c2button.ImageIndex == 1 && r1c3button.ImageIndex == 1) ||
                     (r2c1button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r2c3button.ImageIndex == 1) ||
                     (r3c1button.ImageIndex == 1 && r3c2button.ImageIndex == 1 && r3c3button.ImageIndex == 1) ||
                     (r1c1button.ImageIndex == 1 && r2c1button.ImageIndex == 1 && r3c1button.ImageIndex == 1) ||
                     (r1c2button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r3c2button.ImageIndex == 1) ||
                     (r1c3button.ImageIndex == 1 && r2c3button.ImageIndex == 1 && r3c3button.ImageIndex == 1) ||
                     (r1c1button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r3c3button.ImageIndex == 1) ||
                     (r1c3button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r3c1button.ImageIndex == 1))
            {
                MessageBox.Show("Congratulations, 'X' player! You've won the game!", caption);

                DisableGrid();
            }

            else if((r1c1button.ImageIndex == 0 || r1c1button.ImageIndex == 1) && (r1c2button.ImageIndex == 0 || r1c2button.ImageIndex == 1) &&
                    (r1c3button.ImageIndex == 0 || r1c3button.ImageIndex == 1) && (r2c1button.ImageIndex == 0 || r2c1button.ImageIndex == 1) &&
                    (r2c2button.ImageIndex == 0 || r2c2button.ImageIndex == 1) && (r2c3button.ImageIndex == 0 || r2c3button.ImageIndex == 1) &&
                    (r3c1button.ImageIndex == 0 || r3c1button.ImageIndex == 1) && (r3c2button.ImageIndex == 0 || r3c2button.ImageIndex == 1) &&
                    (r3c3button.ImageIndex == 0 || r3c3button.ImageIndex == 1))
            {
                MessageBox.Show("Try another game!", "Draw");
            }
        }

        private void DisableGrid()
        {
            r1c1button.Enabled = false;
            r1c2button.Enabled = false;
            r1c3button.Enabled = false;

            r2c1button.Enabled = false;
            r2c2button.Enabled = false;
            r2c3button.Enabled = false;

            r3c1button.Enabled = false;
            r3c2button.Enabled = false;
            r3c3button.Enabled = false;
        }

        private void SwitchToBlue()
        {
            r1c1button.ImageList = blueImages;
            r1c2button.ImageList = blueImages;
            r1c3button.ImageList = blueImages;

            r2c1button.ImageList = blueImages;
            r2c2button.ImageList = blueImages;
            r2c3button.ImageList = blueImages;

            r3c1button.ImageList = blueImages;
            r3c2button.ImageList = blueImages;
            r3c3button.ImageList = blueImages;

            blueToolStripMenuItem.Checked = true;
            redToolStripMenuItem.Checked = false;
        }

        private void SwitchToRed()
        {
            r1c1button.ImageList = redImages;
            r1c2button.ImageList = redImages;
            r1c3button.ImageList = redImages;

            r2c1button.ImageList = redImages;
            r2c2button.ImageList = redImages;
            r2c3button.ImageList = redImages;

            r3c1button.ImageList = redImages;
            r3c2button.ImageList = redImages;
            r3c3button.ImageList = redImages;

            redToolStripMenuItem.Checked = true;
            blueToolStripMenuItem.Checked = false;
        }

        private void SaveToFile()
        {
            XmlWriterSettings settings = new XmlWriterSettings();

            settings.ConformanceLevel = ConformanceLevel.Document;

            settings.Indent = true;

            using(XmlWriter saveStream = XmlWriter.Create(saveFileDialog1.FileName))
            {
                saveStream.WriteStartElement("Tic_Tac_Toe");

                if(r1c1button.ImageIndex != 0 && r1c1button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R1C1", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R1C1", r1c1button.ImageIndex.ToString());
                }

                if(r1c2button.ImageIndex != 0 && r1c2button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R1C2", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R1C2", r1c2button.ImageIndex.ToString());
                }

                if(r1c3button.ImageIndex != 0 && r1c3button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R1C3", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R1C3", r1c3button.ImageIndex.ToString());
                }

                if(r2c1button.ImageIndex != 0 && r2c1button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R2C1", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R2C1", r2c1button.ImageIndex.ToString());
                }

                if(r2c2button.ImageIndex != 0 && r2c2button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R2C2", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R2C2", r2c2button.ImageIndex.ToString());
                }

                if(r2c3button.ImageIndex != 0 && r2c3button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R2C3", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R2C3", r2c3button.ImageIndex.ToString());
                }

                if(r3c1button.ImageIndex != 0 && r3c1button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R3C1", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R3C1", r3c1button.ImageIndex.ToString());
                }

                if(r3c2button.ImageIndex != 0 && r3c2button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R3C2", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R3C2", r3c3button.ImageIndex.ToString());
                }

                if(r3c3button.ImageIndex != 0 && r3c3button.ImageIndex != 1)
                {
                    saveStream.WriteElementString("R3C3", "-1");
                }

                else
                {
                    saveStream.WriteElementString("R3C3", r3c3button.ImageIndex.ToString());
                }

                saveStream.WriteEndElement();
            }
        }

        private void LoadFile()
        {
            ClearGrid();

            turnCount = 0;

            XmlReaderSettings settings = new XmlReaderSettings();

            settings.ConformanceLevel = ConformanceLevel.Document;

            settings.IgnoreComments = true;

            settings.IgnoreWhitespace = true;

            using(XmlReader loadStream = XmlReader.Create(openFileDialog1.FileName))
            {
                loadStream.MoveToContent();

                if(loadStream.Name != "Tic_Tac_Toe")
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

                while (loadStream.Read())
                {
                    string imageIndexString = null;

                    if(loadStream.Name == "R1C1" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if(imageIndex != -1)
                        {
                            r1c1button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R1C2" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r1c2button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R1C3" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r1c3button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R2C1" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r2c1button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R2C2" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r2c2button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R2C3" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r2c3button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R3C1" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r3c1button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R3C2" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r3c2button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }

                    if (loadStream.Name == "R3C3" && loadStream.IsStartElement())
                    {
                        imageIndexString = loadStream.ReadString();
                        imageIndex = -1;

                        int.TryParse(imageIndexString, out imageIndex);

                        if (imageIndex != -1)
                        {
                            r3c3button.ImageIndex = imageIndex;

                            turnCount++;
                        }
                    }
                }
            }

            CheckTurn();
        }

        private void ClearGrid()
        {
            turnCount = 0;

            r1c1button.ImageIndex = -1;
            r1c2button.ImageIndex = -1;
            r1c3button.ImageIndex = -1;

            r2c1button.ImageIndex = -1;
            r2c2button.ImageIndex = -1;
            r2c3button.ImageIndex = -1;

            r3c1button.ImageIndex = -1;
            r3c2button.ImageIndex = -1;
            r3c3button.ImageIndex = -1;

            CheckTurn();
        }
    }
}
