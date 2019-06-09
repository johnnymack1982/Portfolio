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
        
        if let nextResponder = textField.superview?.viewWithTag(nextTag) {
            nextResponder.becomeFirstResponder()
        } else {
            textField.resignFirstResponder()
        }
        
        return true
    }
    
    
    
    // Text change listener
    @IBAction func textWatcher(_ sender: UITextField) {
        if sender == masterEmailInput {
            
            if InputUtils.validEmail(Email: masterEmailInput.text) {
                
                TextFieldUtils.turnGreen(textField: masterEmailInput)
                
                mEmail = masterEmailInput.text
                
                mValidEmail = true
                
                if mValidEmail && mValidPassword && mPasswordsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: masterEmailInput)
                
                mValidEmail = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        else if sender == masterPasswordInput {
            
            if InputUtils.validPassword(Password: masterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: masterPasswordInput)
                
                mValidPassword = true
                
                passwordRulesDisplay.isHidden = true
                
                if mValidEmail && mValidPassword && mPasswordsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: masterPasswordInput)
                
                mValidPassword = false
                
                passwordRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
        
        else if sender == confirmMasterPasswordInput {
            
            if InputUtils.passwordsMatch(password: masterPasswordInput.text, passwordConfirm: confirmMasterPasswordInput.text) {
                
                TextFieldUtils.turnGreen(textField: confirmMasterPasswordInput)
                
                mPasswordsMatch = true
                
                passwordMatchRulesDisplay.isHidden = true
                
                mPassword = masterPasswordInput.text
                
                if mValidEmail && mValidPassword && mPasswordsMatch {
                    ButtonUtils.enableButton(button: continueButton)
                }
            }
            
            else {
                TextFieldUtils.turnRed(textField: confirmMasterPasswordInput)
                
                mPasswordsMatch = false
                
                passwordMatchRulesDisplay.isHidden = false
                
                ButtonUtils.disableButton(button: continueButton)
            }
        }
    }
}
