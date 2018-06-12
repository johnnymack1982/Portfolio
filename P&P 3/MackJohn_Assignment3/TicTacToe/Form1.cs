﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

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

        public frmTicTacToe()
        {
            InitializeComponent();
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
        }

        private void CheckForWinner()
        {
            string caption = "WINNER!";

            if(r1c1button.ImageIndex == 0 && r1c2button.ImageIndex == 0)
            {
                MessageBox.Show("Congratulations, 'O' player! You've won the game!", caption);
            }
        }
    }
}
