namespace MackJohn_Assignment1
{
    partial class FormShoppingList
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.saveToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.loadToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.groupBoxHave = new System.Windows.Forms.GroupBox();
            this.haveListBox = new System.Windows.Forms.ListBox();
            this.groupBoxNeed = new System.Windows.Forms.GroupBox();
            this.needListBox = new System.Windows.Forms.ListBox();
            this.moveToHaveBtn = new System.Windows.Forms.Button();
            this.removeSelectedBtn = new System.Windows.Forms.Button();
            this.movetoNeedBtn = new System.Windows.Forms.Button();
            this.addNewBtn = new System.Windows.Forms.Button();
            this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.menuStrip1.SuspendLayout();
            this.groupBoxHave.SuspendLayout();
            this.groupBoxNeed.SuspendLayout();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.ImageScalingSize = new System.Drawing.Size(32, 32);
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(974, 40);
            this.menuStrip1.TabIndex = 0;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.saveToolStripMenuItem,
            this.loadToolStripMenuItem,
            this.exitToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(64, 38);
            this.fileToolStripMenuItem.Text = "File";
            // 
            // saveToolStripMenuItem
            // 
            this.saveToolStripMenuItem.Name = "saveToolStripMenuItem";
            this.saveToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
            this.saveToolStripMenuItem.Size = new System.Drawing.Size(324, 38);
            this.saveToolStripMenuItem.Text = "Save";
            this.saveToolStripMenuItem.Click += new System.EventHandler(this.saveToolStripMenuItem_Click);
            // 
            // loadToolStripMenuItem
            // 
            this.loadToolStripMenuItem.Name = "loadToolStripMenuItem";
            this.loadToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.L)));
            this.loadToolStripMenuItem.Size = new System.Drawing.Size(324, 38);
            this.loadToolStripMenuItem.Text = "Load";
            this.loadToolStripMenuItem.Click += new System.EventHandler(this.loadToolStripMenuItem_Click);
            // 
            // exitToolStripMenuItem
            // 
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Q)));
            this.exitToolStripMenuItem.Size = new System.Drawing.Size(324, 38);
            this.exitToolStripMenuItem.Text = "Exit";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // groupBoxHave
            // 
            this.groupBoxHave.Controls.Add(this.haveListBox);
            this.groupBoxHave.Location = new System.Drawing.Point(32, 73);
            this.groupBoxHave.Name = "groupBoxHave";
            this.groupBoxHave.Size = new System.Drawing.Size(380, 582);
            this.groupBoxHave.TabIndex = 1;
            this.groupBoxHave.TabStop = false;
            this.groupBoxHave.Text = "Items I HAVE";
            // 
            // haveListBox
            // 
            this.haveListBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.haveListBox.FormattingEnabled = true;
            this.haveListBox.ItemHeight = 25;
            this.haveListBox.Location = new System.Drawing.Point(3, 27);
            this.haveListBox.Name = "haveListBox";
            this.haveListBox.Size = new System.Drawing.Size(374, 552);
            this.haveListBox.TabIndex = 0;
            this.haveListBox.SelectedIndexChanged += new System.EventHandler(this.haveListBox_SelectedIndexChanged);
            // 
            // groupBoxNeed
            // 
            this.groupBoxNeed.Controls.Add(this.needListBox);
            this.groupBoxNeed.Location = new System.Drawing.Point(562, 73);
            this.groupBoxNeed.Name = "groupBoxNeed";
            this.groupBoxNeed.Size = new System.Drawing.Size(380, 582);
            this.groupBoxNeed.TabIndex = 2;
            this.groupBoxNeed.TabStop = false;
            this.groupBoxNeed.Text = "Items I NEED";
            // 
            // needListBox
            // 
            this.needListBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.needListBox.FormattingEnabled = true;
            this.needListBox.ItemHeight = 25;
            this.needListBox.Location = new System.Drawing.Point(3, 27);
            this.needListBox.Name = "needListBox";
            this.needListBox.Size = new System.Drawing.Size(374, 552);
            this.needListBox.TabIndex = 0;
            this.needListBox.SelectedIndexChanged += new System.EventHandler(this.needListBox_SelectedIndexChanged);
            // 
            // moveToHaveBtn
            // 
            this.moveToHaveBtn.Location = new System.Drawing.Point(453, 134);
            this.moveToHaveBtn.Name = "moveToHaveBtn";
            this.moveToHaveBtn.Size = new System.Drawing.Size(75, 67);
            this.moveToHaveBtn.TabIndex = 3;
            this.moveToHaveBtn.Text = "<<";
            this.moveToHaveBtn.UseVisualStyleBackColor = true;
            this.moveToHaveBtn.Click += new System.EventHandler(this.moveToHaveBtn_Click);
            // 
            // removeSelectedBtn
            // 
            this.removeSelectedBtn.Location = new System.Drawing.Point(453, 227);
            this.removeSelectedBtn.Name = "removeSelectedBtn";
            this.removeSelectedBtn.Size = new System.Drawing.Size(75, 67);
            this.removeSelectedBtn.TabIndex = 4;
            this.removeSelectedBtn.Text = "X";
            this.removeSelectedBtn.UseVisualStyleBackColor = true;
            this.removeSelectedBtn.Click += new System.EventHandler(this.removeSelectedBtn_Click);
            // 
            // movetoNeedBtn
            // 
            this.movetoNeedBtn.Location = new System.Drawing.Point(453, 321);
            this.movetoNeedBtn.Name = "movetoNeedBtn";
            this.movetoNeedBtn.Size = new System.Drawing.Size(75, 67);
            this.movetoNeedBtn.TabIndex = 5;
            this.movetoNeedBtn.Text = ">>";
            this.movetoNeedBtn.UseVisualStyleBackColor = true;
            this.movetoNeedBtn.Click += new System.EventHandler(this.movetoNeedBtn_Click);
            // 
            // addNewBtn
            // 
            this.addNewBtn.Location = new System.Drawing.Point(453, 481);
            this.addNewBtn.Name = "addNewBtn";
            this.addNewBtn.Size = new System.Drawing.Size(75, 67);
            this.addNewBtn.TabIndex = 6;
            this.addNewBtn.Text = "New Item";
            this.addNewBtn.UseVisualStyleBackColor = true;
            this.addNewBtn.Click += new System.EventHandler(this.addNewBtn_Click);
            // 
            // saveFileDialog1
            // 
            this.saveFileDialog1.DefaultExt = "xml";
            this.saveFileDialog1.Filter = ".xml|";
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.DefaultExt = "xml";
            this.openFileDialog1.FileName = "openFileDialog1";
            this.openFileDialog1.Filter = ".xml|";
            // 
            // FormShoppingList
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(974, 692);
            this.Controls.Add(this.addNewBtn);
            this.Controls.Add(this.movetoNeedBtn);
            this.Controls.Add(this.removeSelectedBtn);
            this.Controls.Add(this.moveToHaveBtn);
            this.Controls.Add(this.groupBoxNeed);
            this.Controls.Add(this.groupBoxHave);
            this.Controls.Add(this.menuStrip1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.Fixed3D;
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "FormShoppingList";
            this.Text = "Shopping List";
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.groupBoxHave.ResumeLayout(false);
            this.groupBoxNeed.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem saveToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem loadToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
        private System.Windows.Forms.GroupBox groupBoxHave;
        private System.Windows.Forms.ListBox haveListBox;
        private System.Windows.Forms.GroupBox groupBoxNeed;
        private System.Windows.Forms.ListBox needListBox;
        private System.Windows.Forms.Button moveToHaveBtn;
        private System.Windows.Forms.Button removeSelectedBtn;
        private System.Windows.Forms.Button movetoNeedBtn;
        private System.Windows.Forms.Button addNewBtn;
        private System.Windows.Forms.SaveFileDialog saveFileDialog1;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
    }
}

