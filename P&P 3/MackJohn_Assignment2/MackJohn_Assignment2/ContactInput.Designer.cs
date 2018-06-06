﻿namespace MackJohn_Assignment2
{
    partial class FormContactDetails
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
            this.groupBoxContactInfo = new System.Windows.Forms.GroupBox();
            this.firstNameInput = new System.Windows.Forms.TextBox();
            this.labelFirstName = new System.Windows.Forms.Label();
            this.lastNameInput = new System.Windows.Forms.TextBox();
            this.labelLastName = new System.Windows.Forms.Label();
            this.phoneInput = new System.Windows.Forms.MaskedTextBox();
            this.labelPhone = new System.Windows.Forms.Label();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.labelEMail = new System.Windows.Forms.Label();
            this.clearBtn = new System.Windows.Forms.Button();
            this.cancelBtn = new System.Windows.Forms.Button();
            this.addBtn = new System.Windows.Forms.Button();
            this.businessRdoBtn = new System.Windows.Forms.RadioButton();
            this.personalRdoBtn = new System.Windows.Forms.RadioButton();
            this.groupBoxContactInfo.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBoxContactInfo
            // 
            this.groupBoxContactInfo.Controls.Add(this.personalRdoBtn);
            this.groupBoxContactInfo.Controls.Add(this.businessRdoBtn);
            this.groupBoxContactInfo.Controls.Add(this.addBtn);
            this.groupBoxContactInfo.Controls.Add(this.cancelBtn);
            this.groupBoxContactInfo.Controls.Add(this.clearBtn);
            this.groupBoxContactInfo.Controls.Add(this.labelEMail);
            this.groupBoxContactInfo.Controls.Add(this.textBox1);
            this.groupBoxContactInfo.Controls.Add(this.labelPhone);
            this.groupBoxContactInfo.Controls.Add(this.phoneInput);
            this.groupBoxContactInfo.Controls.Add(this.labelLastName);
            this.groupBoxContactInfo.Controls.Add(this.lastNameInput);
            this.groupBoxContactInfo.Controls.Add(this.labelFirstName);
            this.groupBoxContactInfo.Controls.Add(this.firstNameInput);
            this.groupBoxContactInfo.Location = new System.Drawing.Point(37, 38);
            this.groupBoxContactInfo.Name = "groupBoxContactInfo";
            this.groupBoxContactInfo.Size = new System.Drawing.Size(444, 432);
            this.groupBoxContactInfo.TabIndex = 0;
            this.groupBoxContactInfo.TabStop = false;
            this.groupBoxContactInfo.Text = "Enter Contact Information";
            // 
            // firstNameInput
            // 
            this.firstNameInput.Location = new System.Drawing.Point(179, 55);
            this.firstNameInput.Name = "firstNameInput";
            this.firstNameInput.Size = new System.Drawing.Size(206, 31);
            this.firstNameInput.TabIndex = 0;
            this.firstNameInput.TextChanged += new System.EventHandler(this.firstNameInput_TextChanged);
            // 
            // labelFirstName
            // 
            this.labelFirstName.AutoSize = true;
            this.labelFirstName.Location = new System.Drawing.Point(51, 58);
            this.labelFirstName.Name = "labelFirstName";
            this.labelFirstName.Size = new System.Drawing.Size(122, 25);
            this.labelFirstName.TabIndex = 1;
            this.labelFirstName.Text = "First Name:";
            // 
            // lastNameInput
            // 
            this.lastNameInput.Location = new System.Drawing.Point(179, 101);
            this.lastNameInput.Name = "lastNameInput";
            this.lastNameInput.Size = new System.Drawing.Size(206, 31);
            this.lastNameInput.TabIndex = 2;
            // 
            // labelLastName
            // 
            this.labelLastName.AutoSize = true;
            this.labelLastName.Location = new System.Drawing.Point(52, 104);
            this.labelLastName.Name = "labelLastName";
            this.labelLastName.Size = new System.Drawing.Size(121, 25);
            this.labelLastName.TabIndex = 3;
            this.labelLastName.Text = "Last Name:";
            // 
            // phoneInput
            // 
            this.phoneInput.Location = new System.Drawing.Point(179, 148);
            this.phoneInput.Mask = "(999) 000-0000";
            this.phoneInput.Name = "phoneInput";
            this.phoneInput.Size = new System.Drawing.Size(164, 31);
            this.phoneInput.TabIndex = 4;
            // 
            // labelPhone
            // 
            this.labelPhone.AutoSize = true;
            this.labelPhone.Location = new System.Drawing.Point(93, 151);
            this.labelPhone.Name = "labelPhone";
            this.labelPhone.Size = new System.Drawing.Size(80, 25);
            this.labelPhone.TabIndex = 5;
            this.labelPhone.Text = "Phone:";
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(179, 195);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(206, 31);
            this.textBox1.TabIndex = 6;
            // 
            // labelEMail
            // 
            this.labelEMail.AutoSize = true;
            this.labelEMail.Location = new System.Drawing.Point(94, 198);
            this.labelEMail.Name = "labelEMail";
            this.labelEMail.Size = new System.Drawing.Size(79, 25);
            this.labelEMail.TabIndex = 7;
            this.labelEMail.Text = "E-Mail:";
            // 
            // clearBtn
            // 
            this.clearBtn.Location = new System.Drawing.Point(24, 343);
            this.clearBtn.Name = "clearBtn";
            this.clearBtn.Size = new System.Drawing.Size(109, 55);
            this.clearBtn.TabIndex = 8;
            this.clearBtn.Text = "Clear";
            this.clearBtn.UseVisualStyleBackColor = true;
            // 
            // cancelBtn
            // 
            this.cancelBtn.Location = new System.Drawing.Point(312, 343);
            this.cancelBtn.Name = "cancelBtn";
            this.cancelBtn.Size = new System.Drawing.Size(109, 55);
            this.cancelBtn.TabIndex = 9;
            this.cancelBtn.Text = "Cancel";
            this.cancelBtn.UseVisualStyleBackColor = true;
            // 
            // addBtn
            // 
            this.addBtn.Enabled = false;
            this.addBtn.Location = new System.Drawing.Point(185, 343);
            this.addBtn.Name = "addBtn";
            this.addBtn.Size = new System.Drawing.Size(109, 55);
            this.addBtn.TabIndex = 10;
            this.addBtn.Text = "Add";
            this.addBtn.UseVisualStyleBackColor = true;
            // 
            // businessRdoBtn
            // 
            this.businessRdoBtn.AutoSize = true;
            this.businessRdoBtn.Location = new System.Drawing.Point(57, 268);
            this.businessRdoBtn.Name = "businessRdoBtn";
            this.businessRdoBtn.Size = new System.Drawing.Size(131, 29);
            this.businessRdoBtn.TabIndex = 11;
            this.businessRdoBtn.TabStop = true;
            this.businessRdoBtn.Text = "Business";
            this.businessRdoBtn.UseVisualStyleBackColor = true;
            // 
            // personalRdoBtn
            // 
            this.personalRdoBtn.AutoSize = true;
            this.personalRdoBtn.Location = new System.Drawing.Point(254, 268);
            this.personalRdoBtn.Name = "personalRdoBtn";
            this.personalRdoBtn.Size = new System.Drawing.Size(128, 29);
            this.personalRdoBtn.TabIndex = 12;
            this.personalRdoBtn.TabStop = true;
            this.personalRdoBtn.Text = "Personal";
            this.personalRdoBtn.UseVisualStyleBackColor = true;
            // 
            // FormContactDetails
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(514, 504);
            this.Controls.Add(this.groupBoxContactInfo);
            this.Name = "FormContactDetails";
            this.Text = "Contact Details";
            this.groupBoxContactInfo.ResumeLayout(false);
            this.groupBoxContactInfo.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBoxContactInfo;
        private System.Windows.Forms.Label labelEMail;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label labelPhone;
        private System.Windows.Forms.MaskedTextBox phoneInput;
        private System.Windows.Forms.Label labelLastName;
        private System.Windows.Forms.TextBox lastNameInput;
        private System.Windows.Forms.Label labelFirstName;
        private System.Windows.Forms.TextBox firstNameInput;
        private System.Windows.Forms.RadioButton personalRdoBtn;
        private System.Windows.Forms.RadioButton businessRdoBtn;
        private System.Windows.Forms.Button addBtn;
        private System.Windows.Forms.Button cancelBtn;
        private System.Windows.Forms.Button clearBtn;
    }
}