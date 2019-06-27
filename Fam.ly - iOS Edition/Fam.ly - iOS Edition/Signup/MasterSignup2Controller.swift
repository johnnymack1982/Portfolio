//
//  MasterSignup2Controller.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/25/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class MasterSignup2Controller: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var masterEmailInput: UITextField!
    @IBOutlet weak var masterPasswordInput: UITextField!
    @IBOutlet weak var confirmMasterPasswordInput: UITextField!
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var passwordRulesDisplay: UILabel!
    @IBOutlet weak var passwordMatchRulesDisplay: UILabel!
    
    
    
    // Class properties
    var mFamilyName: String?
    var mStreetAddress: String?
    var mPostalCode: Int?
    
    var mEmail: String?
    var mPassword: String?
    
    var mValidEmail = false
    var mValidPassword = false
    var mPasswordsMatch = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Set default UI element states
        ButtonUtils.disableButton(button: continueButton)
        passwordRulesDisplay.isHidden = true
        passwordMatchRulesDisplay.isHidden = true
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        var shouldContinue = false
        
        if identifier == "Master2ToMaster3" {
            if mValidEmail && mValidPassword && mPasswordsMatch {
                shouldContinue = true
            }
                
            else {
                shouldContinue = false
            }
        }
        
        return shouldContinue
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.destination is MasterSignup3Controller {
            
            // Reference destination and send user input
            let destination = segue.destination as? MasterSignup3Controller
            
            destination?.mFamilyName = mFamilyName
            destination?.mStreetAddress = mStreetAddress
            destination?.mPostalCode = mPostalCode
            
            destination?.mEmail = mEmail
            destination?.mPassword = mPassword
        }
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
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        
        // Watch master email input
        if sender == masterEmailInput {
            
            // If input is valid...
            if InputUtils.validEmail(Email: masterEmailInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: masterEmailInput)
                
                // Reference user input and indicate input is valid
                mEmail = masterEmailInput.text
                mValidEmail = true
                
                // If all fields have valid input, enable continue button
                if mValidEmail && mValidPassword && mPasswordsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterEmailInput)
                mValidEmail = false
                
                // Disable continue button
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        // Watch master password input
        else if sender == masterPasswordInput {
            
            // If input is valid...
            if InputUtils.validPassword(Password: masterPasswordInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: masterPasswordInput)
                
                // Reference user input and indicate input is valid
                mValidPassword = true
                passwordRulesDisplay.isHidden = true
                
                // If all fields have valid input, enable continue button
                if mValidEmail && mValidPassword && mPasswordsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterPasswordInput)
                mValidPassword = false
                
                // Show password rules and disable continue button
                passwordRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
            // Watch password confirm input
        else if sender == confirmMasterPasswordInput {
            
            // If input is valid...
            if InputUtils.passwordsMatch(password: masterPasswordInput.text, passwordConfirm: confirmMasterPasswordInput.text) {
                
                // Turn input field green
                TextFieldUtils.turnGreen(textField: confirmMasterPasswordInput)
                
                // Indicate input is valid
                mPasswordsMatch = true
                
                // Hide password rules
                passwordMatchRulesDisplay.isHidden = true
                
                // Reference user input
                mPassword = masterPasswordInput.text
                
                // If all fields have valid input, enable continue button
                if mValidEmail && mValidPassword && mPasswordsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: confirmMasterPasswordInput)
                mPasswordsMatch = false
                
                // Hide password rules and disable conntinue button
                passwordMatchRulesDisplay.isHidden = false
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
}
