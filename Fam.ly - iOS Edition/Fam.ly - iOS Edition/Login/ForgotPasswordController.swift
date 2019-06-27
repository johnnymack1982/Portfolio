//
//  ForgotPasswordController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/27/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import FirebaseAuth

class ForgotPasswordController: UIViewController, UITextFieldDelegate {
    
    
    
    // UI Outlets
    @IBOutlet weak var masterEmailInput: UITextField!
    @IBOutlet weak var sendButton: UIButton!
    
    
    
    // Class properties
    var mEmail: String?
    
    var mValidEmail = false
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Disable send button by default
        ButtonUtils.disableButton(button: sendButton)
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
    
    
    
    // Action methods
    @IBAction func sendButtonClicked(_ sender: UIButton) {
        
        // If user input is valid...
        if mValidEmail {
            
            // Attempt to send password reset email. If successful, let the user know
            Auth.auth().sendPasswordReset(withEmail: mEmail!) { error in
                if error == nil {
                    let alert = UIAlertController(title: "On Its Way!", message: "We're sending you a password reset link. Please check your email.", preferredStyle: .alert)
                    
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
                    self.present(alert, animated: true)
                }
                
                // If unsuccessful, let the user know
                else {
                    let alert = UIAlertController(title: "Try Again!", message: "We can't find an account for this email address.", preferredStyle: .alert)
                    
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
                    self.present(alert, animated: true)
                }
            }
        }
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
                
                // If all fields have valid input, enable send button
                if mValidEmail {
                    ButtonUtils.enableButton(button: sendButton)
                }
            }
                
            // If input is invalid...
            else {
                
                // Turn input field red and indicate input is invalid
                TextFieldUtils.turnRed(textField: masterEmailInput)
                mValidEmail = false
                
                // Disable send button
                ButtonUtils.disableButton(button: sendButton)
            }
        }
    }
}
