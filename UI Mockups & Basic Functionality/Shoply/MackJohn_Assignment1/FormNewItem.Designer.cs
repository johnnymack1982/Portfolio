namespace MackJohn_Assignment1
{
    partial class FormNewItem
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
            this.groupBoxNewItem = new System.Windows.Forms.GroupBox();
            this.itemNameTextBox = new System.Windows.Forms.TextBox();
            this.labelItemName = new System.Windows.Forms.Label();
            this.pricePicker = new System.Windows.Forms.NumericUpDown();
            this.labelPrice = new System.Windows.Forms.Label();
            this.haveRdoBtn = new System.Windows.Forms.RadioButton();
            this.needRdoBtn = new System.Windows.Forms.RadioButton();
            this.priorityPicker = new System.Windows.Forms.ComboBox();
            this.labelPriority = new System.Windows.Forms.Label();
            this.clearBtn = new System.Windows.Forms.Button();
            this.cancelBtn = new System.Windows.Forms.Button();
            this.addBtn = new System.Windows.Forms.Button();
            this.groupBoxNewItem.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pricePicker)).BeginInit();
            this.SuspendLayout();
            // 
            // groupBoxNewItem
            // 
            this.groupBoxNewItem.Controls.Add(this.labelPriority);
            this.groupBoxNewItem.Controls.Add(this.priorityPicker);
            this.groupBoxNewItem.Controls.Add(this.needRdoBtn);
            this.groupBoxNewItem.Controls.Add(this.haveRdoBtn);
            this.groupBoxNewItem.Controls.Add(this.labelPrice);
            this.groupBoxNewItem.Controls.Add(this.pricePicker);
            this.groupBoxNewItem.Controls.Add(this.labelItemName);
            this.groupBoxNewItem.Controls.Add(this.itemNameTextBox);
            this.groupBoxNewItem.Location = new System.Drawing.Point(32, 37);
            this.groupBoxNewItem.Name = "groupBoxNewItem";
            this.groupBoxNewItem.Size = new System.Drawing.Size(494, 332);
            this.groupBoxNewItem.TabIndex = 0;
            this.groupBoxNewItem.TabStop = false;
            this.groupBoxNewItem.Text = "New Shopping Item";
            // 
            // itemNameTextBox
            // 
            this.itemNameTextBox.Location = new System.Drawing.Point(169, 50);
            this.itemNameTextBox.Name = "itemNameTextBox";
            this.itemNameTextBox.Size = new System.Drawing.Size(264, 31);
            this.itemNameTextBox.TabIndex = 0;
            this.itemNameTextBox.TextChanged += new System.EventHandler(this.itemNameTextBox_TextChanged);
            // 
            // labelItemName
            // 
            this.labelItemName.AutoSize = true;
            this.labelItemName.Location = new System.Drawing.Point(49, 53);
            this.labelItemName.Name = "labelItemName";
            this.labelItemName.Size = new System.Drawing.Size(114, 25);
            this.labelItemName.TabIndex = 1;
            this.labelItemName.Text = "Item Name";
            // 
            // pricePicker
            // 
            this.pricePicker.DecimalPlaces = 2;
            this.pricePicker.Increment = new decimal(new int[] {
            1,
            0,
            0,
            131072});
            this.pricePicker.Location = new System.Drawing.Point(169, 97);
            this.pricePicker.Name = "pricePicker";
            this.pricePicker.Size = new System.Drawing.Size(120, 31);
            this.pricePicker.TabIndex = 2;
            this.pricePicker.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.pricePicker.ValueChanged += new System.EventHandler(this.pricePicker_ValueChanged);
            // 
            // labelPrice
            // 
            this.labelPrice.AutoSize = true;
            this.labelPrice.Location = new System.Drawing.Point(84, 99);
            this.labelPrice.Name = "labelPrice";
            this.labelPrice.Size = new System.Drawing.Size(79, 25);
            this.labelPrice.TabIndex = 3;
            this.labelPrice.Text = "Pirce $";
            // 
            // haveRdoBtn
            // 
            this.haveRdoBtn.AutoSize = true;
            this.haveRdoBtn.Location = new System.Drawing.Point(169, 159);
            this.haveRdoBtn.Name = "haveRdoBtn";
            this.haveRdoBtn.Size = new System.Drawing.Size(197, 29);
            this.haveRdoBtn.TabIndex = 4;
            this.haveRdoBtn.TabStop = true;
            this.haveRdoBtn.Text = "I HAVE this item";
            this.haveRdoBtn.UseVisualStyleBackColor = true;
            this.haveRdoBtn.CheckedChanged += new System.EventHandler(this.haveRdoBtn_CheckedChanged);
            // 
            // needRdoBtn
            // 
            this.needRdoBtn.AutoSize = true;
            this.needRdoBtn.Location = new System.Drawing.Point(169, 203);
            this.needRdoBtn.Name = "needRdoBtn";
            this.needRdoBtn.Size = new System.Drawing.Size(198, 29);
            this.needRdoBtn.TabIndex = 5;
            this.needRdoBtn.TabStop = true;
            this.needRdoBtn.Text = "I NEED this item";
            this.needRdoBtn.UseVisualStyleBackColor = true;
            this.needRdoBtn.CheckedChanged += new System.EventHandler(this.needRdoBtn_CheckedChanged);
            // 
            // priorityPicker
            // 
            this.priorityPicker.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.priorityPicker.Enabled = false;
            this.priorityPicker.FormattingEnabled = true;
            this.priorityPicker.Items.AddRange(new object[] {
            "URGENT",
            "Must Have",
            "Need Soon",
            "Nice to Have"});
            this.priorityPicker.Location = new System.Drawing.Point(169, 267);
            this.priorityPicker.Name = "priorityPicker";
            this.priorityPicker.Size = new System.Drawing.Size(264, 33);
            this.priorityPicker.TabIndex = 6;
            this.priorityPicker.TextChanged += new System.EventHandler(this.priorityPicker_TextChanged);
            // 
            // labelPriority
            // 
            this.labelPriority.AutoSize = true;
            this.labelPriority.Location = new System.Drawing.Point(84, 270);
            this.labelPriority.Name = "labelPriority";
            this.labelPriority.Size = new System.Drawing.Size(79, 25);
            this.labelPriority.TabIndex = 7;
            this.labelPriority.Text = "Priority";
            // 
            // clearBtn
            // 
            this.clearBtn.Location = new System.Drawing.Point(32, 404);
            this.clearBtn.Name = "clearBtn";
            this.clearBtn.Size = new System.Drawing.Size(149, 67);
            this.clearBtn.TabIndex = 1;
            this.clearBtn.Text = "Clear";
            this.clearBtn.UseVisualStyleBackColor = true;
            this.clearBtn.Click += new System.EventHandler(this.clearBtn_Click);
            // 
            // cancelBtn
            // 
            this.cancelBtn.Location = new System.Drawing.Point(377, 404);
            this.cancelBtn.Name = "cancelBtn";
            this.cancelBtn.Size = new System.Drawing.Size(149, 67);
            this.cancelBtn.TabIndex = 2;
            this.cancelBtn.Text = "Cancel";
            this.cancelBtn.UseVisualStyleBackColor = true;
            this.cancelBtn.Click += new System.EventHandler(this.cancelBtn_Click);
            // 
            // addBtn
            // 
            this.addBtn.Location = new System.Drawing.Point(253, 404);
            this.addBtn.Name = "addBtn";
            this.addBtn.Size = new System.Drawing.Size(113, 67);
            this.addBtn.TabIndex = 3;
            this.addBtn.Text = "Add";
            this.addBtn.UseVisualStyleBackColor = true;
            this.addBtn.Click += new System.EventHandler(this.addBtn_Click);
            // 
            // FormNewItem
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(560, 498);
            this.Controls.Add(this.addBtn);
            this.Controls.Add(this.cancelBtn);
            this.Controls.Add(this.clearBtn);
            this.Controls.Add(this.groupBoxNewItem);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "FormNewItem";
            this.Text = "Add New Item";
            this.groupBoxNewItem.ResumeLayout(false);
            this.groupBoxNewItem.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pricePicker)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBoxNewItem;
        private System.Windows.Forms.Label labelPriority;
        private System.Windows.Forms.ComboBox priorityPicker;
        private System.Windows.Forms.RadioButton needRdoBtn;
        private System.Windows.Forms.RadioButton haveRdoBtn;
        private System.Windows.Forms.Label labelPrice;
        private System.Windows.Forms.NumericUpDown pricePicker;
        private System.Windows.Forms.Label labelItemName;
        private System.Windows.Forms.TextBox itemNameTextBox;
        private System.Windows.Forms.Button clearBtn;
        private System.Windows.Forms.Button cancelBtn;
        private System.Windows.Forms.Button addBtn;
    }
}