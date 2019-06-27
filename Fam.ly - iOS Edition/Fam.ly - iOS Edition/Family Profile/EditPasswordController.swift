//
//  EditPasswordController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/28/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth

class EditPasswordController: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var familyPhoto: UIImageView!
    @IBOutlet weak var oldMasterPasswordInput: UITextField!
    @IBOutlet weak var newMasterPasswordInput: UITextField!
    @IBOutlet weak var confirmNewMasterPasswordInput: UITextField!
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var passwordRulesDisplay: UILabel!
    @IBOutlet weak var passwordMatchRulesDisplay: UILabel!
    
    
    
    // Class properties
    var mAccount: Account?
    
    var mOldPassword: String?
    var mNewPassword: String?
    
    var mValidOldPassword = false;
    var mValidNewPassword = false;
    var mNewPasswordsMatch = false;
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Load account and family photo
        mAccount = AccountUtils.loadAccount()
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        
        // Call custom method to round image view
        roundImageView()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        // On 'enter,' move to next input field. If last field, dismiss keyboard
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Custom methods
    // Custom method to round image view
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }
    
    
    
    // Action methods
    @IBAction func saveButtonClicked(_ sender: UIButton) {
        
        // Attempt to reauthenticate user
        let credential = EmailAuthProvider.credential(withEmail: (Auth.auth().currentUser?.email)!, password: mOldPassword!)
        Auth.auth().currentUser?.reauthenticateAndRetrieveData(with: credential, completion: { (result, error) in
            
            // If successful, update password and return to previous activity
            if error == nil {
                Auth.auth().currentUser?.updatePassword(to: self.mNewPassword!, completion: nil)
                
                self.performSegue(withIdentifier: "EditPasswordToFamilyProfile", sender: nil)
            }
             
            // If unsuccessful, let the user know
            else {
                let alert = UIAlertController(title: "Couldn't Update", message: "There was a problem updateing your information", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                self.present(alert, animated: true)
            }
        })
    }
    
    
    
    // Text change watcher
    @IBAction func textWatcher(_ sender: UITextField) {
        
        // Watch old master password input
        if sender == oldMasterPasswordInput {
            
            // If input is valid...
            if InputUtils.validPassword(Password: oldMasterPasswordInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: oldMasterPasswordInput)
                
                // Reference user input and indicate input is valid
                mValidOldPassword = true
                mOldPassword = oldMasterPasswordInput.text
                
                // Hide password rules
                passwordRulesDisplay.isHidden = true
                
                // If all fields have valid input, enable save button
                if mValidOldPassword && mValidNewPassword && mNewPasswordsMatch {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
               
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: oldMasterPasswordInput)
                mValidOldPassword = false
                
                // Show password rules and disable save button
                passwordRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: saveButton)
            }
        }
        
        // Watch new master password input
        if sender == newMasterPasswordInput {
            
            // If input is valid...
            if InputUtils.validPassword(Password: newMasterPasswordInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: newMasterPasswordInput)
                
                // Reference user input and hide password rules
                mValidNewPassword = true
                passwordRulesDisplay.isHidden = true
                
                // If all fields have valid input, enable save button
                if mValidOldPassword && mValidNewPassword && mNewPasswordsMatch {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: newMasterPasswordInput)
                mValidNewPassword = false
                
                // Show password rules and disable save button
                passwordRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: saveButton)
            }
        }
        
        // Watch confirm new master password input
        if sender == confirmNewMasterPasswordInput {
            
            // If input is valid...
            if InputUtils.passwordsMatch(password: newMasterPasswordInput.text, passwordConfirm: confirmNewMasterPasswordInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: confirmNewMasterPasswordInput)
                
                // Reference user input and indicate input is valid
                mNewPasswordsMatch = true
                mNewPassword = newMasterPasswordInput.text
                
                // Hide password rules
                passwordMatchRulesDisplay.isHidden = true
                
                // If all fields have valid input, enable save button
                if mValidOldPassword && mValidNewPassword && mNewPasswordsMatch {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
               
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: confirmNewMasterPasswordInput)
                mNewPasswordsMatch = false
                
                // Hide password rules and disable save button
                passwordMatchRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: saveButton)
            }
        }
    }
}
