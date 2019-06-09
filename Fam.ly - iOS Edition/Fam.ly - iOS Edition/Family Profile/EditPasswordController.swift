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

        mAccount = AccountUtils.loadAccount()
        AccountUtils.loadAccountPhoto(FamilyPhoto: familyPhoto)
        roundImageView()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        let nextTag = textField.tag + 1
        
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Custom methods
    func roundImageView() {
        familyPhoto.layer.borderWidth = 1.0
        familyPhoto.layer.masksToBounds = false
        familyPhoto.layer.borderColor = UIColor.white.cgColor
        familyPhoto.layer.cornerRadius = familyPhoto.frame.size.width / 2
        familyPhoto.clipsToBounds = true
    }
    
    
    
    // Action methods
    @IBAction func saveButtonClicked(_ sender: UIButton) {
        let credential = EmailAuthProvider.credential(withEmail: (Auth.auth().currentUser?.email)!, password: mOldPassword!)
        
        Auth.auth().currentUser?.reauthenticateAndRetrieveData(with: credential, completion: { (result, error) in
            if error == nil {
                Auth.auth().currentUser?.updatePassword(to: self.mNewPassword!, completion: nil)
                
                self.performSegue(withIdentifier: "EditPasswordToFamilyProfile", sender: nil)
            }
                
            else {
                let alert = UIAlertController(title: "Couldn't Update", message: "There was a problem updateing your information", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                
                self.present(alert, animated: true)
            }
        })
    }
    
    
    
    // Text change watcher
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == oldMasterPasswordInput {
            
            if InputUtils.validPassword(Password: oldMasterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: oldMasterPasswordInput)
                
                mValidOldPassword = true
                
                mOldPassword = oldMasterPasswordInput.text
                
                passwordRulesDisplay.isHidden = true
                
                if mValidOldPassword && mValidNewPassword && mNewPasswordsMatch {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: oldMasterPasswordInput)
                
                mValidOldPassword = false
                
                passwordRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
        
        if sender == newMasterPasswordInput {
            
            if InputUtils.validPassword(Password: newMasterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: newMasterPasswordInput)
                
                mValidNewPassword = true
                
                passwordRulesDisplay.isHidden = true
                
                if mValidOldPassword && mValidNewPassword && mNewPasswordsMatch {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: newMasterPasswordInput)
                
                mValidNewPassword = false
                
                passwordRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
        
        if sender == confirmNewMasterPasswordInput {
            
            if InputUtils.passwordsMatch(password: newMasterPasswordInput.text, passwordConfirm: confirmNewMasterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: confirmNewMasterPasswordInput)
                
                mNewPasswordsMatch = true
                
                mNewPassword = newMasterPasswordInput.text
                
                passwordMatchRulesDisplay.isHidden = true
                
                if mValidOldPassword && mValidNewPassword && mNewPasswordsMatch {
                    ButtonUtils.enableButton(button: saveButton)
                }
            }
                
            else {
                TextFieldUtils.turnRed(textField: confirmNewMasterPasswordInput)
                
                mNewPasswordsMatch = false
                
                passwordMatchRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: saveButton)
            }
        }
    }
}
