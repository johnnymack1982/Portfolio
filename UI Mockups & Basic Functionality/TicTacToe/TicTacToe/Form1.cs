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

        // Will be used throughout the code to keep track of which player's turn it is
        int imageIndex = 0;

        // Will be used throughout the code to keep track of how many turns there have been
        int turnCount = 0;

        // Initialize this form
        public frmTicTacToe()
        {
            InitializeComponent();

            oToolStripMenuItem.Checked = true;
        }

        // Blue menu strip item click (or shortcut key)
        private void blueToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // If blue is not already selected, this will run
            if (blueToolStripMenuItem.Checked == false)
            {
                // Call custom method to switch all icons to blue
                SwitchToBlue();
            }
        }

        // Red menu strip item click (or shortcut key)
        private void redToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // If red is not already selected, this will run
            if (redToolStripMenuItem.Checked == false)
            {
                // Call custom method to switch all icons to red
                SwitchToRed();
            }
        }

        // X menu strip item click (or shortcut key)
        private void xToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // If X is not already selected, this will run
            if (xToolStripMenuItem.Checked == false)
            {
                // Set the current player to X
                imageIndex = 1;

                // Check the X menu strip item
                xToolStripMenuItem.Checked = true;

                // Uncheck the O menu strip item
                oToolStripMenuItem.Checked = false;
            }
        }

        // O menu strip item click (or shortcut key)
        private void oToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // If O is not already selected, this will run
            if (oToolStripMenuItem.Checked == false)
            {
                // Set the current player to O
                imageIndex = 0;

                // Check the O menu strip item
                oToolStripMenuItem.Checked = true;

                // Uncheck the X menu strip item
                xToolStripMenuItem.Checked = false;
            }
        }

        // Save menu strip item click (or shortcut key)
        private void saveGameToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Clear the file name from the Save File dialog to prevent confusion
            saveFileDialog1.FileName = null;

            // Open the Save File dialog and proceed if the user clicks OK
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Call custom method to save current game to file
                SaveToFile();
            }
        }

        // Load menu strip item click
        private void loadGameToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Open the Open File dialog and proceed if the user clicks OK
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                // Call custom method to load a saved game from file
                LoadFile();
            }
        }

        // Exit menu item click (or shortcut key)
        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            // Exit the application
            Application.Exit();
        }

        // New Game button click
        private void toolStripButton1_Click(object sender, EventArgs e)
        {
            // Call custom method to reset the game
            ClearGrid();
        }

        // Row 1 Column 1 click
        private void r1c1button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r1c1button.ImageIndex != 0 && r1c1button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r1c1button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 1 Column 2 click
        private void r1c2button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r1c2button.ImageIndex != 0 && r1c2button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r1c2button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 1 Column 3 click
        private void r1c3button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r1c3button.ImageIndex != 0 && r1c3button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r1c3button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 2 Column 1 click
        private void r2c1button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r2c1button.ImageIndex != 0 && r2c2button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r2c1button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 2 Column 2 click
        private void r2c2button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r2c2button.ImageIndex != 0 && r2c2button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r2c2button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 2 Column 3 click
        private void r2c3button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r2c3button.ImageIndex != 0 && r2c3button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r2c3button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 3 Column 1 click
        private void r3c1button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r3c1button.ImageIndex != 0 && r3c1button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r3c1button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 3 Column 2 click
        private void r3c2button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r3c2button.ImageIndex != 0 && r3c2button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r3c2button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Row 3 Column 3 click
        private void r3c3button_Click(object sender, EventArgs e)
        {
            // If the cell has not already been chosen, this will run
            if (r3c3button.ImageIndex != 0 && r3c3button.ImageIndex != 1)
            {
                // Fill in the appropriate icon for the current player
                r3c3button.ImageIndex = imageIndex;

                // Call custom method to switch to the next player's turn
                NextPlayer();
            }
        }

        // Custom method to switch to the next player's turn
        private void NextPlayer()
        {
            // If the last turn was taken by O, this will run
            if (imageIndex == 0)
            {
                // Next turn belongs to X
                imageIndex = 1;
            }

            // If the last turn was taken by X, this will run
            else if(imageIndex == 1)
            {
                // Next turn belongs to O
                imageIndex = 0;
            }

            // Increase the total turn count by one
            turnCount++;

            // Call custom method to see how many turns have been taken
            CheckTurn();

            // Call custom method to check for a winner
            CheckForWinner();
        }

        // Custom method to see how many turns have been taken
        private void CheckTurn()
        {
            // If one or more turns have been taken, this will run
            if (turnCount != 0)
            {
                // Disable Select menu strip item to prevent players from switching from X to O or vice versa during an active game
                selectToolStripMenuItem.Enabled = false;
            }

            // If no turns have been taken yet, this will run
            else
            {
                // Enable Select menu strip item
                selectToolStripMenuItem.Enabled = true;
            }
        }

        // Custom method to check for a winner
        private void CheckForWinner()
        {
            // Set default Message Window caption
            string caption = "WINNER!";

            // If O has three in a row, column, or diagonal, this will run
            if ((r1c1button.ImageIndex == 0 && r1c2button.ImageIndex == 0 && r1c3button.ImageIndex == 0) ||
               (r2c1button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r2c3button.ImageIndex == 0) ||
               (r3c1button.ImageIndex == 0 && r3c2button.ImageIndex == 0 && r3c3button.ImageIndex == 0) ||
               (r1c1button.ImageIndex == 0 && r2c1button.ImageIndex == 0 && r3c1button.ImageIndex == 0) ||
               (r1c2button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r3c2button.ImageIndex == 0) ||
               (r1c3button.ImageIndex == 0 && r2c3button.ImageIndex == 0 && r3c3button.ImageIndex == 0) ||
               (r1c1button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r3c3button.ImageIndex == 0) ||
               (r1c3button.ImageIndex == 0 && r2c2button.ImageIndex == 0 && r3c1button.ImageIndex == 0))
            {
                // Display message indicating O has won
                MessageBox.Show("Congratulations, 'O' player! You've won the game!", caption);

                // Call custom method to disable the play grid
                DisableGrid();
            }

            // If X has three in a row, column, or diagonal, this will run
            else if ((r1c1button.ImageIndex == 1 && r1c2button.ImageIndex == 1 && r1c3button.ImageIndex == 1) ||
                     (r2c1button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r2c3button.ImageIndex == 1) ||
                     (r3c1button.ImageIndex == 1 && r3c2button.ImageIndex == 1 && r3c3button.ImageIndex == 1) ||
                     (r1c1button.ImageIndex == 1 && r2c1button.ImageIndex == 1 && r3c1button.ImageIndex == 1) ||
                     (r1c2button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r3c2button.ImageIndex == 1) ||
                     (r1c3button.ImageIndex == 1 && r2c3button.ImageIndex == 1 && r3c3button.ImageIndex == 1) ||
                     (r1c1button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r3c3button.ImageIndex == 1) ||
                     (r1c3button.ImageIndex == 1 && r2c2button.ImageIndex == 1 && r3c1button.ImageIndex == 1))
            {
                // Display message indicating X has won
                MessageBox.Show("Congratulations, 'X' player! You've won the game!", caption);

                // Call custom method to disable the play grid
                DisableGrid();
            }

            // If all cells have been selected and there is no winner, this will run
            else if((r1c1button.ImageIndex == 0 || r1c1button.ImageIndex == 1) && (r1c2button.ImageIndex == 0 || r1c2button.ImageIndex == 1) &&
                    (r1c3button.ImageIndex == 0 || r1c3button.ImageIndex == 1) && (r2c1button.ImageIndex == 0 || r2c1button.ImageIndex == 1) &&
                    (r2c2button.ImageIndex == 0 || r2c2button.ImageIndex == 1) && (r2c3button.ImageIndex == 0 || r2c3button.ImageIndex == 1) &&
                    (r3c1button.ImageIndex == 0 || r3c1button.ImageIndex == 1) && (r3c2button.ImageIndex == 0 || r3c2button.ImageIndex == 1) &&
                    (r3c3button.ImageIndex == 0 || r3c3button.ImageIndex == 1))
            {
                // Display message indicated a draw
                MessageBox.Show("Try another game!", "Draw");

                // Call custom method to disable the play grid
                DisableGrid();
            }
        }

        // Custom method to disable the play grid
        private void DisableGrid()
        {
            // Disable Row 1 Column 1
            r1c1button.Enabled = false;

            // Disable Row 1 Column 2
            r1c2button.Enabled = false;

            // Disable Row 1 Column 3
            r1c3button.Enabled = false;

            // Disable Row 2 Column 1
            r2c1button.Enabled = false;

            // Disable Row 2 Column 2
            r2c2button.Enabled = false;

            // Disable Row 2 Column 3
            r2c3button.Enabled = false;

            // Disable Row 3 Column 1
            r3c1button.Enabled = false;

            // Disable Row 3 Column 2
            r3c2button.Enabled = false;

            // Disable Row 3 Column 3
            r3c3button.Enabled = false;
        }

        // Custom method to enable the play grid
        private void EnableGrid()
        {
            // Enable Row 1 Column 1
            r1c1button.Enabled = true;

            // Enable Row 1 Column 2
            r1c2button.Enabled = true;

            // Enable Row 1 Column 3
            r1c3button.Enabled = true;

            // Enable Row 2 Column 1
            r2c1button.Enabled = true;

            // Enable Row 2 Column 2
            r2c2button.Enabled = true;

            // Enable Row 2 Column 3
            r2c3button.Enabled = true;

            // Enable Row 3 Column 1
            r3c1button.Enabled = true;

            // Enable Row 3 Column 2
            r3c2button.Enabled = true;

            // Enable Row 3 Column 3
            r3c3button.Enabled = true;
        }

        // Custom method to switch all icons to blue
        private void SwitchToBlue()
        {
            // Swith Row 1 Column 1 to blue
            r1c1button.ImageList = blueImages;

            // Swith Row 1 Column 2 to blue
            r1c2button.ImageList = blueImages;

            // Siwtch Row 1 Column 3 to blue
            r1c3button.ImageList = blueImages;

            // Switch Row 2 Column 1 to blue
            r2c1button.ImageList = blueImages;

            // Switch Row 2 Column 2 to blue
            r2c2button.ImageList = blueImages;

            // Switch Row 2 Column 3 to blue
            r2c3button.ImageList = blueImages;

            // Switch Row 3 Column 1 to blue
            r3c1button.ImageList = blueImages;

            // Switch Row 3 Column 2 to blue
            r3c2button.ImageList = blueImages;

            // Switch Row 3 Column 3 to blue
            r3c3button.ImageList = blueImages;

            // Check Blue menu strip item
            blueToolStripMenuItem.Checked = true;

            // Uncheck Red menu strip item
            redToolStripMenuItem.Checked = false;
        }

        // Custom method to switch all icons to red
        private void SwitchToRed()
        {
            // Switch Row 1 Column 1 to red
            r1c1button.ImageList = redImages;

            // Switch Row 1 Column 2 to red
            r1c2button.ImageList = redImages;

            // Switch Row 1 Column 3 to red
            r1c3button.ImageList = redImages;

            // Switch Row 2 Column 1 to red
            r2c1button.ImageList = redImages;

            // Switch Row 2 Column 2 to red
            r2c2button.ImageList = redImages;

            // Switch Row 2 Column 3 to red
            r2c3button.ImageList = redImages;

            // Switch Row 3 Column 1 to red
            r3c1button.ImageList = redImages;

            // Switch Row 3 Column 2 to red
            r3c2button.ImageList = redImages;

            // Switch Row 3 Column 3 to red
            r3c3button.ImageList = redImages;

            // Check Red menu strip item
            redToolStripMenuItem.Checked = true;

            // Uncheck Blue menu strip item
            blueToolStripMenuItem.Checked = false;
        }

        // Custom method to save current game to file
        private void SaveToFile()
        {
            // Initialize settings object for the save stream
            XmlWriterSettings settings = new XmlWriterSettings();

            // Set save stream conformance level to Document
            settings.ConformanceLevel = ConformanceLevel.Document;

            // Set save stream to indent child elements
            settings.Indent = true;

            // Open save stream, save to user designated file name, and close when finished
            using (XmlWriter saveStream = XmlWriter.Create(saveFileDialog1.FileName))
            {
                // Write the start element (also the valid file indicator)
                saveStream.WriteStartElement("Tic_Tac_Toe");

                // If no player has selected Row 1 Column 1, this will rin
                if (r1c1button.ImageIndex != 0 && r1c1button.ImageIndex != 1)
                {
                    // Save line to indicate this cell was not selected
                    saveStream.WriteElementString("R1C1", "-1");
                }

                // If a player has selected Row 1 Column 1, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R1C1", r1c1button.ImageIndex.ToString());
                }

                // If no player has selected Row 1 Column 2, this will run
                if (r1c2button.ImageIndex != 0 && r1c2button.ImageIndex != 1)
                {
                    // Save line to indicate this cell was not selected
                    saveStream.WriteElementString("R1C2", "-1");
                }

                // If a player has selected Row 1 Column 2, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R1C2", r1c2button.ImageIndex.ToString());
                }

                // If no player has selected Row 1 Column 3, this will run
                if (r1c3button.ImageIndex != 0 && r1c3button.ImageIndex != 1)
                {
                    // Save line to indicate this cell was not selected
                    saveStream.WriteElementString("R1C3", "-1");
                }

                // If a player has selected Row 1 Column 3, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R1C3", r1c3button.ImageIndex.ToString());
                }

                // If no player has selected Row 2 Column 1, this will run
                if (r2c1button.ImageIndex != 0 && r2c1button.ImageIndex != 1)
                {
                    // Save line to indicate this cell was not selected
                    saveStream.WriteElementString("R2C1", "-1");
                }

                // If a player has selected Row 2 Column 1, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R2C1", r2c1button.ImageIndex.ToString());
                }

                // If no player has selected Row 2 Column 2, this will run
                if (r2c2button.ImageIndex != 0 && r2c2button.ImageIndex != 1)
                {
                    // Save line to indicate this cell was not selected
                    saveStream.WriteElementString("R2C2", "-1");
                }

                // If a player has selected Row 2 Column 2, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R2C2", r2c2button.ImageIndex.ToString());
                }

                // If no player has selected Row 2 Column 3, this will run
                if (r2c3button.ImageIndex != 0 && r2c3button.ImageIndex != 1)
                {
                    // Save line to indicate the cell was not selected
                    saveStream.WriteElementString("R2C3", "-1");
                }

                // If a player has selected Row 2 Column 3, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R2C3", r2c3button.ImageIndex.ToString());
                }

                // If no player has selected Row 3 Column 1, this will run
                if (r3c1button.ImageIndex != 0 && r3c1button.ImageIndex != 1)
                {
                    // Save line to indicate the cell was not selected
                    saveStream.WriteElementString("R3C1", "-1");
                }

                // If a player has selected Row 3 Column 1, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R3C1", r3c1button.ImageIndex.ToString());
                }

                // If no player has selected Row 3 Column 2, this will run
                if (r3c2button.ImageIndex != 0 && r3c2button.ImageIndex != 1)
                {
                    // Save line to indicate the cell was not selected
                    saveStream.WriteElementString("R3C2", "-1");
                }

                // If a player has selected Row 3 Column 2, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R3C2", r3c3button.ImageIndex.ToString());
                }

                // If no player has selected Row 3 Column 3, this will run
                if (r3c3button.ImageIndex != 0 && r3c3button.ImageIndex != 1)
                {
                    // Save line to indicate the cell was not selected
                    saveStream.WriteElementString("R3C3", "-1");
                }

                // If a player has selected Row 3 Column 3, this will run
                else
                {
                    // Save line to indicate which player selected the cell
                    saveStream.WriteElementString("R3C3", r3c3button.ImageIndex.ToString());
                }

                // Write end of file indicator
                saveStream.WriteEndElement();
            }
        }

        // Custom method to load saved game from file
        private void LoadFile()
        {
            // Call custom method to reset the play grid
            ClearGrid();

            // Reset the turn count
            turnCount = 0;

            // Instantiate settings for the load stream
            XmlReaderSettings settings = new XmlReaderSettings();

            // Set the load stream's conformance level to Document
            settings.ConformanceLevel = ConformanceLevel.Document;

            // Set the load stream to ignore comments
            settings.IgnoreComments = true;

            // Set the load stream to ignore white space
            settings.IgnoreWhitespace = true;

            // Open load stream, read user selected file, and close when finished
            using (XmlReader loadStream = XmlReader.Create(openFileDialog1.FileName))
            {
                // Tell the load stream to skip to relevant content
                loadStream.MoveToContent();

                // If the selected file does not contain the valid file indicator, this will run
                if (loadStream.Name != "Tic_Tac_Toe")
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

                // This will continue to run until the entire file has been read
                while (loadStream.Read())
                {
                    // Declare a string variable to contain read information on each line
                    string imageIndexString = null;

                    // If the current line is Row 1 Column 1, this will run
                    if (loadStream.Name == "R1C1" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator into an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r1c1button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 1 Column 2, this will run
                    if (loadStream.Name == "R1C2" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r1c2button.ImageIndex = imageIndex;

                            // Increase turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 1 Column 3, this will run
                    if (loadStream.Name == "R1C3" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Pars the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r1c3button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 2 Column 1, this will run
                    if (loadStream.Name == "R2C1" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r2c1button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 2 Column 2, this will run
                    if (loadStream.Name == "R2C2" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r2c2button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 2 Column 3, this will run
                    if (loadStream.Name == "R2C3" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r2c3button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 3 Column 1, this will run
                    if (loadStream.Name == "R3C1" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r3c1button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 3 Column 2, this will run
                    if (loadStream.Name == "R3C2" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indicator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indicator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r3c2button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }

                    // If the current line is Row 3 Column 3, this will run
                    if (loadStream.Name == "R3C3" && loadStream.IsStartElement())
                    {
                        // Read the player indicator to string
                        imageIndexString = loadStream.ReadString();

                        // Reset the internal player indocator
                        imageIndex = -1;

                        // Parse the read player indicator to an int
                        int.TryParse(imageIndexString, out imageIndex);

                        // If the player indocator does not say nobody selected the cell, this will run
                        if (imageIndex != -1)
                        {
                            // Populate the cell with the appropriate player icon
                            r3c3button.ImageIndex = imageIndex;

                            // Increase the turn count by one
                            turnCount++;
                        }
                    }
                }
            }

            // Call custom method to check how many turns have taken place
            CheckTurn();
        }

        // Custom method to reset the play grid
        private void ClearGrid()
        {
            // Reset the turn count
            turnCount = 0;

            // Reset the current player
            imageIndex = 0;

            // Uncheck the X menu strip item
            xToolStripMenuItem.Checked = false;

            // Check the O menu strip item
            oToolStripMenuItem.Checked = true;

            // Clear Row 1 Column 1
            r1c1button.ImageIndex = -1;

            // Clear Row 1 Column 2
            r1c2button.ImageIndex = -1;

            // Clear Row 1 Column 3
            r1c3button.ImageIndex = -1;

            // Clear Row 2 Column 1
            r2c1button.ImageIndex = -1;

            // Clear Row 2 Column 2
            r2c2button.ImageIndex = -1;

            // Clear Row 2 Column 3
            r2c3button.ImageIndex = -1;

            // Clear Row 3 Column 1
            r3c1button.ImageIndex = -1;
            
            // Clear Row 3 Column 2
            r3c2button.ImageIndex = -1;

            // Clear Row 3 Column 3
            r3c3button.ImageIndex = -1;

            // Call custom method to check the turn count
            CheckTurn();


            // Call custom method to enable the play grid
            EnableGrid();
        }
    }
}
